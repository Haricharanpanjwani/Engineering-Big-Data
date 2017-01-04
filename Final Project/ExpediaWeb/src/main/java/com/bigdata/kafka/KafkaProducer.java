package com.bigdata.kafka;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.zookeeper.KeeperException;

import com.bigdata.hive.HiveDAO;
import com.bigdata.stanfordNLP.NLP;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;


public class KafkaProducer {

	private static final String topic = "testakay";
	public Properties properties =  null;
	public ProducerConfig producerConfig = null;
	public kafka.javaapi.producer.Producer<String, String> producer = null;

	public KafkaProducer()
	{
		properties = new Properties();
		properties.put("metadata.broker.list", "54.197.10.58:9092");
		properties.put("serializer.class", "kafka.serializer.StringEncoder");
		properties.put("client.id","camus");
		producerConfig = new kafka.producer.ProducerConfig(properties);
	}

	public void insertReview(int hotelId, String hotelReview) {

		try
		{
			producer = new kafka.javaapi.producer.Producer<String, String>(producerConfig);
			KeyedMessage<String, String> message = null;
			message = new KeyedMessage<String, String>(topic, String.valueOf(hotelId)+":"+hotelReview);
			producer.send(message);
			HiveDAO.insertReviews(hotelId, hotelReview);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(producer != null)
			{
				producer.close();
			}
		}
	}
	
	public double generateNegativeReviews(int hotelId)
	{
		NLP.init();
		double score = 0;
		double finalScore = 0.0;
		List<String> reviews = readFile("negative");
		for(String str : reviews)
		{
			double sc = NLP.findSentiment(str) + 1.0;
			score += sc;
		}
		finalScore = score/reviews.size();
		//System.out.println("final -ve score: "+finalScore);
		boolean status = HiveDAO.updateReviewScore(hotelId, finalScore);
		if(!status)
		{
			System.out.println("Update hive statement failed in generateNegativeReviews");
		}
		return finalScore;
	}
	
	public double generatePositiveReviews(int hotelId)
	{
		NLP.init();
		double score = 0;
		double finalScore = 0.0;
		List<String> reviews = readFile("positive");
		int i= 0;
		for(String str : reviews)
		{
			double sc = NLP.findSentiment(str) + 1.0;
			score += sc;
			//System.out.println(i + "temp :"+sc);
			i++;
		}
		finalScore = (score / (reviews.size()));
		//System.out.println("final : " + score + " " +  finalScore + " " +reviews.size());
		
		boolean status = HiveDAO.updateReviewScore(hotelId, finalScore);
		if(!status)
			System.out.println("Update hive statement failed in generatePositiveReviews");
		return finalScore;
	}
	
	public List<String> readFile(String whichFile)
	{
		String csvFile = null;
		List<String> list = new ArrayList<String>();
		if(whichFile.toLowerCase().contains("negative"))
			csvFile = "E:\\projectfiles\\Negative_Reviews.csv"; 
		else
			csvFile = "E:\\projectfiles\\Positive_Reviews.csv";
		System.out.println("File used: "+ csvFile);
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] reviews = line.split(cvsSplitBy);
                //System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");
                list.add(reviews[0]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
		return list;
	}
	
	public float checkScore(int hotelId) {
		
		float score = HiveDAO.getScore(hotelId);

		return score;
	}
}
