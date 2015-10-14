package ait.ffma.common.dao.mongodb;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

import ait.ffma.common.exception.FfmaTechnicalRuntimeException;
import ait.ffma.common.exception.ObjectNotFoundException;
import ait.ffma.common.exception.ObjectNotRemovedException;
import ait.ffma.common.exception.ObjectNotStoredException;
import ait.ffma.common.exception.ObjectNotUpdatedException;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.BaseFfmaDomainObject.CommonFieldsEnum;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.domain.MongoDbDomainObject;
import ait.ffma.factory.FfmaAbstractFactory;
import ait.ffma.utils.JSONMappingHelper;

public abstract class BaseMongoDbManager {

	/**
	 * Logger
	 */
	protected static Logger log = Logger.getLogger(MongoDbManager.class
			.getClass());
	protected DB db = null;

	public BaseMongoDbManager() {
		super();
	}

	/**
	 * This method stores Ffma object in the database
	 * 
	 * @param doc
	 *            The object to store
	 * @param isEmbedded
	 *            Storage design pattern
	 * @return The object stored in database
	 * @throws ObjectNotStoredException
	 * @throws ObjectNotFoundException
	 */
	public FfmaDomainObject store(FfmaDomainObject object)
			throws ObjectNotStoredException, ObjectNotFoundException {
		FfmaDomainObject res = null;
		if (object != null) {
			log.debug("store class name: " + object.getClass().getSimpleName());
			if (object.getId() != null) {
				// update object
				try {
					removeObject(object);
				} catch (ObjectNotRemovedException e) {
					log.error("Object could not be removed.");
				}
			}
			// create new object
			// search for child domain objects
			storeChildDomainObjects(object);

			// update the technical fields
			updateCommonFields(object);

			// store the object into the mongo database
			res = storeToMongoDb(object);
			
			if(res == null)
				throw new ObjectNotFoundException(
						"The object was not correctly stored into the database! Return value is null!");
			
		} else {
			log.debug("Object or sub object to store is null.");
		}
			
		return res;
	}

	/**
	 * This method stores Ffma domain objects list to database.
	 * @param objectsList
	 * @return storing result - true if successful, false if failure
	 * @throws ObjectNotStoredException
	 * @throws ObjectNotFoundException
	 */
	public boolean storeList(List<FfmaDomainObject> objectsList)
			throws ObjectNotStoredException, ObjectNotFoundException {
		boolean res = false;
		if (objectsList != null && objectsList.get(0) != null) {
//			log.info("store class name: " + objectsList.get(0).getClass().getSimpleName());
			storeListToMongoDb(objectsList);
			res = true;
		} else {
			log.debug("Object or sub object to store is null.");
		}
		return res;			
	}

	/**
	 * This method stores Ffma object in the database
	 * 
	 * @param doc
	 *            The object to store
	 * @param isEmbedded
	 *            Storage design pattern
	 * @return The object stored in database
	 * @throws ObjectNotStoredException
	 * @throws ObjectNotFoundException
	 */
	public FfmaDomainObject update(FfmaDomainObject object)
			throws ObjectNotStoredException, ObjectNotUpdatedException, ObjectNotFoundException {
		FfmaDomainObject res = null;
		if (object != null) {
			log.debug("update class name: " + object.getClass().getSimpleName());
			if (object.getId() != null) {
				// check creation and last update time. If current object has newest time - update
				BaseFfmaDomainObject dbObject = (BaseFfmaDomainObject) retrieveObject(object);
				if (dbObject.getCreateTime() <= ((BaseFfmaDomainObject) object).getCreateTime() 
						&& dbObject.getLastUpdateTime() <= ((BaseFfmaDomainObject) object).getLastUpdateTime()) {
					return store(object);
				} else {
					log.error("Update not possible. Database contains newer object for id " + object.getId());
					log.error("Database entry create time " + dbObject.getCreateTime() + ", last update time: " + dbObject.getLastUpdateTime());
					log.error("Current object create time " + ((BaseFfmaDomainObject) object).getCreateTime() + 
							", last update time: " + ((BaseFfmaDomainObject) object).getLastUpdateTime());
				}
			}
		} else {
			log.debug("Object or sub object to update is null.");
		}

		return res;
	}

	/**
	 * @param object
	 * @return
	 * @throws ObjectNotFoundException
	 */
	protected FfmaDomainObject readObjectFromDb(FfmaDomainObject object)
			throws ObjectNotFoundException {
		FfmaDomainObject res = null;
		try {
			DBCollection mongoCollection = db.getCollectionFromString(object
					.getClass().getSimpleName());
			BasicDBObject mongoObj = getDbObject(object.getId(),
					mongoCollection);
			res = getObjectFactory().createDomainObject(
					object.getComponentName(), object.getDomainObjectName());
		
			((MongoDbDomainObject) res).initDomainObject(mongoObj);
		
			return res;
		
		} catch (Exception e) {
			throw new ObjectNotFoundException(
					"Object does not exist in database!", e);
		}
	}

	/**
	 * @param object
	 * @param fieldName
	 * @return
	 * @throws ObjectNotFoundException
	 */
	protected FfmaDomainObject readObjectFromDbByField(FfmaDomainObject object, String fieldName)
			throws ObjectNotFoundException {
		FfmaDomainObject res = null;
		try {
			DBCollection mongoCollection = db.getCollectionFromString(object
					.getClass().getSimpleName());
			
			String value = null;
			Method[] methods = object.getClass().getMethods();
			for (Method m : methods) {
				// find getter method for the passed field name
				if (m.getName().equals(JSONMappingHelper.GETTER_METHOD + fieldName)) {
	               value = (String) m.invoke(object);
	               break;
				}
			}		
			
			BasicDBObject mongoObj = getDbObjectByField(value, mongoCollection, fieldName);
			res = getObjectFactory().createDomainObject(
					object.getComponentName(), object.getDomainObjectName());
		
			((MongoDbDomainObject) res).initDomainObject(mongoObj);
		
			return res;
		
		} catch (Exception e) {
			throw new ObjectNotFoundException(
					"Object does not exist in database!", e);
		}
	}

	/**
	 * This method sends request queries like '$in' to the Mongo database 
	 * in order to enhance performance and parses response from the database.
	 * @param object
	 *        The request object
	 * @param fieldName
	 *        The database object field name
	 * @param queryStr
	 *        The query
	 * @param values
	 *        Request values list	 
	 * @return the list of the FfmaDomainObject objects found in database
	 * @throws ObjectNotFoundException
	 */
	protected List<FfmaDomainObject> readObjectsListFromDbByField(
			FfmaDomainObject object, String fieldName, String queryStr,
			List<String> values) throws ObjectNotFoundException {
		
		List<FfmaDomainObject> res = new ArrayList<FfmaDomainObject>();
		try {
			DBCollection mongoCollection = db.getCollectionFromString(object
					.getClass().getSimpleName());
			
			List<BasicDBObject> mongoObjList = getDbObjectsList(mongoCollection, fieldName, queryStr, values);
			Iterator<BasicDBObject> iter = mongoObjList.iterator();
			while (iter.hasNext()) {
				FfmaDomainObject mongoObj = getObjectFactory().createDomainObject(
						object.getComponentName(), object.getDomainObjectName());
			
				((MongoDbDomainObject) mongoObj).initDomainObject(iter.next());
				res.add(mongoObj);
			}
		
			return res;
		
		} catch (Exception e) {
			throw new ObjectNotFoundException(
					"Object does not exist in database!", e);
		}
	}

	/**
	 * This method sends request queries like '$in' to the Mongo database 
	 * in order to enhance performance and parses response from the database.
	 * Additional query focusing is possible using passed field - value pairs.
	 * @param object
	 *        The request object
	 * @param fieldName
	 *        The database object field name
	 * @param queryStr
	 *        The query
	 * @param values
	 *        Request values list	 
	 * @param addFieldsMap
	 *        The additional field - value pairs map
	 * @return the list of the FfmaDomainObject objects found in database
	 * @throws ObjectNotFoundException
	 */
	protected List<FfmaDomainObject> readObjectsListFromDbByFieldExt(
			FfmaDomainObject object, String fieldName, String queryStr,
			List<String> values, Map<String, String> addFieldsMap) throws ObjectNotFoundException {
		
		List<FfmaDomainObject> res = new ArrayList<FfmaDomainObject>();
		try {
			DBCollection mongoCollection = db.getCollectionFromString(object
					.getClass().getSimpleName());
			
			List<BasicDBObject> mongoObjList = getDbObjectsListExt(mongoCollection, fieldName, queryStr, values, addFieldsMap);
			Iterator<BasicDBObject> iter = mongoObjList.iterator();
			while (iter.hasNext()) {
				FfmaDomainObject mongoObj = getObjectFactory().createDomainObject(
						object.getComponentName(), object.getDomainObjectName());
			
				((MongoDbDomainObject) mongoObj).initDomainObject(iter.next());
				res.add(mongoObj);
			}
		
			return res;
		
		} catch (Exception e) {
			throw new ObjectNotFoundException(
					"Object does not exist in database!", e);
		}
	}

	/**
	 * This method sends request queries like '$in' to the Mongo database 
	 * in order to enhance performance and parses response from the database.
	 * @param object
	 *        The request object
	 * @param fieldName
	 *        The database object field name
	 * @param queryStr
	 *        The query
	 * @param values
	 *        Request values list	 
	 * @return the list of the FfmaDomainObject objects found in database
	 * @throws ObjectNotFoundException
	 */
	protected List<FfmaDomainObject> readObjectsListFromDbByQueryObject(
			FfmaDomainObject queryObject) throws ObjectNotFoundException {
		
		List<FfmaDomainObject> res = new ArrayList<FfmaDomainObject>();
		try {
			DBCollection mongoCollection = db.getCollectionFromString(queryObject
					.getClass().getSimpleName());
			
			List<BasicDBObject> mongoObjList = getDbObjectsListByQueryObject(mongoCollection, (BasicDBObject) queryObject);
			Iterator<BasicDBObject> iter = mongoObjList.iterator();
			while (iter.hasNext()) {
				FfmaDomainObject mongoObj = getObjectFactory().createDomainObject(
						queryObject.getComponentName(), queryObject.getDomainObjectName());
			
				((MongoDbDomainObject) mongoObj).initDomainObject(iter.next());
				res.add(mongoObj);
			}
		
			return res;
		
		} catch (Exception e) {
			throw new ObjectNotFoundException(
					"Object does not exist in database!", e);
		}
	}

	/**
	 * This method sends request queries like '$in' to the Mongo database 
	 * in order to enhance performance and parses response from the database.
	 * @param object
	 *        The request object
	 * @param fieldName
	 *        The database object field name
	 * @param queryStr
	 *        The query
	 * @param values
	 *        Request values list	 
	 * @param exclusionsList 
	 *        The field names that should not be removed from the query object
	 * @return the list of the FfmaDomainObject objects found in database
	 * @throws ObjectNotFoundException
	 */
	protected List<FfmaDomainObject> readObjectsListFromDbByQueryObjectExt(
			FfmaDomainObject queryObject, List<String> exclusionsList) throws ObjectNotFoundException {
		
		List<FfmaDomainObject> res = new ArrayList<FfmaDomainObject>();
		try {
			DBCollection mongoCollection = db.getCollectionFromString(queryObject
					.getClass().getSimpleName());
			
			List<BasicDBObject> mongoObjList = getDbObjectsListByQueryObjectExt(mongoCollection, (BasicDBObject) queryObject, exclusionsList);
			Iterator<BasicDBObject> iter = mongoObjList.iterator();
			while (iter.hasNext()) {
				FfmaDomainObject mongoObj = getObjectFactory().createDomainObject(
						queryObject.getComponentName(), queryObject.getDomainObjectName());
			
				((MongoDbDomainObject) mongoObj).initDomainObject(iter.next());
				res.add(mongoObj);
			}
		
			return res;
		
		} catch (Exception e) {
			throw new ObjectNotFoundException(
					"Object does not exist in database!", e);
		}
	}

	/**
	 * This method extracts database collection name and inserts passet object in 
	 * database.
	 * @param object
	 *        Object to insert in database
	 * @return inserted object
	 * @throws ObjectNotStoredException
	 */
	private FfmaDomainObject storeToMongoDb(FfmaDomainObject object)
		throws ObjectNotStoredException {
	
		// TODO: check if exists? last time ? etc
		DBCollection mongoCollection = db.getCollectionFromString(object
				.getClass().getSimpleName());
		WriteResult res = mongoCollection.insert((BasicDBObject) object);
		log.debug("storeToMongoDb() coll:" + mongoCollection + ", res: " + res.toString());
		try {
			return retrieveObject(object);
		} catch (Exception e) {
			throw new ObjectNotStoredException(
					"Cannot store and retreive object from db after creation!",
					e);
		}
	}

	/**
	 * This method stores objects list in database using batch insert method.
	 * @param objectsList
	 * @throws ObjectNotStoredException
	 */
	private void storeListToMongoDb(List<FfmaDomainObject> objectsList)
		throws ObjectNotStoredException {
	
		DBCollection mongoCollection = db.getCollectionFromString(objectsList.get(0)
				.getClass().getSimpleName());
		WriteResult res = mongoCollection.insert(
				objectsList.toArray(new DBObject[objectsList.size()]),
				WriteConcern.SAFE);
		log.debug("storeToMongoDb() coll:" + mongoCollection + ", res: " + res.toString());
	}

	/**
	 * @param object
	 */
	private void updateCommonFields(FfmaDomainObject object) {
		((MongoDbDomainObject) object).updateCommonFields();
	}

	/**
	 * This method stores child domain objects in database
	 * @param object
	 * @throws ObjectNotStoredException
	 * @throws ObjectNotFoundException
	 */
	private void storeChildDomainObjects(FfmaDomainObject object)
			throws ObjectNotStoredException, ObjectNotFoundException {
		log.debug("storeChildDomainObjects() object: " + object);
		for (FieldDefEnum field : object.getFieldsEnum()) {
			if (isDomainObject(field.evalType())) {
				// store child objects recursively
				FfmaDomainObject childObject = (FfmaDomainObject) ((BasicDBObject) object)
						.get(field.evalName());
				FfmaDomainObject storedChildObject = store(childObject);
				if (storedChildObject != null) {
					updateDomainRefToDb(object, field, storedChildObject.getId());
				}
			} else if (isDomainObjectList(field.evalType())) {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				ArrayList<FfmaDomainObject> childObjectMap = (ArrayList) ((BasicDBObject) object)
					.get(field.evalName());
			    Iterator<FfmaDomainObject> itr = childObjectMap.iterator();
			    BasicDBList idMap = new BasicDBList();
			    while (itr.hasNext()) {
			    	FfmaDomainObject childObject = (FfmaDomainObject) itr.next();
					FfmaDomainObject storedChildObject = store(childObject);
					if (storedChildObject != null) {
						BasicDBObject childId = new BasicDBObject();
						childId.put(MongoDbConstants.OBJECT_ID, storedChildObject.getId());
						childId.put(MongoDbConstants.CLASS_NAME, storedChildObject.getClass().getCanonicalName());
						idMap.add(childId);
					}
		  	    }
				updateDomainRefToDb(object, field, idMap);
			}
		}
	}

	/**
	 * Update the reference in the parent object from the Ffma domain object type
	 * to the stored in database Mongo object ID.
	 * @param object
	 * @param field
	 * @param storedChildObject
	 */
	private void updateDomainRefToDb(FfmaDomainObject object,
			FieldDefEnum field, Object storedChildObject) {
		((BasicDBObject) object).remove(field.evalName());
		((BasicDBObject) object).put(field.evalName() + MongoDbConstants.OBJECT_ID,
				storedChildObject);
	}

	/**
	 * Verify if the List class is a class of the given type.
	 * @param evalType
	 *        The class type of the FfmaDomainObject field
	 * @return true if the given type is a list of Ffma domain objects
	 */
	private boolean isDomainObjectList(Class<?> evalType) {
		if (evalType.equals(List.class) || evalType.equals(ArrayList.class)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Verify if the BaseFfmaDomain object class is a superclass of the given type.
	 * @param evalType
	 *        The class type of the FfmaDomainObject field
	 * @return true if BaseFfmaDomainObject is a superclass of the given type
	 */
	private boolean isDomainObject(Class<?> evalType) {
		Class<?> superclass = evalType.getSuperclass();
		if (superclass != null) {
			while(!Object.class.equals(superclass)){
				if(BaseFfmaDomainObject.class.equals(superclass))
					return true;
				
				superclass = superclass.getSuperclass();
			}
		}
		return false;
	}

	/**
	 * This method retrieves Ffma object from database by passed object.
	 * 
	 * @param object
	 *        Request object of type FfmaDomainObject
	 * @return The object retrieved from the database
	 * @throws ObjectNotFoundException
	 */
	public FfmaDomainObject retrieveObject(FfmaDomainObject object)
			throws ObjectNotFoundException {
		FfmaDomainObject res = null;

		try {
			//System.out.println();
			res = readObjectFromDb(object);
			retrieveChildDomainObjects(res);
			log.debug("retrieveObject() res: " + res);
		} catch (Exception e) {
			throw new ObjectNotFoundException(
					MongoDbConstants.DIGITAL_OBJECT_NOT_FOUND, e);
		}

		return res;
	}

	/**
	 * This method retrieves Ffma object from database by passed object and field name.
	 * 
	 * @param object
	 *        Request object of type FfmaDomainObject
	 * @param fieldName
	 *        The database field name
	 * @return The object retrieved from the database
	 * @throws ObjectNotFoundException
	 */
	public FfmaDomainObject retrieveObjectByField(FfmaDomainObject object, String fieldName)
			throws ObjectNotFoundException {
		FfmaDomainObject res = null;

		try {
			res = readObjectFromDbByField(object, fieldName);
			retrieveChildDomainObjects(res);
			log.debug("retrieveObject() res: " + res);
		} catch (Exception e) {
			throw new ObjectNotFoundException(
					MongoDbConstants.DIGITAL_OBJECT_NOT_FOUND, e);
		}

		return res;
	}

	/**
	 * This method retrieves a list of AsstetsDomainObject objects from database
	 * by passed request object, database field name, query and values list. 
	 * Retrieving by query increases performance.
	 * 
	 * @param object
	 *        Request object of type FfmaDomainObject
	 * @param fieldName
	 *        The database object field name
	 * @param queryStr
	 *        The query
	 * @param values
	 *        Request values list	 
	 * @return the list of the retrieved FfmaDomainObject objects 
	 * @throws ObjectNotFoundException
	 */
	public List<FfmaDomainObject> retrieveObjectsListByField(FfmaDomainObject object,
			String fieldName, String queryStr, List<String> values)
			throws ObjectNotFoundException {

		List<FfmaDomainObject> res = new ArrayList<FfmaDomainObject>();
		try {
			List<FfmaDomainObject> objList = readObjectsListFromDbByField(object, fieldName, queryStr, values);
			Iterator<FfmaDomainObject> iter = objList.iterator();
			while (iter.hasNext()) {
				FfmaDomainObject obj = iter.next();
				retrieveChildDomainObjects(obj);
				log.debug("obj: " + obj);
				res.add(obj);
			}

		} catch (Exception e) {
			throw new ObjectNotFoundException(
					MongoDbConstants.DIGITAL_OBJECT_NOT_FOUND, e);
		}

		return res;
	}
	
	/**
	 * This method retrieves a list of AsstetsDomainObject objects from database
	 * by passed request object, database field name, query and values list. 
	 * Retrieving by query increases performance.
	 * Additional query focusing is possible using passed field - value pairs.
	 * 
	 * @param object
	 *        Request object of type FfmaDomainObject
	 * @param fieldName
	 *        The database object field name
	 * @param queryStr
	 *        The query
	 * @param values
	 *        Request values list	 
	 * @param addFieldsMap
	 *        The additional field - value pairs map
	 * @return the list of the retrieved FfmaDomainObject objects 
	 * @throws ObjectNotFoundException
	 */
	public List<FfmaDomainObject> retrieveObjectsListByFieldExt(FfmaDomainObject object,
			String fieldName, String queryStr, List<String> values, Map<String, String> addFieldsMap)
			throws ObjectNotFoundException {

		List<FfmaDomainObject> res = new ArrayList<FfmaDomainObject>();
		try {
			List<FfmaDomainObject> objList = readObjectsListFromDbByFieldExt(object, fieldName, queryStr, values, addFieldsMap);
			Iterator<FfmaDomainObject> iter = objList.iterator();
			while (iter.hasNext()) {
				FfmaDomainObject obj = iter.next();
				retrieveChildDomainObjects(obj);
				log.debug("obj: " + obj);
				res.add(obj);
			}

		} catch (Exception e) {
			throw new ObjectNotFoundException(
					MongoDbConstants.DIGITAL_OBJECT_NOT_FOUND, e);
		}

		return res;
	}
	
	/**
	 * This method retrieves a list of AsstetsDomainObject objects from database
	 * by passed query object. Retrieving by query increases performance.
	 * @param queryObject
	 *        The database query object of type FfmaDomainObject
	 * @return the list of the retrieved FfmaDomainObject objects 
	 */
	public List<? extends FfmaDomainObject> retrieveObjectsListByQuery(FfmaDomainObject queryObject) {

		List<FfmaDomainObject> res = new ArrayList<FfmaDomainObject>();
		try {
			List<FfmaDomainObject> objList = readObjectsListFromDbByQueryObject(queryObject);
			Iterator<FfmaDomainObject> iter = objList.iterator();
			while (iter.hasNext()) {
				FfmaDomainObject obj = iter.next();
				retrieveChildDomainObjects(obj);
				log.debug("obj: " + obj);
				res.add(obj);
			}

		} catch (Exception e) {
			log.debug(MongoDbConstants.DIGITAL_OBJECT_NOT_FOUND, e);
		}

		return res;
	}
	
	/**
	 * This method retrieves a list of AsstetsDomainObject objects from database
	 * by passed query object. Retrieving by query increases performance.
	 * @param queryObject
	 *        The database query object of type FfmaDomainObject
	 * @param exclusionsList 
	 *        The field names that should not be removed from the query object
	 * @return the list of the retrieved FfmaDomainObject objects 
	 */
	public List<? extends FfmaDomainObject> retrieveObjectsListByQueryExt(FfmaDomainObject queryObject, List<String> exclusionsList) {

		List<FfmaDomainObject> res = new ArrayList<FfmaDomainObject>();
		try {
			List<FfmaDomainObject> objList = readObjectsListFromDbByQueryObjectExt(queryObject, exclusionsList);
			Iterator<FfmaDomainObject> iter = objList.iterator();
			while (iter.hasNext()) {
				FfmaDomainObject obj = iter.next();
				retrieveChildDomainObjects(obj);
				log.debug("obj: " + obj);
				res.add(obj);
			}

		} catch (Exception e) {
			log.debug(MongoDbConstants.DIGITAL_OBJECT_NOT_FOUND, e);
		}

		return res;
	}
	
	/**
	 * This method retrieves child domain objects from database
	 * @param object
	 * @throws ObjectNotFoundException
	 */
	private void retrieveChildDomainObjects(FfmaDomainObject object)
			throws ObjectNotFoundException {
		for (FieldDefEnum field : object.getFieldsEnum()) {
			if (isDomainObject(field.evalType())) {
				String id = ((BasicDBObject) object).getString(field.evalName()
						+ MongoDbConstants.OBJECT_ID);
				if (id != null) {
					FfmaDomainObject readChild = readDbObjectById(field.evalType(), id);
					updateDbRefToDomain(object, field, readChild);
				}
			} else if (isDomainObjectList(field.evalType())) {
				BasicDBList idMap = (BasicDBList) ((BasicDBObject) object).get(field.evalName()
						+ MongoDbConstants.OBJECT_ID);
				ArrayList<FfmaDomainObject> childObjectMap = new ArrayList<FfmaDomainObject>();
				Iterator<Object> itr = idMap.iterator();
			    while (itr.hasNext()) {
			    	BasicDBObject idMapObject = (BasicDBObject) itr.next();
			    	String id = idMapObject.getString(MongoDbConstants.OBJECT_ID);
					if (id != null) {
						String className = idMapObject.getString(MongoDbConstants.CLASS_NAME);
						FfmaDomainObject readChild = readDbObjectById(getClassByName(className), id);
						childObjectMap.add(readChild);
					}
			    }
				updateDbRefToDomain(object, field, childObjectMap);
			}
		}
	}

	/**
	 * Update the reference in the parent object from the stored in database Mongo object ID
	 * to the Ffma domain object type.
	 * @param object
	 * @param field
	 * @param readChild
	 */
	private void updateDbRefToDomain(FfmaDomainObject object,
			FieldDefEnum field, Object readChild) {
		//remove the id of the child from parent object
		((BasicDBObject) object).remove(field.evalName() + MongoDbConstants.OBJECT_ID);
		//add the instance of child to parent object
		((BasicDBObject) object).put(field.evalName(), readChild);
	}

	/**
	 * This method reads object from Mongo database by id and converts it into the
	 * Ffma domain object format.
	 * @param classType
	 *        The Ffma domain object type
	 * @param id
	 *        The Mongo database id
	 * @return Ffma domain object
	 * @throws ObjectNotFoundException
	 */
	private FfmaDomainObject readDbObjectById(Class<?> classType, String id)
			throws ObjectNotFoundException {
		FfmaDomainObject childQuery = instantiateObjectByDbId(id, classType);
		FfmaDomainObject readChild = retrieveObject(childQuery);
		return readChild;
	}

	/**
	 * This method instantiates class of the given type by passed id.
	 * @param id
	 *        Passed Mongo database id
	 * @param childClass
	 *        Given Ffma domain object class type
	 * @return Ffma domain object
	 */
	private FfmaDomainObject instantiateObjectByDbId(String id,
			Class<?> childClass) {
		FfmaDomainObject childQuery = instantiateClassForQuery(childClass);
		((MongoDbDomainObject) childQuery).setId(id);
		return childQuery;
	}

	
	/**
	 * This method removes digital object from JCR identified by the passed
	 * permanent URI.
	 * 
	 * @param permanentURI
	 *            The permanent URI of digital object to be removed
	 * @param c
	 *            The Ffma object type
	 * @return result of remove method
	 * @throws ObjectNotRemovedException
	 */
	public void removeObject(FfmaDomainObject object)
			throws ObjectNotRemovedException {

		try {
			DBCollection collection = db.getCollectionFromString(object
					.getClass().getSimpleName());
			log.debug("MongoDbManager.removeObject() classname: " + object
					.getClass().getSimpleName() + ", object: " + object);
			FfmaDomainObject readedObj = readObjectFromDb(object);
			removeChildDomainObjects(readedObj);

			log.debug("removeObject() class: " + object.getClass().getSimpleName() + 
					", objectId: " + object.getId() + ", coll: " + collection);
			
			BasicDBObject cur = getDbObject(object.getId(), collection);
			WriteResult res = collection.remove((BasicDBObject) cur);
			log.debug("removeObject() res: " + res.toString());
		} catch (MongoException e) {
			log.error("MongoDbManager.removeObject() error: " + e);
			throw new ObjectNotRemovedException(
					MongoDbConstants.DIGITAL_OBJECT_NOT_REMOVED, e);
		} catch (Exception e) {
			log.error("MongoDbManager.removeObject() error1: " + e);
			throw new ObjectNotRemovedException(
					MongoDbConstants.DIGITAL_OBJECT_NOT_REMOVED, e);
		}
	}

	/**
	 * This method removes child domain objects from database
	 * @param object
	 * @throws ObjectNotFoundException
	 * @throws ObjectNotRemovedException
	 */
	private void removeChildDomainObjects(FfmaDomainObject object)
			throws ObjectNotFoundException, ObjectNotRemovedException {
		for (FieldDefEnum field : object.getFieldsEnum()) {
			if (isDomainObject(field.evalType())) {
				String id = ((BasicDBObject) object).getString(field.evalName() + MongoDbConstants.OBJECT_ID);
				if (id != null) {
					FfmaDomainObject childQuery = instantiateObjectByDbId(id, field.evalType());
					removeObject(childQuery);
				}
			} else if (isDomainObjectList(field.evalType())) {
				BasicDBList idMap = (BasicDBList) ((BasicDBObject) object).get(field.evalName()
						+ MongoDbConstants.OBJECT_ID);
				Iterator<Object> itr = idMap.iterator();
			    while (itr.hasNext()) {
			    	BasicDBObject idMapObject = (BasicDBObject) itr.next();
			    	String id = idMapObject.getString(MongoDbConstants.OBJECT_ID);
					if (id != null) {
						String className = idMapObject.getString(MongoDbConstants.CLASS_NAME);
				    	Class<?> childClass = getClassByName(className);
						FfmaDomainObject childQuery = instantiateObjectByDbId(id, childClass);
						removeObject(childQuery);
					}
			    }
			}
		}	
	}

	/**
	 * This method extracts class by passed class name.
	 * @param className
	 *        The class name
	 * @return class
	 * @throws ObjectNotFoundException
	 */
	private Class<?> getClassByName(String className)
			throws ObjectNotFoundException {
		Class<?> childClass;
		try {
			childClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ObjectNotFoundException("Class " + className + " not found.", e);
		}
		return childClass;
	}

	/**
	 * this method instantiates database object by passed child class
	 * @param childClass
	 *        The child class
	 * @return database object
	 */
	protected FfmaDomainObject instantiateClassForQuery(Class<?> childClass){
		FfmaDomainObject childQuery;
		try {
			childQuery = (FfmaDomainObject) Class
					.forName(childClass.getName()).newInstance();
		} catch (InstantiationException e) {
			throw new FfmaTechnicalRuntimeException("Cannot instantiate class: " + childClass.getName(), e);
		} catch (IllegalAccessException e) {
			throw new FfmaTechnicalRuntimeException("Cannot instantiate class: " + childClass.getName(), e);
		} catch (ClassNotFoundException e) {
			throw new FfmaTechnicalRuntimeException("Cannot instantiate class: " + childClass.getName(), e);
		}
		return childQuery;
	}

	/**
	 * This method enables queries like '$in' in order to enhance performance.
	 * @param coll
	 *        The database collection
	 * @param fieldName
	 *        The database object field name
	 * @param queryStr
	 *        The query
	 * @param values
	 *        Request values list
	 * @return the list of the objects found in database
	 */
	public List<BasicDBObject> getDbObjectsList(DBCollection coll, String fieldName, String queryStr, List<String> values) {

		BasicDBObject query = new BasicDBObject();
		if (fieldName != null) {
			query.put(fieldName, new BasicDBObject(queryStr, values));
		}
		log.trace(query);
		return findListForQuery(coll, query);
	}

	/**
	 * This method creates connection to the Mongo database,
	 * executes find method for given query and closes this connection.
	 * @param coll
	 * @param query
	 * @return list of results
	 */
	private List<BasicDBObject> findListForQuery(DBCollection coll, BasicDBObject query) {

		List<BasicDBObject> res = new ArrayList<BasicDBObject>();
		
		DBCursor cur = coll.find(query);
		
		if (cur != null && cur.size() == 0) {
			log.debug("object not found in database. query result size is 0.");
		}

		try {
			while (cur.hasNext()) {
				res.add((BasicDBObject) cur.next());
			}
		} finally {
		    cur.close();
		}
		
		return res;
	}
	
	/**
	 * This method creates connection to the Mongo database,
	 * executes find method for given query and closes this connection.
	 * @param coll
	 * @param query
	 * @return single result 
	 */
	private BasicDBObject findSingleForQuery(DBCollection coll, BasicDBObject query) {

		BasicDBObject res = null;

		DBCursor cur = coll.find(query);
		if (cur != null && cur.size() == 0) {
			log.info("object not found in database. query result size is 0.");
		}

		try {
			if (cur.hasNext()) {
				res = (BasicDBObject) cur.next();
			}
		} finally {
		    cur.close();
		}

		return res;
	}
	
	/**
	 * This method enables queries like '$in' in order to enhance performance. 
	 * Additional query focusing is possible using passed field - value pairs.
	 * @param coll
	 *        The database collection
	 * @param fieldName
	 *        The database object field name
	 * @param queryStr
	 *        The query
	 * @param values
	 *        Request values list
	 * @param addFieldsMap
	 *        The additional field - value pairs map
	 * @return the list of the objects found in database
	 */
	public List<BasicDBObject> getDbObjectsListExt(DBCollection coll,
			String fieldName, String queryStr, List<String> values,
			Map<String, String> addFieldsMap) {
		
		BasicDBObject query = new BasicDBObject();
		if (fieldName != null) {
			query.put(fieldName, new BasicDBObject(queryStr, values));
			if (addFieldsMap != null && !addFieldsMap.isEmpty()) {
				// sample query.put("CollectionName", "92001_Ag_EU_TELtreasures"); 
				for (Map.Entry<String, String> entry : addFieldsMap.entrySet()) {
					query.put(entry.getKey(), entry.getValue());
				}
			}
		}
		log.trace(query);
		return findListForQuery(coll, query);
	}
	
	/**
	 * This method enables queries by query object.
	 * @param coll
	 *        The database collection
	 * @param queryObject
	 *        The query object in BasicDBObject format
	 * @return the list of the objects found in database
	 */
	@SuppressWarnings("rawtypes")
	public List<BasicDBObject> getDbObjectsListByQueryObject(DBCollection coll, BasicDBObject queryObject) {
		
		BasicDBObject query = new BasicDBObject();
		
		query.putAll((Map) queryObject);
		
	    for (CommonFieldsEnum value : CommonFieldsEnum.values()) {
			query.remove(value.name());
	    }
		log.debug("query: " + query);
		return findListForQuery(coll, query);
	}
	
	/**
	 * This method enables queries by query object and checks if exclusions list contains 
	 * current value. If it includes this value should not be removed from the database 
	 * query object and this method returns true.
	 * @param coll
	 *        The database collection
	 * @param queryObject
	 *        The query object in BasicDBObject format
	 * @param exclusionsList 
	 *        The field names that should not be removed from the query object
	 * @return the list of the objects found in database
	 */
	@SuppressWarnings("rawtypes")
	public List<BasicDBObject> getDbObjectsListByQueryObjectExt(DBCollection coll, BasicDBObject queryObject, List<String> exclusionsList) {
		
		BasicDBObject query = new BasicDBObject();
		
		query.putAll((Map) queryObject);
			
	    for (CommonFieldsEnum value : CommonFieldsEnum.values()) {
	    	if (exclusionsList == null || !exclusionsList.contains(value.name())) {
	    		query.remove(value.name());
	    	}
	    }
		log.info("query: " + query);
		return findListForQuery(coll, query);
	}
	
	/**
	 * This method removes objects set from database using queries by query object and checks if exclusions list contains 
	 * current value. If it includes this technical value should not be removed from the database query object.
	 * @param queryObject
	 *        The query object in BasicDBObject format
	 * @param exclusionsList 
	 *        The field names that should not be removed from the query object
	 * @return the list of the objects found in database
	 */
	@SuppressWarnings("rawtypes")
	public void removeObjectsSet(FfmaDomainObject queryObject, List<String> exclusionsList)
			throws ObjectNotRemovedException {

		BasicDBObject query = new BasicDBObject();
		DBCollection collection = db.getCollectionFromString(queryObject
				.getClass().getSimpleName());

		query.putAll((Map) queryObject);
		
	    for (CommonFieldsEnum value : CommonFieldsEnum.values()) {
	    	if (exclusionsList == null || !exclusionsList.contains(value.name())) {
	    		query.remove(value.name());
	    	}
	    }
		log.info("remove objects set query: " + query);
		try {
			WriteResult res = collection.remove((BasicDBObject) query);
			log.info("removeObject() res: " + res.toString());
		} catch (MongoException e) {
			log.info("MongoDbManager.removeObject() error: " + e);
			throw new ObjectNotRemovedException(
					MongoDbConstants.DIGITAL_OBJECT_NOT_REMOVED, e);
		} catch (Exception e) {
			log.info("MongoDbManager.removeObject() error1: " + e);
			throw new ObjectNotRemovedException(
					MongoDbConstants.DIGITAL_OBJECT_NOT_REMOVED, e);
		}
	}
	
	/**
	 * This method retrieves database object by object ID.
	 * @param id
	 *        The object ID
	 * @param coll
	 *        The database collection
	 * @return object from database that matches query parameters
	 */
	public BasicDBObject getDbObject(String id, DBCollection coll) {
		
		BasicDBObject query = new BasicDBObject();
		query.put(CommonFieldsEnum._id.name(), new ObjectId(id));

		return findSingleForQuery(coll, query);
	}

	/**
	 * This method retrieves database object by query field name and value.
	 * @param id
	 *        The query value
	 * @param coll
	 *        The database collection
	 * @param fieldName
	 *        The query field name
	 * @return object from database that matches query parameters
	 */
	public BasicDBObject getDbObjectByField(String id, DBCollection coll, String fieldName) {

		BasicDBObject query = new BasicDBObject();
		query.put(fieldName, id);

		return findSingleForQuery(coll, query);
	}
	
	public abstract FfmaAbstractFactory getObjectFactory();

	/**
	 * This method is implementing a high performance selection of the values for the given returnField attribute
	 * for all objects which match the select filter (filterField, filterValue)  
	 * @param object
	 *        The request object
	 * @param fieldName
	 *        The database object field name
	 * @param atttibuteName
	 *        The query
	 * @param values
	 *        Request values list	 
	 * @return the list of the FfmaDomainObject objects found in database
	 * @throws ObjectNotFoundException
	 */
	public Set<String> readAttributeValuesFromDbByField(
			FfmaDomainObject object, String returnFieldName, String filterFieldName,
			List<String> filterValues, String afterDateFieldName, Long afterDateValue) {
		
		Set<String> res = new HashSet<String>();
		
			DBCollection mongoCollection = db.getCollectionFromString(object
					.getClass().getSimpleName());
			
			BasicDBObject query = new BasicDBObject();
			if (filterFieldName != null)
				query.put(filterFieldName, new BasicDBObject(MongoDbConstants.IN_QUERY, filterValues));
			
			if(afterDateFieldName != null && afterDateValue !=null){
				query.put(afterDateFieldName, new BasicDBObject(MongoDbConstants.GREATER_THAN_QUERY, afterDateValue));
			}
			log.info("Search Query: " + query);
						
			BasicDBObject returnKeys = new BasicDBObject();
			returnKeys.append(returnFieldName, 1);
			log.info("return keys: " + returnKeys);
			DBCursor cur = mongoCollection.find(query, returnKeys);
			
			try {
				//List<BasicDBObject> mongoObjList = getDbObjectsList(mongoCollection, fieldName, atttibuteName, values);
				Iterator<DBObject> iter = cur.iterator();
				while (iter.hasNext()) {
					res.add((String)iter.next().get(returnFieldName));
				}
			} finally {
			    cur.close();
			}		
			return res;		
	}
	
	/**
	 * @see #readAttributeValuesFromDbByField(FfmaDomainObject, String, String, List, String, Long)
	 * @param object
	 * @param returnFieldName
	 * @param filterFieldName
	 * @param filterValues
	 * @return
	 */
	public Set<String> readAttributeValuesFromDbByField(
			FfmaDomainObject object, String returnFieldName, String filterFieldName,
			List<String> filterValues) {
		
		return readAttributeValuesFromDbByField(object, returnFieldName, filterFieldName,
				filterValues, null, null);
	}
	
	/**
	 * This method groups collection values by passed field and calculates count of each value.
	 * @param groupByField
	 *        The name of the field for grouping
	 * @param queryObject
	 *        The query object that defines database collection
	 * @return map of grouped values with correspondent counts
	 */
	@SuppressWarnings("rawtypes")
	public Map getGroupedObjectsCount(String groupByField, FfmaDomainObject queryObject) {
		DBCollection mongoCollection = db.getCollectionFromString(queryObject
				.getClass().getSimpleName());

		BasicDBObject keyObject = new BasicDBObject();
		keyObject.put(groupByField, true);
		BasicDBObject initialObject = new BasicDBObject();
		initialObject.put("count", 0);
		BasicDBObject condObject = new BasicDBObject();
		String reduce = "function(obj,prev) { prev.count++;}";

		DBObject resObject = mongoCollection.group(keyObject, condObject, initialObject, reduce);
		
		return resObject.toMap();
	}
		
}