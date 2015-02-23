package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.junit.Test;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import ait.ffma.common.CommonApiServerConfigurationImpl;
import ait.ffma.common.dao.DataStoreDaoImpl;
import ait.ffma.common.dao.mongodb.MongoDbManager;
import ait.ffma.common.exception.ObjectNotFoundException;
import ait.ffma.common.exception.ObjectNotRemovedException;
import ait.ffma.common.exception.ObjectNotStoredException;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.FileFormatStatistics;
import ait.ffma.domain.preservation.riskmanagement.LODFormat;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.domain.preservation.riskmanagement.LODVendor;
import ait.ffma.domain.preservation.riskmanagement.PronomFileFormat;
import ait.ffma.factory.FfmaAbstractFactoryImpl;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODUtils;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODPronomSoftware;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;

/**
 * This class provides LOD (link open data) retrieval experiments for PRONOM LOD repository.
 */
public class TestLODPronomRetrieve {

	private String CSV_FILE = "fileFormatInformationPronom.csv";
	
	private List<String> fileFormatList = Arrays.asList("pdf", "doc",
			"tiff", "jp2", "txt", "xml", "html", "jpg", "psd", "png", "bmp", "gif", 
			"mp1", "mp2", "mp3", "mp4", "m4v", "m4a", "mpg", "mpeg", 
			"mpeg1", "mpeg2", "mpeg3", "mpeg4", "flv", "ply", "dae");

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());
	
	/**
	 * This list contains column names of search columns to support JSON request object creation
	 * using passed search values.
	 */
	List<String> searchColumns = new ArrayList<String>();

	/**
	 * This list contains column names of result columns to find searched value in resulting JSON object.
	 */
	List<String> resultColumns = new ArrayList<String>();
	
	/**
	 * This object provides connection to database for testing 
	 */
	private DataStoreDaoImpl dataStoreDao = null;

	
	@Test
	public void searchFileFormatInfo() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates information from PRONOM using SPARQL request for given file format");
		String searchFileFormat = "tif";
		String columnName = "name";
		LODUtils.initCalculationModel();
		
		List<String> infoList = LODUtils.searchPronomInfoForFileFormat(searchFileFormat, columnName);

		Iterator<String> resultIter = infoList.iterator();
		log.info("info list: " + infoList.size());
		while (resultIter.hasNext()) {
			String extensionsStr = resultIter.next();
			log.info("Column name: " + columnName + ", value found: " + extensionsStr);
		}

		assertNotNull("Retrieved list must not be null", infoList);
		assertTrue(infoList.size() >= 1);
		
		/**
		 * Retrieve PUIDs from PRONOM
		 */
		searchFileFormat = "tif";
		columnName = "puid";

		List<String> infoList2 = LODUtils.searchPronomInfoForFileFormat(searchFileFormat, columnName);

		Iterator<String> resultIter2 = infoList2.iterator();
		log.info("info list2: " + infoList2.size());
		while (resultIter2.hasNext()) {
			String extensionsStr = resultIter2.next();
			log.info("Column name: " + columnName + ", value found: " + extensionsStr);
		}

		assertNotNull("Retrieved list must not be null", infoList2);
		assertTrue(infoList2.size() >= 1);
	}
	
	@Test
	public void searchSoftwareFromExtension() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates compatible software for given extension using Jena and ARQ");
		LODUtils.initCalculationModel();
		String searchExtension = "tif";
		log.info("Extension: " + searchExtension);
		
		// Open the bloggers RDF graph from the filesystem
//		InputStream in = new FileInputStream(new File("bloggers.rdf"));

		// Create an empty in-memory model and populate it from the graph
//		Model model = ModelFactory.createMemModelMaker().createModel();
		Model model = ModelFactory.createMemModelMaker().createDefaultModel();
//		model.read(in,null); // null base URI, since model URIs are absolute
//		in.close();

		// Create a new query
		String queryString = 
//			"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
//			"SELECT ?url " +
//			"WHERE {" +
//			"      ?contributor foaf:name \"Jon Foobar\" . " +
//			"      ?contributor foaf:weblog ?url . " +
//			"      }";
		
			"select distinct ?name ?ext ?puid ?xpuid where {" +			 
			"?s <http://www.w3.org/2000/01/rdfschema#label> ?name ." +
			"?s <http://reference.data.gov.uk/technicalregistry/extension> ?ext ." +
			"OPTIONAL { ?s <http://reference.data.gov.uk/technicalregistry/PUID> ?puid . }" +
			"OPTIONAL { ?s <http://reference.data.gov.uk/technicalregistry/XPUID> ?xpuid . }" +
			"}";

		Query query = QueryFactory.create(queryString);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		// Output query results	
//		ResultSet.out(System.out, results, query);
		log.info(results.toString());

		// Important - free up resources used running the query
		qe.close();

//		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}

	@Test
	public void readPronomXmlSoftware() throws MalformedURLException, URISyntaxException {
		log.info("test retrieves software information from PRONOM in XML form");
		LODUtils.initCalculationModel();
		
		List<LODPronomSoftware> softwareList = LODUtils.retrievePronomSoftwareInfo();

		Iterator<LODPronomSoftware> resultIter = softwareList.iterator();
		log.info("software list size: " + softwareList.size());
		while (resultIter.hasNext()) {
			LODPronomSoftware software = resultIter.next();
			log.info("Software name: " + software.getName() + ", number: " + software.getNumber() + 
					", version: " + software.getVersion() + ", identifier: " + software.getIdentifier() +
					", type: " + software.getType() + ", mediaFormat: " + software.getMediaFormat() + 
					", releaseDate: " + software.getReleaseDate() + ", language: " + software.getLanguage());
			if (software.getVendorsList() != null && software.getVendorsList().size() > 0) {
				log.info("Vendors: " + software.getVendorsList()); 
			}
			if (software.getFileFormatMap() != null && software.getFileFormatMap().size() > 0) {
				log.info("Software file formats: " + software.getFileFormatMap()); 
			}
		}

		assertNotNull("Retrieved list must not be null", softwareList);
		assertTrue(softwareList.size() >= 1);
	}
	
	/**
	 * Help method
	 * @param currentFileFormat
	 * @param currentField
	 * @return
	 */
	private String getStringFromPronomList(String currentFileFormat, String currentField) {
		String nameStr = "";
		List<String> infoList = LODUtils.searchPronomInfoForFileFormat(currentFileFormat, currentField);
	
		if (infoList != null) {
			Iterator<String> resultIter = infoList.iterator();
			while (resultIter.hasNext()) {
				nameStr = nameStr + resultIter.next() + ", ";
			}
		}
	//	log.info("res list: " + nameStr);
		return nameStr;
	}

	/**
	 * Help method to read particular fields from Pronom with exclusions option.
	 * @param currentFileFormat
	 * @param currentField
	 * @param exclusions
	 *        Fields that should not be included in resulting list
	 * @return values list
	 */
	private String getStringFromPronomListExt(String currentFileFormat, String currentField, List<String> exclusions) {
		String nameStr = "";
		List<String> infoList = LODUtils.searchPronomInfoForFileFormat(currentFileFormat, currentField);
	
		if (infoList != null) {
			Iterator<String> resultIter = infoList.iterator();
			while (resultIter.hasNext()) {
				String value = resultIter.next();
				if (!exclusions.contains(value)) {
					nameStr = nameStr + value + ", ";
				}
			}
		}
	//	log.info("res list: " + nameStr);
		return nameStr;
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
		log.info("This test evaluates file format information from linked open data repository PRONOM and stores results in CSV file.");
		try {
			initDataStoreDao();
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			
		    FileWriter writer = new FileWriter(CSV_FILE);
		    String cFileFormatDescriptionRows = "";
			String softwareDescriptionCsv = "";
			String vendorDescriptionCsv = "";

			LODUtils.initCalculationModel();
			List<LODPronomSoftware> softwareList = LODUtils.retrievePronomSoftwareInfo();
			Iterator<LODPronomSoftware> softwareListIter = softwareList.iterator();
			int swCounter = 0;
			while (softwareListIter.hasNext()) {
				LODPronomSoftware software = softwareListIter.next();
				softwareDescriptionCsv = softwareDescriptionCsv
						+ software.getName() + ";" + ";" + ";" + ";" + ";"
						+ ";" + ";" + software.getVersion() + ";"
						+ software.getReleaseDate() + ";" + "\n";
				LODSoftware lodSoftware = new LODSoftware();
				lodSoftware.setSoftwareName(software.getName());
				lodSoftware.setSoftwareLatestVersion(software.getVersion());
				lodSoftware.setSoftwareReleaseDate(software.getReleaseDate());
				lodSoftware.setDescription(software.getDescription());
				lodSoftware.setRepositoryId(software.getIdentifier());
				lodSoftware.setRepository(LODConstants.PRONOM);
				
				LODSoftware checkLodSoftware = new LODSoftware();
				checkLodSoftware.setRepository(LODConstants.PRONOM);
				checkLodSoftware.setRepositoryId(software.getIdentifier());
				updateObject(checkLodSoftware, lodSoftware);

				log.info("*** CURRENT SOFTWARE COUNT *** " + swCounter);
				swCounter++;
			}

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
				String vendorCsv = "";
				int softwareCount = 0;
				String currentFileFormat = fileFormatIter.next();

				String nameStr = getStringFromPronomList(currentFileFormat, LODConstants.PRONOM_NAME);
//				String extStr = getStringFromPronomList(currentFileFormat, LODConstants.PRONOM_EXT);
				String verStr = getStringFromPronomList(currentFileFormat, LODConstants.PRONOM_VER);	
				List<String> exclusions = new ArrayList<String>();
				exclusions.add(LODConstants.EMPTY_PUID);
				String puidStr = getStringFromPronomListExt(currentFileFormat, LODConstants.PRONOM_PUID, exclusions);

				Iterator<LODPronomSoftware> resultIter = softwareList.iterator();
				log.info("software list size: " + softwareList.size());
				while (resultIter.hasNext()) {
					LODPronomSoftware software = resultIter.next();
					log.info("Software name: " + software.getName() + ", number: " + software.getNumber() + 
							", version: " + software.getVersion() + ", identifier: " + software.getIdentifier() +
							", type: " + software.getType() + ", mediaFormat: " + software.getMediaFormat() + 
							", releaseDate: " + software.getReleaseDate() + ", language: " + software.getLanguage());
					if (software.getVendorsList() != null && software.getVendorsList().size() > 0) {
						log.info("Vendors: " + software.getVendorsList()); 
						Iterator<String> vendorIter = software.getVendorsList().iterator();
						int index = 0;
						while (vendorIter.hasNext()) {
							String currentVendor = vendorIter.next();
							if (!vendorDescriptionCsv.contains(currentVendor)) {
								vendorDescriptionCsv = vendorDescriptionCsv + 
									currentVendor + ";" + ";" + ";" + ";" + ";" + ";" + ";" + ";" + ";" + "\n";
								vendorCsv = vendorCsv + currentVendor + ", ";
								LODVendor lodVendor = new LODVendor();
								lodVendor.setOrganisationName(currentVendor);
								lodVendor.setRepositoryId(software.getVendorsIdList().get(index));
								lodVendor.setRepository(LODConstants.PRONOM);
								
								LODVendor checkLodVendor = new LODVendor();
								checkLodVendor.setRepository(LODConstants.PRONOM);
								checkLodVendor.setOrganisationName(currentVendor);
								updateObject(checkLodVendor, lodVendor);
								index++;
							}
						}
					}
					if (software.getFileFormatMap() != null && software.getFileFormatMap().size() > 0) {
						log.info("Software file formats: " + software.getFileFormatMap()); 
						for (Entry<String, String> entry : software.getFileFormatMap().entrySet()) {
							if (entry.getKey().contains(currentFileFormat)) {
								softwareCsv = softwareCsv + entry.getValue() + ", ";
								softwareCount++;
							}
						}
					}
				}

				cFileFormatDescriptionRows = cFileFormatDescriptionRows + 
					nameStr + ";" + ";" + softwareCount + ";" + 
					softwareCsv + ";" + verStr + ";" + ";" + ";" + puidStr + ";" + ";" + "" + ";" +
					"" + ";" + "" + ";" + ";" + currentFileFormat + ";" + vendorCsv + ";" + ";" + "\n";
				LODFormat lodFormat = new LODFormat();
				lodFormat.setFormatName(nameStr);
				lodFormat.setSoftwareCount(softwareCount);
				lodFormat.setSoftware(softwareCsv);
				lodFormat.setPuid(puidStr);
				lodFormat.setVendors(vendorCsv);
				lodFormat.setFileExtensions(currentFileFormat);
				lodFormat.setCurrentFormatVersion(verStr);
				lodFormat.setRepository(LODConstants.PRONOM);
				// retrieve repository IDs and descriptions from PronomFileFormat database collection
				PronomFileFormat pronomFileFormat = new PronomFileFormat();
				pronomFileFormat.setFfmaObjectName(PronomFileFormat.class.getSimpleName());
				pronomFileFormat.setExtension(currentFileFormat);
				List<String> repositoryIDs = new ArrayList<String>();
				List<String> descriptions = new ArrayList<String>();
				try {
					List<? extends FfmaDomainObject> fileFormats = dataStoreDao
						.retrieveCollection((FfmaDomainObject) pronomFileFormat, exclusionsList);
					Iterator<? extends FfmaDomainObject> fileFormatsIter = fileFormats.iterator();
					while (fileFormatsIter.hasNext()) {
						PronomFileFormat fileFormat = (PronomFileFormat) fileFormatsIter.next();
						if (fileFormat.getPuid() != null && fileFormat.getPuid().length() > 0) {
							if (!fileFormat.getPuid().equals(LODConstants.EMPTY_PUID)) {
								repositoryIDs.add(fileFormat.getPuid());
							}
						}
						if (fileFormat.getXpuid() != null && fileFormat.getXpuid().length() > 0) {
							repositoryIDs.add(fileFormat.getXpuid());
						}
						if (fileFormat.getDescription() != null && fileFormat.getDescription().length() > 0) {
							descriptions.add(fileFormat.getDescription());
						}
					}
					if (repositoryIDs.size() > 0) {
						lodFormat.setRepositoryId(repositoryIDs.toArray(new String[]{}));
					}
					if (descriptions.size() > 0) {
						lodFormat.setDescription(descriptions.toArray(new String[]{}));
					}
				} catch (Exception e) {
					fail("Caught ObjectNotRetrievedException error: " + e + e.getStackTrace());
				}

				LODFormat checkLodFormat = new LODFormat();
				checkLodFormat.setRepository(LODConstants.PRONOM);
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
		    writer.append(vendorDescriptionCsv);
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
	public void searchAllFileFormats() throws MalformedURLException, URISyntaxException {
		log.info("test retrieves all file formats from PRONOM using SPARQL request");
		LODUtils.initCalculationModel();
		
		/* find file formats in Pronom */
		List<FfmaDomainObject> FfmaDomainObjectsList = new ArrayList<FfmaDomainObject>();
		List<PronomFileFormat> fileFormatList = LODUtils.searchAllPronomFileFormats();
		Iterator<PronomFileFormat> resultIter = fileFormatList.iterator();
		while (resultIter.hasNext()) {
			PronomFileFormat fileFormatEntry = resultIter.next();
			log.info("File format entry: " + fileFormatEntry);
			FfmaDomainObjectsList.add((FfmaDomainObject) fileFormatEntry);
		}
		log.info("File formats count: " + fileFormatList.size());
		assertNotNull("Retrieved list must not be null", fileFormatList);
		assertTrue(fileFormatList.size() >= 1);		

		/* store retrieved file formats in database */
		boolean res = false;
		try {
			CommonApiServerConfigurationImpl commonApiServerConfiguration = new CommonApiServerConfigurationImpl();
			commonApiServerConfiguration.init();
			MongoDbManager mongoDbManager = new MongoDbManager();
			mongoDbManager.setCommonApiServerConfiguration(commonApiServerConfiguration);
			mongoDbManager.init();
			DataStoreDaoImpl dataStoreDao = new DataStoreDaoImpl();
			dataStoreDao.setMongoDbManager(mongoDbManager);
			/* remove stored in database Pronom file formats */
			dataStoreDao.removeCollectionByName(PronomFileFormat.class.getSimpleName());
			/* store new file formats */
			res = dataStoreDao.storeObjectsList(FfmaDomainObjectsList);
		} catch (Exception e) {
			log.info("Cannot store FfmaFullDocObject list. " + e);
		} finally {
			FfmaDomainObjectsList.clear();
		}

		assertTrue(res);		
	}
		
	//@Test
	public void retrieveAllPronomFileFormatsFromDb() throws MalformedURLException,
			URISyntaxException, ObjectNotStoredException, ObjectNotFoundException {
		log.info("retrieveAllPronomFileFormatsFromDb");

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
			List<? extends FfmaDomainObject> fileFormatsList = dataStoreDao
				.retrieveCollection((FfmaDomainObject) pronomFileFormat, exclusionsList);

			// Ensure that the stored file formats have been retrieved
			assertTrue(fileFormatsList.size() > 0);
			Iterator<? extends FfmaDomainObject> iter = fileFormatsList.iterator();
			while (iter.hasNext()) {
				PronomFileFormat pronomFileFormatObject = (PronomFileFormat) iter.next();
				log.info("retrieved pronomFileFormat object: " + pronomFileFormatObject);
			}
		} catch (Exception e) {
			fail("Caught ObjectNotRetrievedException error: " + e + e.getStackTrace());
		}
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
	
	@Test
	public void getFileFormatsList() throws MalformedURLException,
			URISyntaxException, ObjectNotStoredException, ObjectNotFoundException {
		log.info("getFileFormatsList");

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
			List<? extends FfmaDomainObject> fileFormatsList = dataStoreDao
				.retrieveCollection((FfmaDomainObject) pronomFileFormat, exclusionsList);

			// Ensure that the stored file formats have been retrieved
			assertTrue(fileFormatsList.size() > 0);
			
			List<String> extensionsList = LODUtils.getFileFormatsList(fileFormatsList);
			Iterator<String> iter = extensionsList.iterator();
			while (iter.hasNext()) {
				log.info("extension: " + iter.next());
			}
			log.info("extensionsList size: " + extensionsList.size());
			assertTrue(extensionsList.size() > 0);
		} catch (Exception e) {
			fail("Caught ObjectNotRetrievedException error: " + e + e.getStackTrace());
		}
	}
	
	/**
	 * This map extracts fields and their corresponding counts from data map
	 * received from group method of Mongo database
	 * @param dataMap
	 *        The original map from Mongo database
	 * @return mapping between field value and corresponding count
	 */
	public Map<String, Integer> extractFieldCounts(Map<Integer, Map<String, String>> dataMap) {
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		for (Map.Entry<Integer, Map<String, String>> entry : dataMap.entrySet()) {
			Map<String, String> tmpCountMap = entry.getValue();
			int counter = 0;
			String key = "";
			int count = 0;
			for (Map.Entry<String, String> entryCount : tmpCountMap.entrySet()) {
				Object value = entryCount.getValue();
				if (counter == 0) {
					key = value.toString();
				} else {
					count = ((Double) value).intValue();
				}
				counter++;
			}
			resultMap.put(key, count);
		}
		for (Map.Entry<String, Integer> resultEntry : resultMap.entrySet()) {
			log.info(resultEntry.getKey() + ": " + resultEntry.getValue());
		}
		return resultMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void evaluateFileFormatStatistics() throws MalformedURLException, URISyntaxException {
		log.info("This test evaluates file format statistics from linked open data repository PRONOM and stores results in MongoDB.");
		try {
			initDataStoreDao();
			// clean up old version of file format statistics collections
			FileFormatStatistics fileFormatStatisticsTmp = new FileFormatStatistics();
			fileFormatStatisticsTmp.setRepository(LODConstants.PRONOM);
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			try {
				dataStoreDao.removeObjectsSet(fileFormatStatisticsTmp, exclusionsList);
			} catch (ObjectNotRemovedException e) {
				e.printStackTrace();
			}
			
			// evaluate file format statistics
			PronomFileFormat pronomFileFormat = new PronomFileFormat();
			pronomFileFormat.setFfmaObjectName(PronomFileFormat.class.getSimpleName());
			Map groupedFileFormatExtensionsMap = dataStoreDao.getGroupedObjectsCount(
					PronomFileFormat.FieldsEnum.Extension.evalName(),
					(FfmaDomainObject) pronomFileFormat);
			Map groupedFileFormatMimetypesMap = dataStoreDao.getGroupedObjectsCount(
					PronomFileFormat.FieldsEnum.Mimetype.evalName(),
					(FfmaDomainObject) pronomFileFormat);

			Map<String, Integer> extensionsMap = extractFieldCounts(groupedFileFormatExtensionsMap);
			Map<String, Integer> mimetypesMap = extractFieldCounts(groupedFileFormatMimetypesMap);
			
			// store file format statistics
			FileFormatStatistics fileFormatStatistics = new FileFormatStatistics();
			fileFormatStatistics.setExtensions(sortMapByString(extensionsMap));
			fileFormatStatistics.setMimetypes(sortMapByString(mimetypesMap));
			fileFormatStatistics.setRepository(LODConstants.PRONOM);
			
			Map<String, Integer> res = fileFormatStatistics.getMimetypes();
			for (Map.Entry<String, Integer> resultEntry : res.entrySet()) {
				log.info("resulting map - " + resultEntry.getKey() + ": " + resultEntry.getValue());
			}
			try {
				dataStoreDao.storeObject(fileFormatStatistics);
			} catch (ObjectNotStoredException e) {
				e.printStackTrace();
			} catch (ObjectNotFoundException e) {
				e.printStackTrace();
			}
			log.info("extensions count: " + fileFormatStatistics.getExtensions().size());
			assertTrue(fileFormatStatistics.getExtensions().size() > 0);
			log.info("mimetypes count: " + fileFormatStatistics.getMimetypes().size());
			assertTrue(fileFormatStatistics.getMimetypes().size() > 0);
		}
		catch(Exception e)
		{
		     log.info("Error in file formats statistics retrieval: " + e);
		} 
	}
	
	/**
	 * This method sorts map by string keys.
	 * @param unsortedMap
	 *        The unsorted map
	 * @return sorted map
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Integer> sortMapByString(Map<String, Integer> unsortedMap) {
        ValueComparator valueComparator =  new ValueComparator();
        TreeMap<String, Integer> res = new TreeMap<String, Integer>(valueComparator);
        res.putAll(unsortedMap);
        return res;
	}
	
	/**
	 * This class implements comparison for strings.
	 */
	@SuppressWarnings("rawtypes")
	class ValueComparator implements Comparator {
		public int compare(Object a, Object b) {
			return ((String) a).compareTo(((String) b));
		}
	}
	
	//@Test
	public void removeRepositoryData() throws MalformedURLException, URISyntaxException {
		log.info("This test removes file format information for linked open data repository PRONOM from database.");
		try {
			initDataStoreDao();
			// clean up old version of LOD format, software and vendor collections
			LODFormat lodFormatTmp = new LODFormat();
			lodFormatTmp.setRepository(LODConstants.PRONOM);
			LODSoftware lodSoftwareTmp = new LODSoftware();
			lodSoftwareTmp.setRepository(LODConstants.PRONOM);
			LODVendor lodVendorTmp = new LODVendor();
			lodVendorTmp.setRepository(LODConstants.PRONOM);
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
		log.info("This test removes vendors collection for linked open data repository PRONOM from database.");
		try {
			initDataStoreDao();
			LODVendor lodVendorTmp = new LODVendor();
			lodVendorTmp.setRepository(LODConstants.PRONOM);
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
}
