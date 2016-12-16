package com.voxatec.argame.util;

import java.io.InputStream;
import java.util.Map;
import java.io.IOException;


public class EntityMappingFileReader {
	
	private static String fileName = "/entityMapping.json";
	private static boolean loaded = false;

	
	public static String getTableNameForBeanClass(String beanClassName) {
		InputStream input = null;
		String tableName = null;
		
		try {
			if (!loaded) {
				input = PropertyFileReader.class.getResourceAsStream(fileName);
				// properties.load(input);
				loaded = true;
			}
			
			// tableName = entityMap.getProperty(propertyName);
			// System.out.println("Property " + propertyName + ": " + propertyValue);
		}
		catch (Exception exception) {
			System.out.println("Property file access exception: " + exception.getMessage());
			loaded = false;
		}
		finally {
			try {
				if (input != null) {
					input.close();
				}
			}
			catch (IOException exception) {
			}
		}
		
		return tableName;
	}
	
	
	public static Map<String,String> columnMapForBeanClass(String beanClassName) {
		Map<String,String> columnMap = null;
		
		return columnMap;
	}
	
	
	public static String columnNameForBeanAttribute(String beanClassName, String beanAttributeName) {
		String columnName = null;
		
		
		return columnName;
	}

}
