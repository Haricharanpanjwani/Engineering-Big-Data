package com.hadoop.chromosome;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

@SuppressWarnings("deprecation")
public class ChromoNumberMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>{

	public static int poolSize = 0;

	@Override
	public void configure(JobConf conf) {
		// TODO Auto-generated method stub
		poolSize = Integer.parseInt(conf.get("poolsize"));
	}

	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		try	{
			for(int i=0; i < poolSize; i++)
				output.collect(new Text("one"), new Text(String.valueOf(i)));
		}
		catch(Exception e) {
			System.out.println("exception occurred in mapper");
			e.printStackTrace();
		}
	}

}
