package com.hadoop.matrix;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MatrixMapper extends Mapper<LongWritable, Text, Text, Text> {		
	
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			
			//getting the configuration
			Configuration config = context.getConfiguration();
			
			//getting the row of matrix A and column of matrix B
			//because we want to emit the key value pair
			int p = Integer.parseInt(config.get("p"));
			int r = Integer.parseInt(config.get("r"));
			
			//output key and output value is of type Text
			Text outputKey = new Text();
			Text outputValue = new Text();
			
			//reading the line
			String line = value.toString();
			
			//spliting the line based on ",". As we know input file is
			//matrix name, row, column, value
			String[] matrix = line.split(",");
			
			//checking for the matrix name in the line
			if(matrix[0].equals("A")) {
				
				/*
				 * If the matrix is A, i.e. the first matrix
				 * we are going to emit each value of matrix A
				 * for each column of matrix B with key being
				 * (row of matrix A, column number from 0..r)
				 * and value output value will be rest of the line
				 * i.e.,(matrix name, column, value)
				*/
				for(int i=0; i<r; i++) {
					
					//setting output key to (row of matrix A, column of final matrix C)
					//i.e., (p,r)
					outputKey.set(matrix[1] + "," + i);
					
					//setting output value to (name of matrix (A), column of matrix A, and value)
					//i.e., (A, q, Apq)
					outputValue.set(matrix[0] + "," + matrix[2] + "," + matrix[3]);
					
					//System.out.println("A: " + outputKey + " " + outputValue);
					//emit the key and value for matrix A
					context.write(outputKey, outputValue);
				}
			}
			/*
			 * If the matrix is B, i.e. the second matrix
			 * we are going to emit each value of matrix B
			 * for each row of matrix A with key being
			 * (row of matrix A from 0..p, column of matrix B)
			 * and value output value will be rest of the line
			 * i.e.,(matrix name, row, value)
			*/
			else {
				for(int i=0; i<p; i++) {

					//setting the key(row of matrix A, column of matrix B)
					outputKey.set(i + "," + matrix[2]);
					
					//setting output value to (name of matrix (B), row of matrix B, and value)
					//i.e.,(B,q, Bqr)
					outputValue.set(matrix[0] + "," + matrix[1] + "," + matrix[3]);
					
					//System.out.println("B: " + outputKey + " " + outputValue);
					//emit the key and value for matrix B 
					context.write(outputKey, outputValue);
				}
			}
		}
}
