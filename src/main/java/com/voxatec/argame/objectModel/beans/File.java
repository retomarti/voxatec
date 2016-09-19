package com.voxatec.argame.objectModel.beans;

import com.voxatec.argame.objectModel.beans.NamedObject;

public class File extends NamedObject {

	// attributes
	private String mimeType;
	private String content;

	// methods
	public String getMimeType() {
		return mimeType;
	}
	
	public void setMimeType(String aMimeType) {
		mimeType = aMimeType;
	}
		
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
