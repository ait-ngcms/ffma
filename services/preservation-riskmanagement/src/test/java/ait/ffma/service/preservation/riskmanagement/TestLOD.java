package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

import ait.ffma.common.dao.DataStoreDao;
import ait.ffma.common.dao.DataStoreDaoImpl;
import ait.ffma.common.dao.mongodb.MongoDbManager;
import ait.ffma.domain.preservation.riskmanagement.AitFileFormat;
import ait.ffma.domain.preservation.riskmanagement.DBPediaFileFormat;
import ait.ffma.domain.preservation.riskmanagement.DipFormatId;
import ait.ffma.domain.preservation.riskmanagement.FreebaseFileFormat;
import ait.ffma.domain.preservation.riskmanagement.LODFormat;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.domain.preservation.riskmanagement.LODVendor;
import ait.ffma.domain.preservation.riskmanagement.PronomFileFormat;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODCreator;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODReportGenerator;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODStatisticsAitUtils;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODStatisticsDBPediaUtils;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODStatisticsFreebaseUtils;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODStatisticsPronomUtils;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODStatisticsUtils;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODUtils;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperties;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperty;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODSource;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.ObjectFactory;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ReportConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;
import ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDaoImpl;

public class TestLOD {

	private final static String TEST_COLLECTION_ID = "10";
	private final static String PDF = "pdf";
	private final static String VALUE = "value";
	private final static String FB_VENDOR = "/en/microsoft";
	
	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());
	
	/**
	 * Test objects.
	 */
	private FreebaseFileFormat freebaseFileFormat = new FreebaseFileFormat();
	private PronomFileFormat pronomFileFormat = new PronomFileFormat();
	
	/**
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void databaseFunctionality() throws MalformedURLException, URISyntaxException {
		DataStoreDaoImpl dataStoreDao = new DataStoreDaoImpl();
		dataStoreDao.setMongoDbManager(new MongoDbManager());
		PreservationRiskmanagementDaoImpl preservationRiskmanagementDao = new PreservationRiskmanagementDaoImpl();
		preservationRiskmanagementDao.setDataStoreDao(dataStoreDao);
		DataStoreDao dataStoreDaoRes = preservationRiskmanagementDao.getDataStoreDao();
		assertNotNull("The resulting object should not be null", dataStoreDaoRes);
	}

	/**
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void freebaseUtils() throws MalformedURLException, URISyntaxException {
		freebaseFileFormat.setFfmaObjectName(FreebaseFileFormat.class.getSimpleName());
		freebaseFileFormat.setExtension(PDF);
		@SuppressWarnings("unchecked")
		List<FreebaseFileFormat> fileFormatsList = new ArrayList();
		fileFormatsList.add(freebaseFileFormat);
		List<DipFormatId> res = LODStatisticsFreebaseUtils.getFreebaseDipFileFormatsList(fileFormatsList);
		assertTrue("The size of resulting object should be > 0", res.size() > 0);

		LODUtils.initCalculationModel();

		LODVendor lodVendor = new LODVendor();
		LODStatisticsFreebaseUtils.setFreebaseLodVendorSoftwareName(VALUE, lodVendor);
		assertTrue("The resulting value must be a test value", lodVendor.getSoftwareName().equals(VALUE));
		LODStatisticsFreebaseUtils.setFreebaseLodVendorFileFormat(VALUE, lodVendor);
		assertTrue("The resulting value must be a test value", lodVendor.getFileFormat()[0].equals(VALUE));
		LODStatisticsFreebaseUtils.setFreebaseLodVendorDescriptionAndId(TEST_COLLECTION_ID, VALUE, lodVendor);
		assertTrue("The resulting value must be a test value", lodVendor.getRepositoryId().equals(TEST_COLLECTION_ID));
		LODStatisticsFreebaseUtils.setFreebaseLodVendorFoundationDate(FB_VENDOR, ReportConstants.EMPTYSTRING, lodVendor);
		assertNotNull("The resulting value must be a test value", lodVendor.getFoundationDate());
		LODStatisticsFreebaseUtils.setFreebaseLodVendorRankedList(FB_VENDOR, ReportConstants.EMPTYSTRING, lodVendor);
		assertNotNull("The resulting value must be a test value", lodVendor.getRankedList());
		LODStatisticsFreebaseUtils.setFreebaseLodVendorStockIssues(FB_VENDOR, ReportConstants.EMPTYSTRING, lodVendor);
		assertNotNull("The resulting value must be a test value", lodVendor.getStockIssues());
		LODStatisticsFreebaseUtils.setFreebaseLodVendorCurrentFfma(FB_VENDOR, ReportConstants.EMPTYSTRING, lodVendor);
		assertNotNull("The resulting value must be a test value", lodVendor.getCurrentFfma());
		LODStatisticsFreebaseUtils.setFreebaseLodVendorBusinessStatus(FB_VENDOR, ReportConstants.EMPTYSTRING, lodVendor);
		assertNotNull("The resulting value must be a test value", lodVendor.getBusinessStatus());
		LODStatisticsFreebaseUtils.setFreebaseLodVendorNumOfEmployees(FB_VENDOR, ReportConstants.EMPTYSTRING, lodVendor);
		assertNull("The resulting value must be a test value", lodVendor.getNumberOfEmployees());

		LODSoftware lodSoftware = new LODSoftware();
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareFileFormat(VALUE, lodSoftware);
		assertTrue("The resulting value must be a test value", lodSoftware.getFileFormat()[0].equals(VALUE));
		lodSoftware.setGenre(ReportConstants.EMPTYSTRING);
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareRepGenre(VALUE, lodSoftware);
		assertTrue("The resulting value must be a test value", lodSoftware.getGenre().equals(ReportConstants.EMPTYSTRING));
		lodSoftware.setSoftwareLicense(ReportConstants.EMPTYSTRING);
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareSwLicense(VALUE, lodSoftware);
		assertTrue("The resulting value must be a test value", lodSoftware.getSoftwareLicense().equals(ReportConstants.EMPTYSTRING));
		lodSoftware.setSoftwareLatestVersion(ReportConstants.EMPTYSTRING);
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareLatestVersion(VALUE, lodSoftware);
		assertTrue("The resulting value must be a test value", lodSoftware.getSoftwareLatestVersion().equals(ReportConstants.EMPTYSTRING));
		lodSoftware.setSoftwareReleaseDate(ReportConstants.EMPTYSTRING);
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareLatestRelease(VALUE, lodSoftware);
		assertTrue("The resulting value must be a test value", lodSoftware.getSoftwareReleaseDate().equals(ReportConstants.EMPTYSTRING));
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareDescriptionAndId(VALUE, lodSoftware);
		assertTrue("The resulting value must be a test value", lodSoftware.getRepositoryId().equals(VALUE));
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareReleaseDate(VALUE, ReportConstants.EMPTYSTRING, lodSoftware);
		assertNotNull("The resulting value must be a test value", lodSoftware.getSoftwareReleaseDate());
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareVersion(VALUE, ReportConstants.EMPTYSTRING, lodSoftware);
		assertNotNull("The resulting value must be a test value", lodSoftware.getSoftwareLatestVersion());
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareProgrammingLanguage(VALUE, ReportConstants.EMPTYSTRING, lodSoftware);
		assertNotNull("The resulting value must be a test value", lodSoftware.getProgrammingLanguage());
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareProtocol(VALUE, ReportConstants.EMPTYSTRING, lodSoftware);
		assertNotNull("The resulting value must be a test value", lodSoftware.getProtocols());
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareOs(VALUE, ReportConstants.EMPTYSTRING, lodSoftware);
		assertNotNull("The resulting value must be a test value", lodSoftware.getOperatingSystem());
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareGenre(VALUE, ReportConstants.EMPTYSTRING, lodSoftware);
		assertNotNull("The resulting value must be a test value", lodSoftware.getGenre());
		LODStatisticsFreebaseUtils.setFreebaseLodSoftwareLicense(VALUE, ReportConstants.EMPTYSTRING, lodSoftware);
		assertNotNull("The resulting value must be a test value", lodSoftware.getSoftwareLicense());
	}

	/**
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void lodCreator() throws MalformedURLException, URISyntaxException {
		LODUtils.initCalculationModel();

		LODCreator lodCreator = new LODCreator();
		LODReportGenerator lodReportGenerator = new LODReportGenerator();
		DataStoreDaoImpl dataStoreDao = new DataStoreDaoImpl();
		dataStoreDao.setMongoDbManager(new MongoDbManager());
		PreservationRiskmanagementDaoImpl preservationRiskmanagementDao = new PreservationRiskmanagementDaoImpl();
		preservationRiskmanagementDao.setDataStoreDao(dataStoreDao);
		lodCreator.setPreservationRiskmanagementDao(preservationRiskmanagementDao);
		lodReportGenerator.setPreservationRiskmanagementDao(preservationRiskmanagementDao);
		lodCreator.checkLodData();
		LODSoftware lodSoftware = new LODSoftware();
		String res = lodCreator.checkCollection(lodSoftware);
		assertNotNull("The resulting value must be a test value", res);
		try {
			res = lodCreator.storeAllExtensions(LODConstants.LODRepositories.AIT.name(), true, true);
	    } catch (NullPointerException expected) {
			log.info("Expected NullPointerException because db object is null: " + expected);
	    }
		res = lodReportGenerator.retrievePreservationStatistic(
					LODConstants.PreservationStatisticsTypes.SoftwareAndVendorsForFormat.name(), PDF);
		assertNotNull("The resulting value must be a test value", res);
		res = lodReportGenerator.retrieveSoftware(PDF);
		assertNotNull("The resulting value must be a test value", res);
		res = lodReportGenerator.retrieveVendor(PDF);
		assertNotNull("The resulting value must be a test value", res);
		res = lodReportGenerator.retrieveDipFormatId(PDF);
		assertNotNull("The resulting value must be a test value", res);
	}
	
	/**
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void objectFactory() throws MalformedURLException, URISyntaxException {
	    ObjectFactory objectFactory = new ObjectFactory();
	    LODProperties lodProperties = objectFactory.createLODProperties();
		assertNotNull("The resulting value must be a test value", lodProperties);
	    LODProperty.Value lodPropertyValue = objectFactory.createLODPropertyValue();
		assertNotNull("The resulting value must be a test value", lodPropertyValue);
	    LODSource lodSource = objectFactory.createLODSource();
		assertNotNull("The resulting value must be a test value", lodSource);
	    LODProperty.LODSources lodPropertySources = objectFactory.createLODPropertyLODSources();
		assertNotNull("The resulting value must be a test value", lodPropertySources);
	    LODProperty lodProperty = objectFactory.createLODProperty();
		assertNotNull("The resulting value must be a test value", lodProperty);
	    LODSource.OriginalValue lodSourceOriginalValue = objectFactory.createLODSourceOriginalValue();
		assertNotNull("The resulting value must be a test value", lodSourceOriginalValue);
	}
	
	/**
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void pronomUtils() throws MalformedURLException, URISyntaxException {
		LODUtils.initCalculationModel();
		pronomFileFormat.setFfmaObjectName(PronomFileFormat.class.getSimpleName());
		pronomFileFormat.setExtension(PDF);
		@SuppressWarnings("unchecked")
		List<PronomFileFormat> fileFormatsList = new ArrayList();
		fileFormatsList.add(pronomFileFormat);
		String nameStr = LODStatisticsPronomUtils.getStringFromPronomList(PDF, LODConstants.PRONOM_NAME);
		assertNotNull("The resulting value must be a test value", nameStr);
		String verStr = LODStatisticsPronomUtils.getStringFromPronomList(PDF, LODConstants.PRONOM_VER);	
		assertNotNull("The resulting value must be a test value", verStr);
		List<String> exclusions = new ArrayList<String>();
		exclusions.add(LODConstants.EMPTY_PUID);
		String puidStr = LODStatisticsPronomUtils.getStringFromPronomListExt(PDF, LODConstants.PRONOM_PUID, exclusions);
		assertNotNull("The resulting value must be a test value", puidStr);
//		int softwareCount = LODStatisticsPronomUtils.retrievePronomSoftwareIdsAndNames(PDF, null, 0, null, null, null);
//		assertTrue("The resulting value must be a test value", softwareCount == 0);
	}

	@Test
	public void aitUtils() throws MalformedURLException, URISyntaxException {
		LODUtils.initCalculationModel();
		List<String> formatsRows = LODStatisticsAitUtils.retrieveRows(LODConstants.CSV_FILE_FORMAT_HEADER);
		List<String> softwareRows = LODStatisticsAitUtils.retrieveRows(LODConstants.CSV_SOFTWARE_HEADER);
		assertNotNull("The resulting value must be a test value", formatsRows);
		softwareRows = LODStatisticsAitUtils.retrieveAitFileFormats(softwareRows,
				LODConstants.FormatCsvTypes.Software.name(), "Microsoft");
		assertNotNull("The resulting value must be a test value", softwareRows);
		List<LODFormat> tmpLodFormatList = new ArrayList<LODFormat>();
		int formatsCount = 0;
		LODStatisticsAitUtils.retrieveAitFormats(tmpLodFormatList, formatsCount, formatsRows);
		assertTrue("The resulting value must be a test value", tmpLodFormatList.size() > 0);
		LODFormat lodFormat = new LODFormat();
		String[] vendorNames = {VALUE};
		lodFormat.setVendorId(vendorNames);
		lodFormat.setVendorName(vendorNames);
		lodFormat.setSoftwareId(vendorNames);
		lodFormat.setSoftwareName(vendorNames);
		lodFormat.setSoftwareCount(0);
		lodFormat.setCurrentFormatVersion(VALUE);
		lodFormat.setRepositoryId(vendorNames);
		lodFormat.setFormatName(VALUE);
		lodFormat.setOpenFormat(VALUE);
		lodFormat.setFormatCreator(VALUE);
		lodFormat.setFormatGenre(VALUE);
		lodFormat.setFormatHomepage(VALUE);
		lodFormat.setLimitations(VALUE);
		lodFormat.setFormatLicense(VALUE);
		LODFormat lodFormatTmp = new LODFormat();
		LODStatisticsAitUtils.getAitFormatFileExtensions(tmpLodFormatList, lodFormat);
		LODStatisticsAitUtils.getAitLodFormatVendorIds(lodFormat, lodFormatTmp);
		assertTrue("The resulting value must be a test value", lodFormatTmp.getVendorId()[0].equals(VALUE));
		LODStatisticsAitUtils.getAitLodFormatVendorNames(lodFormat, lodFormatTmp);
		assertTrue("The resulting value must be a test value", lodFormatTmp.getVendorName()[0].equals(VALUE));
		LODStatisticsAitUtils.getAitLodFormatSoftwareIds(lodFormat, lodFormatTmp);
		assertTrue("The resulting value must be a test value", lodFormatTmp.getSoftwareId()[0].equals(VALUE));
		LODStatisticsAitUtils.getAitLodFormatSoftwareName(lodFormat, lodFormatTmp);
		assertTrue("The resulting value must be a test value", lodFormatTmp.getSoftwareName()[0].equals(VALUE));
		LODStatisticsAitUtils.getAitLodFormatSoftwareCount(lodFormat, lodFormatTmp);
		assertTrue("The resulting value must be a test value", lodFormatTmp.getSoftwareCount() == 0);
		LODStatisticsAitUtils.getAitLodFormatVersion(lodFormat, lodFormatTmp);
		assertTrue("The resulting value must be a test value", lodFormatTmp.getCurrentFormatVersion().equals(VALUE));
		LODStatisticsAitUtils.getAitLodFormatRepositoryId(lodFormat, lodFormatTmp);
		assertTrue("The resulting value must be a test value", lodFormatTmp.getRepositoryId()[0].equals(VALUE));
		LODStatisticsAitUtils.getAitLodFormatName(lodFormat, lodFormatTmp);
		assertTrue("The resulting value must be a test value", lodFormatTmp.getFormatName().equals(VALUE));
		List<String> formatsStr = Arrays.asList(formatsRows.get(0).split(ReportConstants.SEMICOLON));
		LODStatisticsAitUtils.getAitFormatSoftwareIdsAndNames(formatsStr, lodFormatTmp);
		assertTrue("The resulting value must be a test value", lodFormatTmp.getVendorId().length > 0);
		LODStatisticsAitUtils.getAitFormatOpenFormat(formatsStr, lodFormatTmp);
//		assertTrue("The resulting value must be a test value", lodFormatTmp.getOpenFormat().equals(VALUE));
		LODStatisticsAitUtils.getAitFormatCreator(formatsStr, lodFormatTmp);
//		assertTrue("The resulting value must be a test value", lodFormatTmp.getFormatCreator().equals(VALUE));
		LODStatisticsAitUtils.getAitFormatGenre(formatsStr, lodFormatTmp);
//		assertTrue("The resulting value must be a test value", lodFormatTmp.getFormatGenre().equals(VALUE));
		LODStatisticsAitUtils.getAitFormatHomepage(formatsStr, lodFormatTmp);
//		assertTrue("The resulting value must be a test value", lodFormatTmp.getFormatHomepage().equals(VALUE));
		LODStatisticsAitUtils.getAitFormatLimitations(formatsStr, lodFormatTmp);
//		assertTrue("The resulting value must be a test value", lodFormatTmp.getLimitations().equals(VALUE));
		LODStatisticsAitUtils.getAitFormatLicense(formatsStr, lodFormatTmp);
//		assertTrue("The resulting value must be a test value", lodFormatTmp.getFormatLicense().equals(VALUE));
		LODStatisticsAitUtils.getAitFormatVendorIdsAndNames(formatsStr, lodFormatTmp);
		assertTrue("The resulting value must be a test value", lodFormatTmp.getVendorId().length > 0);
	}

	/**
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void dbpediaUtils() throws MalformedURLException, URISyntaxException {
		LODUtils.initCalculationModel();
		List<String> formatsRows = LODStatisticsDBPediaUtils.getDBPediaDescriptionsList(PDF);
		assertNotNull("The resulting value must be a test value", formatsRows);
		log.info("DBPedia formats size: " + formatsRows.size());
		assertTrue("The resulting value must be a test value", formatsRows.size() > 0);
	}
	
	/**
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void lodStatisticsUtils() throws MalformedURLException, URISyntaxException {
		LODUtils.initCalculationModel();
		List<String> exclusionsList = new ArrayList<String>();
		exclusionsList.add(RiskConstants.DOMAIN_OBJECT_NAME);
		String res = ReportConstants.EMPTYSTRING;
		LODCreator lodCreator = new LODCreator();
		res = lodCreator.overwriteFreebaseRepositoryFormats(LODConstants.LODRepositories.Freebase.name(), true, res);
		res = lodCreator.overwritePronomRepositoryFormats(LODConstants.LODRepositories.Pronom.name(), true, res);
		res = lodCreator.overwriteDBPediaRepositoryFormats(LODConstants.LODRepositories.DBPedia.name(), true, res);
		res = lodCreator.overwriteAitRepositoryFormats(LODConstants.LODRepositories.AIT.name(), true, res);
		log.info("dipFormatId list size: " + LODStatisticsUtils.getDipFormatIdList());
		assertNotNull("The resulting value must be a test value", LODStatisticsUtils.getDipFormatIdList());
		// fill DipFormatId, DipSoftwareId and DipVendorId collections
		AitFileFormat aitFileFormat = new AitFileFormat();
		PronomFileFormat pronomFileFormat = new PronomFileFormat();
		DBPediaFileFormat dbpediaFileFormat = new DBPediaFileFormat();
		FreebaseFileFormat freebaseFileFormat = new FreebaseFileFormat();
		try {
			lodCreator.retrieveAitFormats(LODConstants.LODRepositories.AIT.name(), aitFileFormat, exclusionsList);
	    } catch (NullPointerException expected) {
			log.info("Expected NullPointerException because db object is null" + expected);
	    }
	    try {
	    	lodCreator.retrievePronomFormats(LODConstants.LODRepositories.Pronom.name(), pronomFileFormat, exclusionsList);
	    } catch (NullPointerException expected) {
			log.info("Expected NullPointerException because db object is null" + expected);
	    }
	    try {
	    	lodCreator.retrieveDBPediaFormats(LODConstants.LODRepositories.DBPedia.name(), dbpediaFileFormat, exclusionsList);
	    } catch (NullPointerException expected) {
			log.info("Expected NullPointerException because db object is null" + expected);
	    }
	    try {
	    	lodCreator.retrieveFreebaseFormats(LODConstants.LODRepositories.Freebase.name(), freebaseFileFormat, exclusionsList);
	    } catch (NullPointerException expected) {
			log.info("Expected NullPointerException because db object is null" + expected);
	    }
		LODStatisticsUtils.searchAllFileFormatsInAIT();
		LODStatisticsUtils.searchFreebaseFileFormatInformation();
//		List<String> formatsRows = LODStatisticsUtils.initFileFormatListFromAllRepositories();
		assertNotNull("The resulting value must be a test value", LODStatisticsUtils.getVendorListCsv());
	}
	
}
