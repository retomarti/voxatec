package com.voxatec.argame.objectModel.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.springframework.web.util.HtmlUtils;

import com.voxatec.argame.objectModel.beans.Adventure;
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
	
	
	public Scene createScene(Scene scene) throws SQLException {
		
		if (scene == null)
			return null;
		
		try {
			this.initConnection();

			// Insert story into DB
			String template = "insert into scene (story_id,name,text,seq_nr) values (%d,\"%s\",\"%s\",%d)";
			Integer storyId = scene.getStoryId();
			String name = HtmlUtils.htmlUnescape(scene.getName());
			String text = HtmlUtils.htmlUnescape(scene.getText());
			Integer seqNr = scene.getSeqNr();
			String stmt = String.format(template, storyId, name, text, seqNr);
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
			this.initConnection();

			// Scene attributes
			String template = "update scene set name=\"%s\", text=\"%s\", seq_nr=%d where id=%d";
			String name = HtmlUtils.htmlUnescape(scene.getName());
			String text = HtmlUtils.htmlUnescape(scene.getText());
			Integer seqNr = scene.getSeqNr();
			Integer id  = scene.getId();
			String stmt = String.format(template, name, text, seqNr, id);
			this.connection.executeUpdateStatement(stmt);
			
			// Riddle attributes
			this.updateRiddle(scene.getRiddle());

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}
	
}
