package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.fail;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;

import ait.ffma.common.CommonApiServerConfigurationImpl;
import ait.ffma.common.dao.DataStoreDaoImpl;
import ait.ffma.common.dao.mongodb.MongoDbManager;
import ait.ffma.common.exception.ObjectNotFoundException;
import ait.ffma.common.exception.ObjectNotRemovedException;
import ait.ffma.common.exception.ObjectNotStoredException;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.LODFormat;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.domain.preservation.riskmanagement.LODVendor;
import ait.ffma.domain.preservation.riskmanagement.PronomFileFormat;
import ait.ffma.factory.FfmaAbstractFactoryImpl;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODUtils;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperty;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.DBPediaConnector;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;

/**
 * This class provides LOD (link open data) retrieval experiments for DBPEDIA LOD repository.
 */
public class TestLODDBPediaRetrieve {

	private String CSV_FILE = "fileFormatInformationDBPedia.csv";
	
	private List<String> fileFormatList = Arrays.asList("pdf", "doc",
			"tiff", "jp2", "txt", "xml", "html", "jpg", "psd", "png", "bmp", "gif", 
			"mp1", "mp2", "mp3", "mp4", "m4v", "m4a", "mpg", "mpeg", 
			"mpeg1", "mpeg2", "mpeg3", "mpeg4", "flv", "ply", "dae");
	
	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());
	
	/**
	 * This object provides connection to database for testing 
	 */
	private DataStoreDaoImpl dataStoreDao = null;

	/**
	 * This cache is a mapping between property id and DBPedia response. It is used to 
	 * improve performance.
	 */
	private Map<String, List<Map<String, String>>> dbpediaCache = new HashMap<String, List<Map<String,String>>>();

	/**
	 * @param resList
	 * @return
	 */
	private String getStringFromList(List<String> resList) {
		String res = "";
		Iterator<String> resultIter = resList.iterator();
//		log.info("Format name list: " + resList.size());
		while (resultIter.hasNext()) {
			String resStr = resultIter.next();
			log.info("Result found: " + resStr);
			res = res + resStr + ", ";
		}
		return res;
	}
	
	/**
	 * This method iterates through the repository response for all 
	 * stored objects filtering by search value.
	 * @param searchValue
	 * @return objects that match search value
	 */
	public List<Map<String, String>> filterResponse(List<Map<String, String>> response, String searchValue) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 

		for (Map<String, String> sparqlResult : response) {
			Map<String, String> currentEntry = new HashMap<String, String>();
			for (String variableName : sparqlResult.keySet()) {
//				log.info(" " + variableName + ": " + sparqlResult.get(variableName));
				currentEntry.put(variableName, sparqlResult.get(variableName));
			}
			if (searchValue != null) {
				Set<String> sparqlSet = currentEntry.keySet();
				for (String key : sparqlSet) {
//					log.info("key=" + key + " , value=" + currentEntry.get(key));
					if (currentEntry.get(key).equals(searchValue)) {
						log.info("added response value: " + currentEntry.get(key));
						list.add(currentEntry);
					}
				}
			} else {
				list.add(currentEntry);
			}
		}
		return list;
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
	private List<String> search(String property, String queryValue,
			String searchColumn, String resultColumn, boolean filter) {
		List<String> resultList = new ArrayList<String>();
		DBPediaConnector dbpedia = new DBPediaConnector();
//		RepositoryDescription dbpedia = new DBPediaConnector();
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

		Iterator<Map<String, String>> iter = dbpediaList.iterator();
		while (iter.hasNext()) {
			Map<String, String> dbpediaMap = iter.next();
			Set<String> dbpediaSet = dbpediaMap.keySet();
			for (String key : dbpediaSet) {
				String value = dbpediaMap.get(resultColumn);
//				log.info("DBPedia response: key=" + key + " , value=" + value);
				if (key.equals(searchColumn)
						&& dbpediaMap.get(key).contains(queryValue)) {
					if (!resultList.contains(value)) {
						resultList.add(dbpediaMap.get(resultColumn));
					}
				}
			}
		}
		return resultList;
	}

	/**
	 * This method initializes extensions list
	 */
	private void initFileFormatList() {
		PronomFileFormat pronomFileFormat = new PronomFileFormat();
		pronomFileFormat.setFfmaObjectName(PronomFileFormat.class.getSimpleName());
		
		try {
			CommonApiServerConfigurationImpl commonApiServerConfiguration = new CommonApiServerConfigurationImpl();
			commonApiServerConfiguration.init();
			MongoDbManager mongoDbManager = new MongoDbManager();
			mongoDbManager.setCommonApiServerConfiguration(commonApiServerConfiguration);
			FfmaAbstractFactoryImpl objectFactory = new FfmaAbstractFactoryImpl();
			mongoDbManager.setObjectFactory(objectFactory);
			mongoDbManager.init();
			DataStoreDaoImpl dataStoreDao = new DataStoreDaoImpl();
			dataStoreDao.setMongoDbManager(mongoDbManager);

			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			List<? extends FfmaDomainObject> fileFormats = dataStoreDao
				.retrieveCollection((FfmaDomainObject) pronomFileFormat, exclusionsList);

			fileFormatList = LODUtils.getFileFormatsList(fileFormats);
			log.info("extensionsList size: " + fileFormatList.size());
		} catch (Exception e) {
			fail("Caught ObjectNotRetrievedException error: " + e + e.getStackTrace());
		}
	}
		
	/**
	 * This method initializes database access for test purposes
	 */
	private void initDataStoreDao() {
		CommonApiServerConfigurationImpl commonApiServerConfiguration = new CommonApiServerConfigurationImpl();
		commonApiServerConfiguration.init();
		MongoDbManager mongoDbManager = new MongoDbManager();
		mongoDbManager.setCommonApiServerConfiguration(commonApiServerConfiguration);
		FfmaAbstractFactoryImpl objectFactory = new FfmaAbstractFactoryImpl();
		mongoDbManager.setObjectFactory(objectFactory);
		mongoDbManager.init();
		dataStoreDao = new DataStoreDaoImpl();
		dataStoreDao.setMongoDbManager(mongoDbManager);
	}
	
	//@Test
	public void searchFileFormatInformationCSV() throws MalformedURLException, URISyntaxException {
		log.info("This test evaluates file format information from linked open data repository DBPedia and stores results in CSV file.");
		try {
			initDataStoreDao();

			FileWriter writer = new FileWriter(CSV_FILE);
		    String cFileFormatDescriptionRows = "";
			String softwareDescriptionCsv = "";
			String vendorsDescriptionCsv = "";

			writer.append("FILE FORMAT DESCRIPTION\n");
			writer.append("FORMAT NAME;CURRENT VERSION RELEASE DATE;SOFTWARE COUNT;SOFTWARE;" +
					"CURRENT FORMAT VERSION;FORMAT LICENSE;LIMITATIONS;PUID;FORMAT HOMEPAGE;MIME TYPE;" +
					"FORMAT GENRE;FORMAT CREATOR;OPEN FORMAT;FILE EXTENSIONS;VENDORS;STANDARDS\n");

			String res = "";
			LODUtils.initCalculationModel();
			initFileFormatList();
			int counter = 0; // limit to test
			Iterator<String> fileFormatIter = fileFormatList.iterator();
			while (fileFormatIter.hasNext()) {
				String softwareCsv = "";
				int softwareCount = 0;
				String vendorsCsv = "";
				String currentFileFormat = fileFormatIter.next();
				String puidStr = "";
				
				String nameStr = getStringFromList(search(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, "."
								+ currentFileFormat, RiskConstants.EXTENSION,
						RiskConstants.COLUMN_NAME, false));
				String mimeTypeStr = getStringFromList(search(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, "."
								+ currentFileFormat, RiskConstants.EXTENSION,
						RiskConstants.MIME, false));
				String versionStr = getStringFromList(search(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, "."
								+ currentFileFormat, RiskConstants.EXTENSION,
						RiskConstants.LATEST_RELEASE_VERSION, false));
				String releasedStr = getStringFromList(search(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, "."
								+ currentFileFormat, RiskConstants.EXTENSION,
						RiskConstants.COLUMN_RELEASED, false));
				String standardStr = getStringFromList(search(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, "."
								+ currentFileFormat, RiskConstants.EXTENSION,
						RiskConstants.STANDARD, false));
				String descriptionStr = getStringFromList(search(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, "."
								+ currentFileFormat, RiskConstants.EXTENSION,
						RiskConstants.DBPEDIA_DESCRIPTION, false));
				String repositoryIdStr = getStringFromList(search(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, "."
								+ currentFileFormat, RiskConstants.EXTENSION,
						RiskConstants.DBPEDIA_ID, false));
				List<String> vendorsList = search(LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, "."
								+ currentFileFormat, RiskConstants.EXTENSION,
						RiskConstants.OWNER, false);
				if (vendorsList != null && vendorsList.size() > 0) {
					vendorsCsv = getStringFromList(vendorsList);
					Iterator<String> vendorsIter = vendorsList.iterator();
					while (vendorsIter.hasNext()) {
						String currentVendorsName = vendorsIter.next();
						String numOfEmployeesStr = getStringFromList(search(LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID,
								currentVendorsName, RiskConstants.COLUMN_NAME, RiskConstants.EMPLOYEES,
								true));
						String homepageStr = getStringFromList(search(LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID,
								currentVendorsName, RiskConstants.COLUMN_NAME, RiskConstants.HOMEPAGE,
								true));
						String locationStr = getStringFromList(search(LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID,
								currentVendorsName, RiskConstants.COLUMN_NAME, RiskConstants.LOCATION,
								true));
						String revenueStr = getStringFromList(search(LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID,
								currentVendorsName, RiskConstants.COLUMN_NAME, RiskConstants.REVENUE,
								true));
						String vendorDescriptionStr = getStringFromList(search(LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID,
								currentVendorsName, RiskConstants.COLUMN_NAME, RiskConstants.DBPEDIA_DESCRIPTION,
								true));
						vendorsDescriptionCsv = vendorsDescriptionCsv
								+ currentVendorsName + ";" + numOfEmployeesStr + ";" + ";" + ";"
								+ ";" + ";" + locationStr + " " + revenueStr + ";" + ";"
								+ homepageStr + ";" + "\n";
						LODVendor lodVendor = new LODVendor();
						lodVendor.setOrganisationName(currentVendorsName);
						try {
							if (numOfEmployeesStr != null && numOfEmployeesStr.length() > 0) {
								lodVendor.setNumberOfEmployees(Integer.parseInt(numOfEmployeesStr));
							}
						} catch (Exception e) {
							log.info("number of employees not numeral value error: " + e);
						}
						lodVendor.setHomepage(homepageStr);
						lodVendor.setCountry(locationStr);
						lodVendor.setBusinessStatus(revenueStr);
						lodVendor.setRepository(LODConstants.DBPEDIA);
						lodVendor.setRepositoryId(currentVendorsName);
						lodVendor.setDescription(vendorDescriptionStr);
						
						LODVendor checkLodVendor = new LODVendor();
						checkLodVendor.setRepository(LODConstants.DBPEDIA);
						checkLodVendor.setOrganisationName(currentVendorsName);
						if (lodVendor.getOrganisationName() != null
								&& lodVendor.getOrganisationName().length() > 0
								&& lodVendor.getRepositoryId() != null
								&& lodVendor.getRepositoryId().contains("http")) {
							updateObject(checkLodVendor, lodVendor);
						}
					}
				}
				List<String> softwareList = search(LODConstants.LOD_SOFTWARE_GENRE_PROPERTY_ID,
						currentFileFormat, RiskConstants.ABSTRACT,
						RiskConstants.LABEL, false);
				
				List<String> softwareIdList = search(LODConstants.LOD_SOFTWARE_GENRE_PROPERTY_ID,
						currentFileFormat, RiskConstants.ABSTRACT,
						RiskConstants.DBPEDIA_ID, false);
				
				List<String> descriptionList = search(LODConstants.LOD_SOFTWARE_GENRE_PROPERTY_ID,
						currentFileFormat, RiskConstants.ABSTRACT,
						RiskConstants.ABSTRACT, false);
				
				int swCounter = 0;
				if (softwareList != null && softwareList.size() > 0) {
					softwareCsv = getStringFromList(softwareList);
					softwareCount = softwareList.size();
					Iterator<String> softwareIter = softwareList.iterator();
					while (softwareIter.hasNext()) {
						String currentSoftwareName = softwareIter.next();
						String os = getStringFromList(search(LODConstants.LOD_SOFTWARE_COMPATIBLE_OS_PROPERTY_ID,
								currentSoftwareName, RiskConstants.LABEL, RiskConstants.PLATFORM,
								true));
						String programmingLanguage = getStringFromList(search(LODConstants.LOD_SOFTWARE_PROGRAMMING_LANGUAGE_PROPERTY_ID,
								currentSoftwareName, RiskConstants.LABEL, RiskConstants.PROGRAMMING_LANGUAGE,
								true));
						String version = getStringFromList(search(LODConstants.LOD_SOFTWARE_LATEST_VERSION_PROPERTY_ID,
								currentSoftwareName, RiskConstants.LABEL, RiskConstants.LATEST_RELEASE_VERSION,
								true));
						String license = getStringFromList(search(LODConstants.LOD_SOFTWARE_LICENSE_PROPERTY_ID,
								currentSoftwareName, RiskConstants.LABEL, RiskConstants.LICENSE,
								true));
						String homepage = getStringFromList(search(LODConstants.LOD_SOFTWARE_LICENSE_PROPERTY_ID,
								currentSoftwareName, RiskConstants.LABEL, RiskConstants.HOMEPAGE,
								true));
						String softwareIdStr = softwareIdList.get(swCounter);
						String swDescriptionStr = descriptionList.get(swCounter);
						softwareDescriptionCsv = softwareDescriptionCsv
								+ currentSoftwareName + ";" + license + ";" + homepage + ";" + ";"
								+ os + ";" + ";" + programmingLanguage + ";" + version + ";"
								+ ";" + "\n";
						LODSoftware lodSoftware = new LODSoftware();
						lodSoftware.setSoftwareName(currentSoftwareName);
						lodSoftware.setSoftwareLatestVersion(version);
						lodSoftware.setOperatingSystem(os);
						lodSoftware.setProgrammingLanguage(programmingLanguage);
						lodSoftware.setSoftwareLicense(license);
						lodSoftware.setSoftwareHomepage(homepage);
						lodSoftware.setRepository(LODConstants.DBPEDIA);
						lodSoftware.setRepositoryId(softwareIdStr);
						lodSoftware.setDescription(swDescriptionStr);
						
						LODSoftware checkLodSoftware = new LODSoftware();
						checkLodSoftware.setRepository(LODConstants.DBPEDIA);
						checkLodSoftware.setRepositoryId(softwareIdStr);
						updateObject(checkLodSoftware, lodSoftware);
						swCounter++;
					}
				}

				cFileFormatDescriptionRows = cFileFormatDescriptionRows + 
					nameStr + ";" + releasedStr + ";" + softwareCount + ";" + 
					softwareCsv + ";" + versionStr + ";" + ";" + ";" + puidStr + ";" + ";" + mimeTypeStr + ";" +
					"" + ";" + "" + ";" + ";" + currentFileFormat + ";" + vendorsCsv + ";" + standardStr + ";" + "\n";
				LODFormat lodFormat = new LODFormat();
				lodFormat.setFormatName(nameStr);
				lodFormat.setCurrentFormatVersion(versionStr);
				lodFormat.setSoftwareCount(softwareCount);
				lodFormat.setSoftware(softwareCsv);
				lodFormat.setMimeType(mimeTypeStr);
				lodFormat.setFileExtensions(currentFileFormat);
				lodFormat.setRepository(LODConstants.DBPEDIA);
				lodFormat.setPuid(puidStr);
				lodFormat.setCurrentVersionReleaseDate(releasedStr);
				lodFormat.setVendors(vendorsCsv);
				lodFormat.setStandards(standardStr);
				List<String> descriptions = new ArrayList<String>();
				if (descriptionStr != null && descriptionStr.length() > 0 ) {
					descriptions.add(descriptionStr);
				}
				List<String> repositoryIdList = new ArrayList<String>();
				if (repositoryIdStr != null && repositoryIdStr.length() > 0 ) {
					repositoryIdList.add(repositoryIdStr);
				}
				lodFormat.setRepositoryId(repositoryIdList.toArray(new String[]{}));
				lodFormat.setDescription(descriptions.toArray(new String[]{}));
				
				LODFormat checkLodFormat = new LODFormat();
				checkLodFormat.setRepository(LODConstants.DBPEDIA);
				checkLodFormat.setFormatName(nameStr);
				updateObject(checkLodFormat, lodFormat);
				log.info("*** CURRENT COUNT *** " + counter);
				counter++;
//				if (counter > 5) break; // limit to test
			}
		    writer.append(cFileFormatDescriptionRows);

			writer.append("\nSOFTWARE DESCRIPTION\n");
			writer.append("SOFTWARE NAME;SOFTWARE LICENSE;SOFTWARE HOMEPAGE;GENRE;" +
					"OPERATING SYSTEM;PROTOCOLS;PROGRAMMING LANGUAGE;SOFTWARE LATEST VERSION;" +
					"SOFTWARE RELEASE DATE\n");
		    writer.append(softwareDescriptionCsv);

			writer.append("\nVENDOR DESCRIPTION\n");
			writer.append("ORGANISATION NAME;NUMBER OF EMPLOYEES;BUSINESS STATUS;" +
					"CURRENT Ffma;STOCK ISSUES;RANKED LIST;COUNTRY;FOUNDATION DATE;" +
					"HOMEPAGE\n");
			writer.append(vendorsDescriptionCsv);

			log.info("************ " + res);
			
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	}
	
	//@Test
	public void removeRepositoryData() throws MalformedURLException, URISyntaxException {
		log.info("This test removes file format information for linked open data repository DBPedia from database.");
		try {
			initDataStoreDao();
			// clean up old version of LOD format, software and vendor collections
			LODFormat lodFormatTmp = new LODFormat();
			lodFormatTmp.setRepository(LODConstants.DBPEDIA);
			LODSoftware lodSoftwareTmp = new LODSoftware();
			lodSoftwareTmp.setRepository(LODConstants.DBPEDIA);
			LODVendor lodVendorTmp = new LODVendor();
			lodVendorTmp.setRepository(LODConstants.DBPEDIA);
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			try {
				dataStoreDao.removeObjectsSet(lodFormatTmp, exclusionsList);
				dataStoreDao.removeObjectsSet(lodSoftwareTmp, exclusionsList);
				dataStoreDao.removeObjectsSet(lodVendorTmp, exclusionsList);
			} catch (ObjectNotRemovedException e) {
				e.printStackTrace();
			}
		}
		catch(Exception e)
		{
		     e.printStackTrace();
		} 
	}
	
	//@Test
	public void removeVendors() throws MalformedURLException, URISyntaxException {
		log.info("This test removes vendors collection for linked open data repository DBPedia from database.");
		try {
			initDataStoreDao();
			LODVendor lodVendorTmp = new LODVendor();
			lodVendorTmp.setRepository(LODConstants.DBPEDIA);
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			try {
				dataStoreDao.removeObjectsSet(lodVendorTmp, exclusionsList);
			} catch (ObjectNotRemovedException e) {
				e.printStackTrace();
			}
		}
		catch(Exception e)
		{
		     e.printStackTrace();
		} 
	}
	
	/**
	 * This method checks if requested object already exists in database and updates database object.
	 * If object already exists is should be updated: removed and replaced by new version.
	 * Otherwise just insert object in database. 
	 * @param queryObject
	 *        The request object to find out if object already exists in database
	 * @param storeObject
	 *        The object to store
	 */
	public void updateObject(FfmaDomainObject queryObject, FfmaDomainObject storeObject) {
		try {
			// first remove old object if already exists
			if (existsInDb(queryObject)) {
				cleanUpInDb(queryObject);
			}
			dataStoreDao.storeObject(storeObject);
		} catch (ObjectNotStoredException e) {
			log.info("Store object: " + storeObject + ", error: " + e);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
//			log.info("Not found object: " + queryObject + ", error: " + e);
		}
	}

	/**
	 * This method checks if requested object already exists in database.
	 * @param queryObject
	 *        The request object to find out if object already exists in database
	 */
	public boolean existsInDb(FfmaDomainObject queryObject) {
		boolean exists = false;
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		List<? extends FfmaDomainObject> checkResultObjectList = dataStoreDao.retrieveCollection(queryObject, exclusionsList);
		if (checkResultObjectList != null && checkResultObjectList.size() > 0) {
			exists = true;
		}
		return exists;
	}
	
	/**
	 * This method removes objects that match query object from database.
	 * @param queryObject
	 *        The request object to find out if object already exists in database
	 */
	public void cleanUpInDb(FfmaDomainObject queryObject) {
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		try {
			dataStoreDao.removeObjectsSet(queryObject, exclusionsList);
		} catch (ObjectNotRemovedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void existsVendors() throws MalformedURLException, URISyntaxException {
		log.info("This test checks if vendors collection for linked open data repository DBPedia exists in database.");
		try {
			initDataStoreDao();
			LODVendor lodVendorTmp = new LODVendor();
			lodVendorTmp.setRepository(LODConstants.DBPEDIA);
			existsInDb(lodVendorTmp);
		}
		catch(Exception e)
		{
		     e.printStackTrace();
		} 
	}
	
}
