package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

import ait.ffma.service.preservation.common.api.measurement.Measurement;
import ait.ffma.service.preservation.common.api.measurement.MeasurementImpl;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.PronomConnector;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.RepositoryDescription;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.profile.DataItemProfile;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.profile.DataItemProfileImpl;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.CalculationModel;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.ConnectorImpl;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskUtils;
import ait.ffma.service.preservation.riskmanagement.mappingregistry.DBPediaRegistry;
import ait.ffma.service.preservation.riskmanagement.mappingregistry.MappingRegistry.RegistryTypesEnum;
import ait.ffma.service.preservation.riskmanagement.mappingregistry.PronomRegistry;

/**
 * This class provides testing functionality for risk analysis report generation.
 */
public class TestGenerateReport {

	public static final String REPORT_XML = "generatedReport.xml";
	
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

	@Test
	public void generateReport() throws MalformedURLException, URISyntaxException {
		log.info("Generate risk analysis report with risks break down to properties. Result is a XML file. " +
				"We assume we have got results from characterisation service: PRONOM ID = fmt/20 for PDF");
		String pronomId = "fmt/20";
		
		// initializations
		PronomRegistry pronomRegistry = new PronomRegistry();
		DBPediaRegistry dbPediaRegistry = new DBPediaRegistry();
		CalculationModel calculationModel = RiskUtils.initCalculationModel();
		
		// initialize PRONOM repository connector
		RepositoryDescription pronom = new PronomConnector();
		
		// evaluate vendor from PRONOM repository
		ConnectorImpl softwareVendorConnector = (ConnectorImpl) RiskUtils
				.updateConnector(RiskConstants.SOFTWARE_VENDOR_PROPERTY_ID, null, RiskConstants.DEVELOPED_BY, 
						pronom, pronomId);
		calculationModel.setPropertyValue(calculationModel.getRiskAnalysis().getRiskFactors(),
				softwareVendorConnector.getPropertyIdentifier(), softwareVendorConnector.getValue());
		String currentValue = softwareVendorConnector.getValue();
		log.info("Ffma://service/id/droid/puid: " + pronomId + ", developed by: " + currentValue);	

		// find mapping to DBPedia registry
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
		}
		log.info("DBPedia software vendor: " + currentValue);	
		assertNotNull("Retrieved from DBPedia mapping softwae vendor value must not be null", currentValue);
		// end evaluate vendor from PRONOM
		
		// calculate risk score
//		int riskScore = calculationModel.getRiskScore();

		// generate report in XML format
//		RiskUtils.storeRiskDataInXML(calculationModel.getRiskScoreBreakdown(), REPORT_XML);

//		assertNotNull("Retrieved risk score value must not be null", riskScore);
//		log.info("RiskScore for software vendor: " + riskScore);	
//		assertTrue(riskScore == 10);
	}
	
	@Test
	public void generateReportWithMeasurements() throws MalformedURLException, URISyntaxException {
		log.info("Generate risk analysis report with risks break down to properties and property sets. " + 
				"Measurements are used for risk score values insertion. Result is a XML file. " +
				"We assume we have got results from characterisation service: PRONOM ID = fmt/20 for PDF");
		String pronomId = "fmt/20";
		
		// Known risk values
		List<Measurement> measurementList = new ArrayList<Measurement>();
		MeasurementImpl measurement = new MeasurementImpl();
		measurement.setId(1);
		measurement.setIdentifier(RiskConstants.STANDARD_PROPERTY_ID); 
		measurement.setValue("NONE"); 
		measurementList.add(measurement);
		
		// initialize data profile
		DataItemProfile profile = new DataItemProfileImpl(measurementList);	

		// initializations
		PronomRegistry pronomRegistry = new PronomRegistry();
		DBPediaRegistry dbPediaRegistry = new DBPediaRegistry();
		CalculationModel calculationModel = RiskUtils.initCalculationModel(profile);
		
		// initialize PRONOM repository connector
		RepositoryDescription pronom = new PronomConnector();
		
		// evaluate vendor from PRONOM repository
		ConnectorImpl softwareVendorConnector = (ConnectorImpl) RiskUtils
				.updateConnector(RiskConstants.SOFTWARE_VENDOR_PROPERTY_ID, null, RiskConstants.DEVELOPED_BY, 
						pronom, pronomId);
		calculationModel.setPropertyValue(calculationModel.getRiskAnalysis().getRiskFactors(),
				softwareVendorConnector.getPropertyIdentifier(), softwareVendorConnector.getValue());
		String currentValue = softwareVendorConnector.getValue();
		log.info("Ffma://service/id/droid/puid: " + pronomId + ", developed by: " + currentValue);	

		// find mapping to DBPedia registry
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
		}
		log.info("DBPedia software vendor: " + currentValue);	
		assertNotNull("Retrieved from DBPedia mapping softwae vendor value must not be null", currentValue);
		// end evaluate vendor from PRONOM
		
		// calculate risk score
//		int riskScore = calculationModel.getRiskScore();

		// generate report in XML format
//		RiskUtils.storeRiskDataInXML(calculationModel.getRiskScoreBreakdown(), REPORT_XML);

//		assertNotNull("Retrieved risk score value must not be null", riskScore);
//		log.info("RiskScore for software vendor: " + riskScore);	
//		assertTrue(riskScore == 110);
	}

	/**
	 * This method creates measurement for particular property.
	 * @param id
	 * @param propertyName
	 * @param value
	 * @return measurement
	 */
	Measurement createMeasurement(int id, String propertyName, String value) {
		MeasurementImpl measurement = new MeasurementImpl();
		measurement.setId(id);
		measurement.setIdentifier(propertyName); 
		measurement.setValue(value);
		return measurement;
	}
	
	@Test
	public void generateReportWithMeasurementsForAggregatedProperties() throws MalformedURLException, URISyntaxException {
		log.info("Generate risk analysis report with risks break down to properties and property sets for aggregated properties. " + 
				"Measurements are used for risk score values insertion. Result is a XML file.");
		
		// Known risk values
		int id = 0;
		List<Measurement> measurementList = new ArrayList<Measurement>();
		measurementList.add(createMeasurement(id++, RiskConstants.IS_SUPPORTED_BY_IMPORTANT_SOFTWARE_VENDORS_PROPERTY_ID, "yes")); //0
		measurementList.add(createMeasurement(id++, RiskConstants.IS_OPEN_FILE_FORMAT_PROPERTY_ID, "no")); //10
		measurementList.add(createMeasurement(id++, RiskConstants.IS_SUPPORTED_BY_WEB_BROWSERS_PROPERTY_ID, "yes")); //0
		measurementList.add(createMeasurement(id++, RiskConstants.WHICH_VERSION_OFFICIALLY_SUPPORTED_BY_VENDOR_PROPERTY_ID, "2.0")); //40
		measurementList.add(createMeasurement(id++, RiskConstants.WHICH_VERSION_FREQUENTLY_USED_PROPERTY_ID, "1.0")); //50
		measurementList.add(createMeasurement(id++, RiskConstants.IS_COMPRESSED_FILE_FORMAT_PROPERTY_ID, "optional")); //10
		measurementList.add(createMeasurement(id++, RiskConstants.HAS_CREATOR_INFORMATION_PROPERTY_ID, "yes")); //0
		measurementList.add(createMeasurement(id++, RiskConstants.HAS_PUBLISHER_INFORMATION_PROPERTY_ID, "no")); //10
		measurementList.add(createMeasurement(id++, RiskConstants.HAS_DIGITAL_RIGHTS_INFORMATION_PROPERTY_ID, "no")); //10
		measurementList.add(createMeasurement(id++, RiskConstants.IS_FILE_MIGRATION_ALLOWED_PROPERTY_ID, "yes")); //0
		measurementList.add(createMeasurement(id++, RiskConstants.HAS_CREATION_DATE_INFORMATION_PROPERTY_ID, "2004-03-15")); //50
		measurementList.add(createMeasurement(id++, RiskConstants.HAS_OBJECT_PREVIEW_PROPERTY_ID, "empty")); //50
		
		// initialize data profile
		DataItemProfile profile = new DataItemProfileImpl(measurementList);	

		// initializations
		CalculationModel calculationModel = RiskUtils.initCalculationModel(profile);
		assertNotNull("Retrieved calculatin model object must not be null", calculationModel);
		
		// calculate risk score
//		int riskScore = calculationModel.getRiskScore();

		// generate report in XML format
//		RiskUtils.storeRiskDataInXML(calculationModel.getRiskScoreBreakdown(), REPORT_XML);

//		assertNotNull("Retrieved risk score value must not be null", riskScore);
//		log.info("RiskScore for software vendor: " + riskScore);	
//		assertTrue(riskScore == 230);
	}

}
