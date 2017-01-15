package com.voxatec.argame.objectModel.beans;

public class ImageFile extends File {

	// image content
	private byte[] data;	// we don't want to use String attribute due to its implicit encoding
	
	// methods
	public byte[] getData() {
		return this.data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
}
