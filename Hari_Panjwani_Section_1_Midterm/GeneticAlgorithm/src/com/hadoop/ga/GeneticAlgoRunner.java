package com.hadoop.ga;

import com.hadoop.chromosome.ChromoNumberMapper;
import com.hadoop.chromosome.ChromoNumberReducer;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;

@SuppressWarnings("deprecation")
public class GeneticAlgoRunner {

	public static enum MoreIterations {
		validSolution
	}

	public static void main(String[] args) throws Exception {

		String inputFile = args[0];
		String outputLocation = args[1];
		String target = args[2];
		String poolSize = args[3];
		String crossoverRate = args[4];
		String mutationRate = args[5];

		int steps = 0;

		while(true){

			JobConf chromoConfig = new JobConf(GeneticAlgoRunner.class);
			chromoConfig.setJobName("Fitness_Score_And_Chromosome");						

			chromoConfig.set("poolsize", poolSize);
			chromoConfig.set("target", target);			
			chromoConfig.set("crossoverRate", crossoverRate);
			chromoConfig.set("mutationRate", mutationRate);

			chromoConfig.setMapperClass(ChromoNumberMapper.class);
			chromoConfig.setReducerClass(ChromoNumberReducer.class);

			chromoConfig.setOutputKeyClass(Text.class);
			chromoConfig.setOutputValueClass(Text.class);

			Path outputPath = new Path(outputLocation + "\\Output");
			FileInputFormat.setInputPaths(chromoConfig,  new Path(inputFile));
			FileOutputFormat.setOutputPath(chromoConfig, outputPath);

			try {
				FileSystem dfs = FileSystem.get(outputPath.toUri(), chromoConfig);
				if (dfs.exists(outputPath)) {
					dfs.delete(outputPath, true);					
				}
				JobClient.runJob(chromoConfig);	
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			JobConf gaConfig = new JobConf(GeneticAlgoRunner.class);
			gaConfig.setJobName("Genetic_Algorithm");

			gaConfig.set("poolsize", poolSize);
			gaConfig.set("target", target);			
			gaConfig.set("crossoverRate", crossoverRate);
			gaConfig.set("mutationRate", mutationRate);

			gaConfig.setMapperClass(GeneticAlgoMapper.class);
			gaConfig.setReducerClass(GeneticAlgoReducer.class);

			gaConfig.setOutputKeyClass(Text.class);
			gaConfig.setOutputValueClass(Text.class);			

			Path resultOutputPath = new Path(outputLocation + "\\Result");
			FileInputFormat.setInputPaths(gaConfig, outputPath);
			FileOutputFormat.setOutputPath(gaConfig, resultOutputPath);

			Job gaJob = new Job(gaConfig);

			try {
				//JobClient.runJob(gaConfig);
				gaJob.waitForCompletion(true);
				Counters gaCounter = gaJob.getCounters();
				long terminationValue = gaCounter.findCounter(MoreIterations.validSolution).getValue();

				if(terminationValue == 1L)
					System.exit(0);

				FileSystem dfs = FileSystem.get(resultOutputPath.toUri(), gaConfig);
				if (dfs.exists(resultOutputPath)) {
					dfs.delete(resultOutputPath, true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			steps ++;

			System.out.println("Number of steps: " + steps );
		}

	}
}