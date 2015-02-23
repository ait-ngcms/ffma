package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import ait.ffma.service.preservation.common.api.measurement.Measurement;
import ait.ffma.service.preservation.common.api.measurement.MeasurementImpl;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.profile.DataItemProfile;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.profile.DataItemProfileImpl;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.CalculationModel;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskUtils;

/**
 * This class provides experiments with different file formats, software and vendors for Freebase 
 * (http://wiki.freebase.com/wiki/Main_Page) repository.
 */
public class TestFreebase {

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
	public void freebaseRequest() throws MalformedURLException, URISyntaxException {
		log.info("test request for freebase repository");
		JSONObject result = null;
		String resourceUrl = "http://api.freebase.com/api/service/mqlread?query={\"query\":";
		try {
			JSONObject json = new JSONObject();
			/*"type": "/computer/software",
			  "developer": "Adobe Systems",
			  "/common/topic/alias" : [],
			  "id" : null,
			  "name" : null,
			  "key" : [],
			  "/common/topic/subjects" : [],*/
			json.put("type", "/computer/software");
//			json.put("developer", "[]");
			json.put("developer", "Adobe Systems");
			json.put("/common/topic/alias", "[]");
			json.put("id", "null");
			json.put("name", "null");
			json.put("key", "[]");
			json.put("/common/topic/subjects", "[]");
			log.info(json.toString());

			String query = json.toString();
			query = query.replace("\"[", "[");
			query = query.replace("]\"", "]");
			query = query.replace("\"null\"", "null");
			query = "[" + query + "]";
			log.info("json as a string: " + query);
			String query_envelope = "{\"query\":" + query + "}";
			String service_url = "http://api.freebase.com/api/service/mqlread";
			resourceUrl = service_url  + "?query=" + URLEncoder.encode(query_envelope, "UTF-8");

			WebResource resource = Client.create().resource(resourceUrl);
			resource.accept(MediaType.APPLICATION_JSON);	
			String res = resource.get(String.class);
			System.out.println("Freebase: " + res);
			JSONObject resultJsonObject = new JSONObject(res);
			JSONArray jsonArray = (JSONArray)resultJsonObject.get("result");
		    for (int i=0; i < jsonArray.length(); i++) {
		    	JSONObject currentJsonObject = (JSONObject) jsonArray.get(i);
		    	log.info("Response software name: " + currentJsonObject.get("name"));
		    }

			result = new JSONObject(res);
		} catch (Exception e) {
			log.info("Freebase error: " + e);
		}

		assertNotNull("Retrieved list must not be null", result);
	}

	@Test
	public void calculationModelFreebase() throws MalformedURLException, URISyntaxException {
		log.info("test calculation model for Freebase repository");
		String searchExtension = "pdf";
		// create test measurements list
		List<Measurement> measurementList = new ArrayList<Measurement>();
		MeasurementImpl measurement1 = new MeasurementImpl();
		measurement1.setId(1);
		measurement1.setIdentifier(RiskConstants.FB_FILE_FORMAT_PROPERTY_ID); 
		measurement1.setValue("PDF"); // risk score 2
		MeasurementImpl measurement2 = new MeasurementImpl();
		measurement2.setId(2);
		measurement2.setIdentifier(RiskConstants.LAST_MODIFICATION_DATE_PROPERTY_ID);
		measurement2.setValue("2004-06-27"); // risk score 50
		measurementList.add(measurement1);
		measurementList.add(measurement2);
		
		// initialize data profile
		DataItemProfile profile = new DataItemProfileImpl(measurementList);	
		CalculationModel calculationModel = RiskUtils.initCalculationModel(profile);
		log.info("Extension: " + searchExtension);
		searchColumns.clear();
		searchColumns.add(RiskConstants.FB_EXTENSION);
		resultColumns.clear();
		resultColumns.add(RiskConstants.FB_WRITTEN_BY);
		resultColumns.add(RiskConstants.FB_NAME);
		List<String> resultList = RiskUtils.searchJSON(
				RiskConstants.FB_FILE_FORMAT_PROPERTY_ID, searchExtension,
				searchColumns, resultColumns); 

		Iterator<String> resultIter = resultList.iterator();
		log.info("Result list: " + resultList.size());
		while (resultIter.hasNext()) {
			String str = resultIter.next();
			log.info("Result found: " + str);
			calculationModel.setPropertyValue(calculationModel.getRiskAnalysis().getRiskFactors(),
					RiskConstants.FB_SOFTWARE_PROPERTY_ID, str); // risk score 10
		}

		calculationModel.setPropertyValue(calculationModel.getRiskAnalysis().getRiskFactors(),
				RiskConstants.BROKEN_OBJECTS_SCORE_PROPERTY_ID, "0.56"); // risk score 10
		
		// evaluate vendor from PRONOM repository
//		ConnectorImpl softwareVendorConnector = (ConnectorImpl) RiskUtils
//				.updateConnector(RiskConstants.SOFTWARE_VENDOR_PROPERTY_ID, null, RiskConstants.DEVELOPED_BY, pronom, "fmt/20");
//		calculationModel.setPropertyValue(riskAnalysis.getRiskFactors(),
//				softwareVendorConnector.getRiskPropertyIdentifier(), softwareVendorConnector.getValue());
//		String currentValue = softwareVendorConnector.getValue();
//		log.info("Ffma://service/id/droid/puid: " + "fmt/20" + ", developed by: " + currentValue);	

		// calculate risk score
		int riskScore = calculationModel.getRiskScore();

		assertNotNull("Retrieved risk score value must not be null", riskScore);
//		log.info("RiskScore for software vendor: " + riskScore);	
//		assertTrue(riskScore == 50);
	}
	
	@Test
	public void searchSoftwareFromExtension() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates compatible software for given extension");
		List<String> formats = new ArrayList<String>();
//		formats.add("tiff");
//		formats.add("pdf");
		formats.add("ply");
//		formats.add("gif");
//		formats.add("doc");
//		formats.add("jpg");
//		formats.add("csv");
		Iterator<String> iter = formats.iterator();
		while (iter.hasNext()) {
			RiskUtils.initCalculationModel();
			String searchExtension = iter.next();
			log.info("Extension: " + searchExtension);
			searchColumns.clear();
			searchColumns.add(RiskConstants.FB_EXTENSION);
			resultColumns.clear();
			resultColumns.add(RiskConstants.FB_WRITTEN_BY);
			resultColumns.add(RiskConstants.FB_NAME);
			List<String> resultList = RiskUtils.searchJSON(
					RiskConstants.FB_FILE_FORMAT_PROPERTY_ID, searchExtension,
					searchColumns, resultColumns);
	
			Iterator<String> resultIter = resultList.iterator();
			log.info("Result list: " + resultList.size());
			while (resultIter.hasNext()) {
				String str = resultIter.next();
				log.info("Result found: " + str);
			}
	
			assertNotNull("Retrieved list must not be null", resultList);
			assertTrue(resultList.size() >= 1);
		}
	}
	
	@Test
	public void searchDeveloperFromExtension() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates developer for given extension");
		RiskUtils.initCalculationModel();
//		String searchExtension = "tiff";
		String searchExtension = "pdf";
//		String searchExtension = "png";
//		String searchExtension = "jpg";
//		String searchExtension = "doc";
//		String searchExtension = "dae";
		List<String> developerList = new ArrayList<String>();
		searchColumns.clear();
		searchColumns.add(RiskConstants.FB_EXTENSION);
		resultColumns.clear();
		resultColumns.add(RiskConstants.FB_WRITTEN_BY);
		resultColumns.add(RiskConstants.FB_NAME);
		List<String> softwareList = RiskUtils.searchJSON(
				RiskConstants.FB_FILE_FORMAT_PROPERTY_ID, searchExtension,
				searchColumns, resultColumns);

		Iterator<String> softwareIter = softwareList.iterator();
		log.info("Result list: " + softwareList.size());
		searchColumns.clear();
		searchColumns.add(RiskConstants.FB_NAME);
		resultColumns.clear();
		resultColumns.add(RiskConstants.FB_DEVELOPER);

		while (softwareIter.hasNext()) {
			String software = softwareIter.next();
			log.info("Software found: " + software);
			List<String> resultList = RiskUtils.searchJSON(
					RiskConstants.FB_SOFTWARE_PROPERTY_ID, software,
					searchColumns, resultColumns);

			Iterator<String> resultIter = resultList.iterator();
			log.info("Result list: " + resultList.size());
			while (resultIter.hasNext()) {
				String str = resultIter.next();
				log.info("Developer found: " + str);
				developerList.add(str);
			}
		}

		assertNotNull("Retrieved list must not be null", developerList);
		assertTrue(developerList.size() >= 1);
	}
	
	@Test
	public void searchDeveloperFromSoftware() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates developer for given software");
		RiskUtils.initCalculationModel();
		List<String> developerList = new ArrayList<String>();
		searchColumns.clear();
		searchColumns.add(RiskConstants.FB_NAME);
		resultColumns.clear();
		resultColumns.add(RiskConstants.FB_DEVELOPER);

		String software = "Adobe Acrobat";
		log.info("Software: " + software);
		List<String> resultList = RiskUtils.searchJSON(
				RiskConstants.FB_SOFTWARE_PROPERTY_ID, software,
				searchColumns, resultColumns);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Result list: " + resultList.size());
		while (resultIter.hasNext()) {
			String str = resultIter.next();
			log.info("Developer found: " + str);
			developerList.add(str);
		}

		assertNotNull("Retrieved list must not be null", developerList);
		assertTrue(developerList.size() >= 1);
	}
	
	@Test
	public void searchSoftwareFromDeveloper() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates software for given developer");
		RiskUtils.initCalculationModel();
		String searchDeveloper = "Adobe Systems";
		log.info("Developer: " + searchDeveloper);
		searchColumns.clear();
		searchColumns.add(RiskConstants.FB_DEVELOPER);
		resultColumns.clear();
		resultColumns.add(RiskConstants.FB_NAME);
		List<String> resultList = RiskUtils.searchJSON(
				RiskConstants.FB_SOFTWARE_PROPERTY_ID, searchDeveloper,
				searchColumns, resultColumns);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Result list: " + resultList.size());
		while (resultIter.hasNext()) {
			String str = resultIter.next();
			log.info("Software found: " + str);
		}

		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void searchExtensionFromDeveloper() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates extension for given developer");
		RiskUtils.initCalculationModel();
		String searchDeveloper = "Adobe Systems";
		log.info("Developer: " + searchDeveloper);
		searchColumns.clear();
		searchColumns.add(RiskConstants.FB_DEVELOPER);
		resultColumns.clear();
		resultColumns.add(RiskConstants.FB_NAME);
		List<String> softwareList = RiskUtils.searchJSON(
				RiskConstants.FB_SOFTWARE_PROPERTY_ID, searchDeveloper,
				searchColumns, resultColumns);

		List<String> extensionList = new ArrayList<String>();
		searchColumns.clear();
		searchColumns.add(RiskConstants.FB_WRITTEN_BY);
		searchColumns.add(RiskConstants.FB_NAME);
		resultColumns.clear();
		resultColumns.add(RiskConstants.FB_EXTENSION);

		Iterator<String> softwareIter = softwareList.iterator();
		log.info("Software list: " + softwareList.size());
		while (softwareIter.hasNext()) {
			String software = softwareIter.next();
			log.info("Software found: " + software);
			List<String> resultList = RiskUtils.searchJSON(
					RiskConstants.FB_FILE_FORMAT_PROPERTY_ID, software,
					searchColumns, resultColumns);
			Iterator<String> resultIter = resultList.iterator();
			log.info("Result list: " + resultList.size());
			while (resultIter.hasNext()) {
				String str = resultIter.next();
				log.info("Extension found: " + str);
				extensionList.add(str);
			}
		}

		assertNotNull("Retrieved list must not be null", extensionList);
		assertTrue(extensionList.size() >= 1);
	}
	
	@Test
	public void searchExtensionFromSoftware() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates extension for given software");
		RiskUtils.initCalculationModel();
		List<String> extensionList = new ArrayList<String>();
		searchColumns.clear();
		searchColumns.add(RiskConstants.FB_WRITTEN_BY);
		searchColumns.add(RiskConstants.FB_NAME);
		resultColumns.clear();
		resultColumns.add(RiskConstants.FB_EXTENSION);

		String software = "Adobe Acrobat";
		log.info("Software found: " + software);
		List<String> resultList = RiskUtils.searchJSON(
				RiskConstants.FB_FILE_FORMAT_PROPERTY_ID, software,
				searchColumns, resultColumns);
		Iterator<String> resultIter = resultList.iterator();
		log.info("Result list: " + resultList.size());
		while (resultIter.hasNext()) {
			String str = resultIter.next();
			log.info("Extension found: " + str);
			extensionList.add(str);
		}

		assertNotNull("Retrieved list must not be null", extensionList);
		assertTrue(extensionList.size() >= 1);
	}
	
	@Test
	public void searchMimeTypeFromFormatName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates mime type for given format name");
		RiskUtils.initCalculationModel();
		searchColumns.clear();
		searchColumns.add(RiskConstants.FB_NAME);
		resultColumns.clear();
		resultColumns.add(RiskConstants.FB_MIME_TYPE);

//		String format = "Portable_Network_Graphics";
//		String format = "Joint_Photographic_Experts_Group";
//		String format = "Microsoft_Word_document";
//		String format = "Graphics Interchange Format";
		String format = "Tagged Image File Format";
//		String format = "Portable Document Format";
		log.info("Format: " + format);
		List<String> resultList = RiskUtils.searchJSON(
				RiskConstants.FB_FILE_FORMAT_PROPERTY_ID, format,
				searchColumns, resultColumns);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Result list: " + resultList.size());
		while (resultIter.hasNext()) {
			String str = resultIter.next();
			log.info("Mime type found: " + str);
		}

		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void searchMimeTypeFromSoftware() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates mime type for given software");
		RiskUtils.initCalculationModel();
		searchColumns.clear();
		searchColumns.add(RiskConstants.FB_WRITTEN_BY);
		searchColumns.add(RiskConstants.FB_NAME);
		resultColumns.clear();
		resultColumns.add(RiskConstants.FB_MIME_TYPE);

		String software = "Microsoft Word";
//		String software = "Corel Paint Shop Pro";
//		String software = "Compuserve";
//		String software = "Adobe Acrobat";
		log.info("Software: " + software);
		List<String> resultList = RiskUtils.searchJSON(
				RiskConstants.FB_FILE_FORMAT_PROPERTY_ID, software,
				searchColumns, resultColumns);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Result list: " + resultList.size());
		while (resultIter.hasNext()) {
			String str = resultIter.next();
			log.info("Mime type found: " + str);
		}

		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void searchOSPlatformFromSoftware() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates OS platform for given software");
		RiskUtils.initCalculationModel();
		searchColumns.clear();
		searchColumns.add(RiskConstants.FB_NAME);
		resultColumns.clear();
		resultColumns.add(RiskConstants.FB_OS_PLATFORM);

		String software = "Microsoft Word";
//		String software = "Adobe Acrobat";
//		String software = "Compuserve";
//		String software = "Corel Paint Shop Pro";
		log.info("Software: " + software);
		List<String> resultList = RiskUtils.searchJSON(
				RiskConstants.FB_SOFTWARE_OS_PLATFORM_PROPERTY_ID, software,
				searchColumns, resultColumns);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Result list: " + resultList.size());
		while (resultIter.hasNext()) {
			String str = resultIter.next();
			log.info("OS platform found: " + str);
		}

		assertNotNull("Retrieved list must not be null", resultList); // no OS platform data available at the moment
	}
	
	@Test
	public void searchProgrammingLanguageFromSoftware() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates programming language for given software");
		RiskUtils.initCalculationModel();
		searchColumns.clear();
		searchColumns.add(RiskConstants.FB_NAME);
		resultColumns.clear();
		resultColumns.add(RiskConstants.FB_PROGRAMMING_LANGUAGE);

		String software = "Eclipse";
		log.info("Software: " + software);
		List<String> resultList = RiskUtils.searchJSON(
				RiskConstants.FB_SOFTWARE_OS_PLATFORM_PROPERTY_ID, software,
				searchColumns, resultColumns);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Result list: " + resultList.size());
		while (resultIter.hasNext()) {
			String str = resultIter.next();
			log.info("Programming language found: " + str);
		}

		assertNotNull("Retrieved list must not be null", resultList); 
		assertTrue(resultList.size() >= 1);
	}
	
}
