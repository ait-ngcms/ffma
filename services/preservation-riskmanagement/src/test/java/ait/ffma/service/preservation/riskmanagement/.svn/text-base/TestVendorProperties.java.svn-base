package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskUtils;

/**
 * This class provides vendor properties searching experiments.
 */
public class TestVendorProperties {

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());
	
	@Test
	public void evaluateVendorRevenueByName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates vendor revenue for given vendor name");
		String searchVendorName = "Adobe Systems";
//		String searchVendorName = "Adobe_Systems";
//		String searchVendorName = "Adobe_Systems_Incorporated";
//		String searchVendorName = "Adobe Systems Incorporated";
//		String searchVendorName = "Aldus";
//		String searchVendorName = "Aldus Corporation";
//		String searchVendorName = "Apple Inc.";
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.evaluateVendorRevenueByName(searchVendorName);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("revenue found: " + resultIter.next());
		}

		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void evaluateVendorLocationByName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates vendor location for given vendor name");
		String searchVendorName = "Adobe Systems";
//		String searchVendorName = "Kia Motors";
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.evaluateVendorLocationByName(searchVendorName);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("location found: " + resultIter.next());
		}

		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void evaluateVendorKeyPeopleByName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates vendor key people for given vendor name");
		String searchVendorName = "Apple Inc.";
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.evaluateVendorKeyPeopleByName(searchVendorName);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("key people found: " + resultIter.next());
		}

		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void evaluateVendorHomepageByName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates vendor homepage for given vendor name");
		String searchVendorName = "Apple Inc.";
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.evaluateVendorHomepageByName(searchVendorName);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("homepage found: " + resultIter.next());
		}

		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void evaluateVendorEmployeesNumberByName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates vendor employees number for given vendor name");
		String searchVendorName = "Adobe Systems";
//		String searchVendorName = "Apple Inc.";
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.evaluateVendorEmployeesNumberByName(searchVendorName);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("employees number found: " + resultIter.next());
		}

		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}
	
}
