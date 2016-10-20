package com.voxatec.argame;

import java.util.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.voxatec.argame.objectModel.beans.*;
import com.voxatec.argame.objectModel.persistence.EntityManager;
import com.voxatec.argame.objectModel.persistence.AdventureEntityManager;
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
    public Adventure getPrototypes() throws ObjectNotFoundException {
		
		Adventure prototype;
    	String errorMsg = "";
    	
    	try {
    		this.logRequest("Get request for prototypes");
    		AdventureEntityManager entityMgr = new AdventureEntityManager();
    		prototype = entityMgr.getAdventurePrototype();
    	}
    	catch (Exception exception) {
    		System.out.println(exception.getMessage());
    		errorMsg = exception.getMessage();
    		prototype = null;  		
    	}

    	if (prototype == null) {
    		// Cities not found in database
            throw new ObjectNotFoundException("prototype", errorMsg);
    	}
    		
    	return prototype;
}
	

	//------------------ Get all Cities -----------------------------------------------------------
	@CrossOrigin
    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public Vector<City> getCities() throws ObjectNotFoundException {
    	
    	Vector<City> allCities = null;
    	String errorMsg = "";
    	
    	try {
    		this.logRequest("GET request for all cities");
    		EntityManager entityMgr = new EntityManager();
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

    
    //------------------ Get Location XML file --------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/xml/{cache_group_id}", method = RequestMethod.GET)
    public File getLocationXmlFile(@PathVariable Integer cache_group_id) throws ObjectNotFoundException {
    	
    	File xmlFile = null;
    	String errorMsg = "";

    	try {
    		this.logRequest(String.format("GET request for xml-file of a cache group with id=%d", cache_group_id));
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
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


    //------------------ Get Location DAT file -------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/dat/{cache_group_id}", method = RequestMethod.GET)
    public File getLocationDatFile(@PathVariable Integer cache_group_id) throws ObjectNotFoundException {
    	
    	File datFile = null;
    	String errorMsg = "";

    	try {
    		this.logRequest(String.format("GET request for dat-file of a cache-group with id=%s", cache_group_id));
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
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
	
	
    //------------------ Get PNG Image of object3d ------------------------------------------------	
	@CrossOrigin
	@ResponseBody
    @RequestMapping(value = "/images/object3D/{object3D_id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getObject3DImage(@PathVariable Integer object3D_id) throws ObjectNotFoundException {
    	
    	byte[] imgData = "".getBytes();

    	try {
    		this.logRequest(String.format("GET request for image of object3D with id=%d", object3D_id));
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		imgData = entityMgr.getImage(object3D_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    	}
    		    			
    	return imgData;    
    }

	
    //------------------ Update PNG Image of object3d ------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/images/object3D/{object3D_id}", method = RequestMethod.PUT)
    public void putObject3DImage(@PathVariable Integer object3D_id,
    							 @RequestBody File imageFile) throws ObjectNotFoundException {
    	
    	try {
    		this.logRequest(String.format("PUT request for image of object3D with id=%d", object3D_id));
    		
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		entityMgr.updateImage(imageFile, object3D_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    	}
    		    			
    }

	
    //------------------ Get OBJ file ---------------------------------------------------------	
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


    //------------------ Update OBJ file ------------------------------------------------------	
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

	
    //------------------ Get MTL file ---------------------------------------------------------	
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
	
	
    //------------------ Update OBJ file ------------------------------------------------------	
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


    //------------------ Get TEX image file of object3d ------------------------------------------------	
	@CrossOrigin
	@ResponseBody
    @RequestMapping(value = "/files/mtl/{object3D_id}/{image_name}", method = RequestMethod.GET, produces = "image/jpeg")
    public byte[] getObject3DTexImage(@PathVariable ("object3D_id") Integer object3D_id,
    								  @PathVariable ("image_name") String image_name) throws ObjectNotFoundException {
    	
    	byte[] imgData = "".getBytes();

    	try {
    		this.logRequest(String.format("GET request for texture of object3D with id=%d and name=%s", object3D_id, image_name));
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		imgData = entityMgr.getTexFile(object3D_id, image_name);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    	}
    		    			
    	return imgData;    
    }

	
    //------------------ Update TEX file ------------------------------------------------------	
	@CrossOrigin
    @RequestMapping(value = "/files/tex/{object3D_id}", method = RequestMethod.PUT)
    public void putObject3DTexFile(@PathVariable Integer object3D_id,
    							   @RequestBody File texFile) throws ObjectNotFoundException {
    	
    	try {
    		this.logRequest(String.format("PUT request for tex-file of object3D with id=%d", object3D_id));
    		
    		Object3DEntityManager entityMgr = new Object3DEntityManager();
    		entityMgr.updateTexFile(texFile, object3D_id);
    			
    	} catch (Exception exception) {
    		System.out.println(exception.getMessage());
    	}
    		    			
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
