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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;

import ait.ffma.common.CommonApiServerConfigurationImpl;
import ait.ffma.common.dao.DataStoreDaoImpl;
import ait.ffma.common.dao.mongodb.MongoDbManager;
import ait.ffma.common.exception.ObjectNotFoundException;
import ait.ffma.common.exception.ObjectNotRemovedException;
import ait.ffma.common.exception.ObjectNotStoredException;
import ait.ffma.domain.FfmaDomainObject;
import ait.ffma.domain.preservation.riskmanagement.DBPediaFileFormat;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;
import ait.ffma.domain.preservation.riskmanagement.DipSoftwareId;
import ait.ffma.domain.preservation.riskmanagement.FreebaseFileFormat;
import ait.ffma.domain.preservation.riskmanagement.LODFormat;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.domain.preservation.riskmanagement.LODVendor;
import ait.ffma.domain.preservation.riskmanagement.PronomFileFormat;
import ait.ffma.factory.FfmaAbstractFactoryImpl;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODStatisticsFreebaseUtils;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODUtils;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperty;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;

/**
 * This class provides LOD (link open data) retrieval experiments for FREEBASE LOD repository.
 */
public class TestLODFreebaseRetrieve {

	private String CSV_FILE = "fileFormatInformationFreebase.csv";
	
	private LODConstants.LODOutputMode outputMode = LODConstants.LODOutputMode.Columns;
	
	private List<String> fileFormatList = Arrays.asList("pdf", "doc",
			"tiff", "jp2", "txt", "xml", "html", "jpg", "psd", "png", "bmp", "gif", 
			"mp1", "mp2", "mp3", "mp4", "m4v", "m4a", "mpg", "mpeg", 
			"mpeg1", "mpeg2", "mpeg3", "mpeg4", "flv", "ply", "dae");

	private List<DipFormatId> dipFormatIdList = new ArrayList<DipFormatId>();
	
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
	
	private String vendorListCsv = "";
	private String vendorDescriptionCsv = "";
	
	/**
	 * This object provides connection to database for testing 
	 */
	private DataStoreDaoImpl dataStoreDao = null;
	
	/**
	 * These maps are used to bind vendor to file format
	 */
	Map<String, String> mapFileFormatToVendor = new HashMap<String, String>();

	
	/**
	 * Check if extension is already in the list
	 * @param extension
	 * @return
	 */
	private boolean extensionExists(String extension) {
		boolean res = false;
		Iterator<DipFormatId> resultIter = dipFormatIdList.iterator();
		while (resultIter.hasNext()) {
			DipFormatId dipFormatId = resultIter.next();
			String currentExtension = dipFormatId.getExtension();
			if (currentExtension != null && extension != null
					&& currentExtension.length() > 0 && extension.length() > 0
					&& currentExtension.equals(extension)) {
				res = true;
			}
		}
		return res;
	}
	
	/**
	 * Retrieve DipFormatId by extension 
	 * @param extension
	 * @return
	 */
	private DipFormatId getDipFormatIdByExtension(String extension) {
		DipFormatId res = null;
		Iterator<DipFormatId> resultIter = dipFormatIdList.iterator();
		while (resultIter.hasNext()) {
			DipFormatId dipFormatId = resultIter.next();
			String currentExtension = dipFormatId.getExtension();
			if (currentExtension != null && extension != null
					&& currentExtension.length() > 0 && extension.length() > 0
					&& currentExtension.equals(extension)) {
				res = dipFormatId;
			}
		}
		return res;
	}
	
	/**
	 * This method merges string arrays to one string array
	 * @param existingValues
	 * @param newValues
	 * @return
	 */
	private String[] addArrays(String[] existingValues, String[] newValues) {
		List<String> existingIds = Arrays.asList(existingValues);
		List<String> newIds = Arrays.asList(newValues);
		List<String> idList = new ArrayList<String>();
		idList.addAll(existingIds);
		idList.addAll(newIds);
		List<String> emptyStrings = new ArrayList<String>();
		emptyStrings.add(" ");
		idList.removeAll(emptyStrings);
		return idList.toArray(new String[]{});
	}

	/**
	 * @param dipFormatId
	 * @param format
	 */
	private void evaluateDipForamtId(DipFormatId dipFormatId, String format) {
		if (format != null && format.length() > 0 && !format.equals("or")) {
			if (!extensionExists(format)) {
				dipFormatId.setExtension(format);
				dipFormatId.setDipId(LODConstants.DIP + format);
				dipFormatIdList.add(dipFormatId);
			} else {
				DipFormatId existingDipFormatId = getDipFormatIdByExtension(format);
				if (dipFormatId.getDBPediaId() != null && dipFormatId.getDBPediaId().length > 0) {
					String[] resDBPedia = addArrays(existingDipFormatId.getDBPediaId(), dipFormatId.getDBPediaId());
					if (resDBPedia != null && resDBPedia.length > 0) {
						existingDipFormatId.setDBPediaId(resDBPedia);
					}
				}
				if (dipFormatId.getPronomId() != null && dipFormatId.getPronomId().length > 0) {
					String[] resPronom = addArrays(existingDipFormatId.getPronomId(), dipFormatId.getPronomId());
					if (resPronom != null && resPronom.length > 0) {
						existingDipFormatId.setPronomId(resPronom);
					}
				}
				if (dipFormatId.getFreebaseId() != null && dipFormatId.getFreebaseId().length > 0) {
					String[] resFreebase = addArrays(existingDipFormatId.getFreebaseId(), dipFormatId.getFreebaseId());
					if (resFreebase != null && resFreebase.length > 0) {
						existingDipFormatId.setFreebaseId(resFreebase);
					}
				}
				if (dipFormatId.getDescription() != null && dipFormatId.getDescription().length > 0) {
					String[] resDescription = addArrays(existingDipFormatId.getDescription(), dipFormatId.getDescription());
					if (resDescription != null && resDescription.length > 0) {
						existingDipFormatId.setDescription(resDescription);
					}
				}
				dipFormatId.setExtension(format);
				dipFormatId.setDipId(LODConstants.DIP + format);
			}
		}
	}
	
	/**
	 * This method aggregates file formats from different LOD repositories
	 * @param formatList
	 */
	private void aggregateFileFormats(List<DipFormatId> formatList) {
		Iterator<DipFormatId> iter = formatList.iterator();
		while (iter.hasNext()) {
			String format = null;
			DipFormatId dipFormatId = (DipFormatId) iter.next();
			String formatStr = dipFormatId.getExtension();
			if (formatStr.contains(" ")) {
				String[] formats = formatStr.split(" ");
				for (int i = 0; i < formats.length; i++) {
					format = formats[i].replace(".", "").replace(",", "").replace(" ", "");
					format = format.toLowerCase();
					evaluateDipForamtId(dipFormatId, format);
				}
			} else {
				if (formatStr.contains(".")) {
					format = formatStr.replace(".", "").replace(" ", "");
				} else {
					format = formatStr;
				}
				format = format.toLowerCase();
				evaluateDipForamtId(dipFormatId, format);
			}
		}
	}
	
	/**
	 * This method initializes extensions list aggregated from all LOD repositories.
	 */
	private void initFileFormatListFromAllRepositories() {
		PronomFileFormat pronomFileFormat = new PronomFileFormat();
		pronomFileFormat.setFfmaObjectName(PronomFileFormat.class.getSimpleName());
		DBPediaFileFormat dbpediaFileFormat = new DBPediaFileFormat();
		dbpediaFileFormat.setFfmaObjectName(DBPediaFileFormat.class.getSimpleName());
		FreebaseFileFormat freebaseFileFormat = new FreebaseFileFormat();
		freebaseFileFormat.setFfmaObjectName(FreebaseFileFormat.class.getSimpleName());
		
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
			List<? extends FfmaDomainObject> dbpediaFileFormats = dataStoreDao
				.retrieveCollection((FfmaDomainObject) dbpediaFileFormat, exclusionsList);
			List<? extends FfmaDomainObject> freebaseFileFormats = dataStoreDao
				.retrieveCollection((FfmaDomainObject) freebaseFileFormat, exclusionsList);

			List<DipFormatId> pronomFileFormatList = LODUtils.getPronomDipFileFormatsList(fileFormats);
			List<DipFormatId> dbpediaFileFormatList = LODUtils.getDBPediaDipFileFormatsList(dbpediaFileFormats);
			List<DipFormatId> freebaseFileFormatList = LODStatisticsFreebaseUtils.getFreebaseDipFileFormatsList(freebaseFileFormats);
			aggregateFileFormats(pronomFileFormatList);
			aggregateFileFormats(dbpediaFileFormatList);
			aggregateFileFormats(freebaseFileFormatList);
			log.info("extensionsList size: " + fileFormatList.size());
		} catch (Exception e) {
			fail("Caught ObjectNotRetrievedException error: " + e + e.getStackTrace());
		}
	}
		
	@Test
	public void searchSoftwareFromExtension() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates compatible software for given extension");
		LODUtils.initCalculationModel();
		String searchExtension = "pdf";
		log.info("Extension: " + searchExtension);
		searchColumns.clear();
		searchColumns.add(LODConstants.FB_EXTENSION);
		resultColumns.clear();
		resultColumns.add(LODConstants.FB_READ_BY);
		resultColumns.add(LODConstants.FB_NAME);
		List<String> resultList = LODUtils.searchJSON(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, searchExtension,
				searchColumns, resultColumns, LODConstants.FREEBASE);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Result list: " + resultList.size());
		while (resultIter.hasNext()) {
			String str = resultIter.next();
			log.info("Result found: " + str);
		}

		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);

		LODProperty lodProperty = LODUtils.insertLODSourceOriginalValue(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
				LODConstants.FREEBASE, resultList);
		assertTrue(LODUtils.getLODSourceOriginalValue(lodProperty, LODConstants.FREEBASE).size() >= 1);
	}
	
	@Test
	public void searchSoftwareVendorFromSoftware() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates software vendors for given software name beginning with software id evaluation for " +
				"particular file format extension");
		LODUtils.initCalculationModel();
		String searchExtension = "pdf";
		log.info("Extension: " + searchExtension);
		searchColumns.clear();
		searchColumns.add(LODConstants.FB_EXTENSION);
		resultColumns.clear();
		resultColumns.add(LODConstants.FB_READ_BY);
		resultColumns.add(LODConstants.FB_ID);
		List<String> resultList = LODUtils.searchJSON(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, searchExtension,
				searchColumns, resultColumns, LODConstants.FREEBASE);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Result list: " + resultList.size());
//		String resultStr = "";
		while (resultIter.hasNext()) {
			String str = resultIter.next();
			log.info("Result found: " + str);
//			resultStr = resultStr + LODConstants.STRING_SEPARATOR + str;
		}

		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);

		LODProperty lodProperty = LODUtils.insertLODSourceOriginalValue(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
				LODConstants.FREEBASE, resultList);
		assertTrue(LODUtils.getLODSourceOriginalValue(lodProperty, LODConstants.FREEBASE).size() >= 1);
		
		/** 
		 * retrieve vendor names from evaluated software id
		 */
		searchColumns.clear();
		searchColumns.add(LODConstants.FB_ID);
		resultColumns.clear();
		resultColumns.add(LODConstants.FB_SOFTWARE_DEVELOPER);
		resultColumns.add(LODConstants.FB_ID);
		List<String> resultList2 = LODUtils.searchJSON(
				LODConstants.LOD_VENDOR_BY_SOFTWARE_PROPERTY_ID, resultList.get(1), // test with /en/adobe_acrobat
				searchColumns, resultColumns, LODConstants.FREEBASE);

		Iterator<String> resultIter2 = resultList2.iterator();
		log.info("Result list2: " + resultList2.size());
		while (resultIter2.hasNext()) {
			String str = resultIter2.next();
			log.info("Result found: " + str);
		}

		assertNotNull("Retrieved list must not be null", resultList2);
		assertTrue(resultList2.size() >= 1);

		LODProperty lodProperty2 = LODUtils.insertLODSourceOriginalValue(
				LODConstants.LOD_VENDOR_BY_SOFTWARE_PROPERTY_ID,
				LODConstants.FREEBASE, resultList2);
		assertTrue(LODUtils.getLODSourceOriginalValue(lodProperty2, LODConstants.FREEBASE).size() >= 1);		
	}
	
	@Test
	public void searchBusinessStatusOfVendor() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates business status of given vendor identified by id");
		LODUtils.initCalculationModel();
		List<String> resultList = LODUtils.searchInFreebase(
				LODConstants.LOD_VENDOR_BUSINESS_STATUS_PROPERTY_ID,
				"/en/adobe_systems", LODConstants.FB_ID,
				LODConstants.FB_VENDOR_REVENUE, LODConstants.FB_AMOUNT);
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_VENDOR_BUSINESS_STATUS_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
	}
	
	@Test
	public void searchNumberOfEmployeesOfVendor() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates number of employees of given vendor identified by id");
		LODUtils.initCalculationModel();
		List<String> resultList = LODUtils.searchInFreebase(
				LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID,
				"/en/adobe_systems", LODConstants.FB_ID,
				LODConstants.FB_VENDOR_NUM_OF_EMPLOYEES, LODConstants.FB_NUMBER);
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
	}

	@Test
	public void searchStockIssuesOfVendor() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates stock issues of given vendor identified by id");
		LODUtils.initCalculationModel();
		List<String> resultList = LODUtils.searchInFreebase(
				LODConstants.LOD_VENDOR_STOCK_ISSUES_PROPERTY_ID,
				"/en/adobe_systems", LODConstants.FB_ID,
				LODConstants.FB_VENDOR_STOCK_ISSUES, LODConstants.FB_NAME);
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_VENDOR_STOCK_ISSUES_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
	}
		
	@Test
	public void searchRankedListOfVendor() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates ranked list of given vendor identified by id");
		LODUtils.initCalculationModel();
		List<String> resultList = LODUtils.searchInFreebase(
				LODConstants.LOD_VENDOR_RANKED_LIST_PROPERTY_ID,
				"/en/adobe_systems", LODConstants.FB_ID,
				LODConstants.FB_VENDOR_RANKED_LIST, LODConstants.FB_LIST);
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_VENDOR_RANKED_LIST_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
		resultList.clear();
		resultList = LODUtils.searchInFreebase(
				LODConstants.LOD_VENDOR_RANKED_LIST_PROPERTY_ID,
				"/en/adobe_systems", LODConstants.FB_ID,
				LODConstants.FB_VENDOR_RANKED_LIST, LODConstants.FB_RANK);
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_VENDOR_RANKED_LIST_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
	}
		
	@Test
	public void searchComputerArchitectureOfOS() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates computer architecture of given operation system identified by id");
		LODUtils.initCalculationModel();
		List<String> resultList = LODUtils.searchInFreebase(
				LODConstants.LOD_OS_COMPUTER_ARCHITECTURE_PROPERTY_ID,
				"/en/mac_os_x", LODConstants.FB_ID,
				LODConstants.FB_OS_COMPUTER_ARCHITECTURE, LODConstants.FB_NAME);
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_OS_COMPUTER_ARCHITECTURE_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
	}
		
	@Test
	public void searchSupportedFileFormatsOfOS() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates supported file formats of given operation system identified by id");
		LODUtils.initCalculationModel();
		List<String> resultList = LODUtils.searchInFreebase(
				LODConstants.LOD_OS_SUPPORTED_FILE_FORMATS_PROPERTY_ID,
				"/en/mac_os_x", LODConstants.FB_ID,
				LODConstants.FB_OS_SUPPORTED_FILE_FORMATS, LODConstants.FB_NAME);
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_OS_SUPPORTED_FILE_FORMATS_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
	}
		
	@Test
	public void searchProgrammingLanguageOfSoftware() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates programming language of given software identified by id");
		LODUtils.initCalculationModel();
		List<String> resultList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_PROGRAMMING_LANGUAGE_PROPERTY_ID,
				"/en/oracle_database", LODConstants.FB_ID,
				LODConstants.FB_SOFTWARE_PROGRAMMING_LANGUAGE, LODConstants.FB_NAME);
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_SOFTWARE_PROGRAMMING_LANGUAGE_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
	}
		
	@Test
	public void searchLicenseOfSoftware() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates license of given software identified by id");
		LODUtils.initCalculationModel();
		List<String> resultList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_LICENSE_PROPERTY_ID,
				"/en/mysql", LODConstants.FB_ID,
				LODConstants.FB_SOFTWARE_LICENSE, LODConstants.FB_NAME);
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_SOFTWARE_LICENSE_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
	}
		
	@Test
	public void searchProtocolOfSoftware() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates protocol of given software identified by id");
		LODUtils.initCalculationModel();
		List<String> resultList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_PROTOCOL_PROPERTY_ID,
				"/en/mozilla_firefox", LODConstants.FB_ID,
				LODConstants.FB_SOFTWARE_PROTOCOL, LODConstants.FB_NAME);
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_SOFTWARE_PROTOCOL_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
	}

	@Test
	public void searchRankedListOfVendorExt() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates all parameters of ranked list of given vendor identified by id");
		LODUtils.initCalculationModel();
		List<List<String>> resultList = LODUtils.searchInFreebaseExt(
				LODConstants.LOD_VENDOR_RANKED_LIST_PROPERTY_ID,
				"/en/adobe_systems", LODConstants.FB_ID, Arrays.asList(
						LODConstants.FB_VENDOR_RANKED_LIST,
						LODConstants.FB_LIST, LODConstants.FB_RANK,
						LODConstants.FB_YEAR));
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_VENDOR_RANKED_LIST_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
	}
		
	@Test
	public void searchBusinessStatusOfVendorExt() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates all parameters of business status of given vendor identified by id");
		LODUtils.initCalculationModel();
		List<String> fields = Arrays.asList(LODConstants.FB_VENDOR_REVENUE, LODConstants.FB_AMOUNT, LODConstants.FB_CURRENCY, LODConstants.FB_VALID_DATE);
		List<List<String>> resultList = LODUtils.searchInFreebaseExt(
				LODConstants.LOD_VENDOR_BUSINESS_STATUS_PROPERTY_ID,
				"/en/adobe_systems", LODConstants.FB_ID,
				fields);
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_VENDOR_BUSINESS_STATUS_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
		/**
		 * print out 
		 */
		Iterator<List<String>> resultIter = resultList.iterator();
		String res = "LOD PROPERTY: "
				+ "LOD_VENDOR_BUSINESS_STATUS_PROPERTY_ID, " + "SEARCH VALUE: "
				+ "/en/adobe_systems, " + "LIST SIZE: " + resultList.size()
				+ "\n";
		while (resultIter.hasNext()) {
			List<String> subList = resultIter.next();
			Iterator<String> subIter = subList.iterator();
			res = res + "ENTRY: ";
			int index = 1;
			while (subIter.hasNext()) {
				String str = subIter.next();
				res = res + fields.get(index) + ": " + str + ", ";
				index++;
			}
			res = res + "\n";
		}
		log.info("************ " + res);
	}
	
	//@Test
	public void searchSoftwareFromExtensionPrintAll() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates compatible software for all given extensions");
		LODUtils.initCalculationModel();
		List<String> fields = Arrays.asList(LODConstants.FB_READ_BY, LODConstants.FB_NAME);
		List<List<String>> resultList = LODUtils.searchInFreebaseExt(
				LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
				"[]", LODConstants.FB_EXTENSION,
				fields);
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		assertTrue(LODUtils
				.getLODSourceOriginalValue(
						LODUtils.getLODPropertyById(LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID),
						LODConstants.FREEBASE).size() >= 1);
		/**
		 * print out 
		 */
		Iterator<List<String>> resultIter = resultList.iterator();
		String res = "LOD PROPERTY: "
				+ "LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, " + "SEARCH VALUE: "
				+ "[], " + "LIST SIZE: " + resultList.size()
				+ "\n";
		while (resultIter.hasNext()) {
			List<String> subList = resultIter.next();
			Iterator<String> subIter = subList.iterator();
			res = res + "ENTRY: ";
			int index = 1;
			while (subIter.hasNext()) {
				String str = subIter.next();
				res = res + fields.get(index) + ": " + str + ", ";
				index++;
			}
			res = res + "\n";
		}
		log.info("************ " + res);
	}
	
	/**
	 * This method initializes extensions list
	 */
	@SuppressWarnings("unused")
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
	
	/**
	 * This method retrieves objects set from database according to Ffma domain object type and to 
	 * passed query parameter
	 * @param FfmaDomainObject
	 * @param parameterMap
	 */
	public void removeObjectsSet(FfmaDomainObject FfmaDomainObject, Map<String, String> queryParameterMap) {		
		try {		
			List<String> exclusionsList = new ArrayList<String>();
			exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
			List<? extends FfmaDomainObject> objectsList = dataStoreDao
				.retrieveCollection((FfmaDomainObject) FfmaDomainObject, exclusionsList);
		
			Iterator<? extends FfmaDomainObject> iter = objectsList.iterator();
			while (iter.hasNext()) {
				dataStoreDao.removeObject(iter.next());
				PronomFileFormat pronomFileFormatObject = (PronomFileFormat) iter.next();
				log.info("retrieved pronomFileFormat object: " + pronomFileFormatObject);
			}
		} catch (Exception e) {
			fail("Caught ObjectNotRetrievedException error: " + e + e.getStackTrace());
		}
	}

	/**
	 * This method evaluates Freebase description for passed guid number.
	 * @param guid
	 * @return description text
	 */
	public String getDescription(String guid) {
		String res = "";
		if (guid != null) {
			guid = guid.replaceAll(LODConstants.STRING_SEPARATOR, "");
	
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().
			setConnectionTimeout(10000);
			String url = "http://hotshot.dfhuynh.user.dev.freebaseapps.com/html?id=/guid/" + guid;
			HttpMethod method = new GetMethod(url);
			method.setFollowRedirects(false);
			try {
				client.executeMethod(method);
				String responseBody = method.getResponseBodyAsString();
				log.info("Response body: " + responseBody);
				String descriptionStr = responseBody.substring(
						responseBody.indexOf(LODConstants.DESC_BEGIN) + LODConstants.DESC_BEGIN.length(),
						responseBody.indexOf(LODConstants.DESC_P_END));
				res = descriptionStr.replaceAll(LODConstants.DESC_P, "");
			} catch (HttpException he) {
				log.fine("Http error connecting to ’" + url + "’");
			} catch (IOException ioe) {
				log.fine("Unable to connect to ’" + url + "’");
			} catch (Exception e) {
				log.fine("Description retrieval exception. Unable to parse response from ’"
						+ url + "’ (Exception: " + e.getLocalizedMessage() + ")");
			}
			method.releaseConnection();
		}
		return res;
	}

	//@Test
	public void searchFileFormatInformationCsv() throws MalformedURLException, URISyntaxException {
		log.info("This test evaluates file format information from linked open data repositories and stores results in CSV file.");
		try {
			initDataStoreDao();

		    FileWriter writer = new FileWriter(CSV_FILE);
		    String cFileFormatDescriptionRows = "";
			String softwareDescriptionCsv = "";
			vendorDescriptionCsv = "";

			if (outputMode.equals(LODConstants.LODOutputMode.Columns)) {
				writer.append("FILE FORMAT DESCRIPTION\n");
				writer.append("FORMAT NAME;CURRENT VERSION RELEASE DATE;SOFTWARE COUNT;SOFTWARE;" +
						"CURRENT FORMAT VERSION;FORMAT LICENSE;LIMITATIONS;PUID;FORMAT HOMEPAGE;MIME TYPE;" +
						"FORMAT GENRE;FORMAT CREATOR;OPEN FORMAT;FILE EXTENSIONS;VENDORS;STANDARDS\n");
			} 

			String res = "";
			LODUtils.initCalculationModel();
//			initFileFormatList();
			initFileFormatListFromAllRepositories();
			int counter = 0; // limit to test
			Iterator<DipFormatId> fileFormatIter = dipFormatIdList.iterator();
			while (fileFormatIter.hasNext()) {
				DipFormatId dipFormatId = fileFormatIter.next();
				String currentFileFormat = dipFormatId.getExtension();

//			Iterator<String> fileFormatIter = fileFormatList.iterator();
//			while (fileFormatIter.hasNext()) {
				String softwareCsv = "";
				vendorListCsv = "";
//				String currentFileFormat = fileFormatIter.next();
	
				List<String> fields = Arrays.asList(LODConstants.FB_READ_BY, LODConstants.FB_NAME, LODConstants.FB_ID);
				List<List<String>> resultList = LODUtils.searchInFreebaseExt(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
						currentFileFormat, LODConstants.FB_EXTENSION,
						fields);
				/**
				 * print out 
				 */
				Iterator<List<String>> resultIter = resultList.iterator();
				res = res + "\n\n" + "LOD PROPERTY: "
						+ "LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID, " + "SEARCH VALUE: "
						+ currentFileFormat + ", " + "LIST SIZE: " + resultList.size()
						+ "\n";
				List<String> softwareNames = new ArrayList<String>();
				List<String> softwareIDs = new ArrayList<String>();
				while (resultIter.hasNext()) {
					List<String> subList = resultIter.next();
					Iterator<String> subIter = subList.iterator();
					res = res + "ENTRY: ";
					String tmpSoftware = "";
					String tmpId = "";
					int index = 1;
					while (subIter.hasNext()) {
						String str = subIter.next();
						if (fields.get(index).equals(LODConstants.FB_NAME)) {
							tmpSoftware = str;
							res = res + fields.get(index) + ": " + str + ", ";
							softwareCsv = softwareCsv +  str + ", ";
						}
						if (fields.get(index).equals(LODConstants.FB_ID)) {
							tmpId = str;
						}
						index++;
					}
					if (outputMode.equals(LODConstants.LODOutputMode.Columns)) {
						// add software name and ID to LODSoftware object
						softwareIDs.add(tmpId);
						softwareNames.add(tmpSoftware);

	                    softwareDescriptionCsv = softwareDescriptionCsv + generateSoftwareDescriptionColumn(tmpSoftware, tmpId, currentFileFormat);
					} else {
	                    softwareDescriptionCsv = softwareDescriptionCsv + generateSoftwareDescription(tmpSoftware, tmpId);
					}
					res = res + "\n";
				}

				List<String> creationDateList = LODUtils.searchInFreebase(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
						currentFileFormat, LODConstants.FB_EXTENSION,
						LODConstants.FB_FORMAT_CREATION_DATE, "");

				String creationDateStr = "";
				Iterator<String> creationDateIter = creationDateList.iterator();
				while (creationDateIter.hasNext()) {
					creationDateStr = creationDateStr + creationDateIter.next() + ", ";
				}

				List<String> nameList = LODUtils.searchInFreebase(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
						currentFileFormat, LODConstants.FB_EXTENSION,
						LODConstants.FB_NAME, "");

				String nameStr = "";
				Iterator<String> nameIter = nameList.iterator();
				while (nameIter.hasNext()) {
					nameStr = nameStr + nameIter.next() + ", ";
				}

				List<String> mimeTypeList = LODUtils.searchInFreebase(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
						currentFileFormat, LODConstants.FB_EXTENSION,
						LODConstants.FB_MIME_TYPE, LODConstants.FB_ID);

				String mimeTypeStr = "";
				Iterator<String> mimeTypeIter = mimeTypeList.iterator();
				while (mimeTypeIter.hasNext()) {
					mimeTypeStr = mimeTypeStr + mimeTypeIter.next() + ", ";
				}

				List<String> repositoryIDs = new ArrayList<String>();
				List<String> repositoryIdList = LODUtils.searchInFreebase(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
						currentFileFormat, LODConstants.FB_EXTENSION,
						LODConstants.FB_ID, LODConstants.FB_ID);
				Iterator<String> repositoryIdIter = repositoryIdList.iterator();
				while (repositoryIdIter.hasNext()) {
					repositoryIDs.add(repositoryIdIter.next());
				}

				List<String> guidList = LODUtils.searchInFreebase(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
						currentFileFormat, LODConstants.FB_EXTENSION,
						LODConstants.FB_GUID, LODConstants.FB_ID);

				List<String> descriptions = new ArrayList<String>();
				String guidStr = "";
				Iterator<String> guidIter = guidList.iterator();
				while (guidIter.hasNext()) {
					guidStr = guidIter.next();
					String descriptionStr = getDescription(guidStr);
					descriptions.add(descriptionStr);
					break;
				}

				List<String> genreList = LODUtils.searchInFreebase(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
						currentFileFormat, LODConstants.FB_EXTENSION,
						LODConstants.FB_GENRE, LODConstants.FB_NAME);

				String genreStr = "";
				Iterator<String> genreIter = genreList.iterator();
				while (genreIter.hasNext()) {
					genreStr = genreStr + genreIter.next() + ", ";
				}

				List<String> creatorList = LODUtils.searchInFreebase(
						LODConstants.LOD_SOFTWARE_BY_FORMAT_PROPERTY_ID,
						currentFileFormat, LODConstants.FB_EXTENSION,
						LODConstants.FB_FORMAT_CREATOR, "");

				String creatorStr = "";
				Iterator<String> creatorIter = creatorList.iterator();
				while (creatorIter.hasNext()) {
					creatorStr = creatorStr + creatorIter.next() + ", ";
					creatorStr = creatorStr.replaceAll("&amp;", "&"); // special case
				}

				if (outputMode.equals(LODConstants.LODOutputMode.Columns)) {
					String vendorCsv = "";
					vendorCsv = mapFileFormatToVendor.get(currentFileFormat);
					cFileFormatDescriptionRows = nameStr + ";" + creationDateStr + ";" + resultList.size() + ";" + 
					softwareCsv + ";" + ";" + ";" + ";" + ";" + ";" + mimeTypeStr + ";" +
					genreStr + ";" + creatorStr + ";" + ";" + currentFileFormat + ";" + vendorCsv + ";" + ";" + "\n";
					LODFormat lodFormat = new LODFormat();
					lodFormat.setFormatName(nameStr);
					lodFormat.setCurrentVersionReleaseDate(creationDateStr);
					lodFormat.setSoftwareCount(resultList.size());
					lodFormat.setSoftware(softwareCsv);
					lodFormat.setMimeType(mimeTypeStr);
					lodFormat.setFormatGenre(genreStr);
					lodFormat.setFormatCreator(creatorStr);
					lodFormat.setFileExtensions(currentFileFormat);
					lodFormat.setVendors(vendorCsv);
					if (repositoryIDs.size() > 0) {
						lodFormat.setRepositoryId(repositoryIDs.toArray(new String[]{}));
					}
					lodFormat.setRepository(LODConstants.FREEBASE);
					lodFormat.setDescription(descriptions.toArray(new String[]{}));
					lodFormat.setSoftwareName(softwareNames.toArray(new String[]{}));
					lodFormat.setSoftwareId(softwareIDs.toArray(new String[]{}));
					
					LODFormat checkLodFormat = new LODFormat();
					checkLodFormat.setRepository(LODConstants.FREEBASE);
					checkLodFormat.setFormatName(nameStr);
					updateObject(checkLodFormat, lodFormat);

				    writer.append(cFileFormatDescriptionRows);
				} else {
					writer.append("FILE FORMAT DESCRIPTION\n");
					writer.append("\n\n ****** FILE FORMAT DESCRIPTION ******\n\n");
				    writer.append("FORMAT NAME: " + nameStr + "\n");		    	    
				    writer.append("CURRENT VERSION RELEASE DATE: " + creationDateStr + "\n");
				    writer.append("SOFTWARE COUNT: " + resultList.size() + "\n");		    
				    writer.append("SOFTWARE: " + softwareCsv + "\n");
				    writer.append("CURRENT FORMAT VERSION: \n");
				    writer.append("FORMAT LICENSE: \n");
				    writer.append("LIMITATIONS: \n");		    
				    writer.append("PUID: \n");
				    writer.append("FORMAT HOMEPAGE: \n");
				    writer.append("MIME TYPE: " + mimeTypeStr + "\n");
				    writer.append("FORMAT GENRE: " + genreStr + "\n");
				    writer.append("FORMAT CREATOR: " + creatorStr + "\n");
				    writer.append("OPEN FORMAT: \n");
				    writer.append("FILE EXTENSIONS: " + currentFileFormat + " \n");
				    writer.append("VENDORS: " + vendorListCsv + "\n");
				    writer.append("STANDARDS: \n");
				    writer.append(softwareDescriptionCsv);
				}
				log.info("*** CURRENT COUNT *** " + counter);
				counter++;
//				if (counter > 2) break; // limit to test
			}
			if (outputMode.equals(LODConstants.LODOutputMode.Columns)) {
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
			}

			log.info("************ " + res);
			
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	}
	
	/**
	 * This method generates CSV information for software 
	 * @param softwareName
	 * @param softwareId
	 * @return CSV information collected from LOD repository
	 */
	private String generateSoftwareDescription(String softwareName, String softwareId) {
		String res = "";
		res = "\n\n*** SOFTWARE DESCRIPTION ***\n\n" + "SOFTWARE NAME: " + softwareName + "\n";
		
		List<String> licenseList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_LICENSE_PROPERTY_ID,
				softwareId, LODConstants.FB_ID,
				LODConstants.FB_SOFTWARE_LICENSE, LODConstants.FB_NAME);

		String licenseStr = "";
		Iterator<String> licenseIter = licenseList.iterator();
		while (licenseIter.hasNext()) {
			licenseStr = licenseStr + licenseIter.next() + ", ";
		}
		res = res + "SOFTWARE LICENSE: " + licenseStr + "\n";
		
		res = res + "SOFTWARE HOMEPAGE: " + "\n";
		List<String> genreFields = Arrays.asList(LODConstants.FB_SOFTWARE_GENRE, "");
		res = res + "GENRE: " + retrieveSimpleObjectFromRepository(genreFields,
				LODConstants.LOD_SOFTWARE_GENRE_PROPERTY_ID,
				softwareId) + "\n";
		List<String> compatibleOsFields = Arrays.asList(LODConstants.FB_SOFTWARE_COMPATIBLE_OS, "");
		res = res + "OPERATING SYSTEM: "
				+ retrieveSimpleObjectFromRepository(compatibleOsFields,
						LODConstants.LOD_SOFTWARE_COMPATIBLE_OS_PROPERTY_ID,
						softwareId) + "\n";
		List<String> protocolFields = Arrays.asList(LODConstants.FB_SOFTWARE_PROTOCOL, "");
		res = res + "PROTOCOLS: "
				+ retrieveSimpleObjectFromRepository(protocolFields,
						LODConstants.LOD_SOFTWARE_PROTOCOL_PROPERTY_ID,
						softwareId) + "\n";
		List<String> programmingLanguageFields = Arrays.asList(LODConstants.FB_SOFTWARE_PROGRAMMING_LANGUAGE, "");
		res = res + "PROGRAMMING LANGUAGE: " 
			+ retrieveSimpleObjectFromRepository(programmingLanguageFields,
				LODConstants.LOD_SOFTWARE_PROGRAMMING_LANGUAGE_PROPERTY_ID,
				softwareId) + "\n";

		List<String> versionList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_LATEST_VERSION_PROPERTY_ID,
				softwareId, LODConstants.FB_ID,
				LODConstants.FB_SOFTWARE_LATEST_VERSION, "");

		String versionStr = "";
		Iterator<String> versionIter = versionList.iterator();
		while (versionIter.hasNext()) {
			versionStr = versionStr + versionIter.next() + ", ";
		}
		res = res + "SOFTWARE LATEST VERSION: " + versionStr + "\n";
		
		List<String> latestReleaseDateList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_LATEST_RELEASE_DATE_PROPERTY_ID,
				softwareId, LODConstants.FB_ID,
				LODConstants.FB_SOFTWARE_LATEST_RELEASE_DATE, "");

		String latestReleaseDateStr = "";
		Iterator<String> latestReleaseDateIter = latestReleaseDateList.iterator();
		while (latestReleaseDateIter.hasNext()) {
			latestReleaseDateStr = latestReleaseDateStr + latestReleaseDateIter.next() + ", ";
		}
		res = res + "SOFTWARE LATEST RELEASE DATE: " + latestReleaseDateStr + "\n";

		List<String> fields = Arrays.asList(LODConstants.FB_SOFTWARE_DEVELOPER, LODConstants.FB_ID, LODConstants.FB_NAME);
		List<List<String>> resultList = LODUtils.searchInFreebaseExt(
				LODConstants.LOD_VENDOR_BY_SOFTWARE_PROPERTY_ID,
				softwareId, LODConstants.FB_ID,
				fields);
		/**
		 * print out 
		 */
		Iterator<List<String>> resultIter = resultList.iterator();
		while (resultIter.hasNext()) {
			List<String> subList = resultIter.next();
			Iterator<String> subIter = subList.iterator();
			String tmpVendorName = "";
			String tmpVendorId = "";
			int index = 1;
			while (subIter.hasNext()) {
				String str = subIter.next();
				if (fields.get(index).equals(LODConstants.FB_NAME)) {
					tmpVendorName = str;
					if (tmpVendorName != null && !vendorListCsv.contains(tmpVendorName)) {
						vendorListCsv = vendorListCsv + tmpVendorName + ", ";
					}
				}
				if (fields.get(index).equals(LODConstants.FB_ID)) {
					tmpVendorId = str;
				}
				index++;
			}
            res = res + generateVendorDescription(tmpVendorId, tmpVendorName);
			res = res + "\n";
		}
		return res;
	}
	
	/**
	 * This method checks the existing array and add new passed value to this array
	 * @param arr
	 * @param value
	 * @return extended array with new value
	 */
	private String[] addValueToStringArray(String[] arr, String value) {
		List<String> existingValues = new ArrayList<String>();
		if (arr != null && arr.length > 0 && !arr[0].equals(" ")) {
			existingValues.addAll(Arrays.asList(arr));
			List<String> emptyStrings = new ArrayList<String>();
			emptyStrings.add(" ");
			existingValues.removeAll(emptyStrings);
		}
		if (existingValues != null && value != null && value.length() > 0) {
			if (!existingValues.contains(value)) {
				try {
					existingValues.add(value);
				} catch (Exception e) {
					log.info("Unsupported operation exception: " + e);
				}
			}
		}
		return existingValues.toArray(new String[]{});
	}
	
	/**
	 * This method generates CSV information for software 
	 * @param softwareName
	 * @param softwareId
	 * @param fileFormat
	 *        To bind file format with vendor
	 * @return CSV information collected from LOD repository
	 */
	private String generateSoftwareDescriptionColumn(String softwareName, String softwareId, String fileFormat) {
		String res = "";
		res = softwareName + ";";
		
		LODSoftware lodSoftware = new LODSoftware();
		lodSoftware.setSoftwareName(softwareName);
		lodSoftware.setRepository(LODConstants.FREEBASE);

		List<String> licenseList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_LICENSE_PROPERTY_ID,
				softwareId, LODConstants.FB_ID,
				LODConstants.FB_SOFTWARE_LICENSE, LODConstants.FB_NAME);

		String licenseStr = "";
		Iterator<String> licenseIter = licenseList.iterator();
		while (licenseIter.hasNext()) {
			licenseStr = licenseStr + licenseIter.next() + ", ";
		}
		res = res + licenseStr + ";";
		lodSoftware.setSoftwareLicense(licenseStr);
		
		res = res + ";";
		List<String> genreFields = Arrays.asList(LODConstants.FB_SOFTWARE_GENRE, "");
		String swGenre = retrieveSimpleObjectFromRepository(genreFields,
				LODConstants.LOD_SOFTWARE_GENRE_PROPERTY_ID,
				softwareId);
		res = res + swGenre + ";";
		lodSoftware.setGenre(swGenre);
		
		List<String> compatibleOsFields = Arrays.asList(LODConstants.FB_SOFTWARE_COMPATIBLE_OS, "");
		String swOs = retrieveSimpleObjectFromRepository(compatibleOsFields,
				LODConstants.LOD_SOFTWARE_COMPATIBLE_OS_PROPERTY_ID,
				softwareId);
		res = res + swOs + ";";
		lodSoftware.setOperatingSystem(swOs);

		List<String> protocolFields = Arrays.asList(LODConstants.FB_SOFTWARE_PROTOCOL, "");
		String swProtocol = retrieveSimpleObjectFromRepository(protocolFields,
				LODConstants.LOD_SOFTWARE_PROTOCOL_PROPERTY_ID,
				softwareId);
		res = res + swProtocol + ";";
		lodSoftware.setProtocols(swProtocol);

		List<String> programmingLanguageFields = Arrays.asList(LODConstants.FB_SOFTWARE_PROGRAMMING_LANGUAGE, "");
		String swProgrammingLanguage = retrieveSimpleObjectFromRepository(programmingLanguageFields,
				LODConstants.LOD_SOFTWARE_PROGRAMMING_LANGUAGE_PROPERTY_ID,
				softwareId);
		res = res + swProgrammingLanguage + ";";
		lodSoftware.setProgrammingLanguage(swProgrammingLanguage);

		List<String> versionList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_LATEST_VERSION_PROPERTY_ID,
				softwareId, LODConstants.FB_ID,
				LODConstants.FB_SOFTWARE_LATEST_VERSION, "");

		String versionStr = "";
		Iterator<String> versionIter = versionList.iterator();
		while (versionIter.hasNext()) {
			versionStr = versionStr + versionIter.next() + ", ";
		}
		res = res + versionStr + ";";
		lodSoftware.setSoftwareLatestVersion(versionStr);
		
		List<String> latestReleaseDateList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_LATEST_RELEASE_DATE_PROPERTY_ID,
				softwareId, LODConstants.FB_ID,
				LODConstants.FB_SOFTWARE_LATEST_RELEASE_DATE, "");

		String latestReleaseDateStr = "";
		Iterator<String> latestReleaseDateIter = latestReleaseDateList.iterator();
		while (latestReleaseDateIter.hasNext()) {
			latestReleaseDateStr = latestReleaseDateStr + latestReleaseDateIter.next() + ", ";
		}
		res = res + latestReleaseDateStr + ";";
		lodSoftware.setSoftwareReleaseDate(latestReleaseDateStr);
		
		// replace repository IDs like /m/123 by /en/123 due to get GUID number
//		if (softwareId == null || !softwareId.contains(LODConstants.FB_EN_ID)) {
//			softwareId = LODConstants.FB_EN_ID + softwareName.toLowerCase().replaceAll(" ", "_");
//		}
		
		List<String> guidList = LODUtils.searchInFreebase(
				LODConstants.LOD_SOFTWARE_BY_REPOSITORY_ID,
				softwareId, LODConstants.FB_ID,
				LODConstants.FB_GUID, LODConstants.FB_ID);

		List<String> descriptions = new ArrayList<String>();
		String guidStr = "";
		Iterator<String> guidIter = guidList.iterator();
		while (guidIter.hasNext()) {
			guidStr = guidIter.next();
			String descriptionStr = getDescription(guidStr);
			descriptions.add(descriptionStr);
			break;
		}

		if (descriptions.size() > 0) {
			lodSoftware.setDescription(descriptions.get(0));
		}
		lodSoftware.setRepositoryId(softwareId);
		
		if (lodSoftware.getSoftwareReleaseDate().length() == 0) {
			List<String> latestReleaseDateList2 = LODUtils.searchInFreebase(
					LODConstants.LOD_SOFTWARE_BY_REPOSITORY_ID,
					softwareId, LODConstants.FB_ID,
					LODConstants.FB_LATEST_RELEASE_DATE, "");
			String latestReleaseDateStr2 = "";
			Iterator<String> latestReleaseDateIter2 = latestReleaseDateList2.iterator();
			while (latestReleaseDateIter2.hasNext()) {
				latestReleaseDateStr2 = latestReleaseDateStr2 + latestReleaseDateIter2.next() + ", ";
			}
			lodSoftware.setSoftwareReleaseDate(latestReleaseDateStr2);
		}

		if (lodSoftware.getSoftwareLatestVersion().length() == 0) {
			List<String> latestVersionList2 = LODUtils.searchInFreebase(
					LODConstants.LOD_SOFTWARE_BY_REPOSITORY_ID,
					softwareId, LODConstants.FB_ID,
					LODConstants.FB_LATEST_VERSION, "");
			String latestVersionStr2 = "";
			Iterator<String> latestVersionIter2 = latestVersionList2.iterator();
			while (latestVersionIter2.hasNext()) {
				latestVersionStr2 = latestVersionStr2 + latestVersionIter2.next() + ", ";
			}
			lodSoftware.setSoftwareLatestVersion(latestVersionStr2);
		}

		if (lodSoftware.getSoftwareLicense().length() == 0) {
			List<String> licenseList2 = LODUtils.searchInFreebase(
					LODConstants.LOD_SOFTWARE_BY_REPOSITORY_ID,
					softwareId, LODConstants.FB_ID,
					LODConstants.FB_LICENSE, "");
			String licenseStr2 = "";
			Iterator<String> licenseIter2 = licenseList2.iterator();
			while (licenseIter2.hasNext()) {
				licenseStr2 = licenseStr2 + licenseIter2.next() + ", ";
			}
			lodSoftware.setSoftwareLicense(licenseStr2);
		}

		if (lodSoftware.getGenre().length() == 0) {
			List<String> genreList2 = LODUtils.searchInFreebase(
					LODConstants.LOD_SOFTWARE_BY_REPOSITORY_ID,
					softwareId, LODConstants.FB_ID,
					LODConstants.FB_GENRE, "");
			String genreStr2 = "";
			Iterator<String> genreIter2 = genreList2.iterator();
			while (genreIter2.hasNext()) {
				genreStr2 = genreStr2 + genreIter2.next() + ", ";
			}
			lodSoftware.setGenre(genreStr2);
		}

		LODSoftware checkLodSoftware = new LODSoftware();
		checkLodSoftware.setRepository(LODConstants.FREEBASE);
		checkLodSoftware.setRepositoryId(softwareId);
		updateObject(checkLodSoftware, lodSoftware);

		// create DipSoftwareId object
		DipSoftwareId dipSoftwareId = new DipSoftwareId();
		dipSoftwareId.setDipId(LODConstants.DIP + lodSoftware.getSoftwareName());
		dipSoftwareId.setName(lodSoftware.getSoftwareName());
		List<String> softwareIdList = new ArrayList<String>();
		softwareIdList.add(softwareId);
		dipSoftwareId.setFreebaseId(softwareIdList.toArray(new String[]{}));
		List<String> dipFormatIdList = new ArrayList<String>();
		dipFormatIdList.add(LODConstants.DIP + fileFormat);
		dipSoftwareId.setDipFormatId(dipFormatIdList.toArray(new String[]{}));
		List<String> descriptionsList = new ArrayList<String>();
		descriptionsList.add(lodSoftware.getDescription());
		dipSoftwareId.setDescription(descriptionsList.toArray(new String[]{}));

		// add DipSoftwareId link to existing DipFormatId objects
		DipFormatId dipFormatId = new DipFormatId();
		dipFormatId.setDipId(LODConstants.DIP + fileFormat);
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		List<? extends FfmaDomainObject> dipFormats = dataStoreDao
			.retrieveCollection((FfmaDomainObject) dipFormatId, exclusionsList);
		Iterator<? extends FfmaDomainObject> iter = dipFormats.iterator();
		while (iter.hasNext()) {
			DipFormatId dipFormatIdEntry = (DipFormatId) iter.next();
			dipFormatIdEntry.setDipSoftwareId(addValueToStringArray(dipFormatIdEntry.getDipSoftwareId(), softwareId));
			updateObject(dipFormatId, dipFormatIdEntry);
		}

		// update dipSoftwareId collection with new entry
		DipSoftwareId checkDipSoftwareId = new DipSoftwareId();
		checkDipSoftwareId.setDipId(LODConstants.DIP + lodSoftware.getSoftwareName());
		checkDipSoftwareId.setName(lodSoftware.getSoftwareName());
		if (existsInDb(checkDipSoftwareId)) {
			List<? extends FfmaDomainObject> dipSoftwareIdList = dataStoreDao
				.retrieveCollection((FfmaDomainObject) checkDipSoftwareId, exclusionsList);
			Iterator<? extends FfmaDomainObject> iterSoftwarId = dipSoftwareIdList.iterator();
			while (iterSoftwarId.hasNext()) {
				DipSoftwareId dipSoftwareIdEntry = (DipSoftwareId) iterSoftwarId.next();
				dipSoftwareIdEntry.setFreebaseId(softwareIdList.toArray(new String[]{}));
				String formatId = LODConstants.DIP + fileFormat;
				dipSoftwareIdEntry.setDipFormatId(addValueToStringArray(dipSoftwareIdEntry.getDipFormatId(), formatId));
				dipSoftwareIdEntry.setDescription(addValueToStringArray(dipSoftwareIdEntry.getDescription(), lodSoftware.getDescription()));
				updateObject(checkDipSoftwareId, dipSoftwareIdEntry);
			}
		} else {
			try {
				dataStoreDao.storeObject(dipSoftwareId);
			} catch (ObjectNotStoredException e) {
				e.printStackTrace();
			} catch (ObjectNotFoundException e) {
				e.printStackTrace();
			}
		}

		List<String> fields = Arrays.asList(LODConstants.FB_SOFTWARE_DEVELOPER, LODConstants.FB_ID, LODConstants.FB_NAME, LODConstants.FB_GUID);
		List<List<String>> resultList = LODUtils.searchInFreebaseExt(
				LODConstants.LOD_VENDOR_BY_SOFTWARE_PROPERTY_ID,
				softwareId, LODConstants.FB_ID,
				fields);
		/**
		 * print out 
		 */
		Iterator<List<String>> resultIter = resultList.iterator();
		while (resultIter.hasNext()) {
			List<String> subList = resultIter.next();
			Iterator<String> subIter = subList.iterator();
			String tmpVendorName = "";
			String tmpVendorId = "";
			String tmpVendorGuid = "";
			int index = 1;
			while (subIter.hasNext()) {
				String str = subIter.next();
				if (fields.get(index).equals(LODConstants.FB_NAME)) {
					tmpVendorName = str;
					if (tmpVendorName != null && !vendorListCsv.contains(tmpVendorName)) {
						vendorListCsv = vendorListCsv + tmpVendorName + ", ";
					}
				}
				if (fields.get(index).equals(LODConstants.FB_ID)) {
					tmpVendorId = str;
				}
				if (fields.get(index).equals(LODConstants.FB_GUID)) {
					tmpVendorGuid = str;
				}
				index++;
			}
			if (!vendorDescriptionCsv.contains(tmpVendorName)) {
				mapFileFormatToVendor.put(fileFormat, tmpVendorName);
				vendorDescriptionCsv = vendorDescriptionCsv + 
					generateVendorDescriptionColumn(tmpVendorId, tmpVendorName, tmpVendorGuid);
			}
		}
		res = res + "\n";
		return res;
	}
	
	/**
	 * This method generates CSV information for software vendors
	 * @param vendorId
	 * @param vendorName
	 * @return CSV information collected from LOD repository
	 */
	private String generateVendorDescription(String vendorId, String vendorName) {
		String res = "";
		res = "\n\n*** VENDOR DESCRIPTION ***\n\n" + "ORGANISATION NAME: " + vendorName + "\n";
		
		List<String> numOfEmployeesFields = Arrays.asList(
				LODConstants.FB_VENDOR_NUM_OF_EMPLOYEES, LODConstants.FB_NUMBER, LODConstants.FB_YEAR);
		res = res
				+ "NUMBER OF EMPLOYEES: "
				+ retrieveComplexObjectFromRepository(numOfEmployeesFields,
						LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID,
						vendorId) + "\n";

		List<String> businessStatusFields = Arrays.asList(
				LODConstants.FB_VENDOR_REVENUE, LODConstants.FB_AMOUNT,
				LODConstants.FB_CURRENCY, LODConstants.FB_VALID_DATE);
		res = res
				+ "BUSINESS STATUS: "
				+ retrieveComplexObjectFromRepository(businessStatusFields,
						LODConstants.LOD_VENDOR_BUSINESS_STATUS_PROPERTY_ID,
						vendorId) + "\n";

		List<String> currentAsssetsFields = Arrays.asList(
				LODConstants.FB_VENDOR_CURRENT_FFMA, LODConstants.FB_AMOUNT,
				LODConstants.FB_CURRENCY, LODConstants.FB_VALID_DATE);
		res = res
				+ "CURRENT Ffma: "
				+ retrieveComplexObjectFromRepository(currentAsssetsFields,
						LODConstants.LOD_VENDOR_CURRENT_FFMA_PROPERTY_ID,
						vendorId) + "\n";

		List<String> stockIssuesFields = Arrays.asList(
				LODConstants.FB_VENDOR_STOCK_ISSUES, LODConstants.FB_NAME);
		res = res
				+ "STOCK ISSUES: "
				+ retrieveComplexObjectFromRepository(stockIssuesFields,
						LODConstants.LOD_VENDOR_STOCK_ISSUES_PROPERTY_ID,
						vendorId) + "\n";

		List<String> rankedListFields = Arrays.asList(
				LODConstants.FB_VENDOR_RANKED_LIST, LODConstants.FB_LIST,
				LODConstants.FB_RANK, LODConstants.FB_YEAR);
		res = res
				+ "RANKED LIST: "
				+ retrieveComplexObjectFromRepository(rankedListFields,
						LODConstants.LOD_VENDOR_RANKED_LIST_PROPERTY_ID,
						vendorId) + "\n";

		res = res + "COUNTRY: " + "\n";
		List<String> foundationDateFields = Arrays.asList(LODConstants.FB_VENDOR_FOUNDATION_DATE, "");
		res = res + "FOUNDATION DATE: " + retrieveSimpleObjectFromRepository(foundationDateFields,
				LODConstants.LOD_VENDOR_FOUNDATION_DATE_PROPERTY_ID,
				vendorId) + "\n";

		res = res + "HOMEPAGE: " + "\n";
		return res;
	}
	
	/**
	 * This method generates CSV information for software vendors
	 * @param vendorId
	 * @param vendorName
	 * @param vendorGuid
	 * @return CSV information collected from LOD repository
	 */
	private String generateVendorDescriptionColumn(String vendorId, String vendorName, String vendorGuid) {
		String res = "";
		res = vendorName + ";";
		LODVendor lodVendor = new LODVendor();
		lodVendor.setRepository(LODConstants.FREEBASE);
		lodVendor.setOrganisationName(vendorName);

		List<String> numOfEmployeesFields = Arrays.asList(
				LODConstants.FB_VENDOR_NUM_OF_EMPLOYEES, LODConstants.FB_NUMBER, LODConstants.FB_YEAR);
		String numOfEmployees = retrieveComplexObjectFromRepositoryInRow(numOfEmployeesFields,
				LODConstants.LOD_VENDOR_NUM_OF_EMPLOYEES_PROPERTY_ID,
				vendorId);
		res = res + numOfEmployees + ";";
		try {
			if (numOfEmployees != null && numOfEmployees.length() > 0) {
				lodVendor.setNumberOfEmployees(Integer.parseInt(numOfEmployees));
			}
		} catch (Exception e) {
			log.info("number of employees not numeral value error: " + e);
		}

		List<String> businessStatusFields = Arrays.asList(
				LODConstants.FB_VENDOR_REVENUE, LODConstants.FB_AMOUNT,
				LODConstants.FB_CURRENCY, LODConstants.FB_VALID_DATE);
		String businessStatus = retrieveComplexObjectFromRepositoryInRow(businessStatusFields,
				LODConstants.LOD_VENDOR_BUSINESS_STATUS_PROPERTY_ID,
				vendorId);
		res = res + businessStatus + ";";
		lodVendor.setBusinessStatus(businessStatus);

		List<String> currentAsssetsFields = Arrays.asList(
				LODConstants.FB_VENDOR_CURRENT_FFMA, LODConstants.FB_AMOUNT,
				LODConstants.FB_CURRENCY, LODConstants.FB_VALID_DATE);
		String currentFfma = retrieveComplexObjectFromRepositoryInRow(currentAsssetsFields,
				LODConstants.LOD_VENDOR_CURRENT_FFMA_PROPERTY_ID,
				vendorId);
		res = res + currentFfma + ";";
		lodVendor.setCurrentFfma(currentFfma);

		List<String> stockIssuesFields = Arrays.asList(
				LODConstants.FB_VENDOR_STOCK_ISSUES, LODConstants.FB_NAME);
		String stockIssues = retrieveComplexObjectFromRepositoryInRow(stockIssuesFields,
				LODConstants.LOD_VENDOR_STOCK_ISSUES_PROPERTY_ID,
				vendorId);
		res = res + stockIssues + ";";
		lodVendor.setStockIssues(stockIssues);

		List<String> rankedListFields = Arrays.asList(
				LODConstants.FB_VENDOR_RANKED_LIST, LODConstants.FB_LIST,
				LODConstants.FB_RANK, LODConstants.FB_YEAR);
		String rankedList = retrieveComplexObjectFromRepositoryInRow(rankedListFields,
				LODConstants.LOD_VENDOR_RANKED_LIST_PROPERTY_ID,
				vendorId);
		res = res + rankedList + ";";
		lodVendor.setRankedList(rankedList);

		res = res + ";"; // country
		List<String> foundationDateFields = Arrays.asList(LODConstants.FB_VENDOR_FOUNDATION_DATE, "");
		String foundationDate = retrieveSimpleObjectFromRepository(foundationDateFields,
				LODConstants.LOD_VENDOR_FOUNDATION_DATE_PROPERTY_ID,
				vendorId);
		res = res + foundationDate + ";";
		lodVendor.setFoundationDate(foundationDate);
		
		List<String> descriptions = new ArrayList<String>();
		descriptions.add(getDescription(vendorGuid));

		if (descriptions.size() > 0) {
			lodVendor.setDescription(descriptions.get(0));
		}
		lodVendor.setRepositoryId(vendorId);

		LODVendor checkLodVendor = new LODVendor();
		checkLodVendor.setRepository(LODConstants.FREEBASE);
		checkLodVendor.setOrganisationName(vendorName);
		updateObject(checkLodVendor, lodVendor);

		res = res + ";"; // homepage
		res = res + "\n"; 
		return res;
	}
	
	/**
	 * This method retrieves encapsulated information from complex LOD object.
	 * @param fields
	 *        The field names in JSON object
	 * @param propertyId
	 * @param repositoryId
	 * @return CSV information for one JSON object
	 */
	public String retrieveComplexObjectFromRepository(List<String> fields, String propertyId, String repositoryId) {
		String res = "";
		LODUtils.initCalculationModel();
		List<List<String>> resultList = LODUtils.searchInFreebaseExt(
				propertyId,
				repositoryId, LODConstants.FB_ID,
				fields);
		/**
		 * print out 
		 */
		Iterator<List<String>> resultIter = resultList.iterator();
		while (resultIter.hasNext()) {
			List<String> subList = resultIter.next();
			Iterator<String> subIter = subList.iterator();
			int index = 1;
			while (subIter.hasNext()) {
				String str = subIter.next();
				res = res + fields.get(index) + ": " + str + ", ";
				index++;
			}
			res = res + "\n";
		}
		return res;
	}
	
	/**
	 * This method retrieves encapsulated information from complex LOD object.
	 * @param fields
	 *        The field names in JSON object
	 * @param propertyId
	 * @param repositoryId
	 * @return CSV information for one JSON object
	 */
	public String retrieveComplexObjectFromRepositoryInRow(List<String> fields, String propertyId, String repositoryId) {
		String res = "";
		LODUtils.initCalculationModel();
		List<List<String>> resultList = LODUtils.searchInFreebaseExt(
				propertyId,
				repositoryId, LODConstants.FB_ID,
				fields);
		/**
		 * print out 
		 */
		Iterator<List<String>> resultIter = resultList.iterator();
		while (resultIter.hasNext()) {
			List<String> subList = resultIter.next();
			Iterator<String> subIter = subList.iterator();
			int index = 1;
			while (subIter.hasNext()) {
				String str = subIter.next();
				res = res + fields.get(index) + ": " + str + ", ";
				index++;
			}
			res = res + "#";
		}
		return res;
	}
	
	/**
	 * This method retrieves information (single list) from LOD object.
	 * @param fields
	 *        The field names in JSON object
	 * @param propertyId
	 * @param repositoryId
	 * @return CSV information for one JSON object
	 */
	public String retrieveSimpleObjectFromRepository(List<String> fields, String propertyId, String repositoryId) {
		List<String> strList = LODUtils.searchInFreebase(
				propertyId, repositoryId, LODConstants.FB_ID,
				fields.get(0), fields.get(1));

		String res = "";
		Iterator<String> strIter = strList.iterator();
		while (strIter.hasNext()) {
			res = res + strIter.next() + ", ";		
		}
//		res = res + "\n";
		return res;
	}

	//@Test
	public void removeRepositoryData() throws MalformedURLException, URISyntaxException {
		log.info("This test removes file format information for linked open data repository FREEBASE from database.");
		try {
			initDataStoreDao();
			// clean up old version of LOD format, software and vendor collections
			LODFormat lodFormatTmp = new LODFormat();
			lodFormatTmp.setRepository(LODConstants.FREEBASE);
			LODSoftware lodSoftwareTmp = new LODSoftware();
			lodSoftwareTmp.setRepository(LODConstants.FREEBASE);
			LODVendor lodVendorTmp = new LODVendor();
			lodVendorTmp.setRepository(LODConstants.FREEBASE);
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
		log.info("This test removes vendors collection for linked open data repository Freebase from database.");
		try {
			initDataStoreDao();
			LODVendor lodVendorTmp = new LODVendor();
			lodVendorTmp.setRepository(LODConstants.FREEBASE);
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
