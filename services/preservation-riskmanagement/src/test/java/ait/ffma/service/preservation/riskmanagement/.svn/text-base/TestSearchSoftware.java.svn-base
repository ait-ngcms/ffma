package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.junit.Test;

import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.CalculationModel;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskUtils;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskAnalysis;

/**
 * This class provides software searching experiments.
 */
public class TestSearchSoftware {

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());


	@Test
	public void searchCompatibleSoftwareForExtension() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates compatible software for given extension");
//		String searchExtensionName = "dae";
//		String searchExtensionName = "J2K";
		String searchExtensionName = "PDF";
//		String searchExtensionName = "TIFF";
//		String searchExtensionName = "GIF";
		List<String> softwareList = new ArrayList<String>();
		
		// initialize calculation model
		CalculationModel calculationModel = new CalculationModel();
		RiskAnalysis riskAnalysis = calculationModel.analyze(null, null, null);
		calculationModel.setRiskAnalysis(riskAnalysis);
		List<String> softwareListTmp = RiskUtils.searchCompatibleSoftwareForExtension(searchExtensionName);
		Iterator<String> softwareTmpIter = softwareListTmp.iterator();
		while (softwareTmpIter.hasNext()) {
			String softwareTmpStr = softwareTmpIter.next();
			softwareList.add(softwareTmpStr);
		}

		Iterator<String> softwareIter = softwareList.iterator();
		log.info("compatible software count: " + softwareList.size());
		while (softwareIter.hasNext()) {
			log.info("Software found: " + softwareIter.next());
		}

		assertNotNull("Retrieved extensionList must not be null", softwareList);
		assertTrue(softwareList.size() >= 1);
	}
	
	@Test
	public void searchCompatibleSoftwareForFormatName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluate software tools for given format name");
//		String searchFormatName = "J2K";
//		String searchFormatName = "JPEG 2000";
//		String searchFormatName = "PDF";
		String searchFormatName = "TIFF";
//		String searchFormatName = "JPEG";
		List<String> softwareList = new ArrayList<String>();
		
		// initialize calculation model
		CalculationModel calculationModel = new CalculationModel();
		RiskAnalysis riskAnalysis = calculationModel.analyze(null, null, null);
		calculationModel.setRiskAnalysis(riskAnalysis);
		List<String> extensionList = RiskUtils.searchExtensionFromFormatName(searchFormatName);

		Iterator<String> resultIter = extensionList.iterator();
		log.info("Extensions list: " + extensionList.size());
		while (resultIter.hasNext()) {
			String extensionsStr = resultIter.next();
			log.info("Extensions found: " + extensionsStr);
	    	// Split by comma ', ' 
	        StringTokenizer st = new StringTokenizer(extensionsStr, ", ");	 
	    	while(st.hasMoreElements()){
	    		String extensionToken = (String) st.nextElement();
	    		extensionToken = extensionToken.substring(1).toUpperCase();
	    		log.info("extensionToken: " + extensionToken);
	    		List<String> softwareListTmp = RiskUtils.searchCompatibleSoftwareForExtension(extensionToken);
	    		Iterator<String> softwareTmpIter = softwareListTmp.iterator();
	    		while (softwareTmpIter.hasNext()) {
	    			String softwareTmpStr = softwareTmpIter.next();
	    			softwareList.add(softwareTmpStr);
	    		}
	    	}
		}

		Iterator<String> softwareIter = softwareList.iterator();
		log.info("compatible software count: " + softwareList.size());
		while (softwareIter.hasNext()) {
			log.info("Software found: " + softwareIter.next());
		}

		assertNotNull("Retrieved extensionList must not be null", extensionList);
		assertTrue(extensionList.size() >= 1);
		assertNotNull("Retrieved extensionList must not be null", softwareList);
		assertTrue(softwareList.size() >= 1);
	}
	
	@Test
	public void searchCompatibleSoftwareForFormatCode() throws MalformedURLException, URISyntaxException {
		log.info("test evaluate software tools for given format code");
//		String searchFormatCode = "MPEG4";
		String searchFormatCode = "TIFF";
		List<String> softwareList = new ArrayList<String>();
		
		// initialize calculation model
		CalculationModel calculationModel = new CalculationModel();
		RiskAnalysis riskAnalysis = calculationModel.analyze(null, null, null);
		calculationModel.setRiskAnalysis(riskAnalysis);
		List<String> extensionList = RiskUtils.searchExtensionFromFormatCode(searchFormatCode);

		Iterator<String> resultIter = extensionList.iterator();
		log.info("Extensions list: " + extensionList.size());
		while (resultIter.hasNext()) {
			String extensionsStr = resultIter.next();
			log.info("Extensions found: " + extensionsStr);
	    	// Split by comma ', ' 
	        StringTokenizer st = new StringTokenizer(extensionsStr, ", ");	 
	    	while(st.hasMoreElements()){
	    		String extensionToken = (String) st.nextElement();
	    		extensionToken = extensionToken.substring(1).toUpperCase();
	    		log.info("extensionToken: " + extensionToken);
	    		List<String> softwareListTmp = RiskUtils.searchCompatibleSoftwareForExtension(extensionToken);
	    		Iterator<String> softwareTmpIter = softwareListTmp.iterator();
	    		while (softwareTmpIter.hasNext()) {
	    			String softwareTmpStr = softwareTmpIter.next();
	    			softwareList.add(softwareTmpStr);
	    		}
	    	}
		}

		Iterator<String> softwareIter = softwareList.iterator();
		log.info("compatible software count: " + softwareList.size());
		while (softwareIter.hasNext()) {
			log.info("Software found: " + softwareIter.next());
		}

		assertNotNull("Retrieved extensionList must not be null", extensionList);
		assertTrue(extensionList.size() >= 1);
		assertNotNull("Retrieved extensionList must not be null", softwareList);
		assertTrue(softwareList.size() >= 1);
	}
	
	@Test
	public void searchSoftwareForMimeAndVersion() throws MalformedURLException, URISyntaxException {
		log.info("test evaluate software for given mime type and version");
		String mime = "image/vnd.ms-photo";
		String version = "1";
		RiskUtils.initCalculationModel();
		List<String> softwareList = RiskUtils.searchSoftwareForMimeAndVersion(mime, version);

		Iterator<String> resultIter = softwareList.iterator();
		log.info("Software list size: " + softwareList.size());
		while (resultIter.hasNext()) {
			String softwareStr = resultIter.next();
			log.info("Software found: " + softwareStr);
		}

		assertNotNull("Retrieved extensionList must not be null", softwareList);
		assertTrue(softwareList.size() >= 1);
	}

	@Test
	public void searchSoftwareForMime() throws MalformedURLException, URISyntaxException {
		log.info("test evaluate software for given mime type");
//		String mime = "image/jp2";
		String mime = "image/tiff";
		RiskUtils.initCalculationModel();
		List<String> softwareList = RiskUtils.searchSoftwareForMime(mime);

		Iterator<String> resultIter = softwareList.iterator();
		log.info("Software list size: " + softwareList.size());
		while (resultIter.hasNext()) {
			String softwareStr = resultIter.next();
			log.info("Software found: " + softwareStr);
		}

		assertNotNull("Retrieved extensionList must not be null", softwareList);
		assertTrue(softwareList.size() >= 1);
	}

	@Test
	public void searchSoftwareForStandard() throws MalformedURLException, URISyntaxException {
		log.info("test evaluate software for given standard");
		String standard = "ISO/IEC 10918"; // JPEG
		RiskUtils.initCalculationModel();
		List<String> softwareList = RiskUtils.searchSoftwareForStandard(standard);

		Iterator<String> resultIter = softwareList.iterator();
		log.info("Software list size: " + softwareList.size());
		while (resultIter.hasNext()) {
			String softwareStr = resultIter.next();
			log.info("Software found: " + softwareStr);
		}

		assertNotNull("Retrieved extensionList must not be null", softwareList);
		assertTrue(softwareList.size() >= 1);
	}

	@Test
	public void searchSoftwareReleasedAfter() throws MalformedURLException, URISyntaxException {
		log.info("test evaluate software released after given date");
		String releaseDate = "1985"; 
		RiskUtils.initCalculationModel();
		List<String> softwareList = RiskUtils.searchSoftwareReleasedAfter(releaseDate);

		Iterator<String> resultIter = softwareList.iterator();
		log.info("Software list size: " + softwareList.size());
		while (resultIter.hasNext()) {
			String softwareStr = resultIter.next();
			log.info("Software found: " + softwareStr);
		}

		assertNotNull("Retrieved extensionList must not be null", softwareList);
		assertTrue(softwareList.size() >= 1);
	}

	@Test
	public void searchFileExtensionsForSoftware() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates file extensions for given software");
//		String searchSoftwareName = "SimpleText";
//		String searchSoftwareName = "Seashore";
		String searchSoftwareName = "Adobe Flash";
		List<String> extensionsList = new ArrayList<String>();
		RiskUtils.initCalculationModel();
		List<String> extensionsListTmp = RiskUtils.searchFileExtensionsForSoftware(searchSoftwareName);
		Iterator<String> extensionsTmpIter = extensionsListTmp.iterator();
		while (extensionsTmpIter.hasNext()) {
			String extensionsTmpStr = extensionsTmpIter.next();
			extensionsList.add(extensionsTmpStr);
		}

		Iterator<String> extensionsIter = extensionsList.iterator();
		log.info("compatible extensions count: " + extensionsList.size());
		while (extensionsIter.hasNext()) {
			log.info("extensions found: " + extensionsIter.next());
		}

		assertNotNull("Retrieved extensionList must not be null", extensionsList);
		assertTrue(extensionsList.size() >= 1);
	}
	
}
