package com.bigdata.expedia;

public class Hotel {
		
	public int id;
	public String name;
	public String city;
	public String address;
	public int rating;
	public float reviewScore;
	public int destinationID;

	public int getDestinationID() {
		return destinationID;
	}

	public void setDestinationID(int destinationID) {
		this.destinationID = destinationID;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public float getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(float reviewScore) {
		this.reviewScore = reviewScore;
	}
}
