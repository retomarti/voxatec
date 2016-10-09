package com.voxatec.argame.objectModel.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.springframework.web.util.HtmlUtils;

import com.voxatec.argame.objectModel.beans.Adventure;
import com.voxatec.argame.objectModel.beans.Cache;
import com.voxatec.argame.objectModel.beans.Story;


public class StoryEntityManager extends EntityManager {

	public Story createStory(Story story) throws SQLException {
		
		if (story == null)
			return null;
		
		try {
			this.initConnection();

			// Insert story into DB
			String template = "insert into story (adventure_id,name,text,seq_nr) values (%d,\"%s\",\"%s\",%d)";
			Integer advId = story.getAdventureId();
			String name = HtmlUtils.htmlUnescape(story.getName());
			String text = HtmlUtils.htmlUnescape(story.getText());
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
			String text = HtmlUtils.htmlUnescape(story.getText());
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
	

	public Vector<Adventure> getAdventureStories() throws SQLException {

		Vector<Adventure> adventureList = new Vector<Adventure>();
		Vector<Story> storyList = null;

		try {
			this.initConnection();

			String stmt = "select * from v_adventure_story";
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			Adventure adventure = null;
			Story story = null;
			Cache cache = null;

			while (resultSet.next()) {
				// Adventure
				int adventureId = resultSet.getInt("adv_id");
				if (adventure == null || adventureId != adventure.getId()) {
					adventure = new Adventure();
					adventure.setId(resultSet.getInt("adv_id"));
					adventure.setName(HtmlUtils.htmlEscape(resultSet.getString("adv_name")));
					adventure.setText(HtmlUtils.htmlEscape(resultSet.getString("adv_text")));
					storyList = new Vector<Story>();
					adventure.setStoryList(storyList);
				
					adventureList.add(adventure);
				}

				// Story
				story = new Story();
				story.setId(resultSet.getInt("sto_id"));
				story.setSeqNr(resultSet.getInt("sto_seq_nr"));
				story.setName(HtmlUtils.htmlEscape(resultSet.getString("sto_name")));
				story.setText(HtmlUtils.htmlEscape(resultSet.getString("sto_text")));
				storyList.add(story);
				
				// Start Cache
				cache = new Cache();
				cache.setId(resultSet.getInt("cah_id"));
				cache.setName(resultSet.getString("cah_name"));
				cache.setStreet(resultSet.getString("cah_street"));
				cache.setGps_lat(resultSet.getBigDecimal("cah_gps_lat"));
				cache.setGps_long(resultSet.getBigDecimal("cah_gps_long"));
				story.setStartCache(cache);

			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return adventureList;
	}
	
}
