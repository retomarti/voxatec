package com.voxatec.argame.objectModel.beans;


import java.math.BigDecimal;

public class Cache extends NamedObject {

	// attributes
	private int cacheGroupId;
	private String street;
	private String targetImageName;
	private String targetImageFileName;
	private Number targetWidth;
	private BigDecimal gpsLatitude;
	private BigDecimal gpsLongitude;

	// accessors
	public int getCacheGroupId() {
		return cacheGroupId;
	}
	
	public void setCacheGroupId(int cacheGroupId) {
		this.cacheGroupId = cacheGroupId;
	}
	
	public String getStreet() {
		return street;
	}

	public void setStreet(String aStreet) {
		street = aStreet;
	}
	
	public String getTargetImageName() {
		return targetImageName;
	}
	
	public void setTargetImageName(String targetImgName) {
		this.targetImageName = targetImgName;
	}
	
	public String getTargetImageFileName() {
		return targetImageFileName;
	}
	
	public void setTargetImageFileName(String fileName) {
		this.targetImageFileName = fileName;
	}
	
	public BigDecimal getGpsLatitude() {
		return gpsLatitude;
	}

	public void setGpsLatitude(BigDecimal gpsLatitude) {
		this.gpsLatitude = gpsLatitude;
	}

	public BigDecimal getGpsLongitude() {
		return gpsLongitude;
	}

	public void setGpsLongitude(BigDecimal gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}

	public Number getTargetWidth() {
		return targetWidth;
	}

	public void setTargetWidth(Number targetWidth) {
		this.targetWidth = targetWidth;
	}
	
	
	public float geoDistanceTo (float locLatitude, float locLongitude) {
		double earthRadius = 6371000; // meters
		float lat1  = this.getGpsLatitude().floatValue();
		float lng1  = this.getGpsLongitude().floatValue();
		double dLat = Math.toRadians(locLatitude - lat1);
		double dLng = Math.toRadians(locLongitude - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(locLongitude)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}

}
