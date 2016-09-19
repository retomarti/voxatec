package com.voxatec.argame.objectModel.beans;

import java.util.Vector;

import com.voxatec.argame.objectModel.beans.Cache;
import com.voxatec.argame.objectModel.beans.NamedObject;
import com.voxatec.argame.objectModel.beans.Scene;


public class Story extends NamedObject {
	
	// attributes
	private int seqNr;
	private Cache startCache;
	private Vector<Scene> sceneList;
	
	// accessors
	public void setSeqNr(int aSeqNr) {
		seqNr = aSeqNr;
	}
	
	public int getSeqNr() {
		return seqNr;
	}
	
	public Cache getStartCache() {
		return startCache;
	}

	public void setStartCache(Cache startCache) {
		this.startCache = startCache;
	}
	
	public Vector<Scene> getSceneList() {
		return sceneList;
	}

	public void setSceneList(Vector<Scene> aSceneList) {
		sceneList = aSceneList;
	}

}
