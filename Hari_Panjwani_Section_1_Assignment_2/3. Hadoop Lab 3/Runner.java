package Cloud.ApacheLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapreduce.Job;

public class Runner {

	
	public static class IpMapper extends MapReduceBase 
    implements Mapper<LongWritable, Text, Text, IntWritable>
	{
	
		// Regular expression to match the IP at the beginning of the line in an
		// Apache access log
		private static final Pattern ipPattern = Pattern.compile("^([\\d\\.]+)\\s");
		
		// Reusable IntWritable for the count
		private static final IntWritable one = new IntWritable(1);
		
		public void map(LongWritable fileOffset, Text lineContents,
			OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
			
			// apply the regex to the line of the access log
			Matcher matcher = ipPattern.matcher(lineContents.toString());
			if(matcher.find())
			{
				// grab the IP
				String ip = matcher.group(1);
				
				// output it with a count of 1
				output.collect(new Text(ip), one);
			}
		}	
	}
	
	public static class IpReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> 
	{

	  public void reduce(Text ip, Iterator<IntWritable> counts,
	      OutputCollector<Text, IntWritable> output, Reporter reporter)
	      throws IOException {
	    
	    int totalCount = 0;
	    
	    // loop over the count and tally it up
	    while (counts.hasNext())
	    {
	      IntWritable count = counts.next();
	      totalCount += count.get();
	    }
	    if(totalCount >= 100)
	    	output.collect(ip, new IntWritable(totalCount));
	  }

	}
	
	public static class SortMap extends MapReduceBase implements Mapper<Object, Text, IntWritable, Text> {

        public void map(Object key, Text value, OutputCollector<IntWritable, Text> collector, Reporter arg3) throws IOException {
            String line = value.toString();
            StringTokenizer stringTokenizer = new StringTokenizer(line);            
            {            	               
                int number = -100;
                String word = "empty";

                if (stringTokenizer.hasMoreTokens()) {          
                	String str0 = stringTokenizer.nextToken();
                    word = str0.trim();
                }

                if (stringTokenizer.hasMoreElements()) {
                    String str1 = stringTokenizer.nextToken();
                    number = Integer.parseInt(str1.trim());
                }
                collector.collect(new IntWritable(number), new Text(word));
            }

        }

    }

    public static class SortReducce extends MapReduceBase implements Reducer<IntWritable, Text, Text, IntWritable> {

        public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<Text, IntWritable> arg2, Reporter arg3) throws IOException {
            
        	while ((values.hasNext())) {
                arg2.collect(values.next(), key);
            }
        }
    }
	
	public static class IntComparator extends WritableComparator {

	    public IntComparator() {
	        super(IntWritable.class);
	    }

	    //@Override
	    public int compare(byte[] b1, int s1, int l1,
	            byte[] b2, int s2, int l2) {

	        Integer v1 = ByteBuffer.wrap(b1, s1, l1).getInt();
	        Integer v2 = ByteBuffer.wrap(b2, s2, l2).getInt();

	        return v1.compareTo(v2) * (-1);
	    }
	}
	
  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
        JobConf conf = new JobConf(Runner.class);
        conf.setJobName("ip-count");                              
        
        conf.setMapperClass(IpMapper.class);
        
        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(IntWritable.class);
        
        conf.setReducerClass(IpReducer.class);
                
        // take the input and output from the command line
        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path("/temp"));

        //JobClient.runJob(conf);
        //-----------------------------------------------------------
        JobConf conf2 = new JobConf(Runner.class);
        conf2.setJobName("sortipcount");      
        
        conf2.setMapperClass(SortMap.class);
        //conf2.setCombinerClass(SortReducce.class);
        conf2.setReducerClass(SortReducce.class);
        
        conf2.setMapOutputKeyClass(IntWritable.class);
        conf2.setMapOutputValueClass(Text.class);
        
        conf2.setOutputKeyClass(Text.class);
        conf2.setOutputValueClass(IntWritable.class);        

        conf2.setInputFormat(TextInputFormat.class);
        conf2.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf2, new Path("/temp"));
        FileOutputFormat.setOutputPath(conf2, new Path(args[1]));

        Job job1 = new Job(conf);
        Job job2 = new Job(conf2);       
        job2.setSortComparatorClass(IntComparator.class);

        job1.submit();
        if (job1.waitForCompletion(true)) {
            job2.submit();
            job2.waitForCompletion(true);
        }
	}

}
