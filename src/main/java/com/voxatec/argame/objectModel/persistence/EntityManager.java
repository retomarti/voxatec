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
	protected Connection connection = null;
    private String dbHost = null;
    private String dbName = null;
    private String userName = null;
    private String password = null;

    // Caches
    // private static Map<String, Client> allClients = null;

    // Database access
    protected void initConnection () throws SQLException {
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
    
    protected Integer getLastAutoInsertedId() throws SQLException {
    	Integer lastInsertedId = -1;
    	
    	try {
    		String stmt = "select LAST_INSERT_ID()";
            ResultSet resultSet = this.connection.executeSelectStatement(stmt);
            
            while (resultSet.next()) {
            	lastInsertedId = resultSet.getInt("LAST_INSERT_ID()");
            }
            
    	} catch (SQLException exception) {
            System.out.print(exception.toString());
            throw exception;
    	}
    	
    	return lastInsertedId;
    }
    
    
    public Vector<City> getCities() throws SQLException {
    	
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
