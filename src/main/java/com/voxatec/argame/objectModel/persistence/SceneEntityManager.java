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


public class SceneEntityManager extends EntityManager {
	
	public Vector<Adventure> getAdventureScenes() throws SQLException {

		Vector<Adventure> adventureList = new Vector<Adventure>();
		Vector<Story> storyList = null;
		Vector<Scene> sceneList = null;

		try {
			this.initConnection();

			String stmt = "select * from v_adventure_scene";
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			Adventure adventure = null;
			Story story = null;
			Scene scene = null;
			Riddle riddle = null;
			Object3D obj3D = null;

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
				int storyId = resultSet.getInt("sto_id");
				if (!resultSet.wasNull() && (story == null || storyId != story.getId())) {
					story = new Story();
					story.setId(storyId);
					story.setAdventureId(adventureId);
					story.setSeqNr(resultSet.getInt("sto_seq_nr"));
					story.setName(HtmlUtils.htmlEscape(resultSet.getString("sto_name")));
					story.setText(HtmlUtils.htmlEscape(resultSet.getString("sto_text")));
					sceneList = new Vector<Scene>();
					story.setSceneList(sceneList);
					storyList.add(story);
				}
				
				// Scene
				int sceneId = resultSet.getInt("sce_id");
				if (!resultSet.wasNull()) {
					scene = new Scene();
					scene.setId(sceneId);
					scene.setSeqNr(resultSet.getInt("sce_seq_nr"));
					scene.setName(resultSet.getString("sce_name"));
					scene.setText(HtmlUtils.htmlEscape(resultSet.getString("sce_text")));
					sceneList.add(scene);
					
					// Riddle
					int riddleId = resultSet.getInt("rid_id");
					if (!resultSet.wasNull()) {
						riddle = new Riddle();
						riddle.setId(riddleId);
						riddle.setChallengeText(HtmlUtils.htmlEscape(resultSet.getString("rid_challenge")));
						riddle.setResponseText(HtmlUtils.htmlEscape(resultSet.getString("rid_response")));
						riddle.setHintText(HtmlUtils.htmlEscape(resultSet.getString("rid_hint")));
						scene.setRiddle(riddle);
					}
					
					// Object3D
					int obj3DId = resultSet.getInt("obj_id");
					if (!resultSet.wasNull()) {
						obj3D = new Object3D();
						obj3D.setId(obj3DId);
						obj3D.setName(HtmlUtils.htmlEscape(resultSet.getString("obj_name")));
						obj3D.setText(HtmlUtils.htmlEscape(resultSet.getString("obj_text")));
						obj3D.setObjFileName(HtmlUtils.htmlEscape(resultSet.getString("obj_file_name")));
						scene.setObject3D(obj3D);
					}
				}
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return adventureList;
	}
	
	
	private Riddle createRiddle(Riddle riddle) throws SQLException {
		if (riddle == null)
			return null;
		
		// Insert story into DB
		String template = "insert into riddle (challenge_text,response_text,hint_text) values (\"%s\",\"%s\",\"%s\")";
		String challengeText = HtmlUtils.htmlUnescape(riddle.getChallengeText());
		String responseText = HtmlUtils.htmlUnescape(riddle.getResponseText());
		String hintText = HtmlUtils.htmlUnescape(riddle.getHintText());
		String stmt = String.format(template, challengeText, responseText, hintText);
		this.connection.executeUpdateStatement(stmt);
		
		// Retrieve auto inserted ID value
		Integer lastInsertedId = this.getLastAutoInsertedId();
		riddle.setId(lastInsertedId);
		
		return riddle;
	}
	
	
	private void updateRiddle(Riddle riddle) throws SQLException {

		if (riddle == null)
			return;

		String template = "update riddle set challenge_text=\"%s\", response_text=\"%s\", hint_text=\"%s\" where id=%d";
		String challengeText = HtmlUtils.htmlUnescape(riddle.getChallengeText());
		String responseText = HtmlUtils.htmlUnescape(riddle.getResponseText());
		String hintText = HtmlUtils.htmlUnescape(riddle.getHintText());
		Integer id  = riddle.getId();
		String stmt = String.format(template, challengeText, responseText, hintText, id);
		
		this.connection.executeUpdateStatement(stmt);

	}
	
	
	public Scene createScene(Scene scene) throws SQLException {
		
		if (scene == null)
			return null;
		
		try {
			this.initConnection();

			// Create riddle first
			Riddle riddle = scene.getRiddle();
			Boolean newRiddle = false;
			
			if (riddle != null ) {
				if (riddle.getId() == -1) {
					riddle = this.createRiddle(riddle);
					newRiddle = riddle.getId() != -1;
				}
				else {
					this.updateRiddle(riddle);
				}
			}
			
			// Insert scene into DB
			String template = null;
			String stmt = null;
			
			Integer storyId = scene.getStoryId();
			String name = HtmlUtils.htmlUnescape(scene.getName());
			String text = HtmlUtils.htmlUnescape(scene.getText());
			Integer seqNr = scene.getSeqNr();
			
			// Object3D
			Integer obj3DId = -1;
			if (scene.getObject3D() != null) {
				obj3DId = scene.getObject3D().getId();
			}
			
			if (newRiddle) {
				Integer riddleId = riddle.getId();
				if (obj3DId != -1) {
					template = "insert into scene (story_id,name,text,seq_nr,object3D_id,riddle_id) values (%d,\"%s\",\"%s\",%d,%d,%d)";
					stmt = String.format(template, storyId, name, text, seqNr, obj3DId, riddleId);
				} else {
					template = "insert into scene (story_id,name,text,seq_nr,riddle_id) values (%d,\"%s\",\"%s\",%d,%d)";
					stmt = String.format(template, storyId, name, text, seqNr, riddleId);
				}
			}
			else {
				if (obj3DId != -1) {
					template = "insert into scene (story_id,name,text,seq_nr,object3D_id) values (%d,\"%s\",\"%s\",%d,%d)";
					stmt = String.format(template, storyId, name, text, seqNr, obj3DId);
				} else {
					template = "insert into scene (story_id,name,text,seq_nr) values (%d,\"%s\",\"%s\",%d)";
					stmt = String.format(template, storyId, name, text, seqNr);
				}
			}
			this.connection.executeUpdateStatement(stmt);
			
			// Retrieve auto inserted ID value
			Integer lastInsertedId = this.getLastAutoInsertedId();
			scene.setId(lastInsertedId);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
		
		return scene;
	}
	
	
	public Scene getSceneById(Integer sceneId) throws SQLException {
		Scene theScene = null;
		
		try {
			this.initConnection();

			String template = "select * from scene where id=%d";
			String stmt = String.format(template, sceneId);
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			while (resultSet.next()) {
				// Adventure
				theScene = new Scene();
				theScene.setId(sceneId);
				theScene.setName(HtmlUtils.htmlEscape(resultSet.getString("name")));
				theScene.setText(HtmlUtils.htmlEscape(resultSet.getString("text")));
				theScene.setSeqNr(resultSet.getInt("seq_nr"));
				Integer riddleId = resultSet.getInt("riddle_id");
				theScene.setRiddle(this.getRiddleById(riddleId));
				Integer obj3DId = resultSet.getInt("object3D_id");
				theScene.setObject3D(this.getObject3DById(obj3DId));
			}
			
		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return theScene;
	}
	
	
	public void updateScene(Scene scene) throws SQLException {
		
		if (scene == null)
			return;
		
		try {
			// Update riddle first
			Riddle riddle = scene.getRiddle();
			Boolean newRiddle = false;
			
			if (riddle != null ) {
				if (riddle.getId() == -1) {
					riddle = this.createRiddle(riddle);
					newRiddle = riddle.getId() != -1;
				}
				else {
					this.updateRiddle(riddle);
				}
			}

			// Update scene
			this.initConnection();

			// Scene attributes (object3D itself is not updated, only relation)
			String template = null;
			String stmt = null;
			String name = HtmlUtils.htmlUnescape(scene.getName());
			String text = HtmlUtils.htmlUnescape(scene.getText());
			Integer seqNr = scene.getSeqNr();
			Integer obj3DId = scene.getObject3D().getId();
			Integer id  = scene.getId();

			if (newRiddle) {
				template = "update scene set name=\"%s\", text=\"%s\", seq_nr=%d, object3D_id=%d, riddle_id=%d where id=%d";
				Integer riddleId = riddle.getId();
				stmt = String.format(template, name, text, seqNr, obj3DId, riddleId, id);
			}
			else {
				template = "update scene set name=\"%s\", text=\"%s\", seq_nr=%d, object3D_id=%d where id=%d";
				stmt = String.format(template, name, text, seqNr, obj3DId, id);
			}
			this.connection.executeUpdateStatement(stmt);
						
		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}
	
	private Riddle getRiddleById(Integer riddleId) throws SQLException {
		
		Riddle theRiddle = null;
		
		if (riddleId != -1) {
			String template = "select challenge_text, response_text, hint_text from riddle where id=%d";
			String stmt = String.format(template, riddleId);			
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			while (resultSet.next()) {
				// Adventure
				theRiddle = new Riddle();
				theRiddle.setId(riddleId);
				theRiddle.setChallengeText(HtmlUtils.htmlEscape(resultSet.getString("challenge_text")));
				theRiddle.setResponseText(HtmlUtils.htmlEscape(resultSet.getString("response_text")));
				theRiddle.setHintText(HtmlUtils.htmlEscape(resultSet.getString("hint_text")));
			}
			
			return theRiddle;
		}
		else {
			return null;
		}
	}
	
	
	private Object3D getObject3DById(Integer object3DId) throws SQLException {
		
		Object3D theObj3D = null;
		
		if (object3DId != -1) {
			String template = "select name, text from object3D where id=%d";
			String stmt = String.format(template, object3DId);			
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			while (resultSet.next()) {
				// Adventure
				theObj3D = new Object3D();
				theObj3D.setId(object3DId);
				theObj3D.setName(HtmlUtils.htmlEscape(resultSet.getString("name")));
				theObj3D.setText(HtmlUtils.htmlEscape(resultSet.getString("text")));
			}
			
			return theObj3D;
		}
		else {
			return null;
		}
	}
	

	private void deleteRiddle(Riddle riddle) throws SQLException {
		
		if (riddle != null && riddle.getId() != -1) {
			String template = "delete from riddle where id=%d";
			String stmt = String.format(template, riddle.getId());
			this.connection.executeUpdateStatement(stmt);			
		}
		
	}
	
	
	public void deleteScene(Integer sceneId) throws SQLException {
		
		Scene theScene = null;
		
		if (sceneId == -1)
			return;   // nothing to do
		
		try {
			theScene = this.getSceneById(sceneId);
			
			this.initConnection();
			
			if (theScene != null) {
				// Delete riddle first (if necessary)
				if (theScene.getRiddle() != null && theScene.getRiddle().getId() != -1) {
					this.deleteRiddle(theScene.getRiddle());
				}
				
				// Now delete scene
				String template = "delete from scene where id=%d";
				String stmt = String.format(template, sceneId);
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
