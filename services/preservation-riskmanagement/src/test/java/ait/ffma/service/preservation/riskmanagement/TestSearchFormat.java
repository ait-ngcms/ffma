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
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.junit.Test;

import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.CalculationModel;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskUtils;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskAnalysis;


/**
 * This class provides format and extensions searching experiments.
 */
public class TestSearchFormat {

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());

	@Test
	public void searchExtensionFromFormatName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates matching software extensions for given format name");
		String searchFormatName = "Tagged Image File Format";
//		String searchFormatName = "Windows Bitmap";
		List<String> extensionResultList = new ArrayList<String>();
		
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
	    		extensionResultList.add(extensionToken);
	    	}
		}

		assertNotNull("Retrieved extensionList must not be null", extensionList);
		assertTrue(extensionList.size() >= 1);
	}
	
	@Test
	public void searchExtensionFromFormatCode() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates matching software extensions for given format code");
//		String searchFormatCode = "TXT";
//		String searchFormatCode = "JPEG 2000";
		String searchFormatCode = "TIFF";
		List<String> extensionResultList = new ArrayList<String>();
		
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
	    		extensionResultList.add(extensionToken);
	    	}
		}

		assertNotNull("Retrieved extensionList must not be null", extensionList);
		assertTrue(extensionList.size() >= 1);
	}
	
	@Test
	public void searchFormatNameForExtension() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates matching format name for given software extensions");
//		String searchExtension = ".psd";
		String searchExtension = ".jp2";
//		String searchExtension = ".tiff";
		List<String> formatNameList = new ArrayList<String>();
		
		// initialize calculation model
		CalculationModel calculationModel = new CalculationModel();
		RiskAnalysis riskAnalysis = calculationModel.analyze(null, null, null);
		calculationModel.setRiskAnalysis(riskAnalysis);
		List<String> extensionList = RiskUtils.searchFormatNameForExtension(searchExtension);

		Iterator<String> resultIter = formatNameList.iterator();
		log.info("Format name list: " + formatNameList.size());
		while (resultIter.hasNext()) {
			String formatNameStr = resultIter.next();
			log.info("Format name found: " + formatNameStr);
    		formatNameList.add(formatNameStr);
		}

		assertNotNull("Retrieved extensionList must not be null", extensionList);
		assertTrue(extensionList.size() >= 1);
	}
	
	@Test
	public void searchFormatCodeFromExtension() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates matching format code for given extension");
//		String searchExtension = ".xml";
		String searchExtension = ".tif";
		List<String> formatCodeList = new ArrayList<String>();
		
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.searchFormatCodeFromExtension(searchExtension);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Format code list: " + resultList.size());
		while (resultIter.hasNext()) {
			String formatCodeStr = resultIter.next();
			log.info("Format code found: " + formatCodeStr);
    		formatCodeList.add(formatCodeStr);
		}

		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void searchFormatCodeFromFormatName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates matching format code for given format name");
		String searchFormatName = "Tagged Image File Format";
		List<String> formatCodeList = new ArrayList<String>();
		
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.searchFormatCodeFromFormatName(searchFormatName);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Format code list: " + resultList.size());
		while (resultIter.hasNext()) {
			String formatCodeStr = resultIter.next();
			log.info("Format code found: " + formatCodeStr);
    		formatCodeList.add(formatCodeStr);
		}

		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void searchFormatNameFromFormatCode() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates matching format name for given format code");
		String searchFormatCode = "TIFF";
		List<String> formatNameResultList = new ArrayList<String>();
		
		// initialize calculation model
		CalculationModel calculationModel = new CalculationModel();
		RiskAnalysis riskAnalysis = calculationModel.analyze(null, null, null);
		calculationModel.setRiskAnalysis(riskAnalysis);
		List<String> resultList = RiskUtils.searchFormatNameFromFormatCode(searchFormatCode);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Format name list: " + resultList.size());
		while (resultIter.hasNext()) {
			String formatNameStr = resultIter.next();
			log.info("Format name found: " + formatNameStr);
    		formatNameResultList.add(formatNameStr);
		}

		assertNotNull("Retrieved list must not be null", formatNameResultList);
		assertTrue(formatNameResultList.size() >= 1);
	}
	
	@Test
	public void searchFormatsFromFormatName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates matching formats for given format name");
//		String searchFormatName = "JPEG";
		String searchFormatName = "TIF";
		
		RiskUtils.initCalculationModel();
		List<Map<String, String>> resultList = RiskUtils.searchFormatsFromFormatName(searchFormatName);

		Iterator<Map<String, String>> resultIter = resultList.iterator();
		log.info("Formats list size: " + resultList.size());
		while (resultIter.hasNext()) {
			Map<String, String> formatsMap = resultIter.next();
			Set<String> dbpediaSet = formatsMap.keySet();
			for (String key : dbpediaSet) {
				log.info("DBPedia formats result: key=" + key + " , value=" + formatsMap.get(key));
			}
		}

		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
	}

	@Test
	public void searchMimeTypeFromExtension() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates matching mime type for given extension");
//		String searchExtension = ".doc";
		String searchExtension = ".tif";
		List<String> mimeTypeList = new ArrayList<String>();
		
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.searchMimeTypeFromExtension(searchExtension);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Mime type list: " + resultList.size());
		while (resultIter.hasNext()) {
			String mimtTypeStr = resultIter.next();
			log.info("Mime type found: " + mimtTypeStr);
    		mimeTypeList.add(mimtTypeStr);
		}

		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void searchMimeTypeFromFormatName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates matching mime type for given format name");
		String searchFormatName = "Tagged Image File Format";
		List<String> mimeTypeList = new ArrayList<String>();
		
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.searchMimeTypeFromFormatName(searchFormatName);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Mime type list: " + resultList.size());
		while (resultIter.hasNext()) {
			String formatCodeStr = resultIter.next();
			log.info("Mime type found: " + formatCodeStr);
    		mimeTypeList.add(formatCodeStr);
		}

		assertNotNull("Retrieved list must not be null", resultList);
		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void searchMimeTypeFromFormatCode() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates matching mime type for given format code");
		String searchFormatCode = "TIFF";
		List<String> mimeTypeList = new ArrayList<String>();
		
		// initialize calculation model
		CalculationModel calculationModel = new CalculationModel();
		RiskAnalysis riskAnalysis = calculationModel.analyze(null, null, null);
		calculationModel.setRiskAnalysis(riskAnalysis);
		List<String> resultList = RiskUtils.searchMimeTypeFromFormatCode(searchFormatCode);

		Iterator<String> resultIter = resultList.iterator();
		log.info("Mime type list: " + resultList.size());
		while (resultIter.hasNext()) {
			String mimeTypeStr = resultIter.next();
			log.info("Mime type found: " + mimeTypeStr);
    		mimeTypeList.add(mimeTypeStr);
		}

		assertNotNull("Retrieved list must not be null", mimeTypeList);
		assertTrue(mimeTypeList.size() >= 1);
	}
	
}
