package com.voxatec.argame.objectModel.persistence;

import com.voxatec.argame.objectModel.mysql.*;
import com.voxatec.argame.objectModel.beans.Object;
import com.voxatec.argame.util.EntityMappingFileReader;
import com.voxatec.argame.util.PropertyFileReader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


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
		
		for (String key: colMap.keySet()) {
		    String attributeName = key;
		    
		}
		
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
