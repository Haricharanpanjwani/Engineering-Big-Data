package com.hadoop.chromosome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import com.hadoop.ga.GeneticAlgoRunner;

@SuppressWarnings("deprecation")
public class ChromoReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> 
{

	public int target = GeneticAlgoRunner.target;

	@Override
	public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output,  
						Reporter reporter) throws IOException {		
		
		ArrayList<String> fitnessScoreList = new ArrayList<String>();
		
		try{			
			while(values.hasNext())	{
				
				values.next();
				// creating new chromosome based on the target, crossover rate and mutation rate
				Chromosome chromosome = new Chromosome(target);

				/*
					add the “fitness score” as well as “chromosome” string in the fitnessScoreList because 
					we cannot send the object of chromosomes from reducer but we can send the reference
					to the chromosome by sending its fitness score and the binary encoded string which is “chromo”.
				 */
				fitnessScoreList.add(chromosome.score + "_" + chromosome.chromo);

				// sorting the chromosome based on the sorting list
				Collections.sort(fitnessScoreList);
			}

			for(int i=0; i < fitnessScoreList.size(); i++)
				output.collect(new Text(String.valueOf(i)), new Text(fitnessScoreList.get(i)));
		}
		catch(Exception e)	{
			System.out.println("exception occurred in reducer while generating fitness score");
			e.printStackTrace();
		}
	}
}
