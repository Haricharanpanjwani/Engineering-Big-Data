package com.bigdata.expedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ExpediaML {

	public static String apiurl;
	public static String apikey;

	// Calling xgboost model
	public List<String> callXGBoostService(String json) {
		System.out.println("calling XGBoost model: ");

		apikey = "0rqmmJGDUY14X8RHmQXB/hZl+ZxGJRTPhJe4+IOnX1Oem7ydxSppLVtwB1E+SYjkB6s0/a837BZzcjskUXeH3w==";
		apiurl = "https://ussouthcentral.services.azureml.net/workspaces/b0c07ddfcf394e40ad0e284c66602ced/services/3a5cf0badf9345ffa1d5a0c6fe13753e/execute?api-version=2.0&details=true";

		String result = rrsHttpPost(json);
		return retrieveSearch(result);
	}

	// Calling random forest model
	public String callRandomForestService(String json) {
		System.out.println("calling random forest model: ");

		apikey = "B76fOBdaAZczdDTjDylZ5U7UcKmGeZX40hm2NdAQ14qAnEtB5rm2pwQHUML04h/DHTy75f7+N0+MXME2egNy6A==";
		apiurl = "https://ussouthcentral.services.azureml.net/workspaces/b0c07ddfcf394e40ad0e284c66602ced/services/abe9371707364c5caef365f7eb89d39d/execute?api-version=2.0&details=true";

		String result = rrsHttpPost(json);
		return retrieveCluster(result);
	}

	/**
	 * Call REST API for retrieving prediction from Azure ML 
	 * @return response from the REST API
	 */	
	public String rrsHttpPost(String jsonBody) {

		HttpPost post;
		HttpClient client;
		StringEntity entity;

		try {

			// create HttpPost and HttpClient object
			post = new HttpPost(apiurl);
			client = HttpClientBuilder.create().build();

			// setup output message by copying JSON body into 
			// apache StringEntity object along with content type
			entity = new StringEntity(jsonBody, HTTP.UTF_8);
			entity.setContentEncoding(HTTP.UTF_8);
			entity.setContentType("text/json");

			// add HTTP headers
			post.setHeader("Accept", "text/json");
			post.setHeader("Accept-Charset", "UTF-8");

			// set Authorization header based on the API key
			post.setHeader("Authorization", ("Bearer "+apikey));
			post.setEntity(entity);

			// Call REST API and retrieve response content
			HttpResponse authResponse = client.execute(post);

			return EntityUtils.toString(authResponse.getEntity());

		}
		catch (Exception e) {   
			System.out.println("Error occurred while calling the service!!");
			return e.toString();
		}
	}	 

	public String retrieveCluster(String input) {

		try {
			JSONParser parser = new JSONParser();	    	
			Object obj = parser.parse(input);	 

			JSONObject json = (JSONObject) obj;

			JSONObject result = (JSONObject) json.get("Results");
			System.out.println(json.get("Results"));

			JSONObject output1 = (JSONObject)result.get("output1");
			System.out.println(result.get("output1"));	

			JSONObject value = (JSONObject)output1.get("value");
			System.out.println(output1.get("value"));

			String res = null; 
			JSONArray strArray = (JSONArray)value.get("Values");				
			Iterator<Object> itr = strArray.iterator();


			while(itr.hasNext()) {
				res = itr.next().toString();
				break;
			}

			StringBuilder sb = new StringBuilder();
			sb.append(res);

			String s = res.substring(2,sb.indexOf("]"));				

			String output =	s.substring(0,s.lastIndexOf('"'));

			//System.out.println(output);
			return output;
		}
		catch(Exception e) {
			System.out.println("Error while parsing output!!");
			e.printStackTrace();;
		}

		return null;
	}
	
	public List<String> retrieveSearch(String input) {

		List<String> searchRank = new ArrayList<String>();
		
		try {
			JSONParser parser = new JSONParser();	    	
			Object obj = parser.parse(input);	 

			JSONObject json = (JSONObject) obj;

			JSONObject result = (JSONObject) json.get("Results");
			System.out.println(json.get("Results"));

			JSONObject output1 = (JSONObject)result.get("output1");
			System.out.println(result.get("output1"));	

			JSONObject value = (JSONObject)output1.get("value");
			System.out.println(output1.get("value"));

			String res = null; 
			JSONArray strArray = (JSONArray)value.get("Values");				
			Iterator<Object> itr = strArray.iterator();			

			while(itr.hasNext()) {
				res = itr.next().toString();
				String[] a = res.split(",");
				
//				StringBuilder sb = new StringBuilder();
//				sb.append(res);
//	
				String s = a[1].substring(1,a[1].indexOf("]"));				
	
				String output =	s.substring(0,s.lastIndexOf('"'));
	
				System.out.println(output);
				
				searchRank.add(output);
			}
			
			return searchRank;
		}
		catch(Exception e) {
			System.out.println("Error while parsing output!!");
			e.printStackTrace();;
		}

		return null;
	}
}
