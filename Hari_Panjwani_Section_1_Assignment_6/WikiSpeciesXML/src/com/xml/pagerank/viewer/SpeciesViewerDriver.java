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
package com.xml.pagerank.viewer;

import com.xml.pagerank.builder.*;
import com.xml.pagerank.iterator.*;
import org.apache.hadoop.fs.Path; 
import org.apache.hadoop.io.*; 
import org.apache.hadoop.io.Text; 
import org.apache.hadoop.mapred.JobClient; 
import org.apache.hadoop.mapred.JobConf; 

import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
  
  
 @SuppressWarnings("deprecation")
public class SpeciesViewerDriver { 
  
   public static void ViewerRunner(String input, String output, int iter) { 
     
	 JobClient client = new JobClient(); 
     JobConf conf = new JobConf(SpeciesViewerDriver.class); 
     conf.setJobName("Species Viewer"); 
  
     //~dk
     //conf.setInputFormat(org.apache.hadoop.mapred.SequenceFileInputFormat.class); 

     conf.setOutputKeyClass(FloatWritable.class); 
     conf.setOutputValueClass(Text.class); 
  
     /*if (args.length < 2) { 
       System.out.println("Usage: SpeciesViewerDriver <input path> <output path>"); 
       System.exit(0); 
     } */

     //~dk
     //conf.setInputPath(new Path(args[0])); 
     //conf.setOutputPath(new Path(args[1])); 
     FileInputFormat.setInputPaths(conf, new Path(input+iter));
     System.out.println("InputFilePath: " + input+iter);
     FileOutputFormat.setOutputPath(conf, new Path(output));
  
     conf.setMapperClass(SpeciesViewerMapper.class); 
     conf.setReducerClass(org.apache.hadoop.mapred.lib.IdentityReducer.class); 
  
     client.setConf(conf); 
     try { 
       JobClient.runJob(conf); 
     } catch (Exception e) { 
       e.printStackTrace(); 
     } 
   } 
 } 
 