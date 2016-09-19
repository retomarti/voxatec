package com.voxatec.argame.objectModel.beans;

import java.util.*;

import com.voxatec.argame.objectModel.beans.NamedObject;


public class City extends NamedObject {

    // attributes
    private String country;
    private Vector<Adventure> adventureList;
    
	// accessors
    public void setCountry(String aCountry) {
    	country = aCountry;
    }
    
    public String getCountry() {
    	return country;
    }
    
    public Vector<Adventure> getAdventureList() {
		return adventureList;
	}

	public void setAdventureList(Vector<Adventure> adventureList) {
		this.adventureList = adventureList;
	}
    
}
