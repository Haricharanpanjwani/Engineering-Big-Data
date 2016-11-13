package com.hadoop.ga;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

@SuppressWarnings("deprecation")
public class GeneticMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>
{
	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, 
					Reporter reporter) throws IOException {		

		StringTokenizer input = new StringTokenizer(value.toString(),"\t");
		int outputKey = Integer.parseInt(input.nextToken());
		String outputValue = input.nextToken();

		if(outputKey % 2 != 0)
			output.collect(new Text(String.valueOf(outputKey - 1)), new Text(outputValue));
		else
			output.collect(new Text(String.valueOf(outputKey)), new Text(outputValue));
	}
}
