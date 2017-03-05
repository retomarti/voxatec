package com.voxatec.argame.objectModel.beans;

import java.util.List;

import com.voxatec.argame.objectModel.beans.NamedObject;
import com.voxatec.argame.objectModel.beans.Scene;


public class Story extends NamedObject {
	
	// attributes
	private int adventureId;
	private int seqNr;
	private List<Scene> sceneList;
	
	// accessors
	public void setAdventureId(int adventureId) {
		this.adventureId = adventureId;
	}
	
	public int getAdventureId() {
		return adventureId;
	}
	
	public void setSeqNr(int aSeqNr) {
		seqNr = aSeqNr;
	}
	
	public int getSeqNr() {
		return seqNr;
	}
	
	public List<Scene> getSceneList() {
		return sceneList;
	}

	public void setSceneList(List<Scene> aSceneList) {
		sceneList = aSceneList;
	}

}
