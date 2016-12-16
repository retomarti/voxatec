package com.voxatec.argame.objectModel.beans;

import java.util.Vector;

public class CacheGroup extends NamedObject {
	
	// private
	private String text;
	private String targetImageDatFileName;
	private String targetImageXmlFileName;
	private Vector<Cache> cacheList;
	private int cityId;
	
	// Accessors
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getTargetImageDatFileName() {
		return targetImageDatFileName;
	}
	
	public void setTargetImageDatFileName(String fileName) {
		this.targetImageDatFileName = fileName;
	}
	
	public String getTargetImageXmlFileName() {
		return targetImageXmlFileName;
	}
	
	public void setTargetImageXmlFileName(String fileName) {
		this.targetImageXmlFileName = fileName;
	}
	
	public Vector<Cache> getCacheList() {
		return cacheList;
	}
	
	public void setCacheList(Vector<Cache> cacheList) {
		this.cacheList = cacheList;
	}
	
	public int getCityId() {
		return cityId;
	}
	
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

}
