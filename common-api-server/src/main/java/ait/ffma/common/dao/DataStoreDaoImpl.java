package ait.ffma.common.dao;

/**
 * This class manages digital objects for Mongo database
 */
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBObject;

import ait.ffma.common.dao.mongodb.MongoDbManager;
import ait.ffma.common.exception.CollectionNotRemovedException;
import ait.ffma.common.exception.ObjectNotFoundException;
import ait.ffma.common.exception.ObjectNotRemovedException;
import ait.ffma.common.exception.ObjectNotStoredException;
import ait.ffma.common.exception.ObjectNotUpdatedException;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.MongoDbDomainObject;

/**
 * Implementation of the DigitalObjectManager interface for Mongo database.
 * @author GrafR
 */
/**
 * @author GrafR
 *
 */
public class DataStoreDaoImpl implements DataStoreDao {
	
	/**
	 * Logger
	 */
	private static Logger log = Logger.getLogger(DataStoreDaoImpl.class
			.getClass());

	/** Manager manages Mongo database functionality */
	@Autowired
	private MongoDbManager mongoDbManager = null;

	
	/**
	 * Simply loads the properties and instantiates the mongoDbManager. The constructor
	 * should only fail because it cannot find the properties file or the
	 * mongoDbManager cannot connect to a database.
	 */
	public DataStoreDaoImpl() {
	}
	
	/**
	 * @param mongoDbManager the mongoDbManager to set
	 */
	public void setMongoDbManager(MongoDbManager mongoDbManager) {
		this.mongoDbManager = mongoDbManager;
	}

	/**
	 * This method removes all digital objects from JCR.
	 * 
	 * @return result of remove method
	 * @throws ObjectNotRemovedException
	 */
	public void removeAll() throws ObjectNotRemovedException {
		mongoDbManager.removeAll();
	}

	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#storeObject(ait.ffma.domain.FfmaDomainObject)
	 */
	public FfmaDomainObject storeObject(FfmaDomainObject object)
			throws ObjectNotStoredException, ObjectNotFoundException {
		
		log.trace("store object in db(FfmaDomainObject ado): " + object);
		if (object instanceof MongoDbDomainObject
				&& object instanceof BasicDBObject)
			return mongoDbManager.store(object);
		else
			throw new ObjectNotStoredException(
					"MongoDbDataStore store can handle only BasicDBObject+MongoDbDomainObject objects");
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#storeObjectsList(java.util.List)
	 */
	public boolean storeObjectsList(List<FfmaDomainObject> objectsList)
			throws ObjectNotStoredException, ObjectNotFoundException {
		boolean res = false;
		log.trace("store objects list in db(FfmaDomainObject ado) first entry in list: " + objectsList.get(0));
		if (objectsList.get(0) != null && objectsList.get(0) instanceof MongoDbDomainObject
				&& objectsList.get(0) instanceof BasicDBObject)
			res = mongoDbManager.storeList(objectsList);
		else
			throw new ObjectNotStoredException(
					"MongoDbDataStore store can handle only BasicDBObject+MongoDbDomainObject objects");
		return res;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#retrieveObject(ait.ffma.domain.FfmaDomainObject)
	 */
	public FfmaDomainObject retrieveObject(FfmaDomainObject object)
			throws ObjectNotFoundException {

		log.trace("retrieve object (FfmaDomainObject ado): " + object);
		return mongoDbManager.retrieveObject(object);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#retrieveObjectByField(ait.ffma.domain.FfmaDomainObject, java.lang.String)
	 */
	public FfmaDomainObject retrieveObjectByField(FfmaDomainObject object, String fieldName)
			throws ObjectNotFoundException {
	
		log.trace("retrieve object by field (FfmaDomainObject ado): " + object);
		return mongoDbManager.retrieveObjectByField(object, fieldName);
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#retrieveObjectsListByField(ait.ffma.domain.FfmaDomainObject, java.lang.String, java.lang.String, java.util.List)
	 */
	public List<FfmaDomainObject> retrieveObjectsListByField(
			FfmaDomainObject object, String fieldName, String queryStr,
			List<String> values) throws ObjectNotFoundException {
	
		log.trace("retrieve object list by field (FfmaDomainObject ado): " + object);
		return mongoDbManager.retrieveObjectsListByField(object, fieldName, queryStr, values);
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#retrieveObjectsListByFieldExt(ait.ffma.domain.FfmaDomainObject, java.lang.String, java.lang.String, java.util.List, java.util.Map)
	 */
	public List<FfmaDomainObject> retrieveObjectsListByFieldExt(
			FfmaDomainObject object, String fieldName, String queryStr,
			List<String> values, Map<String, String> addFieldsMap) throws ObjectNotFoundException {
	
		log.trace("retrieve object list by field (FfmaDomainObject ado): " + object);
		return mongoDbManager.retrieveObjectsListByFieldExt(object, fieldName, queryStr, values, addFieldsMap);
	}
	
	
		
	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#removeObjectsSet(com.mongodb.BasicDBObject, java.util.List)
	 */
	public void removeObjectsSet(FfmaDomainObject queryObject, List<String> exclusionsList)
			throws ObjectNotRemovedException {
		log.trace("remove objects set by query object: " + queryObject);
		mongoDbManager.removeObjectsSet(queryObject, exclusionsList);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#updateObject(ait.ffma.domain.FfmaDomainObject)
	 */
	public FfmaDomainObject updateObject(FfmaDomainObject object)
		throws ObjectNotUpdatedException, ObjectNotStoredException, ObjectNotFoundException {

		log.trace("update object in db(FfmaDomainObject ado): " + object);
		if (object instanceof MongoDbDomainObject
				&& object instanceof BasicDBObject)
			return mongoDbManager.update(object);
		else
			throw new ObjectNotUpdatedException(
					"MongoDbDataStore update can handle only BasicDBObject+MongoDbDomainObject objects");
	}

	/*
	 * (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#removeObject(ait.ffma.domain.FfmaDomainObject)
	 */
	public void removeObject(FfmaDomainObject object)
			throws ObjectNotRemovedException {

		log.trace("remove object (FfmaDomainObject ado): " + object);
		mongoDbManager.removeObject(object);
		
	}

	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#removeCollectionByName(java.lang.String)
	 */
	public void removeCollectionByName(String collectionName)
			throws CollectionNotRemovedException {
		log.trace("remove collection: " + collectionName);
		mongoDbManager.removeCollectionByName(collectionName);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#isDbRunning()
	 */
	public boolean isDbRunning() {
		return mongoDbManager.isDbRunning();
	}

	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#retrieveObjectsListByQuery(ait.ffma.domain.FfmaDomainObject)
	 */
	public List<? extends FfmaDomainObject> retrieveObjectsListByQuery (
			FfmaDomainObject queryObject) {
	
		log.trace("retrieve object list by query (FfmaDomainObject ado): " + queryObject);
		return mongoDbManager.retrieveObjectsListByQuery(queryObject);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#retrieveCollection(ait.ffma.domain.FfmaDomainObject, java.util.List)
	 */
	public List<? extends FfmaDomainObject> retrieveCollection (
			FfmaDomainObject queryObject, List<String> exclusionsList) {
	
		log.trace("retrieve object list by query (FfmaDomainObject ado): " + queryObject);
		return mongoDbManager.retrieveObjectsListByQueryExt(queryObject, exclusionsList);
	}

	/*
	 * (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#readAttributeValuesFromDbWithFilter(ait.ffma.domain.FfmaDomainObject, java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public Set<String> readAttributeValuesFromDbWithFilter(
			FfmaDomainObject object, String returnFieldName,
			String filterFieldName, List<String> filterValues)
			{
		
		log.trace("retrieve attributes values from field: "+ object.getComponentName()+ "." 
				+ object.getDomainObjectName()+ "." + returnFieldName + " with the following filter: ( " + 
				filterFieldName + ":" + filterValues);
		
		return mongoDbManager.readAttributeValuesFromDbByField(object, returnFieldName, filterFieldName, filterValues);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#readAttributeValuesFromDbWithFilter(ait.ffma.domain.FfmaDomainObject, java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public Set<String> readAttributeValuesFromDbWithFilter(
			FfmaDomainObject object, String returnFieldName,
			String filterFieldName, List<String> filterValues,
			 String afterDateFieldName, Long afterDateValue)
			{
		
		log.trace("retrieve attributes values from field: "+ object.getComponentName()+ "." 
				+ object.getDomainObjectName()+ "." + returnFieldName + " with the following filter: ( " + 
				filterFieldName + ":" + filterValues + ", afterDateFieldName: " + afterDateFieldName + 
				", afterDateValue: " + afterDateValue);
		
		return mongoDbManager.readAttributeValuesFromDbByField(object, returnFieldName, filterFieldName, filterValues, afterDateFieldName, afterDateValue);
	}
			
	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#getGroupedObjectsCount(java.lang.String, ait.ffma.domain.FfmaDomainObject)
	 */
	@SuppressWarnings("rawtypes")
	public Map getGroupedObjectsCount (
			String groupByField, FfmaDomainObject queryObject) {	
		log.trace("retrieve object list by query (FfmaDomainObject ado): " + queryObject);
		return mongoDbManager.getGroupedObjectsCount(groupByField, queryObject);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#updateObject(ait.ffma.domain.FfmaDomainObject, ait.ffma.domain.FfmaDomainObject, java.util.List)
	 */
	public void updateObject(FfmaDomainObject queryObject, FfmaDomainObject storeObject, List<String> exclusionsList) {
		try {
			// first remove old object if already exists
			if (existsInDb(queryObject, exclusionsList)) {
				cleanUpInDb(queryObject, exclusionsList);
			}
			storeObject(storeObject);
		} catch (ObjectNotStoredException e) {
			log.info("Store object: " + storeObject + ", error: " + e);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
//			log.info("Not found object: " + queryObject + ", error: " + e);
		}
	}

	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#existsInDb(ait.ffma.domain.FfmaDomainObject, java.util.List)
	 */
	public boolean existsInDb(FfmaDomainObject queryObject, List<String> exclusionsList) {
		boolean exists = false;
		List<? extends FfmaDomainObject> checkResultObjectList = retrieveCollection(queryObject, exclusionsList);
		if (checkResultObjectList != null && checkResultObjectList.size() > 0) {
			exists = true;
		}
		return exists;
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.common.dao.DataStoreDao#cleanUpInDb(ait.ffma.domain.FfmaDomainObject, java.util.List)
	 */
	public void cleanUpInDb(FfmaDomainObject queryObject, List<String> exclusionsList) {
		try {
			removeObjectsSet(queryObject, exclusionsList);
		} catch (ObjectNotRemovedException e) {
			e.printStackTrace();
		}
	}

}
