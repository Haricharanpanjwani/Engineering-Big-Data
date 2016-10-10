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
package com.species.iterator;

 import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
  
  
 public class SpeciesIterDriver { 
  
	 public static void IteratorRunner(String filePath, int count) { 
		 JobClient client = new JobClient(); 
		 JobConf conf = new JobConf(SpeciesIterDriver.class); 
		 conf.setJobName("Species Iter"); 

		 conf.setNumReduceTasks(5); 

		 //~dk
		 //conf.setInputFormat(org.apache.hadoop.mapred.SequenceFileInputFormat.class); 
		 //conf.setOutputFormat(org.apache.hadoop.mapred.SequenceFileOutputFormat.class); 

		 conf.setOutputKeyClass(Text.class); 
		 conf.setOutputValueClass(Text.class); 

		 // if (args.length < 2) { 
		 // System.out.println("Usage: PageRankIter <input path> <output path>"); 
		 // System.exit(0); 
		 // } 

		 //~dk
		 //conf.setInputPath(new Path(args[0])); 
		 //conf.setOutputPath(new Path(args[1])); 
		 FileInputFormat.setInputPaths(conf, new Path(filePath+count));
		 FileOutputFormat.setOutputPath(conf, new Path(filePath+(count+1)));

		 //conf.setInputPath(new Path("graph2")); 
		 //conf.setOutputPath(new Path("graph3")); 

		 conf.setMapperClass(SpeciesIterMapper.class); 
		 conf.setReducerClass(SpeciesIterReducer.class); 
		 conf.setCombinerClass(SpeciesIterReducer.class); 

		 client.setConf(conf); 
		 try { 
			 JobClient.runJob(conf); 
		 } catch (Exception e) { 
			 e.printStackTrace(); 
		 } 
	 } 
 } 
 