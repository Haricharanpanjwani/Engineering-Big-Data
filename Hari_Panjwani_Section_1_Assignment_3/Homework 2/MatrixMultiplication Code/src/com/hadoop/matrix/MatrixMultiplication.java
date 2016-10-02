package com.hadoop.matrix;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class MatrixMultiplication {
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException 
	{	
		//Creating the configuration for the job
		Configuration conf = new Configuration();
		GenericOptionsParser parser = new GenericOptionsParser(conf, args);
		args = parser.getRemainingArgs();
		
		//settings the values of the matrix taking the parameters from the command line
		//In our case we have two matrix 2X5 and 5X3 and the result will be 2X3
		//Therefore, p=2, q=5 and r=3
		conf.set("p", args[0]);
		conf.set("q", args[1]);
		conf.set("r", args[2]);
		
		//setting the name and configuration for the job
		Job job = new Job(conf, "matrixmultiplication");

		job.setJarByClass(MatrixMultiplication.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setMapperClass(MatrixMapper.class);
		job.setReducerClass(MatrixReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		//Input file location
		FileInputFormat.setInputPaths(job, new Path("C:\\Users\\saksh\\Eclipse Workspace\\InputAndOutput\\inputMatrix"));
		//Output file location
		FileOutputFormat.setOutputPath(job, new Path("C:\\Users\\saksh\\Eclipse Workspace\\InputAndOutput\\outputMatrix"));

		System.out.println(job.waitForCompletion(true));
	
	}
}
