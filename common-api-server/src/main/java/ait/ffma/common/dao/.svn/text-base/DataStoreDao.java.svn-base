package ait.ffma.common.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ait.ffma.common.exception.CollectionNotRemovedException;
import ait.ffma.common.exception.ObjectNotFoundException;
import ait.ffma.common.exception.ObjectNotRemovedException;
import ait.ffma.common.exception.ObjectNotStoredException;
import ait.ffma.common.exception.ObjectNotUpdatedException;
import ait.ffma.domain.FfmaDomainObject;

/**
 * This interface declares the functionality that needs to be implemented for providing 
 * a common data storage service.    
 * 
 * @author GordeaS <gsergiu79@gmx.at>
 *
 */
public interface DataStoreDao {

	/**
	 * Stores the given domain object into the database
	 * @param object
	 * @return
	 * @throws ObjectNotStoredException
	 * @throws ObjectNotFoundException
	 */
	public FfmaDomainObject storeObject(FfmaDomainObject object)
		throws ObjectNotStoredException, ObjectNotFoundException;

	/**
	 * This method stores a list of objects using batch insert method.
	 * @param objectsList
	 *        The list of objects to store in database
	 * @return storing result - true if successful, false in failure case
	 * @throws ObjectNotStoredException
	 * @throws ObjectNotFoundException
	 */
	public boolean storeObjectsList(List<FfmaDomainObject> objectsList)
		throws ObjectNotStoredException, ObjectNotFoundException;

	/**
	 * Reads the object identified by the given object id from the database 
	 * @param object
	 * @return
	 * @throws ObjectNotFoundException
	 */
	public FfmaDomainObject retrieveObject(FfmaDomainObject object)
			throws ObjectNotFoundException;

	/**
	 * Reads the object identified by the passed field from the database 
	 * @param object
	 * @param fieldName
	 *        The object identifier field
	 * @return
	 * @throws ObjectNotFoundException
	 */
	public FfmaDomainObject retrieveObjectByField(FfmaDomainObject object, String fieldName)
			throws ObjectNotFoundException;
	
	/**
	 * Reads the object list identified by the passed request object, database field name, query and values list. 
	 * Retrieving by query increases performance.
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
	public List<FfmaDomainObject> retrieveObjectsListByField(
			FfmaDomainObject object, String fieldName, String queryStr,
			List<String> values) throws ObjectNotFoundException;

	/**
	 * Reads the object list identified by the passed request object, database field name, query and values list. 
	 * Retrieving by query increases performance.
	 * Additional query focusing is possible using passed field - value pairs.
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
	public List<FfmaDomainObject> retrieveObjectsListByFieldExt(
			FfmaDomainObject object, String fieldName, String queryStr,
			List<String> values, Map<String, String> addFieldsMap) throws ObjectNotFoundException;

	/**
	 * Reads the values for the given returnFieldName for all objects that match the given filter (filterField, filterValues) 
	 * Retrieving by query increases performance.
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
	public Set<String> readAttributeValuesFromDbWithFilter(
			FfmaDomainObject object, String returnFieldName, String filterFieldName,
			List<String> filterValues);

	/**
	 * Reads the values for the given returnFieldName for all objects that match the given filter (filterField, filterValues) 
	 * Retrieving by query increases performance.
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
	public Set<String> readAttributeValuesFromDbWithFilter(
			FfmaDomainObject object, String returnFieldName, String filterFieldName,
			List<String> filterValues, String afterDateFieldName, Long afterDateValue);
	
	
	/**
	 * Updates the object identified by the given object id from the database 
	 * @param object
	 * @return
	 * @throws ObjectNotFoundException
	 */
	public FfmaDomainObject updateObject(FfmaDomainObject object)
			throws ObjectNotUpdatedException, ObjectNotStoredException, ObjectNotFoundException;

	/**
	 * This method removes the given object from the database
	 * @param object
	 * @throws ObjectNotRemovedException
	 */
	public void removeObject(FfmaDomainObject object)
			throws ObjectNotRemovedException;

	/**
	 * This method removes database collection
	 * @param collectionName
	 * @throws CollectionNotRemovedException
	 */
	public void removeCollectionByName(String collectionName)
			throws CollectionNotRemovedException;

	/**
	 * This utility method checks if the database connection can be established
	 * @return the status of the Mongo database. True if it is running, false if not.
	 */
	public boolean isDbRunning();
	
	/**
	 * Reads the object list identified by the passed query object. Retrieving by query increases performance.
	 * @param queryObject
	 *        Query object of type FfmaDomainObject
	 * @return the list of the retrieved FfmaDomainObject objects 
	 */
	public List<? extends FfmaDomainObject> retrieveObjectsListByQuery (
			FfmaDomainObject queryObject);
	
	/**
	 * This method retrieves database collection specified by name in query object and by 
	 * DomainObjectName in exclusions list. Exclusion means that this technical field will not be
	 * removed from query object before search.
	 * @param queryObject
	 *        Query object of type FfmaDomainObject
	 * @param exclusionsList
	 *        The field names that should not be removed from the query object
	 * @return the list of the database collection objects 
	 */
	public List<? extends FfmaDomainObject> retrieveCollection (
			FfmaDomainObject queryObject, List<String> exclusionsList);
	
	/**
	 * This method removes objects set from database using queries by query object and checks if exclusions list contains 
	 * current value. If it includes this technical value should not be removed from the database query object.
	 * @param queryObject
	 *        The query object in BasicDBObject format
	 * @param exclusionsList 
	 *        The field names that should not be removed from the query object
	 * @return the list of the objects found in database
	 */
	public void removeObjectsSet(FfmaDomainObject queryObject, List<String> exclusionsList)
			throws ObjectNotRemovedException;

	/**
	 * This method groups collection values by passed field and calculates count of each value.
	 * @param groupByField
	 *        The name of the field for grouping
	 * @param queryObject
	 *        The query object that defines database collection
	 * @return map of grouped values with correspondent counts
	 */
	@SuppressWarnings("rawtypes")
	public Map getGroupedObjectsCount (
			String groupByField, FfmaDomainObject queryObject);

	/**
	 * This method checks if requested object already exists in database and updates database object.
	 * If object already exists is should be updated: removed and replaced by new version.
	 * Otherwise just insert object in database. 
	 * @param queryObject
	 *        The request object to find out if object already exists in database
	 * @param storeObject
	 *        The object to store
	 * @param exclusionsList
	 *        The technical fields which should be ignored during the search process
	 */
	public void updateObject(FfmaDomainObject queryObject, FfmaDomainObject storeObject, List<String> exclusionsList);

	/**
	 * This method checks if requested object already exists in database.
	 * @param queryObject
	 *        The request object to find out if object already exists in database
	 * @param exclusionsList
	 *        The technical fields which should be ignored during the search process
	 * @return true if object already exists in database, false otherwise
	 */
	public boolean existsInDb(FfmaDomainObject queryObject, List<String> exclusionsList);
	
	/**
	 * This method removes objects that match query object from database.
	 * @param queryObject
	 *        The request object to find out if object already exists in database
	 * @param exclusionsList
	 *        The technical fields which should be ignored during the search process
	 */
	public void cleanUpInDb(FfmaDomainObject queryObject, List<String> exclusionsList);
	
}