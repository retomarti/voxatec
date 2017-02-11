package com.voxatec.argame.objectModel.persistence;

import com.voxatec.argame.objectModel.mysql.*;
import com.voxatec.argame.objectModel.beans.File;
import com.voxatec.argame.objectModel.beans.ImageFile;
import com.voxatec.argame.objectModel.beans.Object;
import com.voxatec.argame.util.EntityMappingFileReader;
import com.voxatec.argame.util.PropertyFileReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.springframework.web.util.HtmlUtils;


/**
 * Created by retomarti on 16/04/16.
 */
public class EntityManager {

	// Constants
	static protected int UNDEF_ID = -1;
	
    // DB Connection
	protected Connection connection = null;
    private String dbHost = null;
    private String dbName = null;
    private String userName = null;
    private String password = null;    
    
    
    // Database connection --------------------------------------------------------------------------
    
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
    
 	
    // Helper Functions ----------------------------------------------------------------------------
	
	protected String getEntityTable(Object entityBean) {
		return null;
	}
    
    protected Integer getLastAutoInsertedId() throws SQLException {
    	Integer lastInsertedId = EntityManager.UNDEF_ID;
    	
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
    
    
	protected String queryfiableString(String string) {
		
		byte[] chars = string.getBytes();
		
		for (int i=0; i<string.length(); i++) {
			switch (chars[i]) {
				case '"':	chars[i] = '\'';		// we use '"' to embrace strings -> they should not be part of a string
				default: // do nothing
			}
		}
		
		return new String(chars);
	}

	
    // Image Persistency ----------------------------------------------------------------------------

	// INSERT-ImageFile
	protected void createImageFile(File imgFile, 
								String table, String imgColumn, String imgTypeColumn, String imgNameColumn,
								String beanIdColumn, int beanId) throws SQLException, IOException, URISyntaxException {
		
		try {
			this.initConnection();

			// Statement:
			// INSERT INTO <table>
			// (<imgColumn>, <imgTypeColumn>, <imgNameColumn>, <beanIdColumn>)
			// VALUES
			// (<imgFile.content>, <imgFile.imageType>, <imgFile.name>, <beanId>)

			String template = "insert into %s (%s,%s,%s,%s) values (?,?,?,?)";
	        template = String.format(template, table, imgColumn, imgTypeColumn, imgNameColumn, beanIdColumn);

			PreparedStatement stmt = this.connection.newPreparedStatement(template);
			
			// Bind values
			Blob blob = this.connection.newBlob();
			byte[] image = null;
			if (imgFile.getContent() != null) {
				byte[] bytes = imgFile.getContent().getBytes();
				image = Base64.getDecoder().decode(bytes);
				blob.setBytes(1, image);
			}

			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(image);
			ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteInputStream);
			String imageFileType = this.getImageFileExtention(imageInputStream);
			String imageName = HtmlUtils.htmlUnescape(imgFile.getName());
						
			// Bind statement parameters & execute
			stmt.setBlob(1, blob);
			stmt.setString(2, imageFileType.toLowerCase());
			stmt.setString(3, imageName);
	        stmt.setInt(4, beanId);
	        
	        stmt.executeUpdate();
			
		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}

	
	// GET-ImageFile by beanId
	protected ImageFile getImageFileByBeanId(String table, 
			                                 String imgColumn, String imgTypeColumn, String imgNameColumn,
			                                 int beanId) throws Exception {
		ImageFile imageFile = null;
		byte[] imageData = "".getBytes();
		String imageType = "";
		String imageName = "";
		
		try {
			this.initConnection();
			
			// Statement:
			// select <image>, <image.type>, <image.name> from <table> where id=<beanId>
			
	        String template = "select %s, %s, %s from %s where id=%d";
	        String stmt = String.format(template, imgColumn, imgTypeColumn, imgNameColumn, table, beanId);
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob(imgColumn);
				if (blob != null) {
					imageData = blob.getBytes(1, (int) blob.length());
					imageType = resultSet.getString(imgTypeColumn);
					imageName = resultSet.getString(imgNameColumn);
				}
			}
			
			imageFile = new ImageFile();
			imageFile.setData(imageData);
			imageFile.setType(imageType);
			imageFile.setName(imageName);
						
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return imageFile;
	}

	
	// GET-ImageFile By beanId and imageName
	protected ImageFile getImageFileByBeanIdAndName(String table, 
			                                        String imgColumn, String imgTypeColumn, String imgNameColumn,
			                                        String beanIdColumn,
			                                        int beanId, String imageName) throws Exception {
		ImageFile imageFile = null;
		byte[] imageData = "".getBytes();
		String imageType = "";
		
		try {
			this.initConnection();
			
			// Statement:
			// select <image>, <image.type> 
			//   from <table> 
			//  where <beanIdCol>=<beanId> and (<imgNameCol>=<imageName> or <imgNameCol> like "<imageName>.%")
			
	        String template = "select %s, %s from %s where %s=%d and (%s=\"" + imageName + "\" or %s like \"" + imageName + ".%s\")";
	        String stmt = String.format(template, imgColumn, imgTypeColumn, 
	        		                    table, 
	        		                    beanIdColumn, beanId, imgNameColumn, imgNameColumn, "%");
	        ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob(imgColumn);
				if (blob != null) {
					imageData = blob.getBytes(1, (int) blob.length());
					imageType = resultSet.getString(imgTypeColumn);
				}
			}
			
			imageFile = new ImageFile();
			imageFile.setData(imageData);
			imageFile.setType(imageType);
			imageFile.setName(imageName);
						
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
			
		} finally {
			this.connection.close();
		}
		
		return imageFile;
	}

	
	// UPDATE ImageFile
	public String getImageFileExtention(ImageInputStream imageInputStream) throws URISyntaxException, IOException{
	    try{
	        Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
	        ImageReader reader = iter.next();
	        String formatName = reader.getFormatName();
	        
	        return formatName;
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	    return null;
	}
	
	
	public void updateImageFileByBeanId(File imageFile, 
			String table, String imgColumn, String imgTypeColumn, String imgNameColumn, 
			int beanId)
			throws SQLException, UnsupportedEncodingException, URISyntaxException, IOException {

		if (imageFile == null)
			return;

		try {
			this.initConnection();

			// Statement:
			// update <table>
			// set <imgColumn>=<image>, <imgTypeCol>=<image.type>, <imgNameCol>=<image.name>
			// where <beanIdColumn>=<beanId> and (<imgNameCol>=<image.name> or
			// <imgNameCol> like "<image.name>.%")
			
			String template = "update %s set %s=?, %s=?, %s=? where id=?;";
			template = String.format(template, table, imgColumn, imgTypeColumn, imgNameColumn);

			PreparedStatement stmt = this.connection.newPreparedStatement(template);

			// Decode image from Base64
			byte[] image = null;
			if (imageFile.getContent() != null) {
				byte[] bytes = imageFile.getContent().getBytes();
				image = Base64.getDecoder().decode(bytes);
			}

			// Define imageType & image content Blob
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(image);
			ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteInputStream);
			String imageFileType = this.getImageFileExtention(imageInputStream);
			String imageFileName = HtmlUtils.htmlUnescape(imageFile.getName());
			Blob blob = this.connection.newBlob();
			blob.setBytes(1, image);

			// Bind statement parameters '?' & execute
			stmt.setBlob(1, blob); // <image>
			stmt.setString(2, imageFileType.toLowerCase()); // <image.type>
			stmt.setString(3, imageFileName); // <image.name>
			stmt.setInt(4, beanId); // <beanId>

			stmt.executeUpdate();

			byteInputStream.close();
			imageInputStream.close();

		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;
		} finally {
			this.connection.close();
		}
	}	
	
	public void updateImageFileByBeanIdAndName(File imageFile, 
			                                   String table, 
			                                   String imgColumn, String imgTypeColumn, String imgNameColumn,
			                                   String beanIdColumn,
			                                   int beanId) 
		throws SQLException, UnsupportedEncodingException, URISyntaxException, IOException {
		
		if (imageFile == null)
			return;
		
		try {
			this.initConnection();
			
			// Statement:  
			// update <table> 
			//    set <imgColumn>=<image>, <imgTypeCol>=<image.type>, <imgNameCol>=<image.name>
			//  where <beanIdColumn>=<beanId> and (<imgNameCol>=<image.name> or <imgNameCol> like "<image.name>.%")
			String template = "update %s set %s=?, %s=?, %s=? where %s=? and (%s=\"%s\" or %s like \"%s.%s\");";
			
			String imgFileName = HtmlUtils.htmlUnescape(imageFile.getName());
			template = String.format(template, 
								     table, 
								     imgColumn, imgTypeColumn, imgNameColumn, 
								     beanIdColumn, 
								     imgNameColumn, imgFileName, 
								     imgNameColumn, imgFileName,
								     "%");

			PreparedStatement stmt = this.connection.newPreparedStatement(template);
			
			// Decode image from Base64
			byte[] image = null;
			if (imageFile.getContent() != null) {
				byte[] bytes = imageFile.getContent().getBytes();
				image = Base64.getDecoder().decode(bytes);
			}
			
			// Define imageType & image content Blob
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(image);
			ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteInputStream);
			String imgFileType = this.getImageFileExtention(imageInputStream);
			Blob blob = this.connection.newBlob();
			blob.setBytes(1, image);
			
			// Bind statement parameters '?' & execute
			stmt.setBlob(1, blob);				// <image>
			stmt.setString(2, imgFileType.toLowerCase());	// <image.type>
			stmt.setString(3, imgFileName);		// <image.name>
	        stmt.setInt(4, beanId);				// <beanId>
	        
	        stmt.executeUpdate();
			
	        byteInputStream.close();
	        imageInputStream.close();
	        
		} catch (Exception exception) {
			System.out.print(exception.toString());
			throw exception;        	
		} finally {
			this.connection.close();
		}
	}

	
	// Bean Persistency ----------------------------------------------------------------------------
	
	protected String columnNameOfAttribute(Object entityBean, String attributeName) {
		Map<String,String> colMap = EntityMappingFileReader.columnMapForBeanClass(entityBean.getClassName());
		String columnName = colMap.get(attributeName);
		return columnName;
	}
	
	protected String getInsertColumnList(Object entityBean) {
		Map<String,String> colMap = EntityMappingFileReader.columnMapForBeanClass(entityBean.getClassName());
		String columnList = new String();
		
		for (String key: colMap.keySet()) {
		    String columnName = colMap.get(key);
		    
		    if (!columnList.isEmpty() && columnName != null && !columnName.isEmpty()) {
		    	columnList = columnList + ',';
		    }
		    columnList += columnName;
		}
		
		return columnList;
	}
	
	protected String getInsertColumnValueList(Object entityBean) {
		Map<String,String> colMap = EntityMappingFileReader.columnMapForBeanClass(entityBean.getClassName());
		String columnValueList = new String();
		
		return columnValueList;
	}
	
	public Object insertEntityBean(Object entityBean) throws SQLException {
		if (entityBean == null)
			return null;
		
		try {
			this.initConnection();
			
			String template = "insert into %s (%s) values (%s)";
			String tableName = this.getEntityTable(entityBean);
			String colNameList = this.getInsertColumnList(entityBean);
			String colValueList = this.getInsertColumnValueList(entityBean);
			String stmt = String.format(template, tableName, colNameList, colValueList);
			this.connection.executeUpdateStatement(stmt);

			// Retrieve auto inserted ID value
			Integer lastInsertedId = this.getLastAutoInsertedId();
			entityBean.setId(lastInsertedId);

		} catch (SQLException exception) {
			entityBean = null;
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
		
		return entityBean;
	}

	
	public Object selectEntityBean(int beanId) throws SQLException {
		Object theBean = null;
		
		return theBean;
	}
	

	protected String updateColumnValueList(Object entityBean) {
		return null;
	}
		

	public void updateEntityBean(Object entityBean) throws SQLException {
		if (entityBean == null)
			return;
		
		try {
			this.initConnection();
			
			String template = "update %s set %s where id=%d";
			String colNameValueList = this.updateColumnValueList(entityBean);
			String stmt = String.format(template, this.getEntityTable(entityBean), colNameValueList, entityBean.getId());
			this.connection.executeUpdateStatement(stmt);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}
	
	public void deleteEntityBean(Object entityBean) throws SQLException {
		
		if (entityBean == null || entityBean.getId() == EntityManager.UNDEF_ID)
			return;   // nothing to do
		
		try {			
			this.initConnection();
			
			String template = "delete from %s where id=%d";
			String stmt = String.format(template, this.getEntityTable(entityBean), entityBean.getId());
			this.connection.executeUpdateStatement(stmt);
		
		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}
    
}
