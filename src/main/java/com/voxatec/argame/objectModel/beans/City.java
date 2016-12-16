package com.voxatec.argame.objectModel.beans;

import java.util.*;

import com.voxatec.argame.objectModel.beans.NamedObject;


public class City extends NamedObject {

    // attributes
    private String country;
    private String zip;
    private Vector<CacheGroup> cacheGroupList;
    private Vector<Adventure> adventureList;
    
	// accessors
    public void setCountry(String aCountry) {
    	country = aCountry;
    }
    
    public String getCountry() {
    	return country;
    }
    
    public void setZip(String zip) {
    	this.zip = zip;
    }
    
    public String getZip() {
    	return zip;
    }
    
    public void setCacheGroupList(Vector<CacheGroup> cacheGroupList) {
    	this.cacheGroupList = cacheGroupList;
    }
    
    public Vector<CacheGroup> getCacheGroupList() {
    	return cacheGroupList;
    }
    
    public Vector<Adventure> getAdventureList() {
		return adventureList;
	}

	public void setAdventureList(Vector<Adventure> adventureList) {
		this.adventureList = adventureList;
	}
    
}
