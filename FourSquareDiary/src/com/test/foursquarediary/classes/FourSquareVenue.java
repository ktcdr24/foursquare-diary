package com.test.foursquarediary.classes;

import android.location.Location;

public class FourSquareVenue {

	private String id;
	private String name;
	private String address;
	private String crossStreet;
	private String type;
	private Location location;
	private String city;
	private String country;
	private int herenow;
	private int beenHere;

	public FourSquareVenue() {
	}

	public FourSquareVenue(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public FourSquareVenue(String id, String name, double lat, double lng) {
		this(id, name);
		Location loc = new Location("FourSquareVenues");
		loc.setLongitude(lng);
		loc.setLatitude(lat);
		this.location = loc;
	}

	public FourSquareVenue(String id, String name, double lat, double lng,
			int beenHere, int herenow) {
		this(id, name, lat, lng);
		this.beenHere = beenHere;
		this.herenow = herenow;
	}

	public FourSquareVenue(String id, String name, String address,
			String crossStreet, double lat, double lng, String city,
			String country, int beenHere, int herenow) {
		this(id, name, lat, lng, beenHere, herenow);
		this.city = city;
		this.country = country;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCrossStreet() {
		return crossStreet;
	}

	public void setCrossStreet(String crossStreet) {
		this.crossStreet = crossStreet;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public void setLocation(double lat, double lng) {
		Location loc = new Location("FourSquareVenues");
		loc.setLatitude(lat);
		loc.setLongitude(lng);
		this.location = loc;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getHerenow() {
		return herenow;
	}

	public void setHerenow(int herenow) {
		this.herenow = herenow;
	}

	public int getBeenHere() {
		return beenHere;
	}

	public void setBeenHere(int beenHere) {
		this.beenHere = beenHere;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.name);//.append(" - ").append(this.location.getLatitude()).append(",").append(this.location.getLongitude());
		return sb.toString();
	}
}
