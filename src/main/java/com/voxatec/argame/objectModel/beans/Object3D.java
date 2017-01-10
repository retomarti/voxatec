package com.voxatec.argame.objectModel.beans;

import java.util.Vector;

import com.voxatec.argame.objectModel.beans.Material;
import com.voxatec.argame.objectModel.beans.NamedObject;
import com.voxatec.argame.objectModel.beans.Texture;


public class Object3D extends NamedObject {

	// attributes
	private String objFileName;
	private String mtlFileName;
	private Vector<Texture> textureList;

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
	
	public void setTextureList(Vector<Texture> textureList) {
		this.textureList = textureList;
	}
	
	public Vector<Texture> getTextureList() {
		return textureList;
	}
	
}
