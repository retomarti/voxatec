package com.voxatec.argame.objectModel.beans;

import java.util.Vector;

import com.voxatec.argame.objectModel.beans.NamedObject;
import com.voxatec.argame.objectModel.beans.Story;


public class Adventure extends NamedObject {

	// attributes
	private Vector<Story> storyList;

	// accessors
	public Vector<Story> getStoryList() {
		return storyList;
	}

	public void setStoryList(Vector<Story> aStoryList) {
		storyList = aStoryList;
	}
	
}
