package com.voxatec.argame.objectModel.persistence;

import com.voxatec.argame.objectModel.beans.*;
import com.voxatec.argame.objectModel.mysql.*;
import com.voxatec.argame.util.PropertyFileReader;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.springframework.web.util.HtmlUtils;

/**
 * Created by retomarti on 16/04/16.
 */
public class EntityManager {

    // Connection
    private Connection connection = null;
    private String dbHost = null;
    private String dbName = null;
    private String userName = null;
    private String password = null;

    // Caches
    // private static Map<String, Client> allClients = null;

    // Database access
    private void initConnection () throws SQLException {
        if (connection == null) {
            connection = new Connection();
        }

        if (!connection.isOpen()) {
            dbHost = PropertyFileReader.getValueFor("dbHost");
            dbName = PropertyFileReader.getValueFor("dbName");
            userName = PropertyFileReader.getValueFor("dbUser");
            password = PropertyFileReader.getValueFor("dbPassword");
            connection.open(dbHost, dbName, userName, password);
        }
    }

    public Vector<City> loadCities() throws SQLException {
    	
    	Vector<City> cityList = new Vector<City>();

        try {
            this.initConnection();

            String stmt = "select * from city";
            ResultSet resultSet = this.connection.executeSelectStatement(stmt);

            while (resultSet.next()) {
                // City object
                City newCity = new City();
                newCity.setId(resultSet.getInt("id"));
                newCity.setName(HtmlUtils.htmlEscape(resultSet.getString("name")));
                newCity.setCountry(HtmlUtils.htmlEscape(resultSet.getString("country")));
               cityList.add(newCity);
            }

        } catch (SQLException exception ) {
            System.out.print(exception.toString());
            throw exception;

        } finally {
            this.connection.close();
        }

        return cityList;
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
				if (story == null || storyId != story.getId()) {
					story = new Story();
					story.setId(storyId);
					story.setSeqNr(resultSet.getInt("sto_seq_nr"));
					story.setName(HtmlUtils.htmlEscape(resultSet.getString("sto_name")));
					story.setText(HtmlUtils.htmlEscape(resultSet.getString("sto_text")));
					sceneList = new Vector<Scene>();
					story.setSceneList(sceneList);
					storyList.add(story);
				}
				
				// Scene
				scene = new Scene();
				scene.setId(resultSet.getInt("sce_id"));
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

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return adventureList;
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
	
	
	public void updateRiddle(Riddle riddle) throws SQLException {

		if (riddle == null)
			return;

		try {
			this.initConnection();

			String template = "update riddle set challenge_text=\"%s\", response_text=\"%s\", hint_text=\"%s\" where id=%d";
			String challengeText = HtmlUtils.htmlUnescape(riddle.getChallengeText());
			String responseText = HtmlUtils.htmlUnescape(riddle.getResponseText());
			String hintText = HtmlUtils.htmlUnescape(riddle.getHintText());
			Integer id  = riddle.getId();
			String stmt = String.format(template, challengeText, responseText, hintText, id);
			
			this.connection.executeUpdateStatement(stmt);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
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
	
	
	public File getXmlFile(Integer cacheGroupId) throws SQLException {
		File xmlFile = new File();
		
        try {
			this.initConnection();

	        String template = "select target_img_xml_file_name, target_img_xml_file from cache_group where id=%d";
	        String stmt = String.format(template, cacheGroupId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("target_img_xml_file");
				if (blob != null) {
					String strContent = new String(blob.getBytes(1l, (int) blob.length()));
					xmlFile.setContent(HtmlUtils.htmlEscape(strContent));
					xmlFile.setMimeType("text/plain");
				}
				xmlFile.setName(resultSet.getString("target_img_xml_file_name"));
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return xmlFile;
	}
	

	public File getDatFile(Integer cacheGroupId) throws SQLException {
		File datFile = new File();
		
        try {
			this.initConnection();

	        String template = "select target_img_dat_file_name, target_img_dat_file from cache_group where id=%d";
	        String stmt = String.format(template, cacheGroupId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("target_img_dat_file");
				if (blob != null) {
					String strContent = new String(blob.getBytes(1l, (int) blob.length()));
					datFile.setContent(HtmlUtils.htmlEscape(strContent));
					datFile.setMimeType("application/octet-stream");
				}
				datFile.setName(resultSet.getString("target_img_dat_file_name"));
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return datFile;
	}
	

	public File getObjFile(Integer object3DId) throws SQLException {
		File objFile = new File();
		
        try {
			this.initConnection();

	        String template = "select obj_file_name, obj_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("obj_file");
				String strContent = new String(blob.getBytes(1l, (int) blob.length()));
        		objFile.setContent(HtmlUtils.htmlEscape(strContent));
        		objFile.setMimeType("text/plain");
				objFile.setName(resultSet.getString("obj_file_name"));
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return objFile;
	}
	
	public File getMtlFile(Integer object3DId) throws SQLException {
		File mtlFile = new File();
		
        try {
			this.initConnection();

	        String template = "select mtl_file_name, mtl_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("mtl_file");
				String strContent = new String(blob.getBytes(1l, (int) blob.length()));
				mtlFile.setContent(HtmlUtils.htmlEscape(strContent));
				mtlFile.setMimeType("text/plain");
				mtlFile.setName(resultSet.getString("mtl_file_name"));
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return mtlFile;
	}
	
	public File getTexFile(Integer object3DId) throws SQLException {
		File texFile = new File();
		
        try {
			this.initConnection();

	        String template = "select tex_file_name, tex_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob("tex_file");
				String strContent = new String(blob.getBytes(1l, (int) blob.length()));
				texFile.setContent(HtmlUtils.htmlEscape(strContent));
				texFile.setMimeType("text/plain");
				texFile.setName(resultSet.getString("tex_file_name"));
        	}
			
        } catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return texFile;
	}
}
