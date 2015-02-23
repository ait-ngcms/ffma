package ait.ffma.service.preservation.riskmanagement.dao;

import java.util.List;
import java.util.Map;

import ait.ffma.common.dao.FfmaDao;
import ait.ffma.common.exception.CollectionNotRemovedException;
import ait.ffma.common.exception.FfmaCommonException;
import ait.ffma.common.exception.ObjectNotFoundException;
import ait.ffma.common.exception.ObjectNotStoredException;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.FileDescription;
import ait.ffma.domain.preservation.riskmanagement.FormatRiskAnalysisReport;


public interface PreservationRiskmanagementDao extends FfmaDao{

	/**
	 * This method retrieves format risk analysis report from database by passed object id.
	 * @param id
	 *        The object ID
	 * @return Format risk analysis report object
	 * @throws FfmaCommonException
	 */
	public FormatRiskAnalysisReport getFormatRiskAnalysisReport(String id) throws FfmaCommonException;

	/**
	 * This method stores format risk analysis report in database.
	 * @param formatRiskAnalysisReport
	 *        The initial format risk analysis report object without object ID and another database parameter
	 * @return Stored format risk analysis report object
	 * @throws FfmaCommonException
	 */
	public FormatRiskAnalysisReport storeFormatRiskAnalysisReport(FormatRiskAnalysisReport formatRiskAnalysisReport) throws FfmaCommonException;


	/**
	 * This method retrieves file description from database by passed object id.
	 * @param id
	 *        The object ID
	 * @return File description object
	 * @throws FfmaCommonException
	 */
	public FileDescription getFileDescription(String id) throws FfmaCommonException;

	/**
	 * This method stores file description in database.
	 * @param fileDescription
	 *        The initial file description object without object ID and another database parameter
	 * @return Stored file description object
	 * @throws FfmaCommonException
	 */
	public FileDescription storeFileDescription(FileDescription fileDescription) throws FfmaCommonException;

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
			List<String> values) throws FfmaCommonException;

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
			List<String> values, Map<String, String> addFieldsMap) throws FfmaCommonException;

	/**
	 * This method checks if requested object already exists in database and updates database object.
	 * If object already exists is should be updated: removed and replaced by new version.
	 * Otherwise just insert object in database. 
	 * @param queryObject
	 *        The request object to find out if object already exists in database
	 * @param storeObject
	 *        The object to store
	 */
	public void updateObject(FfmaDomainObject queryObject, FfmaDomainObject storeObject) throws FfmaCommonException;

	/**
	 * This method checks if requested object already exists in database.
	 * @param queryObject
	 *        The request object to find out if object already exists in database
	 */
	public boolean existsInDb(FfmaDomainObject queryObject);
	
	/**
	 * This method removes objects that match query object from database.
	 * @param queryObject
	 *        The request object to find out if object already exists in database
	 */
	public void cleanUpInDb(FfmaDomainObject queryObject);
	
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
	 * This method removes database collection
	 * @param collectionName
	 * @throws CollectionNotRemovedException
	 */
	public void removeCollectionByName(String collectionName)
			throws CollectionNotRemovedException;

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
	 * Stores the given domain object into the database
	 * @param object
	 * @return
	 * @throws ObjectNotStoredException
	 * @throws ObjectNotFoundException
	 */
	public FfmaDomainObject storeObject(FfmaDomainObject object)
		throws ObjectNotStoredException, ObjectNotFoundException;
}
