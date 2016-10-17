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
import java.io.IOException; 

import org.apache.hadoop.io.Writable; 
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase; 
import org.apache.hadoop.mapred.Mapper; 
import org.apache.hadoop.mapred.OutputCollector; 
import org.apache.hadoop.mapred.Reporter;

import com.species.Driver.WikiSpecies;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text; 
  
  
@SuppressWarnings("deprecation")
public class SpeciesIterMapper extends MapReduceBase implements Mapper<WritableComparable, Writable, Text, Text> { 
	
	public static int speciesCount = 0;
	public static double d = 0;
	public static double initialRank = 0;
	
	public void configure(JobConf job) {
		speciesCount = Integer.parseInt(job.get("speciesCount"));
		d = Double.parseDouble(job.get("dampeningFactor"));
		initialRank = Double.parseDouble(job.get("intialRank"));
	}
	
	public void map(WritableComparable key, Writable value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException { 				
	
		//System.out.println("Species count " + speciesCount);
		
		// get the current page
		String outlinks = ((Text)value).toString();		
		
		int index = outlinks.indexOf(":"); 		
		if (index == -1) 
			return; 
		
		// split into title and PR (tab or variable number of blank spaces)
		String toParse = outlinks.substring(0, index).trim(); 
		String[] splits = toParse.split("\t"); 
		if(splits.length == 0) {
			splits = toParse.split(" ");
			if(splits.length == 0) {
				return;
			}
		}
		
		String pagetitle = splits[0].trim(); 
		String pagerank = splits[splits.length - 1].trim();

		// parse current score	
		double currScore = 0.0;
		
		try { 
			currScore = Double.parseDouble(pagerank); 
		} catch (Exception e) {
			System.out.println("Page Rank Exception     "  + ((Text)value).toString());
			System.out.println(pagerank);
			currScore = initialRank;
		} 
		
		// get number of outlinks
		outlinks = outlinks.substring(index+1);
		//System.out.println("Data: " + outlinks);
		
		String[] pages = outlinks.split(" "); 
		int numoutlinks = 0;
		if (pages.length == 0) {
			numoutlinks = 1;
		} else {
			for (String page : pages) { 
				if(page.length() > 0) {
					numoutlinks = numoutlinks + 1;
				}
			} 
		}

		// collect each outlink, with the dampened PR of its inlink, and its inlink
		Text toEmit = new Text((new Double(d * (currScore / numoutlinks))).toString()); 
		for (String page : pages) { 
			if(page.length() > 0) {
				//page = page.trim();
				output.collect(new Text(page), toEmit); 
				//output.collect(new Text(page), new  Text(" " + pagetitle)); 
			}
		}			
		 		
		double dampening = ((1-d) / speciesCount);		
		DoubleWritable factor = new DoubleWritable(dampening);
		String dampeingFactor = (factor).toString(); 
				
		// collect the inlink with its dampening factor, and all outlinks
		output.collect(new Text(pagetitle), new Text(dampeingFactor)); 
		output.collect(new Text(pagetitle), new Text(" " + outlinks)); 
	} 
	
} 
