package com.hadoop.file;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import com.hadoop.chromosome.Chromosome;
import com.hadoop.ga.GeneticAlgoRunner;

@SuppressWarnings("deprecation")
public class ReadMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>
{
	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, 
					Reporter reporter) throws IOException {
		String[] line = value.toString().split(" ");
		if(line[0].toString().contains("Size")) {
			GeneticAlgoRunner.poolSize = Integer.parseInt(line[1].toString());
			output.collect(new Text("populationsize"), new Text(line[1]));
		}
		else if(line[0].toString().contains("Length"))
			Chromosome.chromoLength = Integer.parseInt(line[1].toString());
		else if(line[0].toString().contains("Mutation"))
			Chromosome.mutationRate = Double.parseDouble(line[1].toString());
		else if(line[0].toString().contains("Crossover"))
			Chromosome.crossoverRate = Double.parseDouble(line[1].toString());
		else if(line[0].toString().contains("target"))
			GeneticAlgoRunner.target = Integer.parseInt(line[1].toString());			
	}
}
