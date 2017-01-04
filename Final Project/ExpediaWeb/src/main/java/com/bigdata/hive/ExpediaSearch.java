package com.bigdata.hive;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bigdata.expedia.Hotel;

public class ExpediaSearch {

	public Connection con = null;

	public ExpediaSearch() {
		try {
			con = HiveDAO.getConnection();
		}
		catch(Exception e) {
			System.out.println("Error while making connection with Hive");
			e.printStackTrace();
		}		
	}

	public List<Hotel> getHotel(int searchID) {

		if(con == null) {
			System.out.println("Unable to make the connection with hive");
			return null;
		}

		try {
			//creating the statement
			Statement stmt = con.createStatement();

			stmt.execute("use expedia");

			System.out.println("Executing Hotel Search");


			String sql = "select hotel_id, review_score from expedia_hotel_review where hotel_id in ( select prop_id from expedia_hotel_search where srch_id = " + searchID +" ) order by hotel_id";

			System.out.println("Running: " + sql);

			ResultSet reviewSet = stmt.executeQuery(sql);

			Map<Integer, Float> reviewMap = new HashMap<Integer, Float>();

			while(reviewSet.next())
			{
				System.out.println(reviewSet.getInt(1) + ":" + reviewSet.getFloat(2));
				reviewMap.put(reviewSet.getInt(1), reviewSet.getFloat(2));
			}
			System.out.println(reviewMap.size());

			List<Hotel> hotelList = new ArrayList<Hotel>();

			String hotelSql = "select c.prop_id, c.hotel_name, c.city, c.prop_starrating from expedia_hotel_search c where c.srch_id = "+searchID + " order by c.prop_id";
			Statement stmttwo = con.createStatement();
			ResultSet hotelSet = stmttwo.executeQuery(hotelSql);

			while(hotelSet.next())
			{
				//if(hotelSet.getInt(1) == 0)
					//continue;
				if(reviewMap.containsKey(hotelSet.getInt(1))) {
				Hotel h = new Hotel();
				h.id = hotelSet.getInt(1);
				System.out.println("Hotel id in get hotel " + hotelSet.getInt(1));
				h.name = hotelSet.getString(2);
				h.city = hotelSet.getString(3);
				h.rating = hotelSet.getInt(4);
				h.reviewScore = reviewMap.get(h.id);
				h.destinationID = searchID;
				hotelList.add(h);
				}
			}

			System.out.println("HotelList "+hotelList.size());

			return hotelList;
		}
		catch(Exception e) {
			System.out.println("Error while executing the query!");
			e.printStackTrace();
		}

		return null;
	}

	public Hotel getHotelByID(int hotelID) {

		if(con == null) {
			System.out.println("Unable to make the connection with hive");
			return null;
		}

		try {
			//creating the statement
			Statement stmt = con.createStatement();

			stmt.execute("use expedia");

			System.out.println("Executing Hotel Query");

			String reviewSql = "select hotel_id, review_score from expedia_hotel_review where hotel_id = "+hotelID;
			String hotelSql = "select prop_id, hotel_name, city, prop_starrating from expedia_hotel_search where prop_id = "+hotelID;
			System.out.println("executing "+reviewSql );
			ResultSet reviewSet = stmt.executeQuery(reviewSql);

			Statement hotelStatement = con.createStatement();
			ResultSet hotelSet = hotelStatement.executeQuery(hotelSql);
			Hotel h = new Hotel();
			while(reviewSet.next() && hotelSet.next())
			{
				h.id = Integer.parseInt(reviewSet.getString(1));
				h.name = hotelSet.getString(2);
				h.city = hotelSet.getString(3);
				h.rating = hotelSet.getInt(4);
				h.reviewScore = reviewSet.getFloat(2);
			}

			return h;
		}
		catch(Exception e) {
			System.out.println("Error while executing the query!");
			e.printStackTrace();
		}

		return null;
	}

	public List<Hotel> getHotelByCluster(int clusterID) {

		if(con == null) {
			System.out.println("Unable to make the connection with hive");
			return null;
		}

		try {
			//creating the statement
			Statement stmt = con.createStatement();
			stmt.execute("use expedia");
			System.out.println("Executing Cluster Query");


			String sql = "select hotel_id, review_score from expedia_hotel_review where hotel_id in ( select prop_id from expedia_hotel_cluster where clusterid = " + clusterID + ")";

			System.out.println("Running: " + sql);

			ResultSet reviewSet = stmt.executeQuery(sql);

			Map<Integer, Float> reviewMap = new HashMap<Integer, Float>();

			while(reviewSet.next())
			{
				System.out.println(reviewSet.getInt(1) + ":" + reviewSet.getFloat(2));
				reviewMap.put(reviewSet.getInt(1), reviewSet.getFloat(2));
			}
			System.out.println(reviewMap.size());

			List<Hotel> hotelList = new ArrayList<Hotel>();

			String hotelSql = "select c.prop_id, c.hotel_name, c.city, c.prop_starrating from expedia_hotel_cluster c where c.clusterid = "+clusterID;
			Statement stmttwo = con.createStatement();
			ResultSet hotelSet = stmttwo.executeQuery(hotelSql);

			while(hotelSet.next())
			{
				Hotel h = new Hotel();
				h.id = hotelSet.getInt(1);
				h.name = hotelSet.getString(2);
				h.city = hotelSet.getString(3);
				h.rating = hotelSet.getInt(4);
				h.reviewScore = reviewMap.get(h.id);
				hotelList.add(h);
			}

			System.out.println("HotelList "+hotelList.size());

			return hotelList;

			//			
			//			
			//			
			//			
			//			System.out.println("Executing Query");
			//
			//			String sql = "select c.prop_id, c.hotel_name, c.city, c.prop_starrating, r.review_score "
			//					+ "from  expedia_hotel_cluster c JOIN expedia_hotel_review r ON (c.prop_id = r.hotel_id) where clusterID = " + clusterID;
			//
			//			//			String sql = "select prop_id, hotel_name, city, prop_starrating"
			//			//					+ " from  " + tableName + " where clusterID = " + clusterID;
			//
			//			System.out.println("Running: " + sql);
			//			ResultSet res = stmt.executeQuery(sql);
			//
			//			return res;
		}
		catch(Exception e) {
			System.out.println("Error while executing the query!");
			e.printStackTrace();
		}

		return null;
	}
}
