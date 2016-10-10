// 
// Author - Jack Hebert (jhebert@cs.washington.edu) 
// Copyright 2007 
// Distributed under GPLv3 
// 
// Modified - Dino Konstantopoulos
// Distributed under the "If it works, remolded by Dino Konstantopoulos, 
// otherwise no idea who did! And by the way, you're free to do whatever 
// you want to with it" dinolicense
// 
package com.species.builder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.StringUtils;

import java.util.*; 
import java.lang.StringBuilder; 
  
 /* 
  * This class reads in a serialized download of wikispecies, extracts out the links, and 
  * foreach link: 
  *   emits (currPage, (linkedPage, 1)) 
  * 
  * 
  */ 
public class SpeciesGraphBuilderMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> { 

	@Override 
	public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException
	{
		// Prepare the input data. 
		String page = value.toString(); 

		//System.out.println("Page:" + page); 
		//if(page.contains("[[") && page.contains("]]")) {			
		
			String title = this.GetTitle(page, reporter); 
			if (title.length() > 0) { 
				title = title.replaceAll(" ", "_");
				title = title.replaceAll(":", "_");
				title = title.toLowerCase();
				reporter.setStatus(title); 
			} else { 
				//System.out.println("Title :" + page + " " + title.length());
				return; 
			} 
	
			ArrayList<String> outlinks = this.GetOutlinks(page); 
			StringBuilder builder = new StringBuilder(); 
			for (String link : outlinks) { 
				link = link.replace(" ", "_"); 
				builder.append(" "); 
				builder.append(link); 
			} 
			output.collect(new Text(title), new Text(builder.toString())); 
		}
	//} 

	public String GetTitle(String page, Reporter reporter) throws IOException{ 
		int end = page.indexOf(",");
		if (end == -1)
			return "";
		return page.substring(0, end);
	} 

	//function to get the list of outlinks from the URL
	public ArrayList<String> GetOutlinks(String page){ 

		int end; 
		//creating a list to store the outlinks
		ArrayList<String> outlinks = new ArrayList<String>();
		//all the outlinks start from "[[", getting the index of "[[", 
		//so that we can extract just the text using substring
		int start=page.indexOf("[[");

		while (start > 0) { 

			start = start+2; 
			end = page.indexOf("]]", start); 
			//if((end==-1)||(end-start<0)) 
			if (end == -1) 
				break;  

			String toAdd = page.substring(start); 
			toAdd = toAdd.substring(0, end-start);
			toAdd.replaceAll("\\:", "_");
			
			int index = toAdd.indexOf(":");
			
			if(index > -1) {
				//System.out.println("True");
				toAdd = toAdd.substring(0, index) + "_" + toAdd.substring(index+1);
			}
			
			if(toAdd.contains(",")) {
				int count = toAdd.indexOf(",");
				String a = toAdd.substring(0, count);
				outlinks.add(a);
				
				String b = toAdd.substring(index+1);
				count = toAdd.indexOf("[[");
				toAdd = toAdd.substring(count+2);
				
				//System.out.println("Link: " + a+ " " + toAdd);
			}
			
			outlinks.add(toAdd); 
			start = page.indexOf("[[", end+1); 
		} 
		return outlinks; 
	} 
}

