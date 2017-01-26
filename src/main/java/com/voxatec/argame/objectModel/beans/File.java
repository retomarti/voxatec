package com.voxatec.argame.objectModel.beans;

import com.voxatec.argame.objectModel.beans.NamedObject;

public class File extends NamedObject {

	// attributes
	private String type;
	private String content;

	// methods
	public String getType() {
		return type;
	}
	
	public void setType(String aType) {
		type = aType;
	}
		
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
