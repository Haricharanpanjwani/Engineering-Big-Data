package com.xml.pagerank.builder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class SpeciesGraphBuilderMapper extends MapReduceBase implements
Mapper<LongWritable, Text, Text, Text> {

	private static final String TITLE_TAG = "title";
	private static final String TEXT_TAG = "text";

	private static String nodeValue = "";

	/*
	 * Map is provided with a Text instance, which represents an XML
	 * representation of a wiki page, delineated by <page></page>. This mapper
	 * extracts and outputs the <title> and outlinks of the subject page.
	 */
	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {

		String page = value.toString();
		List<String> outlinks = new ArrayList<String>();
		String title = "";

		try {
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(
					new StringReader(page)));
			if (doc.hasChildNodes()) {
				if (retriveNode(doc.getChildNodes(), TITLE_TAG)) {
					if (nodeValue.length() > 0) {
						title = nodeValue;						
						reporter.setStatus(title);
					} else {
						return;
					}
				} else {
					return;
				}

				if (retriveNode(doc.getChildNodes(), TEXT_TAG)) {
					outlinks = getOutLinks(nodeValue);
				}								

				StringBuilder sb = new StringBuilder();
				for (String outlink : outlinks) {					

					if(!isForeignCharacter(outlink)) {														

						int pxPos = outlink.indexOf("px|");
						if(pxPos > -1) {
							String pxString = outlink.substring(0, pxPos+2);
							outlink = outlink.replace(pxString, "");
						}

						//lets eliminate any possible processing confusion over colons and space							
						outlink = outlink.replace("&nbsp;", "_");
						outlink = outlink.replace("*nbsp;", "_");
						outlink = outlink.replace("''", "");
						outlink = outlink.replace("Help:", "");
						outlink = outlink.replace("Template:", "");
						outlink = outlink.replace("Wikispecies:", "");
						outlink = outlink.replace(":", "_");
						outlink = outlink.replace(" ", "_");

						if(!(outlink.contains(".js") || outlink.contains(".css") 
								|| outlink.contains(".jpg") || outlink.contains(".JPG")
								|| outlink.contains(".png") || outlink.contains(".gif")
								|| outlink.contains("px"))) {									
							//appending the outlinks with space in a string
							sb.append(" ").append(outlink);
						}
					}
				}

				try {
					if(!isForeignCharacter(title)) {
						
						int pxPos = title.indexOf("px|");
						if(pxPos > -1) {
							String pxString = title.substring(0, pxPos+2);
							title = title.replace(pxString, "");
						}

						//lets eliminate any possible processing confusion over colons and space							
						title = title.replace("&nbsp;", "_");
						title = title.replace("*nbsp;", "_");
						title = title.replace("''", "");
						title = title.replace("Help:", "");
						title = title.replace("Template:", "");
						title = title.replace("Wikispecies:", "");
						title = title.replace(":", "_");
						title = title.replace(" ", "_");

						if(!(title.contains(".js") || title.contains(".css")
								|| title.contains(".jpg") || title.contains(".JPG")
								|| title.contains(".gif") || title.contains(".png")
								|| title.contains("px")))						
							output.collect(new Text(title.trim()), new Text(sb
									.toString().trim()));
					}
				} catch (IOException ex) {
					Logger.getLogger(SpeciesGraphBuilderMapper.class.getName())
					.log(Level.SEVERE, null, ex);
				}

			} else {
				return;
			}

		} catch (Exception ex) {
			Logger.getLogger(SpeciesGraphBuilderMapper.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	private boolean retriveNode(NodeList nodeList, String nodeName) {

		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);

			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (tempNode.getNodeName().equals(nodeName)) {
					nodeValue = tempNode.getTextContent();
					return true;
				}

				if (tempNode.hasChildNodes()) {
					if (retriveNode(tempNode.getChildNodes(), nodeName)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isForeignCharacter(String s) {

		for(char character : s.toCharArray()) {
			if (!(Character.UnicodeBlock.of(character) == Character.UnicodeBlock.BASIC_LATIN || 
					Character.UnicodeBlock.of(character) == Character.UnicodeBlock.LATIN_1_SUPPLEMENT || 
					Character.UnicodeBlock.of(character) == Character.UnicodeBlock.LATIN_EXTENDED_A ||
					Character.UnicodeBlock.of(character) == Character.UnicodeBlock.GENERAL_PUNCTUATION)) {
				return true;
			}				
		}
		return false;
	}

	private List<String> getOutLinks(String textStr) throws Exception {

		List<String> outlinks = new ArrayList<String>();
		boolean found = false;

		Pattern regex = Pattern.compile("(== Taxonavigation ==|==Taxonavigation==)([^(==)]+)");
		Matcher match = regex.matcher(textStr);
		while (match.find()) {
			String page = match.group();
			getLinks(page, outlinks);
			found = true;
		}

		regex = Pattern.compile("(== Name ==|==Name==)([^(==)]+)");
		match = regex.matcher(textStr);
		while (match.find()) {
			String page = match.group();
			getLinks(page, outlinks);
			found = true;
		}

		/*regex = Pattern.compile("(== References ==|==References==)([^(==)]+)");
		match = regex.matcher(textStr);
		while (match.find()) {
			String page = match.group();
			getLinks(page, outlinks);
			found = true;
		}*/

		if (!found) {
			outlinks.add("");
		}
		return outlinks;
	}

	private void getLinks(String page, List<String> links) {
		int start = page.indexOf("[[");
		int end;
		while (start > 0) {
			start = start + 2;

			int squareBracket = page.indexOf("[[", start);						
			end = page.indexOf("]]", start);

			if(squareBracket > 0) {
				if(squareBracket < end)
					start = squareBracket + 2;
			}

			if (end == -1) {
				break;
			}

			String toAdd = page.substring(start);
			toAdd = toAdd.substring(0, (end - start));						
			links.add(toAdd);
			start = page.indexOf("[[", end + 1);
		}

		return;
	}

}