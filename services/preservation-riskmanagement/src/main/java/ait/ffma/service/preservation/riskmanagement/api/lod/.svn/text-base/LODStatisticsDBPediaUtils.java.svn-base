package ait.ffma.service.preservation.riskmanagement.api.lod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import ait.ffma.domain.preservation.riskmanagement.LODFormat;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.domain.preservation.riskmanagement.LODVendor;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperty;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.DBPediaConnector;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ReportConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;

/**
 * This is a helper class for LOD statistics calculations
 */
public final class LODStatisticsDBPediaUtils {

	/**
	 * To prevent initialization
	 */
	private LODStatisticsDBPediaUtils() {} 

	/**
	 * Logger
	 */
	private static Logger log = Logger.getLogger(LODStatisticsDBPediaUtils.class.getName());

	/**
	 * This cache is a mapping between property id and DBPedia response. It is used to 
	 * improve performance.
	 */
	private static Map<String, List<Map<String, String>>> dbpediaCache = new HashMap<String, List<Map<String,String>>>();

	/**
	 * @return
	 */
	public static Map<String, List<Map<String, String>>> getDbpediaCache() {
		return dbpediaCache;
	}

	/**
	 * @param dbpediaCache
	 */
	public static void setDbpediaCache(
			Map<String, List<Map<String, String>>> dbpediaCache) {
		LODStatisticsDBPediaUtils.dbpediaCache = dbpediaCache;
	}

	/**
	 * This method searches in DBPedia for passed parameters.
	 * 
	 * @param property
	 *            The risk property
	 * @param queryValue
	 *            The query value as string
	 * @param searchColumn
	 *            The name of the search column that contains query value
	 * @param resultColumn
	 *            The name of the result column that contains result values
	 * @param filter
	 *            Filter response list if true
	 * @return list containing found values
	 */
	public static List<String> searchInDBPedia(String property, String queryValue,
			String searchColumn, String resultColumn, boolean filter) {
		List<String> resultList = new ArrayList<String>();
		DBPediaConnector dbpedia = new DBPediaConnector();
		String filterStr = null;

		LODProperty lodProperty = LODUtils.getLODPropertyById(property);
		String query = LODUtils.getQueryFromLODProperty(lodProperty.getLODSources().getLODSource(), LODConstants.DBPEDIA);
		dbpedia.setQuery(query); 

		if (filter) {
			filterStr = queryValue;
		}
		
		List<Map<String, String>> dbpediaList = null;
		List<Map<String, String>> dbpediaListAll = null;
		if (dbpediaCache.containsKey(property) && dbpediaCache.get(property).size() > 0) {
			dbpediaListAll = dbpediaCache.get(property);
		} else {
			dbpediaListAll = dbpedia.retrieveLODAll(null);
			dbpediaCache.put(property, dbpediaListAll);
		}
		if (dbpediaListAll != null) {
			dbpediaList = filterResponse(dbpediaListAll, filterStr);
		}

		retrieveDBPediaValues(queryValue, searchColumn, resultColumn,
				resultList, dbpediaList);
		return resultList;
	}

	/**
	 * This method iterates through the repository response for all 
	 * stored objects filtering by search value.
	 * @param searchValue
	 * @return objects that match search value
	 */
	public static List<Map<String, String>> filterResponse(List<Map<String, String>> response, String searchValue) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 

		if (response != null && response.size() > 0) {
			for (Map<String, String> sparqlResult : response) {
				parseSparqlResponse(searchValue, list, sparqlResult);
			}
		}
		return list;
	}

	/**
	 * This method filters Sparql response according to the search value.
	 * @param searchValue
	 * @param list
	 * @param sparqlResult
	 */
	public static void parseSparqlResponse(String searchValue,
			List<Map<String, String>> list, Map<String, String> sparqlResult) {
		if (searchValue != null) {
			Set<Map.Entry<String, String>> sparqlSet = sparqlResult.entrySet();
			for (Map.Entry<String, String> entry : sparqlSet) {
//				String key = entry.getKey();
				String value = entry.getValue();
//					log.info("key=" + key + " , value=" + value);
				if (value.equals(searchValue)) {
					log.info("added response value: " + value);
					list.add(sparqlResult);
				}
			}
		} else {
			list.add(sparqlResult);
		}
	}

	
	/**
	 * @param queryValue
	 * @param searchColumn
	 * @param resultColumn
	 * @param resultList
	 * @param dbpediaList
	 */
	public static void retrieveDBPediaValues(String queryValue,
			String searchColumn, String resultColumn, List<String> resultList,
			List<Map<String, String>> dbpediaList) {
		if (dbpediaList != null && dbpediaList.size() > 0) {
			Iterator<Map<String, String>> iter = dbpediaList.iterator();
			while (iter.hasNext()) {
				Map<String, String> dbpediaMap = iter.next();
				getDBPediaValuesList(queryValue, searchColumn, resultColumn,
						resultList, dbpediaMap);
			}
		}
	}

	/**
	 * This method retrieves DBPedia values list using given search- and result column
	 * names and query value.
	 * @param queryValue
	 * @param searchColumn
	 * @param resultColumn
	 * @param resultList
	 * @param dbpediaMap
	 */
	public static void getDBPediaValuesList(String queryValue,
			String searchColumn, String resultColumn, List<String> resultList,
			Map<String, String> dbpediaMap) {
		Set<Map.Entry<String, String>> dbpediaSet = dbpediaMap.entrySet();
		for (Map.Entry<String, String> entry : dbpediaSet) {
			String key = entry.getKey();
			String value = entry.getValue();
//				log.info("DBPedia response: key=" + key + " , value=" + value);
			if (key.equals(searchColumn) && value.contains(queryValue)) {
				String resultColumnValue = dbpediaMap.get(resultColumn);
				if (!resultList.contains(resultColumnValue)) {
					resultList.add(resultColumnValue);
				}
			}
		}
	}

	/**
	 * @param resList
	 * @return
	 */
	public static String getStringFromList(List<String> resList) {
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		Iterator<String> resultIter = resList.iterator();
//		log.info("Format name list: " + resList.size());
		while (resultIter.hasNext()) {
			String resStr = resultIter.next();
//			log.info("Result found: " + resStr);
			buf.append(resStr);
			buf.append(ReportConstants.COMMASPACE);
		}
		return buf.toString();
	}
	
	/**
	 * @param currentFileFormat
	 * @param softwareCsv
	 * @param softwareCount
	 * @param vendorsCsv
	 * @param nameStr
	 * @param mimeTypeStr
	 * @param versionStr
	 * @param releasedStr
	 * @param standardStr
	 * @return
	 */
	public static LODFormat evaluateDBPediaFormat(String currentFileFormat,
			String softwareCsv, int softwareCount, String vendorsCsv,
			String nameStr, String mimeTypeStr, String versionStr,
			String releasedStr, String standardStr) {
		LODFormat lodFormat = new LODFormat();
		getDBPediaFormatName(nameStr, lodFormat);
		getDBPediaFormatVersion(versionStr, lodFormat);
		getDBPediaFormatSoftwareCount(softwareCsv, softwareCount, lodFormat);
		getDBPediaFormatMimeType(mimeTypeStr, lodFormat);
		getDBPediaFormatCurrentFileFormat(currentFileFormat, lodFormat);
		getDBPediaFormatReleased(releasedStr, lodFormat);
		getDBPediaFormatVendors(vendorsCsv, lodFormat);
		getDBPediaFormatStandards(standardStr, lodFormat);
		return lodFormat;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	public static List<String> getDBPediaDescriptionsList(
			String currentFileFormat) {
		List<String> descriptionList = searchInDBPedia(LODConstants.LOD_SOFTWARE_GENRE_PROPERTY_ID,
				currentFileFormat, RiskConstants.ABSTRACT,
				RiskConstants.ABSTRACT, false);
		return descriptionList;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	public static List<String> getDBPediaSoftwareIds(String currentFileFormat) {
		List<String> softwareIdList = searchInDBPedia(LODConstants.LOD_SOFTWARE_GENRE_PROPERTY_ID,
				currentFileFormat, RiskConstants.ABSTRACT,
				RiskConstants.DBPEDIA_ID, false);
		return softwareIdList;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	public static List<String> getDBPediaSoftwareList(String currentFileFormat) {
		List<String> softwareList = searchInDBPedia(LODConstants.LOD_SOFTWARE_GENRE_PROPERTY_ID,
				currentFileFormat, RiskConstants.ABSTRACT,
				RiskConstants.LABEL, false);
		return softwareList;
	}

	/**
	 * @param descriptionStr
	 * @return
	 */
	public static List<String> getDBPediaFormatDescriptions(
			String descriptionStr) {
		List<String> descriptions = new ArrayList<String>();
		if (descriptionStr != null && descriptionStr.length() > 0 ) {
			descriptions.add(descriptionStr);
		}
		return descriptions;
	}

	/**
	 * @param standardStr
	 * @param lodFormat
	 */
	public static void getDBPediaFormatStandards(String standardStr,
			LODFormat lodFormat) {
		if (standardStr != null && standardStr.length() > 0) {
			lodFormat.setStandards(standardStr);
		}
	}

	/**
	 * @param vendorsCsv
	 * @param lodFormat
	 */
	public static void getDBPediaFormatVendors(String vendorsCsv,
			LODFormat lodFormat) {
		if (vendorsCsv != null && vendorsCsv.length() > 0) {
			lodFormat.setVendors(vendorsCsv);
		}
	}

	/**
	 * @param releasedStr
	 * @param lodFormat
	 */
	public static void getDBPediaFormatReleased(String releasedStr,
			LODFormat lodFormat) {
		if (releasedStr != null && releasedStr.length() > 0) {
			lodFormat.setCurrentVersionReleaseDate(releasedStr);
		}
	}

	/**
	 * @param currentFileFormat
	 * @param lodFormat
	 */
	public static void getDBPediaFormatCurrentFileFormat(
			String currentFileFormat, LODFormat lodFormat) {
		if (currentFileFormat != null && currentFileFormat.length() > 0) {
			lodFormat.setFileExtensions(currentFileFormat);
		}
	}

	/**
	 * @param mimeTypeStr
	 * @param lodFormat
	 */
	public static void getDBPediaFormatMimeType(String mimeTypeStr,
			LODFormat lodFormat) {
		if (mimeTypeStr != null && mimeTypeStr.length() > 0) {
			lodFormat.setMimeType(mimeTypeStr);
		}
	}

	/**
	 * @param softwareCsv
	 * @param softwareCount
	 * @param lodFormat
	 */
	public static void getDBPediaFormatSoftwareCount(String softwareCsv,
			int softwareCount, LODFormat lodFormat) {
		lodFormat.setSoftwareCount(softwareCount);
		if (softwareCsv != null && softwareCsv.length() > 0) {
			lodFormat.setSoftware(softwareCsv);
		}
	}

	/**
	 * @param versionStr
	 * @param lodFormat
	 */
	public static void getDBPediaFormatVersion(String versionStr,
			LODFormat lodFormat) {
		if (versionStr != null && versionStr.length() > 0) {
			lodFormat.setCurrentFormatVersion(versionStr);
		}
	}

	/**
	 * @param nameStr
	 * @param lodFormat
	 */
	public static void getDBPediaFormatName(String nameStr, LODFormat lodFormat) {
		if (nameStr != null && nameStr.length() > 0) {
			lodFormat.setFormatName(nameStr);
		}
	}

	/**
	 * @param softwareNames
	 * @param softwareIDs
	 * @param currentSoftwareName
	 * @param softwareIdStr
	 */
	public static void getDBPediaSoftwareNamesAndIds(
			List<String> softwareNames, List<String> softwareIDs,
			String currentSoftwareName, String softwareIdStr) {
		if (currentSoftwareName != null && currentSoftwareName.length() > 0) {
			softwareNames.add(currentSoftwareName);
		}
		if (softwareIdStr != null && softwareIdStr.length() > 0) {
			softwareIDs.add(softwareIdStr);
		}
	}

	/**
	 * @param currentFileFormat
	 * @param lodSoftware
	 */
	public static void getDBPediaLodSoftwareFileForamtList(
			String currentFileFormat, LODSoftware lodSoftware) {
		List<String> fileFormatList = new ArrayList<String>();
		fileFormatList.add(currentFileFormat);
		if (fileFormatList.size() > 0) {
			lodSoftware.setFileFormat(fileFormatList.toArray(new String[fileFormatList.size()]));
		}
	}

	/**
	 * @param swDescriptionStr
	 * @param lodSoftware
	 */
	public static void getDBPediaLodSoftwareDescriptions(
			String swDescriptionStr, LODSoftware lodSoftware) {
		if (swDescriptionStr != null && swDescriptionStr.length() > 0) {
			lodSoftware.setDescription(swDescriptionStr);
		}
	}

	/**
	 * @param softwareIdStr
	 * @param lodSoftware
	 */
	public static void getDBPediaLodSoftwareSoftwareIds(String softwareIdStr,
			LODSoftware lodSoftware) {
		if (softwareIdStr != null && softwareIdStr.length() > 0) {
			lodSoftware.setRepositoryId(softwareIdStr);
		}
	}

	/**
	 * @param homepage
	 * @param lodSoftware
	 */
	public static void getDBPediaLodSoftwareHomepage(String homepage,
			LODSoftware lodSoftware) {
		if (homepage != null && homepage.length() > 0) {
			lodSoftware.setSoftwareHomepage(homepage);
		}
	}

	/**
	 * @param license
	 * @param lodSoftware
	 */
	public static void getDBPediaLodSoftwareLicense(String license,
			LODSoftware lodSoftware) {
		if (license != null && license.length() > 0) {
			lodSoftware.setSoftwareLicense(license);
		}
	}

	/**
	 * @param programmingLanguage
	 * @param lodSoftware
	 */
	public static void getDBPediaLodSoftwareProgrammingLanguage(
			String programmingLanguage, LODSoftware lodSoftware) {
		if (programmingLanguage != null && programmingLanguage.length() > 0) {
			lodSoftware.setProgrammingLanguage(programmingLanguage);
		}
	}

	/**
	 * @param os
	 * @param lodSoftware
	 */
	public static void getDBPediaLodSoftwareOs(String os,
			LODSoftware lodSoftware) {
		if (os != null && os.length() > 0) {
			lodSoftware.setOperatingSystem(os);
		}
	}

	/**
	 * @param version
	 * @param lodSoftware
	 */
	public static void getDBPediaLodSoftwareVersion(String version,
			LODSoftware lodSoftware) {
		if (version != null && version.length() > 0) {
			lodSoftware.setSoftwareLatestVersion(version);
		}
	}

	/**
	 * @param currentSoftwareName
	 * @param lodSoftware
	 */
	public static void getDBPediaLodSoftwareName(String currentSoftwareName,
			LODSoftware lodSoftware) {
		if (currentSoftwareName != null && currentSoftwareName.length() > 0) {
			lodSoftware.setSoftwareName(currentSoftwareName);
		}
	}

	/**
	 * @param currentSoftwareName
	 * @return
	 */
	public static String getDBPediaSoftwareHomepage(
			String currentSoftwareName) {
		String homepage = getStringFromList(searchInDBPedia(LODConstants.LOD_SOFTWARE_LICENSE_PROPERTY_ID,
				currentSoftwareName, RiskConstants.LABEL, RiskConstants.HOMEPAGE,
				true));
		return homepage;
	}

	/**
	 * @param currentSoftwareName
	 * @return
	 */
	public static String getDBPediaSoftwareLicense(
			String currentSoftwareName) {
		String license = getStringFromList(searchInDBPedia(LODConstants.LOD_SOFTWARE_LICENSE_PROPERTY_ID,
				currentSoftwareName, RiskConstants.LABEL, RiskConstants.LICENSE,
				true));
		return license;
	}

	/**
	 * @param currentSoftwareName
	 * @return
	 */
	public static String getDBPediaSoftwareVersion(
			String currentSoftwareName) {
		String version = getStringFromList(searchInDBPedia(LODConstants.LOD_SOFTWARE_LATEST_VERSION_PROPERTY_ID,
				currentSoftwareName, RiskConstants.LABEL, RiskConstants.LATEST_RELEASE_VERSION,
				true));
		return version;
	}

	/**
	 * @param currentSoftwareName
	 * @return
	 */
	public static String getDBPediaSoftwareProgrammingLanguage(
			String currentSoftwareName) {
		String programmingLanguage = getStringFromList(searchInDBPedia(LODConstants.LOD_SOFTWARE_PROGRAMMING_LANGUAGE_PROPERTY_ID,
				currentSoftwareName, RiskConstants.LABEL, RiskConstants.PROGRAMMING_LANGUAGE,
				true));
		return programmingLanguage;
	}

	/**
	 * @param currentSoftwareName
	 * @return
	 */
	public static String getDBPediaSoftwareOs(String currentSoftwareName) {
		String os = getStringFromList(searchInDBPedia(LODConstants.LOD_SOFTWARE_COMPATIBLE_OS_PROPERTY_ID,
				currentSoftwareName, RiskConstants.LABEL, RiskConstants.PLATFORM,
				true));
		return os;
	}

	/**
	 * @param currentFileFormat
	 * @param lodVendor
	 */
	public static void setDBPediaLodVendorFileFormatList(
			String currentFileFormat, LODVendor lodVendor) {
		if (currentFileFormat != null && currentFileFormat.length() > 0 && !currentFileFormat.equals(ReportConstants.NULL)) {
			List<String> fileFormatList = new ArrayList<String>();
			fileFormatList.add(currentFileFormat);
			if (fileFormatList.size() > 0) {
				lodVendor.setFileFormat(fileFormatList.toArray(new String[fileFormatList.size()]));
			} 
		}
	}

	/**
	 * @param vendorDescriptionStr
	 * @param lodVendor
	 */
	public static void getDBPediaLodVendorDescriptions(
			String vendorDescriptionStr, LODVendor lodVendor) {
		if (vendorDescriptionStr != null && vendorDescriptionStr.length() > 0) {
			lodVendor.setDescription(vendorDescriptionStr);
		}
	}

	/**
	 * @param currentVendorsName
	 * @param lodVendor
	 */
	public static void getDBPediaLodVendorNames(String currentVendorsName,
			LODVendor lodVendor) {
		if (currentVendorsName != null && currentVendorsName.length() > 0) {
			lodVendor.setRepositoryId(currentVendorsName);
		}
	}

	/**
	 * @param revenueStr
	 * @param lodVendor
	 */
	public static void getDBPediaLodVendorRevenue(String revenueStr,
			LODVendor lodVendor) {
		if (revenueStr != null && revenueStr.length() > 0) {
			lodVendor.setBusinessStatus(revenueStr);
		}
	}

	/**
	 * @param homepageStr
	 * @param locationStr
	 * @param lodVendor
	 */
	public static void getDBPediaLodVendorCountry(String locationStr, LODVendor lodVendor) {
		if (locationStr != null && locationStr.length() > 0) {
			lodVendor.setCountry(locationStr);
		}
	}

	/**
	 * @param homepageStr
	 * @param lodVendor
	 */
	public static void getDBPediaLodVendorHomepage(String homepageStr,
			LODVendor lodVendor) {
		if (homepageStr != null && homepageStr.length() > 0) {
			lodVendor.setHomepage(homepageStr);
		}
	}

	/**
	 * @param currentVendorsName
	 * @return
	 */
	public static String getDBPediaVendorDescriptions(String currentVendorsName) {
		String vendorDescriptionStr = getStringFromList(searchInDBPedia(LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID,
				currentVendorsName, RiskConstants.SUBJECT, RiskConstants.DBPEDIA_DESCRIPTION,
				true));
		return vendorDescriptionStr;
	}

	/**
	 * @param currentVendorsName
	 * @return
	 */
	public static String getDBPediaVendorRevenue(String currentVendorsName) {
		String revenueStr = getStringFromList(searchInDBPedia(LODConstants.LOD_VENDOR_LOCATION_PROPERTY_ID,
				currentVendorsName, RiskConstants.SUBJECT, RiskConstants.REVENUE,
				true));
		return revenueStr;
	}

	/**
	 * @param currentVendorsName
	 * @return
	 */
	public static String getDBPediaVendorLocation(String currentVendorsName) {
		String locationStr = getStringFromList(searchInDBPedia(LODConstants.LOD_VENDOR_LOCATION_PROPERTY_ID,
				currentVendorsName, RiskConstants.SUBJECT, RiskConstants.LOCATION,
				true));
		return locationStr;
	}

	/**
	 * @param currentVendorsName
	 * @return
	 */
	public static String getDBPediaVendorHomepage(String currentVendorsName) {
		String homepageStr = getStringFromList(searchInDBPedia(LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID,
				currentVendorsName, RiskConstants.SUBJECT, RiskConstants.HOMEPAGE,
				true));
		return homepageStr;
	}

	/**
	 * @param currentVendorsName
	 * @return
	 */
	public static String getDBPediaVendorNumOfEmployees(
			String currentVendorsName) {
		String numOfEmployeesStr = getStringFromList(searchInDBPedia(LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID,
				currentVendorsName, RiskConstants.SUBJECT, RiskConstants.EMPLOYEES,
				true));
		return numOfEmployeesStr;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	public static String getDBPediaFormatRepositoryIds(String currentFileFormat) {
		String repositoryIdStr = getStringFromList(searchInDBPedia(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, ReportConstants.POINT
						+ currentFileFormat, RiskConstants.EXTENSION,
				RiskConstants.DBPEDIA_ID, false));
		return repositoryIdStr;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	public static String getDBPediaFormatDescription(String currentFileFormat) {
		String descriptionStr = getStringFromList(searchInDBPedia(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, ReportConstants.POINT
						+ currentFileFormat, RiskConstants.EXTENSION,
				RiskConstants.DBPEDIA_DESCRIPTION, false));
		return descriptionStr;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	public static String getDBPediaFormatStandard(String currentFileFormat) {
		String standardStr = getStringFromList(searchInDBPedia(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, ReportConstants.POINT
						+ currentFileFormat, RiskConstants.EXTENSION,
				RiskConstants.STANDARD, false));
		return standardStr;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	public static String getDBPediaFormatReleased(String currentFileFormat) {
		String releasedStr = getStringFromList(searchInDBPedia(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, ReportConstants.POINT
						+ currentFileFormat, RiskConstants.EXTENSION,
				RiskConstants.COLUMN_RELEASED, false));
		return releasedStr;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	public static String getDBPediaFormatVersion(String currentFileFormat) {
		String versionStr = getStringFromList(searchInDBPedia(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, ReportConstants.POINT
						+ currentFileFormat, RiskConstants.EXTENSION,
				RiskConstants.LATEST_RELEASE_VERSION, false));
		return versionStr;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	public static String getDBPediaFormatMimeType(String currentFileFormat) {
		String mimeTypeStr = getStringFromList(searchInDBPedia(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, ReportConstants.POINT
						+ currentFileFormat, RiskConstants.EXTENSION,
				RiskConstants.MIME, false));
		return mimeTypeStr;
	}

	/**
	 * @param currentFileFormat
	 * @return
	 */
	public static String getDBPediaFormatName(String currentFileFormat) {
		String nameStr = getStringFromList(searchInDBPedia(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, ReportConstants.POINT
						+ currentFileFormat, RiskConstants.EXTENSION,
				RiskConstants.COLUMN_NAME, false));
		return nameStr;
	}
	
	/**
	 * Help method
	 * @param currentFileFormat
	 * @param currentField
	 * @return
	 */
	public static String getStringFromPronomList(String currentFileFormat, String currentField) {
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		List<String> infoList = LODUtils.searchPronomInfoForFileFormat(currentFileFormat, currentField);
	
		if (infoList != null) {
			Iterator<String> resultIter = infoList.iterator();
			while (resultIter.hasNext()) {
				buf.append(resultIter.next());
				buf.append(ReportConstants.COMMASPACE);
			}
		}
	//	log.info("res list: " + buf.toString());
		return buf.toString();
	}

}
