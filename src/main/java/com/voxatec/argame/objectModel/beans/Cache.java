package com.voxatec.argame.objectModel.beans;


import java.math.BigDecimal;

public class Cache extends NamedObject {

	// attributes
	private String street;
	private BigDecimal gps_lat;
	private BigDecimal gps_long;

	// accessors
	public String getStreet() {
		return street;
	}

	public void setStreet(String aStreet) {
		street = aStreet;
	}
	
	public BigDecimal getGps_lat() {
		return gps_lat;
	}

	public void setGps_lat(BigDecimal gps_lat) {
		this.gps_lat = gps_lat;
	}

	public BigDecimal getGps_long() {
		return gps_long;
	}

	public void setGps_long(BigDecimal gps_long) {
		this.gps_long = gps_long;
	}

}
