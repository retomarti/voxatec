package com.voxatec.argame.objectModel.beans;

import com.voxatec.argame.objectModel.beans.Material;
import com.voxatec.argame.objectModel.beans.NamedObject;


public class Object3D extends NamedObject {

	// attributes
	private int objFileLength;
	private Material material;

    // accessors
    public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

    public int getObjFileLength() {
		return objFileLength;
	}

	public void setObjFileLength(int objFileLength) {
		this.objFileLength = objFileLength;
	}
	
}
