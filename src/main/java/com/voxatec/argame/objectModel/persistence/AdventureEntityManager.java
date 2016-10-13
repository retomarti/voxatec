package com.voxatec.argame.objectModel.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.springframework.web.util.HtmlUtils;

import com.voxatec.argame.objectModel.beans.Adventure;
import com.voxatec.argame.objectModel.beans.Object3D;
import com.voxatec.argame.objectModel.beans.Riddle;
import com.voxatec.argame.objectModel.beans.Scene;
import com.voxatec.argame.objectModel.beans.Story;

public class AdventureEntityManager extends EntityManager {
	
    public Adventure getAdventurePrototype() {
    	
    	// Adventure
    	Adventure adventure = new Adventure();
    	adventure.setName(" ");
    	adventure.setText(" ");
    	
    	// Story
    	Story story = new Story();
    	story.setName(" ");
    	story.setText(" ");
    	Vector<Story> storyList = new Vector<Story>();
    	storyList.add(story);
    	adventure.setStoryList(storyList);
    	
    	// Scene
    	Scene scene = new Scene();
    	scene.setName(" ");
    	scene.setText(" ");
    	Vector<Scene> sceneList = new Vector<Scene>();
    	sceneList.add(scene);
    	story.setSceneList(sceneList);
    	
    	// Riddle
    	Riddle riddle = new Riddle();
    	riddle.setChallengeText(" ");
    	riddle.setResponseText(" ");
    	riddle.setHintText(" ");
    	scene.setRiddle(riddle);
    	
    	// Object3D
    	Object3D obj3D = new Object3D();
    	obj3D.setName(" ");
    	obj3D.setText(" ");
    	obj3D.setObjFileName(" ");
    	scene.setObject3D(obj3D);
    	
    	return adventure;
    }


	public Adventure createAdventure(Adventure adventure) throws SQLException {
		
		if (adventure == null)
			return null;
		
		try {
			this.initConnection();

			// Insert adventure into DB
			String template = "insert into adventure (name,text) values (\"%s\",\"%s\")";
			String name = HtmlUtils.htmlUnescape(adventure.getName());
			String text = HtmlUtils.htmlUnescape(adventure.getText());
			String stmt = String.format(template, name, text);
			this.connection.executeUpdateStatement(stmt);
			
			// Retrieve auto inserted ID value
			Integer lastInsertedId = this.getLastAutoInsertedId();
			adventure.setId(lastInsertedId);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
		
		return adventure;
	}
	
	
	public Vector<Adventure> getAdventures() throws SQLException {

		Vector<Adventure> adventureList = new Vector<Adventure>();

		try {
			this.initConnection();

			String stmt = "select * from v_adventure";
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			while (resultSet.next()) {
				// Adventure
				Adventure adventure = new Adventure();
				adventure.setId(resultSet.getInt("adv_id"));
				adventure.setName(HtmlUtils.htmlEscape(resultSet.getString("adv_name")));
				adventure.setText(HtmlUtils.htmlEscape(resultSet.getString("adv_text")));
				
				adventureList.add(adventure);
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return adventureList;
	}
	
	
	public Adventure getAdventureById(Integer adventureId) throws SQLException {
		
		Adventure theAdventure = null;
		
		try {
			this.initConnection();

			String template = "select * from adventure where id=%d";
			String stmt = String.format(template, adventureId);
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			while (resultSet.next()) {
				// Adventure
				theAdventure = new Adventure();
				theAdventure.setId(adventureId);
				theAdventure.setName(HtmlUtils.htmlEscape(resultSet.getString("name")));
				theAdventure.setText(HtmlUtils.htmlEscape(resultSet.getString("text")));
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return theAdventure;
	}
	
	
	public void updateAdventure(Adventure adventure) throws SQLException {
		
		if (adventure == null)
			return;
		
		try {
			this.initConnection();

			String template = "update adventure set name=\"%s\", text=\"%s\" where id=%d";
			String name = HtmlUtils.htmlUnescape(adventure.getName());
			String text = HtmlUtils.htmlUnescape(adventure.getText());
			Integer id  = adventure.getId();
			String stmt = String.format(template, name, text, id);
			
			this.connection.executeUpdateStatement(stmt);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}
	
}
