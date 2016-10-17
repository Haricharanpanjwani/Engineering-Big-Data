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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

// import org.apache.nutch.parse.Parse; 
// import org.apache.nutch.parse.ParseException; 
// import org.apache.nutch.parse.ParseUtil; 
// import org.apache.nutch.protocol.Content; 

import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;

import com.species.Driver.WikiSpecies;


public class SpeciesGraphBuilder { 
	
	//BuilderJob
	public static void BuilderRunner(String input, String output, String initialRank) throws Exception
	{ 
		JobClient client = new JobClient(); 
		JobConf conf = new JobConf(SpeciesGraphBuilder.class); 
		conf.setJobName("Page-rank Species Graph Builder"); 

		//conf.setOutputKeyClass(Text.class); 
		//conf.setOutputValueClass(Text.class); 
		conf.setMapperClass(SpeciesGraphBuilderMapper.class); 
		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(Text.class);
		
		conf.set("intialRank", initialRank);

		//conf.setInputFormat(org.apache.hadoop.mapred.TextInputFormat.class); 
		//conf.setOutputFormat(org.apache.hadoop.mapred.SequenceFileOutputFormat.class); 

		conf.setReducerClass(SpeciesGraphBuilderReducer.class); 
		//conf.setCombinerClass(SpeciesGraphBuilderReducer.class); 

		//conf.setInputPath(new Path("graph1")); 
		//conf.setOutputPath(new Path("graph2")); 
		// take the input and output from the command line
		FileInputFormat.setInputPaths(conf, new Path(input));
		FileOutputFormat.setOutputPath(conf, new Path(output));

		client.setConf(conf); 
		try { 
			JobClient.runJob(conf);
//			Job job = new Job(conf);

				FileSystem fs = FileSystem.get(conf);
				BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(new Path(output+"/part-00000"))));
				String line;
				line=br.readLine();
				while (line != null){
					StringTokenizer st = new StringTokenizer(line);
					while(st.hasMoreTokens()) {	
						WikiSpecies.species.add(st.nextToken());
					}
					line=br.readLine();
				}
				
				WikiSpecies.speciesCount = WikiSpecies.species.size();
				
				System.out.println("Set has " + WikiSpecies.species.size());
				
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
	} 
}  
