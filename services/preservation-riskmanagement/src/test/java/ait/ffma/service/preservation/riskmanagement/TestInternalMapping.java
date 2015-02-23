package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Test;

import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.PronomConnector;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.CalculationModel;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ConnectorImpl;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskUtils;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskProperty;
import ait.ffma.service.preservation.riskmanagement.mappingregistry.DBPediaRegistry;
import ait.ffma.service.preservation.riskmanagement.mappingregistry.MappingRegistry.RegistryTypesEnum;
import ait.ffma.service.preservation.riskmanagement.mappingregistry.PronomRegistry;

/**
 * This class provides experiments with different file formats for internal mapping between
 * PRONOM and DBPedia repositories.
 */
public class TestInternalMapping {

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());
	
	@Test
	public void map_pdf() throws MalformedURLException, URISyntaxException {
		log.info("test mapping for pdf");
		String pronomID = "fmt/20";
		// initialize mapping registries
		PronomRegistry pronomRegistry = new PronomRegistry();
		DBPediaRegistry dbPediaRegistry = new DBPediaRegistry();
		
		CalculationModel calculationModel = RiskUtils.initCalculationModel();

		RepositoryDescription pronom = new PronomConnector();	
		RiskProperty softwareVendorProperty = RiskUtils.getRiskPropertyById(RiskConstants.SOFTWARE_VENDOR_PROPERTY_ID);
		ConnectorImpl softwareVendorConnector = new ConnectorImpl(softwareVendorProperty);
		softwareVendorConnector.setSearchValue(RiskConstants.DEVELOPED_BY);
		((PronomConnector) pronom).setConnector(softwareVendorConnector);
		((PronomConnector) pronom).setQuery(pronomID); 
		softwareVendorConnector = (ConnectorImpl) ((PronomConnector) pronom).update();
		String currentValue = softwareVendorConnector.getValue();
		calculationModel.setPropertyValue(calculationModel.getRiskAnalysis().getRiskFactors(),
				softwareVendorConnector.getPropertyIdentifier(), softwareVendorConnector.getValue());
		log.info("Ffma://service/id/droid/puid: " + pronomID + ", developed by: " + currentValue);	
		
		currentValue = pronomRegistry.getKey(RegistryTypesEnum.SoftwareVendor, currentValue);
		log.info("pronom key: " + currentValue);
		String key = currentValue;
		currentValue = dbPediaRegistry.getValue(RegistryTypesEnum.SoftwareVendor, currentValue);
		log.info("dbpedia://softwarevendor/" + key + ": " + currentValue);	
		List<String> vendors = RiskUtils.splitString(currentValue, RiskConstants.STRING_SEPARATOR);

		boolean found = false;
		List<String> resultList = null;
		Iterator<String> vendorsIter = vendors.iterator();
		while (vendorsIter.hasNext()) {
			if (found) {
				break;
			}
			currentValue = vendorsIter.next();		
			log.info("current vendor: " + currentValue);
//			resultList = RiskUtils.search(RiskConstants.VENDOR_DBPEDIA_YAGO_PROPERTY_ID,
//					currentValue, RiskConstants.SUBJECT, RiskConstants.EMPLOYEES,
//					false);
			resultList = RiskUtils.search(RiskConstants.VENDOR_DBPEDIA2_PROPERTY_ID,
					currentValue, RiskConstants.SUBJECT, RiskConstants.EMPLOYEES,
					false);
			Iterator<String> resultIter = resultList.iterator();
			log.info("list size: " + resultList.size());
			if (resultList.size() > 0) {
				found = true;
			}
			while (resultIter.hasNext()) {
				log.info("employee number found: " + resultIter.next());
			}
		}
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
		
		// DROID characterization service provides with extension
		String currentExtension = "pdf";
		log.info("currentExtension: " + currentExtension);
		resultList = RiskUtils.searchCompatibleSoftwareForExtension(currentExtension.toUpperCase());
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("Compatible software found: " + resultIter.next());
		}
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void map_jpg() throws MalformedURLException, URISyntaxException {
		log.info("test mapping for jpg");
		String pronomID = "fmt/44";
		// initialize mapping registries
		PronomRegistry pronomRegistry = new PronomRegistry();
		DBPediaRegistry dbPediaRegistry = new DBPediaRegistry();
		
		RiskUtils.initCalculationModel();

		RepositoryDescription pronom = new PronomConnector();	
		((PronomConnector) pronom).setQuery(pronomID); // image/jpeg
		Map<String, String> pronomMap = ((PronomConnector) pronom).retrieve();
		String currentValue = pronomMap.get(RiskConstants.DEVELOPED_BY);
		log.info("Ffma://service/id/droid/puid: " + pronomID + ", developed by: " + currentValue);

		currentValue = pronomRegistry.getKey(RegistryTypesEnum.GraphicsFileFormats, currentValue);
		log.info("pronom key: " + currentValue);
		String key = currentValue;
		currentValue = dbPediaRegistry.getValue(RegistryTypesEnum.GraphicsFileFormats, currentValue);
		log.info("dbpedia://softwarevendor/" + key + ": " + currentValue);	
		List<String> vendors = RiskUtils.splitString(currentValue, RiskConstants.STRING_SEPARATOR);
		boolean found = false;
		List<String> resultList = null;
		Iterator<String> vendorsIter = vendors.iterator();
		while (vendorsIter.hasNext()) {
			if (found) {
				break;
			}
			currentValue = vendorsIter.next();		
			log.info("current vendor: " + currentValue);
			resultList = RiskUtils.search(RiskConstants.WORKING_GROUPS_PROPERTY_ID,
					currentValue, RiskConstants.SUBJECT, RiskConstants.HOMEPAGE,
					false);
			Iterator<String> resultIter = resultList.iterator();
			log.info("list size: " + resultList.size());
			if (resultList.size() > 0) {
				found = true;
			}
			while (resultIter.hasNext()) {
				log.info("found: " + resultIter.next());
			}
		}
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);

		// DROID characterization service provides with extension
		/*String currentExtension = "JPG";
		log.info("currentExtension: " + currentExtension);
//		List<String> formatCodeList = RiskUtils.searchFormatCodeFromExtension(currentExtension);
//		String currentFormatCode = formatCodeList.get(0);
//		currentFormatCode = "JPEG";
		resultList = RiskUtils.searchCompatibleSoftwareForExtension(currentExtension);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("Compatible software found: " + resultIter.next());
		}
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);*/
	}
	
	@Test
	public void map_csv() throws MalformedURLException, URISyntaxException {
		log.info("test mapping for csv");
		String pronomID = "x-fmt/18";
		// initialize mapping registries
		PronomRegistry pronomRegistry = new PronomRegistry();
		DBPediaRegistry dbPediaRegistry = new DBPediaRegistry();
		
		RiskUtils.initCalculationModel();

		RepositoryDescription pronom = new PronomConnector();	
		((PronomConnector) pronom).setQuery(pronomID); // text/cvs
		Map<String, String> pronomMap = ((PronomConnector) pronom).retrieve();
		String currentValue = pronomMap.get(RiskConstants.DEVELOPED_BY);
		log.info("Ffma://service/id/droid/puid: " + pronomID + ", developed by: " + currentValue);

		currentValue = pronomRegistry.getKey(RegistryTypesEnum.GraphicsFileFormats, currentValue);
		log.info("pronom key: " + currentValue);
		String key = RiskConstants.REG_CSV;
		currentValue = dbPediaRegistry.getValue(RegistryTypesEnum.GraphicsFileFormats, key);
		log.info("dbpedia://softwarevendor/" + key + ": " + currentValue);	

		// DROID characterization service provides with extension 
		String currentExtension = "csv";
		log.info("currentExtension: " + currentExtension);
		List<String> resultList = RiskUtils.searchCompatibleSoftwareForExtension(currentExtension.toUpperCase());
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("Compatible software found: " + resultIter.next());
		}
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void map_png() throws MalformedURLException, URISyntaxException {
		log.info("test mapping for png");
		String pronomID = "fmt/13";
		// initialize mapping registries
		PronomRegistry pronomRegistry = new PronomRegistry();
		DBPediaRegistry dbPediaRegistry = new DBPediaRegistry();
		
		RiskUtils.initCalculationModel();

		RepositoryDescription pronom = new PronomConnector();	
		((PronomConnector) pronom).setQuery(pronomID); // image/png
		Map<String, String> pronomMap = ((PronomConnector) pronom).retrieve();
		String currentValue = pronomMap.get(RiskConstants.DEVELOPED_BY);
		log.info("Ffma://service/id/droid/puid: " + pronomID + ", developed by: " + currentValue);

		// PRONOM does not contain developer use DBPedia definition
		currentValue = pronomRegistry.getKey(RegistryTypesEnum.GraphicsFileFormats, currentValue);
		log.info("pronom key: " + currentValue);
		String key = RiskConstants.REG_PNG;
		currentValue = dbPediaRegistry.getValue(RegistryTypesEnum.GraphicsFileFormats, key);
		log.info("dbpedia://softwarevendor/" + key + ": " + currentValue);	
		// DROID characterization service provides with extension
		String currentExtension = "png";
		log.info("currentExtension: " + currentExtension);
		List<String> resultList = RiskUtils.searchCompatibleSoftwareForExtension(currentExtension.toUpperCase());
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("Compatible software found: " + resultIter.next());
		}
		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
	}
}
