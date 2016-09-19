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
            dbHost = PropertyFileReader.getValueFor("dbHost");
            dbName = PropertyFileReader.getValueFor("dbName");
            userName = PropertyFileReader.getValueFor("dbUser");
            password = PropertyFileReader.getValueFor("dbPassword");
            connection = new Connection();
            connection.open(dbHost, dbName, userName, password);
        }
    }

    public Vector<City> loadCities() throws SQLException {
    	
    	Vector<City> cityList = new Vector<City>();

        try {
            this.initConnection();

            String stmt = "select * from city";
            ResultSet resultSet = this.connection.executeStatement(stmt);

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
    
	public Vector<Adventure> loadAdventures() throws SQLException {

		Vector<Adventure> adventureList = new Vector<Adventure>();

		try {
			this.initConnection();

			String stmt = "select * from v_adventure";
			ResultSet resultSet = this.connection.executeStatement(stmt);

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
	

	public Vector<Story> loadStories() throws SQLException {

		Vector<Story> storyList = new Vector<Story>();

		try {
			this.initConnection();

			String stmt = "select * from v_story";
			ResultSet resultSet = this.connection.executeStatement(stmt);

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
	

	public Vector<Adventure> loadAdventureStories() throws SQLException {

		Vector<Adventure> adventureList = new Vector<Adventure>();
		Vector<Story> storyList = null;

		try {
			this.initConnection();

			String stmt = "select * from v_adventure_story";
			ResultSet resultSet = this.connection.executeStatement(stmt);

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
	
	public Vector<Adventure> loadAdventureScenes() throws SQLException {

		Vector<Adventure> adventureList = new Vector<Adventure>();
		Vector<Story> storyList = null;
		Vector<Scene> sceneList = null;

		try {
			this.initConnection();

			String stmt = "select * from v_adventure_scene";
			ResultSet resultSet = this.connection.executeStatement(stmt);

			Adventure adventure = null;
			Story story = null;
			Scene scene = null;

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
					story.setId(resultSet.getInt("sto_id"));
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
				scene.setName(resultSet.getString("sce_name"));
				scene.setText(HtmlUtils.htmlEscape(resultSet.getString("sce_text")));
				sceneList.add(scene);
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return adventureList;
	}
	
	
	public Vector<Scene> loadStoryScenes(Integer storyId) throws SQLException {

		Vector<Scene> sceneList = new Vector<Scene>();

		try {
			this.initConnection();
			
            String template = "select * from v_story_scene where sto_id = %d";
            String stmt = String.format(template, storyId);
            ResultSet resultSet = this.connection.executeStatement(stmt);
            
			while (resultSet.next()) {
				// Scene
				Scene scene = new Scene();
				scene.setId(resultSet.getInt("sce_id"));
				scene.setName(HtmlUtils.htmlEscape(resultSet.getString("sce_name")));
				scene.setText(HtmlUtils.htmlEscape(resultSet.getString("sce_text")));
				sceneList.add(scene);
				
				// Cache
				Cache cache = new Cache();
				cache.setId(resultSet.getInt("cah_id"));
				cache.setName(HtmlUtils.htmlEscape(resultSet.getString("cah_name")));
				cache.setText(HtmlUtils.htmlEscape(resultSet.getString("cah_text")));
				cache.setStreet(HtmlUtils.htmlEscape(resultSet.getString("cah_street")));
				cache.setGps_lat(resultSet.getBigDecimal("cah_gps_lat"));
				cache.setGps_long(resultSet.getBigDecimal("cah_gps_long"));
				scene.setCache(cache);

				// Object3D
				Object3D object3D = new Object3D();
				object3D.setId(resultSet.getInt("obj_id"));
				object3D.setName(HtmlUtils.htmlEscape(resultSet.getString("obj_name")));
				object3D.setObjFileLength(resultSet.getInt("obj_obj_file_len"));
				scene.setObject3D(object3D);

				// Material
				Material material = new Material();
				material.setMtlFileLength(resultSet.getInt("obj_mtl_file_len"));
				material.setTexFileLength(resultSet.getInt("obj_tex_file_len"));
				object3D.setMaterial(material);
				
				// Riddle
				String challenge = (resultSet.getString("rid_challenge"));
				if (challenge != null) {
					Riddle riddle = new Riddle();
					riddle.setChallengeText(challenge);
					riddle.setResponseText(resultSet.getString("rid_response"));
					scene.setRiddle(riddle);
				}
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return sceneList;
	}
	
	
	public File loadXmlFile(Integer cacheGroupId) throws SQLException {
		File xmlFile = new File();
		
        try {
			this.initConnection();

	        String template = "select target_img_xml_file_name, target_img_xml_file from cache_group where id=%d";
	        String stmt = String.format(template, cacheGroupId);
	        ResultSet resultSet = this.connection.executeStatement(stmt);

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
	

	public File loadDatFile(Integer cacheGroupId) throws SQLException {
		File datFile = new File();
		
        try {
			this.initConnection();

	        String template = "select target_img_dat_file_name, target_img_dat_file from cache_group where id=%d";
	        String stmt = String.format(template, cacheGroupId);
	        ResultSet resultSet = this.connection.executeStatement(stmt);

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
	

	public File loadObjFile(Integer object3DId) throws SQLException {
		File objFile = new File();
		
        try {
			this.initConnection();

	        String template = "select obj_file_name, obj_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeStatement(stmt);

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
	
	public File loadMtlFile(Integer object3DId) throws SQLException {
		File mtlFile = new File();
		
        try {
			this.initConnection();

	        String template = "select mtl_file_name, mtl_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeStatement(stmt);

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
	
	public File loadTexFile(Integer object3DId) throws SQLException {
		File texFile = new File();
		
        try {
			this.initConnection();

	        String template = "select tex_file_name, tex_file from object3D where id=%d";
	        String stmt = String.format(template, object3DId);
	        ResultSet resultSet = this.connection.executeStatement(stmt);

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
