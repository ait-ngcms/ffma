package ait.ffma.service.preservation.riskmanagement.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import ait.ffma.common.dao.BaseFfmaDao;
import ait.ffma.common.dao.DataStoreDao;
import ait.ffma.common.dao.mongodb.MongoDbConstants;
import ait.ffma.common.exception.CollectionNotRemovedException;
import ait.ffma.common.exception.FfmaCommonException;
import ait.ffma.common.exception.ObjectNotFoundException;
import ait.ffma.common.exception.ObjectNotStoredException;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.FileDescription;
import ait.ffma.domain.preservation.riskmanagement.FormatRiskAnalysisReport;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;

public class PreservationRiskmanagementDaoImpl extends BaseFfmaDao implements PreservationRiskmanagementDao {

	@Autowired
	private DataStoreDao dataStoreDao;
	
	/**
	 * Simply loads the properties and instantiates the mongoDbManager. The constructor
	 * should only fail because it cannot find the properties file or the
	 * mongoDbManager cannot connect to a database.
	 */
	public PreservationRiskmanagementDaoImpl() {
	}
	
	/**
	 * @return the dataStoreDao
	 */
	public DataStoreDao getDataStoreDao() {
		return dataStoreDao;
	}

	/**
	 * @param dataStoreDao the dataStoreDao to set
	 */
	public void setDataStoreDao(DataStoreDao dataStoreDao) {
		this.dataStoreDao = dataStoreDao;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao#getFileDescription(java.lang.String)
	 */
	public FileDescription getFileDescription(String id) throws FfmaCommonException {
		try {
			FileDescription fileDescription = new FileDescription();
			fileDescription.setId(id);

			return (FileDescription) dataStoreDao.retrieveObject((FfmaDomainObject) fileDescription);
		} catch (Exception e) {
			throw new FfmaCommonException("Error analysis result retrieve. " + e);
		}
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao#storeFileDescription(ait.ffma.domain.preservation.riskmanagement.FileDescription)
	 */
	public FileDescription storeFileDescription(FileDescription fileDescription) throws FfmaCommonException {
		try {
			return (FileDescription) dataStoreDao.storeObject((FfmaDomainObject) fileDescription);
		} catch (Exception e) {
			throw new FfmaCommonException("Error file description store. " + e);
		}
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao#retrieveObjectsListByField(ait.ffma.domain.FfmaDomainObject, java.lang.String, java.lang.String, java.util.List)
	 */
	public List<FfmaDomainObject> retrieveObjectsListByField(
			FfmaDomainObject object, String fieldName, String queryStr,
			List<String> values) throws FfmaCommonException {
		try {		
			return dataStoreDao.retrieveObjectsListByField(
					(FfmaDomainObject) object, fieldName, MongoDbConstants.IN_QUERY, values);
		} catch (Exception e) {
			throw new FfmaCommonException("Error metadata analysis report retrieve. " + e);
		}
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao#retrieveObjectsListByFieldExt(ait.ffma.domain.FfmaDomainObject, java.lang.String, java.lang.String, java.util.List, java.util.Map)
	 */
	public List<FfmaDomainObject> retrieveObjectsListByFieldExt(
			FfmaDomainObject object, String fieldName, String queryStr,
			List<String> values, Map<String, String> addFieldsMap) throws FfmaCommonException {
		try {		
			return dataStoreDao.retrieveObjectsListByFieldExt(
					(FfmaDomainObject) object, fieldName, MongoDbConstants.IN_QUERY, values, addFieldsMap);
		} catch (Exception e) {
			throw new FfmaCommonException("Error metadata analysis report retrieve. " + e);
		}
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao#updateObject(ait.ffma.domain.FfmaDomainObject, ait.ffma.domain.FfmaDomainObject)
	 */
	public void updateObject(FfmaDomainObject queryObject, FfmaDomainObject storeObject) throws FfmaCommonException {
		try {
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			dataStoreDao.updateObject(queryObject, storeObject, exclusionsList);
		} catch (Exception e) {
			throw new FfmaCommonException("Store object: " + storeObject + ", error: " + e);
		}
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao#existsInDb(ait.ffma.domain.FfmaDomainObject)
	 */
	public boolean existsInDb(FfmaDomainObject queryObject) {
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		return dataStoreDao.existsInDb(queryObject, exclusionsList);
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao#cleanUpInDb(ait.ffma.domain.FfmaDomainObject)
	 */
	public void cleanUpInDb(FfmaDomainObject queryObject) {
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		dataStoreDao.cleanUpInDb(queryObject, exclusionsList);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao#retrieveCollection(ait.ffma.domain.FfmaDomainObject, java.util.List)
	 */
	public List<? extends FfmaDomainObject> retrieveCollection (
			FfmaDomainObject queryObject, List<String> exclusionsList) {
		return dataStoreDao.retrieveCollection(queryObject, exclusionsList);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao#removeCollectionByName(java.lang.String)
	 */
	public void removeCollectionByName(String collectionName)
			throws CollectionNotRemovedException {
        dataStoreDao.removeCollectionByName(collectionName);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao#storeObjectsList(java.util.List)
	 */
	public boolean storeObjectsList(List<FfmaDomainObject> objectsList)
			throws ObjectNotStoredException, ObjectNotFoundException {
		return dataStoreDao.storeObjectsList(objectsList);
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao#storeObject(ait.ffma.domain.FfmaDomainObject)
	 */
	public FfmaDomainObject storeObject(FfmaDomainObject object)
			throws ObjectNotStoredException, ObjectNotFoundException {
		return dataStoreDao.storeObject(object);
	}
	
	public FormatRiskAnalysisReport getFormatRiskAnalysisReport(String id) throws FfmaCommonException {
		try {
			FormatRiskAnalysisReport formatRiskAnalysisReport = new FormatRiskAnalysisReport(true);
			formatRiskAnalysisReport.setExtension(id);
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			
			FormatRiskAnalysisReport res = null;
			List<? extends FfmaDomainObject> checkResultObjectList = dataStoreDao.retrieveCollection(formatRiskAnalysisReport, exclusionsList);
			if (checkResultObjectList != null && checkResultObjectList.size() > 0) {
				res = (FormatRiskAnalysisReport) checkResultObjectList.get(0);
			}
			return res;
		} catch (Exception e) {
			throw new FfmaCommonException("Error format risk analysis report retrieve. " + e);
		}
	}

	public FormatRiskAnalysisReport storeFormatRiskAnalysisReport(FormatRiskAnalysisReport formatRiskAnalysisReport) throws FfmaCommonException {
		try {
			FormatRiskAnalysisReport formatRiskAnalysisReportQuery = new FormatRiskAnalysisReport(true);
			formatRiskAnalysisReportQuery.setExtension(formatRiskAnalysisReport.getExtension());
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			dataStoreDao.updateObject(formatRiskAnalysisReportQuery, formatRiskAnalysisReport, exclusionsList);
			return getFormatRiskAnalysisReport(formatRiskAnalysisReport.getExtension().toString());
		} catch (Exception e) {
			throw new FfmaCommonException("Error format risk analysis report store. " + e);
		}
	}

}
