package com.xml.pagerank.builder;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class SpeciesGraphBuilderReducer extends MapReduceBase implements
Reducer<Text, Text, Text, Text> {

	public static double initialRank = 0;

	public void configure(JobConf job) {
		initialRank = Double.parseDouble(job.get("intialRank"));
	}

	public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {

		reporter.setStatus(key.toString());
		String toWrite = "";
		//int count = 0;
		Double initPgRk=initialRank;

		while (values.hasNext()) {
			String page = ((Text) values.next()).toString();
			toWrite += " " + page;
			//count += 1;
			//initPgRk+=0.1;
		}

		//IntWritable i = new IntWritable(count);
		//String num = (i).toString();
		String num = initPgRk.toString();
		toWrite = num + ":" + toWrite;
		output.collect(key, new Text(toWrite));
	}

}
