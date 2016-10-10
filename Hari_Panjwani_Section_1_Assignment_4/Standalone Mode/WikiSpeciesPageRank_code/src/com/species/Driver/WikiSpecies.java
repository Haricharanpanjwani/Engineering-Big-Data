package com.species.Driver;

import com.species.builder.SpeciesGraphBuilder;
import com.species.iterator.SpeciesIterDriver;
import com.species.viewer.SpeciesViewerDriver;

public class WikiSpecies {
	
	public static void main(String[] args) {
		
		try {
			SpeciesGraphBuilder.BuilderRunner(args[1], args[2]+"0");
			
			int iteration = Integer.parseInt(args[0]);
			
			for(int i=0; i < iteration; i++)
				SpeciesIterDriver.IteratorRunner(args[2], i);
			
			SpeciesViewerDriver.ViewerRunner(args[2], args[3], iteration);
			
		}
		catch(Exception e) {
			System.out.println("Error is thrown while running a job");
			e.printStackTrace();
		}
	}

}
