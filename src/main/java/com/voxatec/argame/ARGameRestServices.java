package com.voxatec.argame;

import java.util.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.voxatec.argame.objectModel.beans.*;
import com.voxatec.argame.objectModel.persistence.EntityManager;

@RestController
public class ARGameRestServices {

    //------------------ Get all Cities -----------------------------------------------------------
	@CrossOrigin
    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public Vector<City> getCities() throws ObjectNotFoundException {
    	
    	Vector<City> allCities = null;
    	String errorMsg = "";
    	
    	try {
    		System.out.println("GET request for all cities");
    		EntityManager entityMgr = new EntityManager();
    		allCities = entityMgr.loadCities();
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

    //------------------ Get all Adventures ------------------------------------------------------
	@CrossOrigin
    @RequestMapping(value = "/adventures", method = RequestMethod.GET)
    public Vector<Adventure> getAdventures() throws ObjectNotFoundException {
    	
    	Vector<Adventure> adventureList = null;
    	String errorMsg = "";
    	
    	try {
    		System.out.println("GET request for all adventures");
    		EntityManager entityMgr = new EntityManager();
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
	
    //------------------ Update an Adventure ------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/adventures/{adventure_id}", method = RequestMethod.PUT)
	public Adventure putAdventure(@PathVariable Integer adventure_id,
								  @RequestBody Adventure adventure) throws ObjectNotFoundException {
		
		Adventure theAdventure = null;
    	String errorMsg = "";

		try {
    		System.out.println("----------------------------");
    		System.out.println("PUT request for an adventure");
    		
    		// Retrieve adventure entity
    		EntityManager entityMgr = new EntityManager();
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


    //------------------ Get all Stories ---------------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/stories", method = RequestMethod.GET)
    public Vector<Story> getStories() throws ObjectNotFoundException {
    	
    	Vector<Story> storyList = null;
    	String errorMsg = "";
    	
    	try {
    		System.out.println("GET request for all stories");
    		EntityManager entityMgr = new EntityManager();
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

    
    //------------------ Update a Story ----------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/stories/{story_id}", method = RequestMethod.PUT)
	public Story putStory(@PathVariable Integer story_id,
						  @RequestBody Story story) throws ObjectNotFoundException {
		
		Story theStory = null;
    	String errorMsg = "";

		try {
    		System.out.println("-----------------------");
    		System.out.println("PUT request for a story");
    		
    		// Retrieve adventure entity
    		EntityManager entityMgr = new EntityManager();
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


	//------------------ Get all Adventure Scenes ----------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/adventure-scenes", method = RequestMethod.GET)
    public Vector<Adventure> getAdventureScenes() throws ObjectNotFoundException {
    	
    	Vector<Adventure> adventureList = null;
    	String errorMsg = "";
    	
    	try {
    		System.out.println("GET request for all adventure scenes");
    		EntityManager entityMgr = new EntityManager();
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


    //------------------ Update a Scene ----------------------------------------------------------	
	@CrossOrigin
	@RequestMapping(value = "/scenes/{scene_id}", method = RequestMethod.PUT)
	public Scene putScene(@PathVariable Integer scene_id,
						  @RequestBody Scene scene) throws ObjectNotFoundException {
		
		Scene theScene = null;
    	String errorMsg = "";

		try {
    		System.out.println("-----------------------");
    		System.out.println("PUT request for a scene");
    		
    		// Retrieve adventure entity
    		EntityManager entityMgr = new EntityManager();
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


	@CrossOrigin
    @RequestMapping(value = "/files/xml/{cache_group_id}", method = RequestMethod.GET)
    public File getLocationXmlFile(@PathVariable Integer cache_group_id) throws ObjectNotFoundException {
    	
    	File xmlFile = null;
    	String errorMsg = "";

    	try {
    		System.out.println(String.format("GET request for xml-file of a cache group with id=%d", cache_group_id));
    		EntityManager entityMgr = new EntityManager();
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


	@CrossOrigin
    @RequestMapping(value = "/files/dat/{cache_group_id}", method = RequestMethod.GET)
    public File getLocationDatFile(@PathVariable Integer cache_group_id) throws ObjectNotFoundException {
    	
    	File datFile = null;
    	String errorMsg = "";

    	try {
    		System.out.println(String.format("GET request for dat-file of a cache-group with id=%s", cache_group_id));
    		EntityManager entityMgr = new EntityManager();
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


	@CrossOrigin
    @RequestMapping(value = "/files/obj/{object3D_id}", method = RequestMethod.GET)
    public File getObject3DObjFile(@PathVariable Integer object3D_id) throws ObjectNotFoundException {
    	
    	File objFile = null;
    	String errorMsg = "";

    	try {
    		System.out.println(String.format("GET request for obj-file of an object3D with id=", object3D_id));
    		EntityManager entityMgr = new EntityManager();
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


	@CrossOrigin
    @RequestMapping(value = "/files/mtl/{object3D_id}", method = RequestMethod.GET)
    public File getObject3DMtlFile(@PathVariable Integer object3D_id) throws ObjectNotFoundException {
    	
    	File mtlFile = null;
    	String errorMsg = "";

    	try {
    		System.out.println(String.format("GET request for mtl-file of an object3D with id=%d", object3D_id));
    		EntityManager entityMgr = new EntityManager();
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


	@CrossOrigin
    @RequestMapping(value = "/files/tex/{object3D_id}", method = RequestMethod.GET)
    public File getObject3DTexFile(@PathVariable Integer object3D_id) throws ObjectNotFoundException {
    	
    	File texFile = null;
    	String errorMsg = "";

    	try {
    		System.out.println(String.format("GET request for tex-file of an object3D with id=%d", object3D_id));
    		EntityManager entityMgr = new EntityManager();
    		texFile = entityMgr.getTexFile(object3D_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		texFile = null;
    	}
    		
    	if (texFile == null) {
    		// Object not found in database
            throw new ObjectNotFoundException("files/tex", object3D_id, errorMsg);
    	}
    			
    	return texFile;
    }


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
