package com.voxatec.argame.objectModel.beans;

import com.voxatec.argame.objectModel.beans.Cache;
import com.voxatec.argame.objectModel.beans.NamedObject;
import com.voxatec.argame.objectModel.beans.Object3D;
import com.voxatec.argame.objectModel.beans.Riddle;

public class Scene extends NamedObject {
	
	// attributes
	private int seqNr;
	private Cache cache;
	private Object3D object3D;
	private Riddle riddle;

	// accessors
	public void setSeqNr(int aSeqNr) {
		seqNr = aSeqNr;
	}
	
	public int getSeqNr() {
		return seqNr;
	}
	
	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}
	
	public Object3D getObject3D() {
		return object3D;
	}

	public void setObject3D(Object3D object3d) {
		object3D = object3d;
	}
	
	public void setRiddle(Riddle aRiddle) {
		riddle = aRiddle;
	}
	
	public Riddle getRiddle() {
		return riddle;
	}

}