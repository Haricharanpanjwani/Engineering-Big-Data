package com.xml.pagerank.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

import com.xml.pagerank.Driver.WikiSpeciesXML;

@SuppressWarnings("deprecation")
public class SpeciesGraphBuilder {

	/**
	 * @param args
	 *            [0] input file directory args[1] output file directory --
	 *            existing directory will be deleted
	 */
//	public static void main(String[] args) throws Exception {
//		try {
//			runJob(args[0], args[1]);			
//		} catch (IOException ex) {
//			Logger.getLogger(SpeciesGraphBuilder.class.getName()).log(
//					Level.SEVERE, null, ex);
//		}
//	}

	@SuppressWarnings("deprecation")
	public static void runJob(String input, String output, String initialRank) throws IOException {
		JobClient client = new JobClient();
		JobConf jobConf = new JobConf(SpeciesGraphBuilder.class);
		jobConf.setJobName("WikiSpecies P/R Graph Builder");

		jobConf.set("xmlinput.start", "<page>");
		jobConf.set("xmlinput.end", "</page>");
		
		jobConf.set("intialRank", initialRank);

		// TODO what is the purpose of io.serializations?
		jobConf.set("io.serializations",
				"org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");

		// specify a map function
		jobConf.setMapperClass(SpeciesGraphBuilderMapper.class);
		// specify a reducer function
		jobConf.setReducerClass(SpeciesGraphBuilderReducer.class);
		// optionally add a combiner
		// conf.setCombinerClass(XReduce.class);

		jobConf.setNumReduceTasks(1);

		jobConf.setInputFormat(XmlInputFormat.class);
		// call a custom formatter to produce XML wrapped output
		// jobConf.setOutputFormat(XmlOutputFormat.class);
		// specify output types
		jobConf.setOutputKeyClass(Text.class);
		jobConf.setOutputValueClass(Text.class);

		Path outPath = new Path(output);
		FileInputFormat.addInputPath(jobConf, new Path(input));
		FileOutputFormat.setOutputPath(jobConf, outPath);

		FileSystem dfs = FileSystem.get(outPath.toUri(), jobConf);
		if (dfs.exists(outPath)) {
			dfs.delete(outPath, true);
		}

		client.setConf(jobConf);
		try {
			JobClient.runJob(jobConf);
			
			FileSystem fs = FileSystem.get(jobConf);
			BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(new Path(output+"/part-00000"))));
			String line;
			line=br.readLine();
			while (line != null){
				StringTokenizer st = new StringTokenizer(line);
				while(st.hasMoreTokens()) {	
					WikiSpeciesXML.species.add(st.nextToken());
				}
				line=br.readLine();
			}
			
			WikiSpeciesXML.speciesCount = WikiSpeciesXML.species.size() - 1;
			
			System.out.println("Set has " + WikiSpeciesXML.species.size());
		} catch (IOException ex) {
			Logger.getLogger(SpeciesGraphBuilder.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

}