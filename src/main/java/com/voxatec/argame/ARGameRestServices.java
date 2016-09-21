package com.voxatec.argame;

import java.util.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.voxatec.argame.objectModel.beans.*;
import com.voxatec.argame.objectModel.persistence.EntityManager;

@RestController
public class ARGameRestServices {

    /*--GET--------------------------------------------------------------------------------*/
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

    
	@CrossOrigin
    @RequestMapping(value = "/adventures", method = RequestMethod.GET)
    public Vector<Adventure> getAdventures() throws ObjectNotFoundException {
    	
    	Vector<Adventure> adventureList = null;
    	String errorMsg = "";
    	
    	try {
    		System.out.println("GET request for all adventures");
    		EntityManager entityMgr = new EntityManager();
    		adventureList = entityMgr.loadAdventures();
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


	@CrossOrigin
    @RequestMapping(value = "/stories", method = RequestMethod.GET)
    public Vector<Story> getStories() throws ObjectNotFoundException {
    	
    	Vector<Story> storyList = null;
    	String errorMsg = "";
    	
    	try {
    		System.out.println("GET request for all stories");
    		EntityManager entityMgr = new EntityManager();
    		storyList = entityMgr.loadStories();
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

    
	@CrossOrigin
    @RequestMapping(value = "/adventure-scenes", method = RequestMethod.GET)
    public Vector<Adventure> getAdventureScenes() throws ObjectNotFoundException {
    	
    	Vector<Adventure> adventureList = null;
    	String errorMsg = "";
    	
    	try {
    		System.out.println("GET request for all adventure scenes");
    		EntityManager entityMgr = new EntityManager();
    		adventureList = entityMgr.loadAdventureScenes();
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


	@CrossOrigin
    @RequestMapping(value = "/scenes/{story_id}", method = RequestMethod.GET)
    public Vector<Scene> getStoryScenes(@PathVariable Integer story_id) throws ObjectNotFoundException {
    	
    	Vector<Scene> sceneList = null;
    	String errorMsg = "";
    	
    	try {
    		System.out.println(String.format("GET request for all scenes of story with id=%d", story_id));
    		EntityManager entityMgr = new EntityManager();
    		sceneList = entityMgr.loadStoryScenes(story_id);
    	}
    	catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		sceneList = null;
    	}
    		
    	if (sceneList == null) {
    		// Objects not found in database
            throw new ObjectNotFoundException("scenes", story_id, errorMsg);
    	}
    			
    	return sceneList;
    }
    
    
	@CrossOrigin
    @RequestMapping(value = "/files/xml/{cache_group_id}", method = RequestMethod.GET)
    public File getLocationXmlFile(@PathVariable Integer cache_group_id) throws ObjectNotFoundException {
    	
    	File xmlFile = null;
    	String errorMsg = "";

    	try {
    		System.out.println(String.format("GET request for xml-file of a cache group with id=%d", cache_group_id));
    		EntityManager entityMgr = new EntityManager();
    		xmlFile = entityMgr.loadXmlFile(cache_group_id);
    			
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
    		datFile = entityMgr.loadDatFile(cache_group_id);
    			
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
    		objFile = entityMgr.loadObjFile(object3D_id);
    			
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
    		mtlFile = entityMgr.loadMtlFile(object3D_id);
    			
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
    		texFile = entityMgr.loadTexFile(object3D_id);
    			
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
