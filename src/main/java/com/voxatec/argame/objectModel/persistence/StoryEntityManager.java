package com.voxatec.argame.objectModel.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.springframework.web.util.HtmlUtils;

import com.voxatec.argame.objectModel.beans.Adventure;
import com.voxatec.argame.objectModel.beans.Cache;
import com.voxatec.argame.objectModel.beans.Object3D;
import com.voxatec.argame.objectModel.beans.Riddle;
import com.voxatec.argame.objectModel.beans.Scene;
import com.voxatec.argame.objectModel.beans.Story;


public class StoryEntityManager extends EntityManager {

	public Vector<Story> getStories() throws SQLException {

		Vector<Story> storyList = new Vector<Story>();

		try {
			this.initConnection();

			String stmt = "select * from v_story";
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			while (resultSet.next()) {
				// Story
				Story story = new Story();
				story.setId(resultSet.getInt("sto_id"));
				story.setName(HtmlUtils.htmlEscape(resultSet.getString("sto_name")));
				story.setText(HtmlUtils.htmlEscape(resultSet.getString("sto_text")));
				story.setSeqNr(resultSet.getInt("sto_seq_nr"));
				
				storyList.add(story);
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return storyList;
	}
	

	public Story createStory(Story story) throws SQLException {
		
		if (story == null)
			return null;
		
		try {
			this.initConnection();

			// Insert story into DB
			String template = "insert into story (adventure_id,name,text,seq_nr) values (%d,\"%s\",\"%s\",%d)";
			Integer advId = story.getAdventureId();
			String name = HtmlUtils.htmlUnescape(story.getName());
			String text = this.queryfiableString(HtmlUtils.htmlUnescape(story.getText()));
			Integer seqNr = story.getSeqNr();
			String stmt = String.format(template, advId, name, text, seqNr);
			this.connection.executeUpdateStatement(stmt);
			
			// Retrieve auto inserted ID value
			Integer lastInsertedId = this.getLastAutoInsertedId();
			story.setId(lastInsertedId);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
		
		return story;
	}
	
	
	public Story getStoryById(Integer storyId) throws SQLException {
		
		Story theStory = null;
		
		try {
			this.initConnection();

			String template = "select * from story where id=%d";
			String stmt = String.format(template, storyId);
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			while (resultSet.next()) {
				// Adventure
				theStory = new Story();
				theStory.setId(storyId);
				theStory.setName(HtmlUtils.htmlEscape(resultSet.getString("name")));
				theStory.setText(HtmlUtils.htmlEscape(resultSet.getString("text")));
				theStory.setSeqNr(resultSet.getInt("seq_nr"));
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return theStory;
	}
	
	
	public void updateStory(Story story) throws SQLException {
		
		if (story == null)
			return;
		
		try {
			this.initConnection();

			String template = "update story set name=\"%s\", text=\"%s\", seq_nr=%d where id=%d";
			String name = HtmlUtils.htmlUnescape(story.getName());
			String text = this.queryfiableString(HtmlUtils.htmlUnescape(story.getText()));
			Integer seqNr = story.getSeqNr();
			Integer id  = story.getId();
			String stmt = String.format(template, name, text, seqNr, id);
			
			this.connection.executeUpdateStatement(stmt);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}
	
	
	public void deleteStory(Integer storyId) throws SQLException {
		
		Story theStory = null;
		
		if (storyId == EntityManager.UNDEF_ID)
			return;   // nothing to do
		
		try {
			theStory = this.getStoryById(storyId);
			
			this.initConnection();
			
			if (theStory != null) {
				// Delete story
				String template = "delete from story where id=%d";
				String stmt = String.format(template, storyId);
				this.connection.executeUpdateStatement(stmt);
			}
		
		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}
		
}
