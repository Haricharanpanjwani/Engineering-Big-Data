package com.hadoop.ga;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import com.hadoop.chromosome.Chromosome;

@SuppressWarnings("deprecation")
public class GeneticAlgoReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text>{

	int target =0;
	double mutationRate = 0.0;
	double crossoverRate = 0.0;
	int chromoLength = 0;

	@Override
	public void configure(JobConf conf) {
		target = Integer.parseInt(conf.get("target"));
		mutationRate = Double.parseDouble(conf.get("mutationRate"));
		crossoverRate = Double.parseDouble(conf.get("crossoverRate"));
		chromoLength = Integer.parseInt(conf.get("chromoLength"));
	}

	@Override
	public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {


		while(values.hasNext()){

			Chromosome firstChromosome = new Chromosome(target, crossoverRate, mutationRate, chromoLength);
			Chromosome secondChromosome = new Chromosome(target,crossoverRate, mutationRate, chromoLength);

			//selection of two 
			String firstSelected = values.next().toString();
			String secondSelected = values.next().toString();

			String[] fitnessChromoOne = firstSelected.split("_");
			firstChromosome.score  = Double.parseDouble(fitnessChromoOne[0]);
			firstChromosome.chromo = new StringBuffer(fitnessChromoOne[1]);

			String[] fitnessChromoTwo = secondSelected.split("_");
			secondChromosome.score  = Double.parseDouble(fitnessChromoTwo[0]);
			secondChromosome.chromo = new StringBuffer(fitnessChromoTwo[1]);

			//crossover between two selected chromosome
			firstChromosome.crossOver(secondChromosome);

			// mutating the chromosome		
			firstChromosome.mutate();
			secondChromosome.mutate();

			// compute the score of the chromosome
			firstChromosome.scoreChromo(target);
			secondChromosome.scoreChromo(target);			

//			System.out.println("First selected Chromosome: " + firstSelected);
//			System.out.println("Second selected Chromosome: " + secondSelected);
//			
//			System.out.println("First Chromosome total: " + firstChromosome.total + "\nFirst Chromosome isvalid: " + firstChromosome.isValid());
//			System.out.println("Second Chromosome total: " + secondChromosome.total + "\nSecond Chromosome isvalid: " + secondChromosome.isValid());

			// Check to see if either is the solution
			if (firstChromosome.total == target && firstChromosome.isValid()) {				

				output.collect(new Text("Chromosome") , new Text(fitnessChromoOne[1]));
				output.collect(new Text("Fitness Score") , new Text(fitnessChromoOne[0]));
				output.collect(new Text("Solution"), new Text(firstChromosome.decodeChromo().toString()));

				System.out.println("Solution: " + firstChromosome.decodeChromo());
				reporter.getCounter(GeneticAlgoRunner.MoreIterations.validSolution).increment(1L);
			}
			if (secondChromosome.total == target && secondChromosome.isValid()) { 				

				output.collect(new Text("Chromosome") , new Text(fitnessChromoTwo[1]));				
				output.collect(new Text("Fitness Score") , new Text(fitnessChromoTwo[0]));
				output.collect(new Text("Solution"), new Text(secondChromosome.decodeChromo().toString()));

				System.out.println("Solution: " + secondChromosome.decodeChromo());				
				reporter.getCounter(GeneticAlgoRunner.MoreIterations.validSolution).increment(1L);
			}
		}
	}
}
