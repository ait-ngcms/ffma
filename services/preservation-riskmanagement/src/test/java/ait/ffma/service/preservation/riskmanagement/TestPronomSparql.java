package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;

import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.DBPediaConnector;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.PronomConnector;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.CalculationModel;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ConnectorImpl;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskUtils;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskAnalysis;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskProperty;
import ait.ffma.service.preservation.riskmanagement.mappingregistry.DBPediaRegistry;
import ait.ffma.service.preservation.riskmanagement.mappingregistry.MappingRegistry.RegistryTypesEnum;
import ait.ffma.service.preservation.riskmanagement.mappingregistry.PronomRegistry;


/**
 * This class provides experiments with risk analysis properties
 * and risk score calculations.
 */
public class TestPronomSparql {

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());

	@Test
	public void calculationModel() throws MalformedURLException, URISyntaxException {
		log.info("test calculation model");

//		WebResource resource = Client.create().resource("http://www.nationalarchives.gov.uk/PRONOM/x-sfw/1.xml");
//        String response = resource.get(String.class);
//        log.info("Pronom: " + response);

		CalculationModel calculationModel = RiskUtils.initCalculationModel();
		
		// evaluate software from extension
		RepositoryDescription dbpedia = new DBPediaConnector();
		RiskProperty fileFormatProperty = RiskUtils.getRiskPropertyById(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID);
		ConnectorImpl fileFormatConnector = new ConnectorImpl(fileFormatProperty);
		fileFormatConnector.getColumnNames().add(RiskConstants.EXTENSION);
		fileFormatConnector.getColumnNames().add(RiskConstants.SOFTWARE);
//		log.info("file name: " + file.getName());	
//		fileFormatConnector.setSearchValue(file.getName().substring(file.getName().indexOf(".")));
//		((DBPediaConnector) dbpedia).setConnector(fileFormatConnector);
//		fileFormatConnector = (ConnectorImpl)((DBPediaConnector) dbpedia).update();
//		log.info("Read from DBPedia: value: " + fileFormatConnector.getValue() + ", property: " + fileFormatConnector.getRiskProperty().getName());	
//
//		calculationModel.setPropertyValue(riskAnalysis.getRiskFactors(), fileFormatConnector.getRiskPropertyIdentifier(), fileFormatConnector.getValue());
		
		// evaluate vendor from software
		RiskProperty softwareVendorProperty = RiskUtils.getRiskPropertyById(RiskConstants.SOFTWARE_VENDOR_PROPERTY_ID);
		ConnectorImpl softwareVendorConnector = new ConnectorImpl(softwareVendorProperty);
		softwareVendorConnector.getColumnNames().add(RiskConstants.PRODUCT);
		softwareVendorConnector.getColumnNames().add(RiskConstants.VENDOR);
		String currentValue = fileFormatConnector.getValue();
		if (currentValue != null) {
			currentValue = currentValue.replaceAll(">", "").substring(currentValue.lastIndexOf("/") + 1);
			log.info("currentSoftware: " + currentValue);
		}
		softwareVendorConnector.setSearchValue("Internet_Explorer_6");
//		softwareVendorConnector.setSearchValue(currentValue);
		((DBPediaConnector) dbpedia).setConnector(softwareVendorConnector);
		softwareVendorConnector = (ConnectorImpl)((DBPediaConnector) dbpedia).update();
		log.info("Read from DBPedia: value: " + softwareVendorConnector.getValue() + ", property: " + softwareVendorConnector.getRiskProperty().getName());	

		if (softwareVendorConnector != null) {
			String vendorValue = softwareVendorConnector.getValue();
			if (vendorValue != null) {
				vendorValue = vendorValue.replaceAll(">", "").substring(vendorValue.lastIndexOf("/") + 1);
				softwareVendorConnector.setValue(vendorValue);
				log.info("Read vendor from DBPedia: " + vendorValue);
			}
		}
		calculationModel.setPropertyValue(calculationModel.getRiskAnalysis()
				.getRiskFactors(), softwareVendorConnector
				.getPropertyIdentifier(), softwareVendorConnector
				.getValue());
		
		// calculate risk score
		int riskScore = calculationModel.getRiskScore();

		assertNotNull("Retrieved risk score value must not be null", riskScore);
		log.info("RiskScore for software vendor: " + riskScore);	
//		assertTrue(riskScore == 20);
	}
	
	@Test
	public void calculationModel_retrieveDataEntry() throws MalformedURLException, URISyntaxException {
		log.info("test calculation model retrieve repository data entry");

		// initialize calculation model
		CalculationModel calculationModel = new CalculationModel();
		RiskAnalysis riskAnalysis = calculationModel.analyze(null, null, null);
		calculationModel.setRiskAnalysis(riskAnalysis);

		RepositoryDescription pronom = new PronomConnector();
		
		((PronomConnector) pronom).setQuery("fmt/44"); // image/jpeg
		Map<String, String> pronomMap = ((PronomConnector) pronom).retrieve();
		String currentValue = pronomMap.get(RiskConstants.DEVELOPED_BY);
		log.info("Ffma://service/id/droid/puid: " + "fmt/44" + ", developed by: " + currentValue);
		Set<String> set = pronomMap.keySet();
		for (String key : set) {
			log.info("PRONOM response: key=" + key + " , value=" + pronomMap.get(key));
		}
		
		// evaluate software from extension
		RepositoryDescription dbpedia = new DBPediaConnector();
		
		// evaluate vendor from software
		RiskProperty fileFormatsProperty = RiskUtils.getRiskPropertyById(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID);
		ConnectorImpl fileFormatsConnector = new ConnectorImpl(fileFormatsProperty);

		// Ffma://service/id/droid/mime-type
		fileFormatsConnector.setSearchValue("image/jpeg");
		((DBPediaConnector) dbpedia).setConnector(fileFormatsConnector);
		Map<String, String> dbpediaMap = ((DBPediaConnector) dbpedia).retrieve();
		Set<String> dbpediaSet = dbpediaMap.keySet();
		for (String key : dbpediaSet) {
			log.info("DBPedia response: key=" + key + " , value=" + dbpediaMap.get(key));
		}
		assertNotNull("Retrieved risk score value must not be null", dbpediaSet);
	}
	
	@Test
	public void calculationModel_retrieveAll() throws MalformedURLException, URISyntaxException {
		log.info("test calculation model retrieve all data for particular query");

		// initialize calculation model
		CalculationModel calculationModel = new CalculationModel();
		RiskAnalysis riskAnalysis = calculationModel.analyze(null, null, null);
		calculationModel.setRiskAnalysis(riskAnalysis);

		RepositoryDescription pronom = new PronomConnector();
		
		((PronomConnector) pronom).setQuery("fmt/44#fmt/20#fmt/13"); 
		List<Map<String, String>> pronomList = ((PronomConnector) pronom).retrieveAll();
		Iterator<Map<String, String>> iter = pronomList.iterator();
		while (iter.hasNext()) {
			Map<String, String> pronomMap = iter.next();
			Set<String> pronomSet = pronomMap.keySet();
			for (String key : pronomSet) {
				log.info("PRONOM response: key=" + key + " , value=" + pronomMap.get(key));
			}
		}
		assertNotNull("Retrieved PRONOM list must not be null", pronomList);
	}
	
	//@Test
	public void calculationModel_update() throws MalformedURLException, URISyntaxException {
		log.info("test calculation model update");
		// initialize mapping registries
		PronomRegistry pronomRegistry = new PronomRegistry();
		DBPediaRegistry dbPediaRegistry = new DBPediaRegistry();
		List<String> columnList = new ArrayList<String>();

		CalculationModel calculationModel = RiskUtils.initCalculationModel();

		// initialize PRONOM repository connector
		RepositoryDescription pronom = new PronomConnector();
		
		// evaluate vendor from PRONOM repository
		ConnectorImpl softwareVendorConnector = (ConnectorImpl) RiskUtils
				.updateConnector(RiskConstants.SOFTWARE_VENDOR_PROPERTY_ID,
						null, RiskConstants.DEVELOPED_BY, pronom, "fmt/20");
		calculationModel.setPropertyValue(calculationModel.getRiskAnalysis().getRiskFactors(),
				softwareVendorConnector.getPropertyIdentifier(), softwareVendorConnector.getValue());
		String currentValue = softwareVendorConnector.getValue();
		log.info("Ffma://service/id/droid/puid: " + "fmt/20" + ", developed by: " + currentValue);	
		
		currentValue = pronomRegistry.getKey(RegistryTypesEnum.SoftwareVendor, currentValue);
		log.info("pronom key: " + currentValue);	
		currentValue = dbPediaRegistry.getValue(RegistryTypesEnum.SoftwareVendor, currentValue);
		log.info("dbpedia://softwarevendor/adobe: " + currentValue);	

		List<String> vendors = RiskUtils.splitString(currentValue, RiskConstants.STRING_SEPARATOR);

		boolean found = false;
		Iterator<String> vendorsIter = vendors.iterator();
		while (vendorsIter.hasNext()) {
			if (found) {
				break;
			}
			currentValue = vendorsIter.next();

			// evaluate software from extension
			RepositoryDescription dbpedia = new DBPediaConnector();
			
			// evaluate employees number from vendor 
			columnList.clear();
			columnList.add(RiskConstants.VENDOR);
			columnList.add(RiskConstants.NUMBER_EMPLOYEES);
			ConnectorImpl vendorConnector = (ConnectorImpl) RiskUtils
					.updateConnector(RiskConstants.VENDOR_PROPERTY_ID, columnList, currentValue, dbpedia);
			log.info("Read from DBPedia numberOfEmployees: "
					+ vendorConnector.getValue() + ", property: "
					+ vendorConnector.getRiskProperty().getName());
	
			// if no value found in dbpedia name space try to find in dbpedia2 name space
			if (vendorConnector.getValue() == null) {
				columnList.clear();
				columnList.add(RiskConstants.SUBJECT);
				columnList.add(RiskConstants.EMPLOYEES);
				ConnectorImpl vendorDBPedia2Connector = (ConnectorImpl) RiskUtils
						.updateConnector(RiskConstants.VENDOR_DBPEDIA2_PROPERTY_ID, columnList, currentValue, dbpedia);
				log.info("Read from DBPedia2 numberOfEmployees: "
						+ vendorDBPedia2Connector.getValue() + ", property: "
						+ vendorDBPedia2Connector.getRiskProperty().getName());
			
				if (vendorDBPedia2Connector != null) {
					found = true;
					String numberEmployeeValue = vendorDBPedia2Connector.getValue();
					if (numberEmployeeValue != null) {
						numberEmployeeValue = numberEmployeeValue.replaceAll(">", "").substring(numberEmployeeValue.lastIndexOf("/") + 1);
						vendorDBPedia2Connector.setValue(numberEmployeeValue);
						log.info("Read number employee from DBPedia2: " + numberEmployeeValue + ", for vendor: " + currentValue);
					}
				}
				calculationModel.setPropertyValue(calculationModel.getRiskAnalysis().getRiskFactors(),
						vendorDBPedia2Connector.getPropertyIdentifier(),
						vendorDBPedia2Connector.getValue());
			}
		}

		// calculate risk score
		int riskScore = calculationModel.getRiskScore();

		assertNotNull("Retrieved risk score value must not be null", riskScore);
		log.info("RiskScore for software vendor: " + riskScore);	
//		assertTrue(riskScore == 60);
	}
	
	@Test
	public void calculationModel_image() throws MalformedURLException, URISyntaxException {
		log.info("test calculation model image");
		
//		CalculationModel calculationModel = RiskUtils.initCalculationModel();

		RepositoryDescription pronom = new PronomConnector();
		
		((PronomConnector) pronom).setQuery("fmt/44"); // image/jpeg
		Map<String, String> pronomMap = ((PronomConnector) pronom).retrieve();
		String currentValue = pronomMap.get(RiskConstants.DEVELOPED_BY);
		log.info("Ffma://service/id/droid/puid: " + "fmt/44" + ", developed by: " + currentValue);
		Set<String> set = pronomMap.keySet();
		for (String key : set) {
			log.info("PRONOM response: key=" + key + " , value=" + pronomMap.get(key));
		}
		
		// evaluate software from extension
		RepositoryDescription dbpedia = new DBPediaConnector();
		
		// evaluate vendor from software
		RiskProperty fileFormatsProperty = RiskUtils.getRiskPropertyById(RiskConstants.GRAPHICS_FILE_FORMATS_PROPERTY_ID);
		ConnectorImpl fileFormatsConnector = new ConnectorImpl(fileFormatsProperty);

		// Ffma://service/id/droid/mime-type
		fileFormatsConnector.setSearchValue("image/jpeg");
		((DBPediaConnector) dbpedia).setConnector(fileFormatsConnector);
		Map<String, String> dbpediaMap = ((DBPediaConnector) dbpedia).retrieve();
		Set<String> dbpediaSet = dbpediaMap.keySet();
		for (String key : dbpediaSet) {
			log.info("DBPedia response: key=" + key + " , value=" + dbpediaMap.get(key));
		}

		assertNotNull("Retrieved risk score value must not be null", dbpediaSet);
	}

	@Test
	public void evaluateSupportingToolsCountForFormat() throws MalformedURLException, URISyntaxException {
		log.info("test evaluate supporting software tools count for given format");
		
		// initialize calculation model
		CalculationModel calculationModel = new CalculationModel();
		RiskAnalysis riskAnalysis = calculationModel.analyze(null, null, null);
		calculationModel.setRiskAnalysis(riskAnalysis);
		List<String> softwareList = RiskUtils.searchCompatibleSoftwareForExtension("PDF");
		Iterator<String> resultIter = softwareList.iterator();
		log.info("PDF compatible software count: " + softwareList.size());
		while (resultIter.hasNext()) {
			log.info("Software found: " + resultIter.next());
		}
		
		assertNotNull("Retrieved software list must not be null", softwareList);
		assertTrue(softwareList.size() >= 1);
	}

}
