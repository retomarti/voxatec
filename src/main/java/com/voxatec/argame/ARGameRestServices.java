package com.voxatec.argame;

import java.util.*;

import javax.sound.sampled.AudioFormat.Encoding;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.voxatec.argame.objectModel.beans.*;
import com.voxatec.argame.objectModel.persistence.AdventureEntityManager;
import com.voxatec.argame.objectModel.persistence.CityEntityManager;
import com.voxatec.argame.objectModel.persistence.CacheEntityManager;
import com.voxatec.argame.objectModel.persistence.StoryEntityManager;
import com.voxatec.argame.objectModel.persistence.SceneEntityManager;
import com.voxatec.argame.objectModel.persistence.Object3DEntityManager;


@RestController
public class ARGameRestServices {

    //------------------ Helper -------------------------------------------------------------------
	protected void logRequest(String message) {
		System.out.println("---------------------------------------------");
		System.out.println(message);
		System.out.println("---------------------------------------------");
	}
	
	
    //------------------ Get Prototypes -----------------------------------------------------------
	@CrossOrigin
    @RequestMapping(value = "/prototypes", method = RequestMethod.GET)
    public Vector<NamedObject> getPrototypes() throws ObjectNotFoundException {
		
		Vector<NamedObject> prototypes = new Vector<NamedObject>();
		
		Adventure adventurePrototype = null;
		City cityPrototype = null;
    	String errorMsg = "";
    	
    	try {
    		this.logRequest("Get request for prototypes");
    		AdventureEntityManager adventureMgr = new AdventureEntityManager();
    		adventurePrototype = adventureMgr.getAdventurePrototype();
    		prototypes.add(adventurePrototype);
    		
    		CityEntityManager cityMgr = new CityEntityManager();
    		cityPrototype = cityMgr.getCityPrototype();
    		prototypes.add(cityPrototype);
    	}
    	catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		prototypes = null;  		
    	}

    	if (prototypes == null) {
    		// Prototypes not found in database
            throw new ObjectNotFoundException("prototypes", errorMsg);
    	}
    		
    	return prototypes;
	}
	
	
	//------------------ Get all Adventures ------------------------------------------------------
	@CrossOrigin
    @RequestMapping(value = "/adventures", method = RequestMethod.GET)
    public Vector<Adventure> getAdventures() throws ObjectNotFoundException {
    	
    	Vector<Adventure> adventureList = null;
    	String errorMsg = "";
    	
    	try {
    		this.logRequest("GET request for all adventures");
    		AdventureEntityManager entityMgr = new AdventureEntityManager();
    		adventureList = entityMgr.getAdventures();
    	}
    	catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		adventureList = null;
    	}
    		
    	if (adventureList == null) {
    		// Objects not found in database
            throw new ObjectNotFoundException("adventures", errorMsg);
    	}
    			
    	return adventureList;
    }
	
	
    //------------------ Create an Adventure ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/adventures", method = RequestMethod.POST)
	public Adventure postAdventure(@RequestBody Adventure adventure) throws ObjectNotFoundException {
		
		Adventure theAdventure = null;
    	String errorMsg = "";

		try {
    		this.logRequest("POST request for an adventure");

    		AdventureEntityManager entityMgr = new AdventureEntityManager();
    		theAdventure = entityMgr.createAdventure(adventure);
    		
		} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theAdventure = null;		
		}
		
    	if (theAdventure == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("adventure", errorMsg);
    	}
		
		return theAdventure;
	}
	
	
	//------------------ Update an Adventure ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/adventures/{adventure_id}", method = RequestMethod.PUT)
	public Adventure putAdventure(@PathVariable Integer adventure_id,
								  @RequestBody Adventure adventure) throws ObjectNotFoundException {
		
		Adventure theAdventure = null;
    	String errorMsg = "";

		try {
    		this.logRequest(String.format("PUT request for an adventure with id=%d", adventure_id));
    		
    		// Retrieve adventure entity
    		AdventureEntityManager entityMgr = new AdventureEntityManager();
    		theAdventure = entityMgr.getAdventureById(adventure_id);
			
			if (theAdventure != null) {
				theAdventure.setName(adventure.getName());
				theAdventure.setText(adventure.getText());
				entityMgr.updateAdventure(theAdventure);
			}
		}
		catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theAdventure = null;		
		}
		
    	if (theAdventure == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("adventure", errorMsg);
    	}
		
		return theAdventure;
	}


    //------------------ Delete an Adventure ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/adventures/{adventure_id}", method = RequestMethod.DELETE)
	public void deleteAdventure(@PathVariable Integer adventure_id) throws ObjectNotFoundException {
		
		Adventure theAdventure = null;
    	String errorMsg = "";
		
		try {
			this.logRequest(String.format("DELETE request for an adventure with id=%d", adventure_id));
			AdventureEntityManager entityMgr = new AdventureEntityManager();
			theAdventure = entityMgr.getAdventureById(adventure_id);

    		if (theAdventure != null) {
    			entityMgr.deleteAdventure(adventure_id);
    		}
		}
		catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		
    	if (theAdventure == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("adventure", errorMsg);
    	}

	}

    //------------------ Get all Stories ---------------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/stories", method = RequestMethod.GET)
    public Vector<Story> getStories() throws ObjectNotFoundException {
    	
    	Vector<Story> storyList = null;
    	String errorMsg = "";
    	
    	try {
    		this.logRequest("GET request for all stories");
    		StoryEntityManager entityMgr = new StoryEntityManager();
    		storyList = entityMgr.getStories();
    	}
    	catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		storyList = null;
    	}
    		
    	if (storyList == null) {
    		// Objects not found in database
            throw new ObjectNotFoundException("stories", errorMsg);
    	}
    			
    	return storyList;
    }

    
    //------------------ Create a Story -----------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/stories", method = RequestMethod.POST)
	public Story postStory(@RequestBody Story story) throws ObjectNotFoundException {
		
		Story theStory = null;
    	String errorMsg = "";

		try {
    		this.logRequest("POST request for a story");

    		StoryEntityManager entityMgr = new StoryEntityManager();
    		theStory = entityMgr.createStory(story);
    		
		} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theStory = null;		
		}
		
    	if (theStory == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("story", errorMsg);
    	}
		
		return theStory;
	}
	
	
    //------------------ Update a Story ----------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/stories/{story_id}", method = RequestMethod.PUT)
	public Story putStory(@PathVariable Integer story_id,
						  @RequestBody Story story) throws ObjectNotFoundException {
		
		Story theStory = null;
    	String errorMsg = "";

		try {
    		this.logRequest(String.format("PUT request for a story with id=%d", story_id));
    		StoryEntityManager entityMgr = new StoryEntityManager();
    		theStory = entityMgr.getStoryById(story_id);
			
			if (theStory != null) {
				theStory.setName(story.getName());
				theStory.setText(story.getText());
				theStory.setSeqNr(story.getSeqNr());
				entityMgr.updateStory(theStory);
			}
		}
		catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theStory = null;		
		}
		
    	if (theStory == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("story", errorMsg);
    	}
		
		return theStory;
	}
	
	
    //------------------ Delete a Story ----------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/stories/{story_id}", method = RequestMethod.DELETE)
	public void deleteStory(@PathVariable Integer story_id) throws ObjectNotFoundException {
		
		Story theStory = null;
    	String errorMsg = "";
		
		try {
			this.logRequest(String.format("DELETE request for a story with id=%d", story_id));
			StoryEntityManager entityMgr = new StoryEntityManager();
			theStory = entityMgr.getStoryById(story_id);

    		if (theStory != null) {
    			entityMgr.deleteStory(story_id);
    		}
		}
		catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		
    	if (theStory == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("story", errorMsg);
    	}

	}


	//------------------ Get all Adventure Scenes ----------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/adventure-scenes", method = RequestMethod.GET)
    public Vector<Adventure> getAdventureScenes() throws ObjectNotFoundException {
    	
    	Vector<Adventure> adventureList = null;
    	String errorMsg = "";
    	
    	try {
    		this.logRequest("GET request for all adventure scenes");
    		SceneEntityManager entityMgr = new SceneEntityManager();
    		adventureList = entityMgr.getAdventureScenes();
    	}
    	catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		adventureList = null;
    	}
    		
    	if (adventureList == null) {
    		// Objects not found in database
            throw new ObjectNotFoundException("adventures", errorMsg);
    	}
    			
    	return adventureList;
    }
	
	
	//------------------ Get nearby Adventure Caches ----------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/adventure-caches", method = RequestMethod.GET)
    public Vector<Adventure> getNearbyAdventureCaches() throws ObjectNotFoundException {
    	
    	Vector<Adventure> adventureList = null;
    	String errorMsg = "";
    	
    	try {
    		this.logRequest("GET request for nearby adventure caches");
    		CacheEntityManager entityMgr = new CacheEntityManager();
    		adventureList = entityMgr.getNearbyAdventureCaches();
    	}
    	catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		adventureList = null;
    	}
    		
    	if (adventureList == null) {
    		// Objects not found in database
            throw new ObjectNotFoundException("adventures", errorMsg);
    	}
    			
    	return adventureList;
    }
	
	
    //------------------ Create a Scene -----------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/scenes", method = RequestMethod.POST)
	public Scene postStory(@RequestBody Scene scene) throws ObjectNotFoundException {
		
		Scene theScene = null;
    	String errorMsg = "";

		try {
    		this.logRequest("POST request for a scene");

    		SceneEntityManager entityMgr = new SceneEntityManager();
    		theScene = entityMgr.createScene(scene);
    		
		} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theScene = null;		
		}
		
    	if (theScene == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("scene", errorMsg);
    	}
		
		return theScene;
	}


    //------------------ Update a Scene ----------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/scenes/{scene_id}", method = RequestMethod.PUT)
	public Scene putScene(@PathVariable Integer scene_id,
						  @RequestBody Scene scene) throws ObjectNotFoundException {
		
		Scene theScene = null;
    	String errorMsg = "";

		try {
    		this.logRequest(String.format("PUT request for a scene with id=%d", scene_id));

    		// Retrieve scene entity
    		SceneEntityManager entityMgr = new SceneEntityManager();
    		theScene = entityMgr.getSceneById(scene_id);
			
			if (theScene != null) {
				theScene.setName(scene.getName());
				theScene.setText(scene.getText());
				theScene.setSeqNr(scene.getSeqNr());
				entityMgr.updateScene(scene);
			}
		}
		catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theScene = null;		
		}
		
    	if (theScene == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("scene", errorMsg);
    	}
		
		return theScene;
	}

	
    //------------------ Delete a Scene ----------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/scenes/{scene_id}", method = RequestMethod.DELETE)
	public void deleteScene(@PathVariable Integer scene_id) throws ObjectNotFoundException {
		
		Scene theScene = null;
    	String errorMsg = "";
		
		try {
			this.logRequest(String.format("DELETE request for a scene with id=%d", scene_id));
			SceneEntityManager entityMgr = new SceneEntityManager();
    		theScene = entityMgr.getSceneById(scene_id);

    		if (theScene != null) {
    			entityMgr.deleteScene(scene_id);
    		}
		}
		catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		
    	if (theScene == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("scene", errorMsg);
    	}

	}
		
		
	//------------------ Get all Objects3D -------------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/objects3D", method = RequestMethod.GET)
    public Vector<Object3D> getObjects3D() throws ObjectNotFoundException {
    	
    	Vector<Object3D> object3DList = null;
    	String errorMsg = "";
    	
    	try {
    		this.logRequest("GET request for all objects3D");
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		object3DList = entityMgr.getObjects3D();
    	}
    	catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		object3DList = null;
    	}
    		
    	if (object3DList == null) {
    		// Objects not found in database
            throw new ObjectNotFoundException("objects3D", errorMsg);
    	}
    			
    	return object3DList;
    }
	
	
	//------------------ Get one Objects3D -------------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/objects3D/{object3D_id}", method = RequestMethod.GET)
    public Object3D getObject3DById(@PathVariable Integer object3D_id) throws ObjectNotFoundException {
    	
    	Object3D object3D = null;
    	String errorMsg = "";
    	
    	try {
    		this.logRequest("GET request for one objects3D");
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		object3D = entityMgr.getObject3DById(object3D_id);
    	}
    	catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		object3D = null;
    	}
    		
    	if (object3D == null) {
    		// Objects not found in database
            throw new ObjectNotFoundException("objects3D", errorMsg);
    	}
    			
    	return object3D;
    }
	
	
    //------------------ Create an Object3D -------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/objects3D", method = RequestMethod.POST)
	public Object3D postObject3D(@RequestBody Object3D object3D) throws ObjectNotFoundException {
		
		Object3D theObject3D = null;
    	String errorMsg = "";

		try {
    		this.logRequest("POST request for an object3D");

    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		theObject3D = entityMgr.createObject3D(object3D);
    		
		} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theObject3D = null;		
		}
		
    	if (theObject3D == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("object3D", errorMsg);
    	}
		
		return theObject3D;
	}
	
	
	//------------------ Update an Object3D ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/objects3D/{object3D_id}", method = RequestMethod.PUT)
	public Object3D putObject3D(@PathVariable Integer object3D_id,
								@RequestBody Object3D object3D) throws ObjectNotFoundException {
		
		Object3D theObject3D = null;
    	String errorMsg = "";

		try {
    		this.logRequest(String.format("PUT request for an object3D with id=%d", object3D_id));
    		
    		// Retrieve adventure entity
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		theObject3D = entityMgr.getObject3DById(object3D_id);
			
			if (theObject3D != null) {
				theObject3D.setName(object3D.getName());
				theObject3D.setText(object3D.getText());
				entityMgr.updateObject3D(theObject3D);
			}
		}
		catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theObject3D = null;		
		}
		
    	if (theObject3D == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("object3D", errorMsg);
    	}
		
		return theObject3D;
	}

    
    //------------------ Delete an Object3D ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/objects3D/{object3D_id}", method = RequestMethod.DELETE)
	public void deleteObject3D(@PathVariable Integer object3D_id) throws ObjectNotFoundException {
		
		Object3D theObject3D = null;
    	String errorMsg = "";
		
		try {
			this.logRequest(String.format("DELETE request for an object3D with id=%d", object3D_id));
			Object3DEntityManager entityMgr = new Object3DEntityManager();
			theObject3D = entityMgr.getObject3DById(object3D_id);

    		if (theObject3D != null) {
    			entityMgr.deleteObject3D(object3D_id);
    		}
		}
		catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		
    	if (theObject3D == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("object3D", errorMsg);
    	}

	}

	
	//------------------ Get all Cities -----------------------------------------------------------
	@CrossOrigin
    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public Vector<City> getCities() throws ObjectNotFoundException {
    	
    	Vector<City> allCities = null;
    	String errorMsg = "";
    	
    	try {
    		this.logRequest("GET request for all cities");
    		CityEntityManager entityMgr = new CityEntityManager();
    		allCities = entityMgr.getCities();
    	}
    	catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		allCities = null;
    	}
    		
    	if (allCities == null) {
    		// Cities not found in database
            throw new ObjectNotFoundException("cities", errorMsg);
    	}
    			
    	return allCities;
    }
	
	
    //------------------ Create a City -------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/cities", method = RequestMethod.POST)
	public City postCity(@RequestBody City city) throws ObjectNotFoundException {
		
		City theCity = null;
    	String errorMsg = "";

		try {
    		this.logRequest("POST request for a city");

    		CityEntityManager entityMgr = new CityEntityManager();
    		theCity = entityMgr.createCity(city);
    		
		} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theCity = null;		
		}
		
    	if (theCity == null) {
    		// City not found in database
            throw new ObjectNotFoundException("city", errorMsg);
    	}
		
		return theCity;
	}
	
	
	//------------------ Update a City ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/cities/{city_id}", method = RequestMethod.PUT)
	public City putCity(@PathVariable Integer city_id,
					    @RequestBody City city) throws ObjectNotFoundException {
		
		City theCity = null;
    	String errorMsg = "";

		try {
    		this.logRequest(String.format("PUT request for a city with id=%d", city_id));
    		
    		// Retrieve adventure entity
    		CityEntityManager entityMgr = new CityEntityManager();
    		theCity = entityMgr.getCityById(city_id);
			
			if (theCity != null) {
				theCity.setName(city.getName());
				theCity.setText(city.getText());
				theCity.setZip(city.getZip());
				theCity.setCountry(city.getCountry());
				entityMgr.updateCity(theCity);
			}
		}
		catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theCity = null;		
		}
		
    	if (theCity == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("city", errorMsg);
    	}
		
		return theCity;
	}

    
    //------------------ Delete a City ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/cities/{city_id}", method = RequestMethod.DELETE)
	public void deleteCity(@PathVariable Integer city_id) throws ObjectNotFoundException {
		
		City theCity = null;
    	String errorMsg = "";
		
		try {
			this.logRequest(String.format("DELETE request for a city with id=%d", city_id));
			CityEntityManager entityMgr = new CityEntityManager();
			theCity = entityMgr.getCityById(city_id);

    		if (theCity != null) {
    			entityMgr.deleteCity(city_id);
    		}
		}
		catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		
    	if (theCity == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("city", errorMsg);
    	}

	}
	
	
    //------------------ Create a CacheGroup -------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/cache-groups", method = RequestMethod.POST)
	public CacheGroup postCacheGroup(@RequestBody CacheGroup cacheGroup) throws ObjectNotFoundException {
		
		CacheGroup theCacheGroup = null;
    	String errorMsg = "";

		try {
    		this.logRequest("POST request for a cache-group");

    		CacheEntityManager entityMgr = new CacheEntityManager();
    		theCacheGroup = entityMgr.createCacheGroup(cacheGroup);
    		
		} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theCacheGroup = null;		
		}
		
    	if (theCacheGroup == null) {
    		// CacheGroup not found in database
            throw new ObjectNotFoundException("cache-group", errorMsg);
    	}
		
		return theCacheGroup;
	}
	
	
	//------------------ Update a CacheGroup ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/cache-groups/{cacheGroup_id}", method = RequestMethod.PUT)
	public CacheGroup putCacheGroup(@PathVariable Integer cacheGroup_id,
					                @RequestBody CacheGroup cacheGroup) throws ObjectNotFoundException {
		
		CacheGroup theCacheGroup = null;
    	String errorMsg = "";

		try {
    		this.logRequest(String.format("PUT request for a cache-group with id=%d", cacheGroup_id));
    		
    		// Retrieve adventure entity
    		CacheEntityManager entityMgr = new CacheEntityManager();
    		theCacheGroup = entityMgr.getCacheGroupById(cacheGroup_id);
			
			if (theCacheGroup != null) {
				theCacheGroup.setName(cacheGroup.getName());
				theCacheGroup.setText(cacheGroup.getText());
				theCacheGroup.setTargetImageDatFileName(cacheGroup.getTargetImageDatFileName());
				theCacheGroup.setTargetImageXmlFileName(cacheGroup.getTargetImageXmlFileName());
				entityMgr.updateCacheGroup(theCacheGroup);
			}
		}
		catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theCacheGroup = null;		
		}
		
    	if (theCacheGroup == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("cache-group", errorMsg);
    	}
		
		return theCacheGroup;
	}

    
    //------------------ Delete a CacheGroup ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/cache-groups/{cacheGroup_id}", method = RequestMethod.DELETE)
	public void deleteCacheGroup(@PathVariable Integer cacheGroup_id) throws ObjectNotFoundException {
		
		CacheGroup theCacheGroup = null;
    	String errorMsg = "";
		
		try {
			this.logRequest(String.format("DELETE request for a cache-group with id=%d", cacheGroup_id));
			CacheEntityManager entityMgr = new CacheEntityManager();
			theCacheGroup = entityMgr.getCacheGroupById(cacheGroup_id);

    		if (theCacheGroup != null) {
    			entityMgr.deleteCacheGroup(cacheGroup_id);
    		}
		}
		catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		
    	if (theCacheGroup == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("cache-group", errorMsg);
    	}

	}


	//------------------ Get all City Caches -----------------------------------------------------------
	@CrossOrigin
    @RequestMapping(value = "/city-caches", method = RequestMethod.GET)
    public Vector<City> getCityCaches() throws ObjectNotFoundException {
    	
    	Vector<City> cityList = null;
    	String errorMsg = "";
    	
    	try {
    		this.logRequest("GET request for all city caches");
    		CacheEntityManager entityMgr = new CacheEntityManager();
    		cityList = entityMgr.getCityCaches();
    	}
    	catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		cityList = null;
    	}
    		
    	if (cityList == null) {
    		// Objects not found in database
            throw new ObjectNotFoundException("cities", errorMsg);
    	}
    			
    	return cityList;
    }
	
	
    //------------------ Create a Cache -------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/caches", method = RequestMethod.POST)
	public Cache postCache(@RequestBody Cache cache) throws ObjectNotFoundException {
		
		Cache theCache = null;
    	String errorMsg = "";

		try {
    		this.logRequest("POST request for a cache");

    		CacheEntityManager entityMgr = new CacheEntityManager();
    		theCache = entityMgr.createCache(cache);
    		
		} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theCache = null;		
		}
		
    	if (theCache == null) {
    		// Cache not found in database
            throw new ObjectNotFoundException("cache", errorMsg);
    	}
		
		return theCache;
	}
	
	
	//------------------ Update a Cache ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/caches/{cache_id}", method = RequestMethod.PUT)
	public Cache putCache(@PathVariable Integer cache_id,
					      @RequestBody Cache cache) throws ObjectNotFoundException {
		
		Cache theCache = null;
    	String errorMsg = "";

		try {
    		this.logRequest(String.format("PUT request for a cache with id=%d", cache_id));
    		
    		// Retrieve cache entity
    		CacheEntityManager entityMgr = new CacheEntityManager();
    		theCache = entityMgr.getCacheById(cache_id);
			
			if (theCache != null) {
				theCache.setName(cache.getName());
				theCache.setText(cache.getText());
				theCache.setStreet(cache.getStreet());
				theCache.setGpsLatitude(cache.getGpsLatitude());
				theCache.setGpsLongitude(cache.getGpsLongitude());
				entityMgr.updateCache(theCache);
			}
		}
		catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		theCache = null;		
		}
		
    	if (theCache == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("cache", errorMsg);
    	}
		
		return theCache;
	}

    
    //------------------ Delete a Cache ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/caches/{cache_id}", method = RequestMethod.DELETE)
	public void deleteCache(@PathVariable Integer cache_id) throws ObjectNotFoundException {
		
		Cache theCache = null;
    	String errorMsg = "";
		
		try {
			this.logRequest(String.format("DELETE request for a cache with id=%d", cache_id));
			CacheEntityManager entityMgr = new CacheEntityManager();
			theCache = entityMgr.getCacheById(cache_id);

    		if (theCache != null) {
    			entityMgr.deleteCache(cache_id);
    		}
		}
		catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		
    	if (theCache == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("cache", errorMsg);
    	}

	}
	
	
    //------------------ Get Target Image file of a cache -------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/target-img/{cache_id}", method = RequestMethod.GET, produces = "image/*")
    public ResponseEntity<byte[]> getCacheTargetImageFile(@PathVariable Integer cache_id) throws ObjectNotFoundException {
    	
    	ImageFile imageFile = null;
    	String errorMsg = "";

    	try {
    		this.logRequest(String.format("GET request for target image file of a cache with id=%s", cache_id));
    		CacheEntityManager entityMgr = new CacheEntityManager();
    		imageFile = entityMgr.getTargetImageFile(cache_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		imageFile = null;
    	}
    		
    	if (imageFile == null) {
    		// Target image file not found in database
            throw new ObjectNotFoundException("files/target-img", cache_id, errorMsg);
    	}
    	
    	// Decode imageFile content first    	
    	return
    			ResponseEntity.ok()
                .contentLength(imageFile.getData().length)
                .contentType(new MediaType("image", imageFile.getType()))
                .body(imageFile.getData());
    }
	
	
    //------------------ Update Target Image file of a cache ----------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/target-img/{cache_id}", method = RequestMethod.PUT)
    public void putCacheTargetImageFile(@PathVariable Integer cache_id,
    							        @RequestBody File targetImgFile) throws ObjectNotFoundException {
    	
    	try {
    		this.logRequest(String.format("PUT request for target image file of cache with id=%d", cache_id));
    		
    		CacheEntityManager entityMgr = new CacheEntityManager();
    		entityMgr.updateTargetImageFile(targetImgFile, cache_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    	}
    		    			
    }


    //------------------ Get Target Image XML file of a cache-group ---------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/target-xml/{cache_group_id}", method = RequestMethod.GET, produces = "text/xml")
    public String getCacheGroupXmlFile(@PathVariable Integer cache_group_id) throws ObjectNotFoundException {
    	
		String xmlFile = null;
    	String errorMsg = "";

    	try {
    		this.logRequest(String.format("GET request for xml-file of a cache group with id=%d", cache_group_id));
    		CacheEntityManager entityMgr = new CacheEntityManager();
    		xmlFile = entityMgr.getXmlFile(cache_group_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		xmlFile = null;
    	}
    		
    	if (xmlFile == null) {
    		// Xml file not found in database
            throw new ObjectNotFoundException("files/xml", cache_group_id, errorMsg);
    	}
    			
    	return xmlFile;
    }


    //------------------ Update Target Image XML file of a cache-group ------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/target-xml/{cacheGroup_id}", method = RequestMethod.PUT)
    public void putCacheGroupXmlFile(@PathVariable Integer cacheGroup_id,
    							     @RequestBody File xmlFile) throws ObjectNotFoundException {
    	
    	try {
    		this.logRequest(String.format("PUT request for xml-file of cache-group with id=%d", cacheGroup_id));
    		
    		CacheEntityManager entityMgr = new CacheEntityManager();
    		entityMgr.updateXmlFile(xmlFile, cacheGroup_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    	}
    		    			
    }

	
    //------------------ Get Target Image DAT file of a cache-group ---------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/target-dat/{cache_group_id}", method = RequestMethod.GET, produces = "application/octet-stream")
    public byte[] getCacheGroupDatFile(@PathVariable Integer cache_group_id) throws ObjectNotFoundException {
    	
    	byte[] datFile = null;
    	String errorMsg = "";

    	try {
    		this.logRequest(String.format("GET request for dat-file of a cache-group with id=%s", cache_group_id));
    		CacheEntityManager entityMgr = new CacheEntityManager();
    		datFile = entityMgr.getDatFile(cache_group_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		datFile = null;
    	}
    		
    	if (datFile == null) {
    		// Dat file not found in database
            throw new ObjectNotFoundException("files/dat", cache_group_id, errorMsg);
    	}
    			
    	return datFile;
    }
	
	
    //------------------ Update Target Image DAT file of a cache-group ------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/target-dat/{cacheGroup_id}", method = RequestMethod.PUT)
    public void putCacheGroupDatFile(@PathVariable Integer cacheGroup_id,
    							     @RequestBody File datFile) throws ObjectNotFoundException {
    	
    	try {
    		this.logRequest(String.format("PUT request for dat-file of cache-group with id=%d", cacheGroup_id));
    		
    		CacheEntityManager entityMgr = new CacheEntityManager();
    		entityMgr.updateDatFile(datFile, cacheGroup_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    	}
    		    			
    }

	
    //------------------ Get OBJ file of an Object3D ------------------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/obj/{object3D_id}", method = RequestMethod.GET, produces = "text/plain")
    public String getObject3DObjFile(@PathVariable Integer object3D_id) throws ObjectNotFoundException {
    	
    	String objFile = null;
    	String errorMsg = "";

    	try {
    		this.logRequest(String.format("GET request for obj-file of an object3D with id=%d", object3D_id));
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		objFile = entityMgr.getObjFile(object3D_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		objFile = null;
    	}
    		
    	if (objFile == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("files/obj", object3D_id, errorMsg);
    	}
    			
    	return objFile;
    }


    //------------------ Update OBJ file of an Object3D ---------------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/obj/{object3D_id}", method = RequestMethod.PUT)
    public void putObject3DObjFile(@PathVariable Integer object3D_id,
    							   @RequestBody File objFile) throws ObjectNotFoundException {
    	
    	try {
    		this.logRequest(String.format("PUT request for obj-file of object3D with id=%d", object3D_id));
    		
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		entityMgr.updateObjFile(objFile, object3D_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    	}
    		    			
    }

	
    //------------------ Get MTL file of an Object3D ------------------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/mtl/{object3D_id}", method = RequestMethod.GET, produces = "text/plain")
    public String getObject3DMtlFile(@PathVariable Integer object3D_id) throws ObjectNotFoundException {
    	
    	String mtlFile = null;
    	String errorMsg = "";

    	try {
    		this.logRequest(String.format("GET request for mtl-file of an object3D with id=%d", object3D_id));
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		mtlFile = entityMgr.getMtlFile(object3D_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		mtlFile = null;
    	}
    		
    	if (mtlFile == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("files/mtl", object3D_id, errorMsg);
    	}
    			
    	return mtlFile;
    }
	
	
    //------------------ Update MTL file of an Object3D ---------------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/mtl/{object3D_id}", method = RequestMethod.PUT)
    public void putObject3DMtlFile(@PathVariable Integer object3D_id,
    							   @RequestBody File mtlFile) throws ObjectNotFoundException {
    	
    	try {
    		this.logRequest(String.format("PUT request for mtl-file of object3D with id=%d", object3D_id));
    		
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		entityMgr.updateMtlFile(mtlFile, object3D_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    	}
    		    			
    }

	
    //------------------ Create TEX image of an Object3D --------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/files/mtl/{object3D_id}/{image_name}", method = RequestMethod.POST)
	public void postObject3DTexFile(@PathVariable ("object3D_id") Integer object3D_id,
									@PathVariable ("image_name") String image_name,
									@RequestBody File imageFile) throws ObjectNotFoundException {
		
		try {
			this.logRequest(String.format("POST request for tex-file of object3D with id=%d", object3D_id));
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		entityMgr.createTexFile(imageFile, object3D_id);
    		
		} catch (Exception exception) {
    		System.out.println(exception.getMessage());
		}
				
	}
	
	
    //------------------ Get TEX image file of an Object3D ------------------------------------------------------	
	
	@CrossOrigin
    @RequestMapping(value = "/files/mtl/{object3D_id}/{image_name}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getObject3DTexImage(@PathVariable ("object3D_id") Integer object3D_id,
			  										  @PathVariable ("image_name") String texture_name) throws ObjectNotFoundException {
    	
    	ImageFile imageFile = null;
    	String errorMsg = "";

    	try {
    		this.logRequest(String.format("GET request for texture image file of an object3D with id=%s", object3D_id));
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		imageFile = entityMgr.getTextureFile(object3D_id, texture_name);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		imageFile = null;
    	}
    		
    	if (imageFile == null) {
    		// Texture image file not found in database
            throw new ObjectNotFoundException("texture", object3D_id, errorMsg);
    	}
    	
    	// Decode imageFile content first    	
    	return
    			ResponseEntity.ok()
                .contentLength(imageFile.getData().length)
                .contentType(new MediaType("image", imageFile.getType()))
                .body(imageFile.getData());
    }


    //------------------ Update TEX image file of an Object3D ------------------------------------------------------	
	
	@CrossOrigin
    @RequestMapping(value = "/files/mtl/{object3D_id}/{texture_id}", method = RequestMethod.PUT)
    public void putObject3DTexImage(@PathVariable Integer object3D_id,
    								@PathVariable Integer texture_id,
    							    @RequestBody File targetImgFile) throws ObjectNotFoundException {
    	
    	try {
    		this.logRequest(String.format("PUT request for texture image file of texture with id=%d", texture_id));
    		
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		entityMgr.updateTextureFile(targetImgFile, texture_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    	}			
    }

    
    //------------------ Delete TEX image file of Object3D ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/files/mtl/{object3D_id}/{image_name}", method = RequestMethod.DELETE)
	public void deleteObject3DTexImage(@PathVariable ("object3D_id") Integer object3D_id,
									   @PathVariable ("image_name") String image_name) throws ObjectNotFoundException {
		
		Object3D theObject3D = null;
    	String errorMsg = "";
		
		try {
			this.logRequest(String.format("DELETE request for a texture image of object3D with id=%d", object3D_id));
			Object3DEntityManager entityMgr = new Object3DEntityManager();
			theObject3D = entityMgr.getObject3DById(object3D_id);

    		if (theObject3D != null) {
    			entityMgr.deleteTextureFile(image_name, object3D_id);
    		}
		}
		catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		
    	if (theObject3D == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("object3D", errorMsg);
    	}

	}

	
    //------------------ OBJECT-NOT-FOUND Exception -------------------------------------------------------------	
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class ObjectNotFoundException extends RuntimeException {

 		private static final long serialVersionUID = -2653781247214818577L;

		// constructors
    	public ObjectNotFoundException(String objectType, String details) {
    		super(String.format("Could not find %s.", objectType, details));
    	}

    	public ObjectNotFoundException(String objectType, int objectId, String details) {
    		super(String.format("Could not find %s with id=%s. %s.", objectType, objectId, details));
    	}
    }
    
}
