package com.voxatec.argame.objectModel.persistence;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.springframework.web.util.HtmlUtils;

import com.voxatec.argame.objectModel.beans.Cache;
import com.voxatec.argame.objectModel.beans.CacheGroup;
import com.voxatec.argame.objectModel.beans.City;


public class CityEntityManager extends EntityManager {

    public City getCityPrototype() {
    	
    	// City
    	City city = new City();
    	city.setName("");
    	city.setZip("");
    	city.setCountry("");
    	
    	// CacheGroup
    	CacheGroup cacheGroup = new CacheGroup();
    	cacheGroup.setName("");
    	cacheGroup.setText("");
    	cacheGroup.setCityId(-1);
    	cacheGroup.setTargetImageDatFileName("");
    	cacheGroup.setTargetImageXmlFileName("");
    	Vector<CacheGroup> cacheGroupList = new Vector<CacheGroup>();
    	cacheGroupList.add(cacheGroup);
    	city.setCacheGroupList(cacheGroupList);
    	
    	// Cache
    	Cache cache = new Cache();
    	cache.setName("");
    	cache.setText("");
    	cache.setStreet("");
    	cache.setCacheGroupId(-1);
    	cache.setTargetImageFileName("");
    	cache.setGpsLatitude(new BigDecimal("0.0000000"));
    	cache.setGpsLongitude(new BigDecimal("0.0000000"));
    	Vector<Cache> cacheList = new Vector<Cache>();
    	cacheList.add(cache);
    	cacheGroup.setCacheList(cacheList);
    	
    	return city;
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
    
    
	public City createCity(City city) throws SQLException {
		
		if (city == null)
			return null;
		
		try {
			this.initConnection();

			// Insert adventure into DB
			String template = "insert into city (name,zip,country) values (\"%s\",\"%s\",\"%s\")";
			String name = HtmlUtils.htmlUnescape(city.getName());
			String zip = HtmlUtils.htmlUnescape(city.getZip());
			String country = HtmlUtils.htmlUnescape(city.getCountry());
			String stmt = String.format(template, name, zip, country);
			this.connection.executeUpdateStatement(stmt);
			
			// Retrieve auto inserted ID value
			Integer lastInsertedId = this.getLastAutoInsertedId();
			city.setId(lastInsertedId);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
		
		return city;
	}
	
	
	public void updateCity(City city) throws SQLException {
		
		if (city == null)
			return;
		
		try {
			this.initConnection();

			String template = "update city set name=\"%s\", zip=\"%s\", country=\"%s\" where id=%d";
			String name = HtmlUtils.htmlUnescape(city.getName());
			String zip = HtmlUtils.htmlUnescape(city.getZip());
			String country = HtmlUtils.htmlUnescape(city.getCountry());
			Integer id  = city.getId();
			String stmt = String.format(template, name, zip, country, id);
			
			this.connection.executeUpdateStatement(stmt);

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}
	}
	
	
	public City getCityById(Integer cityId) throws SQLException {
		
		City theCity = null;
		
		try {
			this.initConnection();

			String template = "select * from city where id=%d";
			String stmt = String.format(template, cityId);
			ResultSet resultSet = this.connection.executeSelectStatement(stmt);

			while (resultSet.next()) {
				// Adventure
				theCity = new City();
				theCity.setId(cityId);
				theCity.setName(HtmlUtils.htmlEscape(resultSet.getString("name")));
				theCity.setZip(HtmlUtils.htmlEscape(resultSet.getString("zip")));
				theCity.setCountry(HtmlUtils.htmlEscape(resultSet.getString("country")));
			}

		} catch (SQLException exception) {
			System.out.print(exception.toString());
			throw exception;

		} finally {
			this.connection.close();
		}

		return theCity;
	}
	
	
	public void deleteCity(Integer cityId) throws SQLException {
		
		City theCity = null;
		
		if (cityId == EntityManager.UNDEF_ID)
			return;   // nothing to do
		
		try {
			theCity = this.getCityById(cityId);
			
			this.initConnection();
			
			if (theCity != null) {
				// Delete city
				String template = "delete from city where id=%d";
				String stmt = String.format(template, cityId);
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
