package com.voxatec.argame.objectModel.beans;

import java.util.*;

import com.voxatec.argame.objectModel.beans.NamedObject;


public class City extends NamedObject {

    // attributes
    private String country;
    private String zip;
    private List<CacheGroup> cacheGroupList;
    private List<Adventure> adventureList;
    
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
    
    public List<CacheGroup> getCacheGroupList() {
    	return cacheGroupList;
    }
    
    public List<Adventure> getAdventureList() {
		return adventureList;
	}

	public void setAdventureList(List<Adventure> adventureList) {
		this.adventureList = adventureList;
	}
	
	public Adventure firstAdventure() {
		if (this.getAdventureList() != null & this.getAdventureList().size() > 0) {
			return this.getAdventureList().get(0);
		}
		else {
			return null;
		}
	}

    
}
