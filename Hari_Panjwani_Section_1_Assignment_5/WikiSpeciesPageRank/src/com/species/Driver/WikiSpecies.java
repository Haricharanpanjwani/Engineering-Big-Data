package com.species.Driver;

import java.util.Set;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

import com.species.builder.SpeciesGraphBuilder;
import com.species.iterator.SpeciesIterDriver;
import com.species.viewer.SpeciesViewerDriver;

public class WikiSpecies {
	
	public static Set<String> species = new HashSet<String>();
	public static int speciesCount = 0;
	
	public static void main(String[] args) {				
		
		try {
			
			String numberOfIteration = args[0];
			String input = args[1];
			String outputIter = args[2];
			String output = args[3];
			String initialRank = args[4];
			String dampening = args[5];
			
			SpeciesGraphBuilder.BuilderRunner(input, outputIter+"0", initialRank);			
			
			int iteration = Integer.parseInt(numberOfIteration);
			
			for(int i=0; i < iteration; i++)
				SpeciesIterDriver.IteratorRunner(outputIter, i, speciesCount, initialRank, dampening);
			
			SpeciesViewerDriver.ViewerRunner(outputIter, output, iteration);
			
		}
		catch(Exception e) {
			System.out.println("Error is thrown while running a job");
			e.printStackTrace();
		}
	}

}
