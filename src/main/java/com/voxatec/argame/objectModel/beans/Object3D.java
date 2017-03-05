package com.voxatec.argame.objectModel.beans;

import java.util.List;

import com.voxatec.argame.objectModel.beans.NamedObject;
import com.voxatec.argame.objectModel.beans.Texture;


public class Object3D extends NamedObject {

	// attributes
	private String objFileName;
	private String mtlFileName;
	private double objScaleFactor = 1.0;
	private List<Texture> textureList;

    // accessors
	public String getObjFileName() {
		return objFileName;
	}
	
	public void setObjFileName(String objFileName) {
		this.objFileName = objFileName;
	}
	
	public String getMtlFileName() {
		return mtlFileName;
	}
	
	public void setMtlFileName(String mtlFileName) {
		this.mtlFileName = mtlFileName;
	}
	
	public void setTextureList(List<Texture> textureList) {
		this.textureList = textureList;
	}
	
	public List<Texture> getTextureList() {
		return textureList;
	}

	public double getObjScaleFactor() {
		return objScaleFactor;
	}

	public void setObjScaleFactor(double objScaleFactor) {
		this.objScaleFactor = objScaleFactor;
	}
	
}
