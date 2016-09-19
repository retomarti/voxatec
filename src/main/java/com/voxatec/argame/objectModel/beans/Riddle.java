package com.voxatec.argame.objectModel.beans;

import com.voxatec.argame.objectModel.beans.Object;

public class Riddle extends Object {

	// attributes
	private String challengeText;
	private String responseText;
	
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

}
