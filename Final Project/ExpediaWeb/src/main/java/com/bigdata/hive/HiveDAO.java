package com.bigdata.hive;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bigdata.kafka.KafkaProducer;
import com.bigdata.stanfordNLP.NLP;

public class HiveDAO {

	private static String driverName = "org.apache.hive.jdbc.HiveDriver";

	/**
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public static Connection con = null;

	public static Connection getConnection() throws SQLException, ClassNotFoundException {

		//loading the driver
		Class.forName(driverName);

		//making the connection
		Connection con = DriverManager.getConnection("jdbc:hive2://localhost:10000/expedia", "hiveuser", "hiveuser");
		return con;
	}
	
	public static boolean insertReviews(int hotelId, String message)
	{
		boolean ret = false;
		try
		{
			Connection con = getConnection();
			Statement st = con.createStatement();
			NLP.init();
			double score = NLP.findSentiment(message);
			String sql = "Insert into table hotel_review_kafka values (" + hotelId +", "+score+ ", \""+ message + " \")";
			
			st.executeQuery(sql);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(con != null)
			{
				try
				{
					con.close();
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		ret = true;
		return ret;
	}

	public static boolean updateReviewScore(int hotelId, double score)
	{
		boolean ret = false;
		try
		{
			Connection con = getConnection();
			String sql = "update expedia_hotel_review set review_score = 0.8 * review_score + 0.2 * "+score+" where hotel_id = "+hotelId;
			Statement statement = con.createStatement();
			statement.executeQuery(sql);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(con != null)
			{
				try
				{
					con.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		ret = true;
		return ret;
	}
	
public static float getScore(int hotelId) {
		
		try {
			
			Connection con = getConnection();
			
			//creating the statement
			Statement stmt = con.createStatement();

			stmt.execute("use expedia");

			System.out.println("Checking Review Score");


			String sql = "select hotel_id, review_score from expedia_hotel_review where hotel_id = " + hotelId;

			System.out.println("Running: " + sql);

			ResultSet reviewSet = stmt.executeQuery(sql);

			//Map<Integer, Float> reviewMap = new HashMap<Integer, Float>();

			float score = 0;
			while(reviewSet.next())
			{
				//System.out.println(reviewSet.getInt(1) + ":" + reviewSet.getFloat(2));
				//reviewMap.put(reviewSet.getInt(1), reviewSet.getFloat(2));
				
				score = reviewSet.getFloat(2);
			}
			//System.out.println(reviewMap.size());
			return score;
	      }
		catch(Exception e) {
			e.printStackTrace();
		}

		return -1;
}
	public static void main(String[] args) {
		KafkaProducer kf = new KafkaProducer();
		//kf.generatePositiveReviews(131363);
		kf.generateNegativeReviews(131363);
	}
}