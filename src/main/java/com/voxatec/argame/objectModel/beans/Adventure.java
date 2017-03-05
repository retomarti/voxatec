package com.voxatec.argame.objectModel.beans;

import java.util.List;

import com.voxatec.argame.objectModel.beans.NamedObject;
import com.voxatec.argame.objectModel.beans.Story;


public class Adventure extends NamedObject {

	// attributes
	private List<Story> storyList;

	// accessors
	public List<Story> getStoryList() {
		return storyList;
	}

	public void setStoryList(List<Story> aStoryList) {
		storyList = aStoryList;
	}
	
	public Cache firstCache() {
		Cache firstCache = null;
		
		if (this.getStoryList().size() > 0) {
			Story firstStory = this.getStoryList().get(0);
			
			List<Scene> sceneList = firstStory.getSceneList();
			if (sceneList != null && sceneList.size() > 0) {
				Scene firstScene = sceneList.get(0);
				
				if (firstScene != null) {
					firstCache = firstScene.getCache();
				}
			}
		}
		return firstCache;
	}
	
}
