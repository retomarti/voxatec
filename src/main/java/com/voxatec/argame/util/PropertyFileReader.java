package com.voxatec.argame.util;

import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class PropertyFileReader {
	
	private static String fileName = "/application.properties";
	private static Properties properties = new Properties();
	private static boolean loaded = false;
	
	public static String getValueFor(String propertyName) {
		InputStream input = null;
		String propertyValue = null;
		
		try {
			if (!loaded) {
				input = PropertyFileReader.class.getResourceAsStream(fileName);
				properties.load(input);
				loaded = true;
			}
			
			propertyValue = properties.getProperty(propertyName);
			System.out.println("Property " + propertyName + ": " + propertyValue);
		}
		catch (FileNotFoundException exception) {
			System.out.println("Property file '" + fileName + "' not found.");
			loaded = false;
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
		
		return propertyValue;
	}
}
