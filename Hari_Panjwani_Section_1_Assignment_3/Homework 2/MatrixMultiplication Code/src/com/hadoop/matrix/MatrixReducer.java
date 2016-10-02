package com.hadoop.matrix;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MatrixReducer extends Reducer<Text, Text, Text, Text> {
		
	protected void reduce(Text key, Iterable<Text> values, Context context) 
			throws IOException, InterruptedException {
		
		//getting the configuration
		Configuration config = context.getConfiguration();
		
		 /*
		 * getting the column of matrix A and row of matrix B
		 * because for computing the every block of matrix C
		 * we need to multiply and add the number and the addition
		 * is going to be q times
		 */		 
		int q = Integer.parseInt(config.get("q"));
		
		//creating the hashmap for matrix A and matrix B with key as Integer and value as Float
		HashMap<Integer, Float> matrixA = new HashMap<Integer, Float>();
		HashMap<Integer, Float> matrixB = new HashMap<Integer, Float>();
		
		String[] outputValue;
		
		/*
		 * We will have Iterable for values for each key, we will traverse
		 * the values and put the value in the appropriate HashMap
		 */
		for(Text value : values) {
			
			//spliting the value based on delimeter ","
			outputValue = value.toString().split(",");
			//System.out.println(value.toString());
			
			//if the matrix is A, key will be the column of matrix A and it's value
			if(outputValue[0].equals("A")) {
				matrixA.put(Integer.parseInt(outputValue[1]), Float.parseFloat(outputValue[2]));
			}
			//if the matrix is B, key will be the row of matrix B and it's value
			else {
				matrixB.put(Integer.parseInt(outputValue[1]), Float.parseFloat(outputValue[2]));
			}
		}
		
		float result = 0.0f;
		float a_pq;
		float b_qr;
		
		//multiply the column value and row value and add the result
		for(int k=0; k<q; k++) {
			a_pq = matrixA.containsKey(k) ? matrixA.get(k) : 0;
			b_qr = matrixB.containsKey(k) ? matrixB.get(k) : 0;
			result += a_pq * b_qr;
		}
		
		//put the result in the output file
		if(result != 0.0f)
			context.write(null, new Text(key.toString() + "," + Float.toString(result)));
		}
}
