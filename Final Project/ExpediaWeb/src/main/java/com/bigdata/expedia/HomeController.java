package com.bigdata.expedia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.taglibs.standard.tei.ForEachTEI;
//import org.apache.kafka.common.security.auth.KafkaPrincipal;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bigdata.hive.ExpediaSearch;
//import com.bigdata.kafka.KafkaProducer;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate );

		return "index";
	}

	@RequestMapping(value = "/search.htm", method = RequestMethod.POST)
	public ModelAndView submitQuery(HttpServletRequest request, HttpServletResponse response) throws SQLException {		
		
		ModelAndView mv= new ModelAndView();
		mv.setViewName("result");

		List<Hotel> hotelList = new ArrayList<Hotel>();
		ExpediaSearch es = new ExpediaSearch();
		
		HashMap<Integer, String> cityMap = new HashMap<Integer, String>();
		cityMap.put(2, "New York");
		cityMap.put(3, "Barcelona");
		cityMap.put(7, "Mumbai");
		cityMap.put(9, "Amsterdam");
		cityMap.put(10, "Rome");
		cityMap.put(13, "Tuscany");		
		cityMap.put(14, "Dubai");
		cityMap.put(15, "Paris");
		cityMap.put(16, "London");
		cityMap.put(18, "Berlin");
		System.out.println("Size of cityMap: " + cityMap.size());
		
		int[] userCountry = {1, 3, 12, 32, 77, 214, 205, 55, 66};
		int[] hotelSite = {1537, 246, 416, 824, 1434, 419, 1522, 1454, 606};
		int rnd = new Random().nextInt(userCountry.length);

		//String checkindate = "";
		Date arrivalDate = null;
		String res = null;			

		String checkindate = request.getParameter("checkindate");
		String destinationID = request.getParameter("city");
		String userLocation = String.valueOf(userCountry[rnd]);
		String hotelMarket = String.valueOf(hotelSite[rnd]);
		String model = request.getParameter("model");

		System.out.println("Model: " + model);		

		if(destinationID == "" || userLocation == "" || hotelMarket == "" ||
				model == "" || checkindate == "") {
			mv.addObject("error", "no_value");
			return mv;
		}

		try {			
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
			arrivalDate = (Date)formatter.parse(checkindate); 
		}
		catch(Exception e) {			
			System.out.println("Error in parsing Date");
			e.printStackTrace();
			mv.addObject("error", "date");
			return mv;
		}

		//Creating instance of azure class
		ExpediaML ml = new ExpediaML();	

		ResultSet resultSet = null;

		/*creating json from form values */
		JSONObject obj = new JSONObject();
		JSONObject inputs = new JSONObject();
		JSONObject input = new JSONObject();		

		if(model.equals("cluster")) {

			JSONArray columnName = new JSONArray();
			columnName.add("srch_destination_id");
			columnName.add("user_location_country");
			columnName.add("hotel_market");
			columnName.add("dist1");
			columnName.add("checkInMonth");

			JSONArray allValues = new JSONArray();
			JSONArray value = new JSONArray();
			value.add(destinationID);
			value.add(userLocation);
			value.add(hotelMarket);
			value.add(2567);
			value.add(arrivalDate.getMonth());
			allValues.add(value);

			input.put("ColumnNames", columnName);
			input.put("Values", allValues);
			inputs.put("input1", input);
			obj.put("Inputs", inputs);

			System.out.println("json data for Random Forest " + obj);

			//converting json to string
			String jsonBody = obj.toString();

			res = ml.callRandomForestService(jsonBody);

			int clusterID = Integer.parseInt(res.substring(1));
			System.out.println("Cluster ID: " + clusterID);

			hotelList = es.getHotelByCluster(clusterID);
			
			for(Hotel h : hotelList) {
				System.out.println("City: " + cityMap.get(Integer.parseInt(destinationID)));
				h.setCity(cityMap.get(Integer.parseInt(destinationID)));
				System.out.println("Hotel city: " + h.city);
			}
			
//			while (resultSet.next()) {
//				//System.out.println(resultSet.getInt(1));
//
//				Hotel h = new Hotel();
//				h.id = resultSet.getInt(1);
//				h.name = resultSet.getString(2);
//				h.city = resultSet.getString(3);
//				h.rating = resultSet.getInt(4);
//				h.reviewScore = resultSet.getFloat(5);
//				h.destinationID =  Integer.parseInt(destinationID);
//
//				hotelList.add(h);
//			}

		}
		else if(model.equals("search")) {

			//resultSet = es.getHotel(Integer.parseInt(destinationID));			
			
			hotelList = es.getHotel(Integer.parseInt(destinationID));
			mv.setViewName("search");
		}
		
		mv.addObject("hotelList", hotelList);
		return mv;

		
	}

	@RequestMapping(value = "/ranking.htm", method = RequestMethod.POST)
	public ModelAndView findRanking(HttpServletRequest request, HttpServletResponse response) throws SQLException {				

		ModelAndView mv= new ModelAndView();
		mv.setViewName("result");

		//Creating instance of azure class
		ExpediaML ml = new ExpediaML();		

		int destinationID = Integer.parseInt(request.getParameter("destinationID"));
		System.out.println("Ranking Destination ID: " + destinationID);

		/*creating json from form values */
		JSONObject obj = new JSONObject();
		JSONObject inputs = new JSONObject();
		JSONObject input = new JSONObject();	

		JSONArray columnName = new JSONArray();
		columnName.add("srch");			

		JSONArray allValues = new JSONArray();
		JSONArray value = new JSONArray();
		value.add(destinationID);
		allValues.add(value);

		input.put("ColumnNames", columnName);
		input.put("Values", allValues);
		inputs.put("input1", input);
		obj.put("Inputs", inputs);

		System.out.println("json data for XGBoost " + obj);

		//converting json to string
		String jsonBody = obj.toString();

		List<String> searchRank = new ArrayList<String>();
		searchRank = ml.callXGBoostService(jsonBody);

		List<Hotel> sortedHotelList = new ArrayList<Hotel>();
		ExpediaSearch es = new ExpediaSearch();

		for(String s : searchRank) {

			System.out.println("Hotel ID: " + s);
			Hotel h = es.getHotelByID(Integer.parseInt(s));

//			Hotel h = new Hotel();
//			h.id = Integer.parseInt(result.getString(1));
//			h.name = result.getString(2);
//			h.city = result.getString(3);
//			h.rating = result.getInt(4);
//			h.reviewScore = result.getFloat(5);

			sortedHotelList.add(h);
		}				

		mv.addObject("hotelList", sortedHotelList);
		return mv;
	}
}
