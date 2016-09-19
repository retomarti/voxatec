package com.voxatec.argame.objectModel.beans;

import com.voxatec.argame.objectModel.beans.Object;

public class NamedObject extends Object {

	// attributes
    private String name;
    private String text;

    // accessors
    public void setName (String aName) {
    	name = aName;
    }
    
    public String getName() {
    	return name;
    }
    
    public void setText (String aText) {
    	text = aText;
    }
    
    public String getText() {
    	return text;
    }
    
}
