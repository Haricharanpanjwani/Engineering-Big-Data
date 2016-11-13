package com.hadoop.file;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;

import com.hadoop.ga.GeneticAlgoRunner;

import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;

@SuppressWarnings("deprecation")
public class Runner {

	public static void main(String[] args) throws Exception {

		String inputFile = args[0];
		String outputLocation = args[1];

		while(true){
			
			JobConf readConfig = new JobConf(Runner.class);
			readConfig.setJobName("Read_Input_File");

			readConfig.setMapperClass(ReadMapper.class);
			readConfig.setMapOutputKeyClass(Text.class);
			readConfig.setMapOutputValueClass(Text.class);

			Path outputPath = new Path(outputLocation + "\\readOutput");
			FileInputFormat.setInputPaths(readConfig,  new Path(inputFile));
			FileOutputFormat.setOutputPath(readConfig, outputPath);
			
			Job readJob = new Job(readConfig);

			try {
				if(readJob.waitForCompletion(true))
					// We are calling GeneticAlgoRunner class, once we have
					// completed reading the file
					GeneticAlgoRunner.evolutionaryGA(inputFile, outputLocation);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
