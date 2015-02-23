package ait.ffma.preservation.riskmanagement.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ait.ffma.common.exception.client.FfmaClientException;
import ait.ffma.domain.preservation.riskmanagement.LODSoftware;
import ait.ffma.preservation.riskmanagement.PreservationRiskmanagementClientConfiguration;
import ait.ffma.preservation.riskmanagement.PreservationRiskmanagementLodDataAnalysis;

/**
 * This class provides LOD statistics evaluated from database.
 */
@ContextConfiguration(locations = { "/Ffma-preservation-riskmanagement-client-application-context.xml", 
		                            "/common-client-application-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestLODAnalysis {
	
	private static String TEST_FILE_EXTENSION = "pdf";
	private static String NOT_EXISTING_FILE_EXTENSION = "ooo";
	private static String NOT_EXISTING_TYPE = "None";
	private static String AVI_FILE_EXTENSION = "avi";
	private static String BMP_FILE_EXTENSION = "bmp";
	private static String PDF_FILE_EXTENSION = "pdf";
	
	@Autowired
	PreservationRiskmanagementLodDataAnalysis preservationRiskmanagementLodAnalysis;
	
	/**
	 * This is a configuration of client manager.
	 */
	@Autowired
	PreservationRiskmanagementClientConfiguration configuration;

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void testLodStatistics() throws FfmaClientException {
		log.info("test LOD data statistics using wrong or not existing parameter");
	}

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void checkLodData() throws FfmaClientException {
		log.info("check LOD data existence in database");
		String report = preservationRiskmanagementLodAnalysis.checkLodData();
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
//		String resStr = "PronomFileFormat;DBPediaFileFormat;FreebaseFileFormat;LODFormat;LODSoftware;LODVendor;DipFormatId;DipSoftwareId;DipVendorId;";
//		assertTrue("All defined collections are required", report.equals(resStr));	
		assertTrue("All defined collections are required", report.length() > 0);			
	}

	/**
	 * In this test FREEBASE repository is tested with overwriting collections in database.
	 * @throws FfmaClientException
	 */
//	@Test
	public void storeAllExtensions() throws FfmaClientException {
		log.info("retrieve preservation LOD preservation rich data from LOD repositories and store it in database.");
		String report = preservationRiskmanagementLodAnalysis
				.storeAllExtensions(
						"DBPedia", true,
//						"Freebase", true,
//						"AIT", true,
//						"Pronom", true,
						true);
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * In this test all repositories are tested with overwriting repository formats collections in database
	 * but without overwriting LOD data collections.
	 * @throws FfmaClientException
	 */
//	@Test
	public void storeAllExtensionsForAllRepositoriesWithoutLodOverwriting() throws FfmaClientException {
		log.info("retrieve preservation LOD preservation rich data from LOD repositories and store it in database.");
		String report = preservationRiskmanagementLodAnalysis
				.storeAllExtensions(
						"All", true,
						false);
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * In this test FREEBASE repository is tested without overwriting collections in database.
	 * @throws FfmaClientException
	 */
//	@Test
	public void storeAllExtensionsWithoutOverwriting() throws FfmaClientException {
		log.info("retrieve preservation LOD preservation rich data from LOD repositories and store it in database.");
		String report = preservationRiskmanagementLodAnalysis
				.storeAllExtensions(
						"Freebase", false,
						false);
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * In this test DBPedia repository is tested with overwriting collections in database.
	 * @throws FfmaClientException
	 */
//	@Test
	public void storeAllExtensionsDBPedia() throws FfmaClientException {
		log.info("retrieve preservation LOD preservation rich data from LOD repositories and store it in database.");
		String report = preservationRiskmanagementLodAnalysis
				.storeAllExtensions(
						"DBPedia", true,
						true);
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * In this test Pronom repository is tested with overwriting collections in database.
	 * @throws FfmaClientException
	 */
//	@Test
	public void storeAllExtensionsPronom() throws FfmaClientException {
		log.info("retrieve preservation LOD preservation rich data from LOD repositories and store it in database.");
		String report = preservationRiskmanagementLodAnalysis
				.storeAllExtensions(
						"Pronom", true,
						true);
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * In this test AIT repository is tested with overwriting collections in database.
	 * @throws FfmaClientException
	 */
//	@Test
	public void storeAllExtensionsAIT() throws FfmaClientException {
		log.info("retrieve preservation LOD preservation rich data from LOD repositories and store it in database.");
		String report = preservationRiskmanagementLodAnalysis
				.storeAllExtensions(
						"AIT", true,
						true);
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void retrieveSoftware() throws FfmaClientException {
		log.info("retrieve software for passed file extension");
		String report = preservationRiskmanagementLodAnalysis.retrieveSoftware(TEST_FILE_EXTENSION);
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void retrieveVendor() throws FfmaClientException {
		log.info("retrieve vendors for passed file extension");
		String report = preservationRiskmanagementLodAnalysis.retrieveVendor(TEST_FILE_EXTENSION);
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void retrievePreservationStatistic() throws FfmaClientException {
		log.info("retrieve preservation statistics of different types for passed file extension");
		String report = preservationRiskmanagementLodAnalysis
				.retrievePreservationStatistic(
						"All",
						TEST_FILE_EXTENSION);
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void retrievePreservationStatisticWithNotExistingExtension() throws FfmaClientException {
		log.info("retrieve preservation statistics of different types for passed not existing file extension");
		String report = preservationRiskmanagementLodAnalysis
			.retrievePreservationStatistic(
					"SoftwareAndVendorsForFormat",
					NOT_EXISTING_FILE_EXTENSION);
		log.info("report: " + report);
		assertTrue("Retrieved statistic should be empty", report.length() < 3);
	}

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void retrievePreservationStatisticWithNotExistingType() throws FfmaClientException {
		log.info("retrieve preservation statistics of different types for passed file extension and not existing type");
		String report = preservationRiskmanagementLodAnalysis
			.retrievePreservationStatistic(
					NOT_EXISTING_TYPE,
					TEST_FILE_EXTENSION);
		log.info("report: " + report);
		assertTrue("Retrieved statistic should be empty", report.length() < 3);
	}

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void retrievePreservationStatisticReferencesToLodRepositories() throws FfmaClientException {
		log.info("retrieve preservation statistics of ReferencesToLodRepositories type for passed file extension");
		String report = preservationRiskmanagementLodAnalysis
				.retrievePreservationStatistic(
						"ReferencesToLodRepositories",
						TEST_FILE_EXTENSION);
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void retrievePreservationStatisticTextualFormatDescriptions() throws FfmaClientException {
		log.info("retrieve preservation statistics of TextualFormatDescriptions type for passed file extension");
		String report = preservationRiskmanagementLodAnalysis
				.retrievePreservationStatistic(
						"TextualFormatDescriptions",
						TEST_FILE_EXTENSION);
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void retrievePreservationStatisticSoftwareAndVendorsForFormat() throws FfmaClientException {
		log.info("retrieve preservation statistics of SoftwareAndVendorsForFormat type for passed file extension");
		String report = preservationRiskmanagementLodAnalysis
				.retrievePreservationStatistic(
						"SoftwareAndVendorsForFormat",
						TEST_FILE_EXTENSION);
		log.info("report: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void retrieveSoftwareFromAitRepository() throws FfmaClientException {
		log.info("retrieve software for passed file extension from AIT repository");
		String report = preservationRiskmanagementLodAnalysis.retrieveSoftware(PDF_FILE_EXTENSION);
		log.info("report PDF: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
		report = preservationRiskmanagementLodAnalysis.retrieveSoftware(BMP_FILE_EXTENSION);
		log.info("report BMP: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
		report = preservationRiskmanagementLodAnalysis.retrieveSoftware(AVI_FILE_EXTENSION);
		log.info("report AVI: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void retrieveSoftwareFromAllRepositories() throws FfmaClientException {
		log.info("retrieve software for passed file extension from all LOD repositories and" +
				"present readable results");
		String report = preservationRiskmanagementLodAnalysis.retrieveSoftware(BMP_FILE_EXTENSION);
		List<String> softwareObjList = Arrays.asList(report.split("###"));
		String res = "report BMP: " + softwareObjList.size() + " results.\n";
		Iterator<String> swObjIter = softwareObjList.iterator();
		while (swObjIter.hasNext()) {
			String swObjStr = swObjIter.next();
			try {
				LODSoftware lodSoftware = new LODSoftware();
				JSONObject json = new JSONObject(swObjStr);
				lodSoftware.initDomainObject(json,
						LODSoftware.FieldsEnum.values());
				res = res + "Software: " + lodSoftware.getSoftwareName() + 
						", Repository: " + lodSoftware.getRepository() + 
						", RepositoryId: " + lodSoftware.getRepositoryId() + "\n";
			} catch (Exception e) {
				throw new FfmaClientException(
						"cannot parse string to Json object! "
								+ swObjStr, e);
			}
		}
		log.info(res);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
		report = preservationRiskmanagementLodAnalysis.retrieveSoftware(AVI_FILE_EXTENSION);
		res = "report AVI: " + softwareObjList.size() + " results.\n";
		List<String> softwareObjAviList = Arrays.asList(report.split("###"));
		Iterator<String> swObjAviIter = softwareObjAviList.iterator();
		while (swObjAviIter.hasNext()) {
			String swObjStr = swObjAviIter.next();
			try {
				LODSoftware lodSoftware = new LODSoftware();
				JSONObject json = new JSONObject(swObjStr);
				lodSoftware.initDomainObject(json,
						LODSoftware.FieldsEnum.values());
				res = res + "Software: " + lodSoftware.getSoftwareName() + 
						", Repository: " + lodSoftware.getRepository() + 
						", RepositoryId: " + lodSoftware.getRepositoryId() + "\n";
			} catch (Exception e) {
				throw new FfmaClientException(
						"cannot parse string to Json object! "
								+ swObjStr, e);
			}
		}
		log.info(res);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
		report = preservationRiskmanagementLodAnalysis.retrieveSoftware(PDF_FILE_EXTENSION);
		List<String> softwareObjPdfList = Arrays.asList(report.split("###"));
		res = "report PDF: " + softwareObjPdfList.size() + " results.\n";
		Iterator<String> swObjPdfIter = softwareObjPdfList.iterator();
		while (swObjPdfIter.hasNext()) {
			String swObjStr = swObjPdfIter.next();
			try {
				LODSoftware lodSoftware = new LODSoftware();
				JSONObject json = new JSONObject(swObjStr);
				lodSoftware.initDomainObject(json,
						LODSoftware.FieldsEnum.values());
				res = res + "Software: " + lodSoftware.getSoftwareName() + 
						", Repository: " + lodSoftware.getRepository() + 
						", RepositoryId: " + lodSoftware.getRepositoryId() + "\n";
			} catch (Exception e) {
				throw new FfmaClientException(
						"cannot parse string to Json object! "
								+ swObjStr, e);
			}
		}
		log.info(res);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * @throws FfmaClientException
	 */
	@Test
	public void retrieveVendors() throws FfmaClientException {
		log.info("retrieve vendors for passed file extension");
		String report = preservationRiskmanagementLodAnalysis.retrieveVendor(PDF_FILE_EXTENSION);
		log.info("report PDF: " + report);
		assertNotNull("Retrieved LOD data analysis report must not be null", report);	
	}

	/**
	 * This test stores extensions from not existing LOD repository.
	 * @throws FfmaClientException
	 */
	@Test
	public void storeAllExtensionsForNotExistingRepository() throws FfmaClientException {
		log.info("test LOD data statistics using wrong or not existing parameter");
		String report = preservationRiskmanagementLodAnalysis.storeAllExtensions(NOT_EXISTING_TYPE, false, false);
		log.info("report: " + report);
		assertTrue("Retrieved report for not existing repository should be empty", report.length() == 0);
	}

	/**
	 * Test the LOD risk management configuration.
	 */
	@Test
	public void testLodConfiguration() {
		log.info("test check LOD configuration");
		preservationRiskmanagementLodAnalysis.setConfiguration(new PreservationRiskmanagementClientConfiguration());
		PreservationRiskmanagementClientConfiguration conf = preservationRiskmanagementLodAnalysis.getConfiguration();
		assertNotNull("Retrieved configuration must be null", conf);	
	}

}
