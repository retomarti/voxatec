package com.voxatec.argame.objectModel.beans;

public class Object {

    // attributes
	private String className;
    private int id;
    
    // constructors
    public Object () {
    	this.className = this.getClass().getSimpleName();
    	this.id = -1;
    }
    
	// accessors
    public String getClassName() {
    	return className;
    }
    
    public void setId (int aId) {
        this.id = aId;
    }
    
    public int getId () {
        return id;
    }
    
}
