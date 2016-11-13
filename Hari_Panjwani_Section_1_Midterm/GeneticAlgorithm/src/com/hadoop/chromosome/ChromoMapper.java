package com.hadoop.chromosome;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import com.hadoop.ga.GeneticAlgoRunner;

@SuppressWarnings("deprecation")
public class ChromoMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>
{
	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, 
					Reporter reporter) throws IOException {
		try	{
			for(int i=0; i < GeneticAlgoRunner.poolSize; i++)
				output.collect(new Text("one"), new Text(String.valueOf(i)));
		}
		catch(Exception e) {
			System.out.println("exception occurred in mapper");
			e.printStackTrace();
		}
	}

}
