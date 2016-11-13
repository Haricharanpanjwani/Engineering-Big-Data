package com.hadoop.ga;

import com.hadoop.chromosome.ChromoMapper;
import com.hadoop.chromosome.ChromoReducer;

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
	
	public static int target = 0;
	public static int poolSize = 0;

	public static enum MoreIterations {
		validSolution
	}

	public static void evolutionaryGA(String inputFile, String outputLocation) throws Exception {

		int steps = 0;

		while(true){
			
			JobConf chromoConfig = new JobConf(GeneticAlgoRunner.class);
			chromoConfig.setJobName("Fitness_Score_And_Chromosome");						

			chromoConfig.setMapperClass(ChromoMapper.class);
			chromoConfig.setReducerClass(ChromoReducer.class);

			chromoConfig.setOutputKeyClass(Text.class);
			chromoConfig.setOutputValueClass(Text.class);
			
			Path inputPath = new Path(outputLocation + "\\readOutput");
			Path outputPath = new Path(outputLocation + "\\Output");
			FileInputFormat.setInputPaths(chromoConfig,  inputPath);
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

			gaConfig.setMapperClass(GeneticMapper.class);
			gaConfig.setReducerClass(GeneticReducer.class);

			gaConfig.setOutputKeyClass(Text.class);
			gaConfig.setOutputValueClass(Text.class);			

			Path resultOutputPath = new Path(outputLocation + "\\Result");
			FileInputFormat.setInputPaths(gaConfig, outputPath);
			FileOutputFormat.setOutputPath(gaConfig, resultOutputPath);

			Job gaJob = new Job(gaConfig);

			try {
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