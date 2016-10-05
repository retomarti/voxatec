package com.voxatec.argame.objectModel.beans;

import com.voxatec.argame.objectModel.beans.Object;

public class Riddle extends Object {

	// attributes
	private String challengeText;
	private String responseText;
	private String hintText;
	
	// accessors
	public String getChallengeText() {
		return challengeText;
	}
	
	public void setChallengeText(String challengeText) {
		this.challengeText = challengeText;
	}
	
	public String getResponseText() {
		return responseText;
	}
	
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public String getHintText() {
		return hintText;
	}
	
	public void setHintText(String hintText) {
		this.hintText = hintText;
	}

}
