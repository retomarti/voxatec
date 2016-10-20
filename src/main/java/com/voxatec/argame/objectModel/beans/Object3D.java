package com.voxatec.argame.objectModel.beans;

import com.voxatec.argame.objectModel.beans.Material;
import com.voxatec.argame.objectModel.beans.NamedObject;


public class Object3D extends NamedObject {

	// attributes
	private String objFileName;
	private Material material;
	private byte[] image;

    // accessors
	public String getObjFileName() {
		return objFileName;
	}
	
	public void setObjFileName(String objFileName) {
		this.objFileName = objFileName;
	}
	
    public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	}
	
}
