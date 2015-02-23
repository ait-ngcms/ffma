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
 * This class provides software properties searching experiments.
 */
public class TestSoftwareProperties {

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());
	
	@Test
	public void evaluateSoftwareComputerPlatformByName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates software computer platform for given software name");
//		String softwareName = "Powerpoint";
//		String softwareName = "Corel DRAW";
//		String softwareName = "Adobe Photoshop";
//		String softwareName = "Illustrator";
//		String softwareName = "Framemaker";
//		String softwareName = "Adobe Acrobat";
//		String softwareName = "GoLive";
		String softwareName = "AutoCAD";
//		String softwareName = "QuickView";
//		String softwareName = "Google Docs";
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.evaluateSoftwareComputerPlatformByName(softwareName);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("found: " + resultIter.next());
		}

		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void evaluateSoftwareProgrammingLanguageByName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates software programming language for given software name");
		String softwareName = "Mozilla Firefox";
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.evaluateSoftwareProgrammingLanguageByName(softwareName);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("found: " + resultIter.next());
		}

		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void evaluateSoftwareHomepageByName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates software homepage for given software name");
//		String softwareName = "AutoCAD";
//		String softwareName = "SimpleText";
//		String softwareName = "Powerpoint";
//		String softwareName = "QuickView";
		String softwareName = "Adobe Photoshop";
//		String softwareName = "Mozilla Firefox";
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.evaluateSoftwareHomepageByName(softwareName);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("found: " + resultIter.next());
		}

		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void evaluateSoftwareNameByVersion() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates software name for given software version");
		String softwareVersion = "4";
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.evaluateSoftwareNameByVersion(softwareVersion);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("found: " + resultIter.next());
		}

		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void evaluateSoftwareLatestReleaseVersionByName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates software latest release version for given software name");
		String softwareName = "Adobe Photoshop";
//		String softwareName = "AutoCAD";
//		String softwareName = "SimpleText";
//		String softwareName = "QuickView";
//		String softwareName = "FileMaker";
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.evaluateSoftwareLatestReleaseVersionByName(softwareName);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("found: " + resultIter.next());
		}

		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void evaluateSoftwareLicenseByName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates software license for given software name");
		String softwareName = "AutoCAD";
//		String softwareName = "SimpleText";
//		String softwareName = "Powerpoint";
//		String softwareName = "QuickView";
//		String softwareName = "Adobe Photoshop";
//		String softwareName = "FileMaker";
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.evaluateSoftwareLicenseByName(softwareName);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("found: " + resultIter.next());
		}

		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}
	
	@Test
	public void evaluateSoftwareGenreByName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates software genre for given software name");
//		String softwareName = "SimpleText";
//		String softwareName = "Powerpoint";
//		String softwareName = "QuickView";
//		String softwareName = "Adobe Photoshop";
		String softwareName = "QuickView";
//		String softwareName = "FileMaker";
		RiskUtils.initCalculationModel();
		List<String> resultList = RiskUtils.evaluateSoftwareGenreByName(softwareName);
		Iterator<String> resultIter = resultList.iterator();
		log.info("list size: " + resultList.size());
		while (resultIter.hasNext()) {
			log.info("found: " + resultIter.next());
		}

		assertNotNull("Retrieved list must not be null", resultList);
//		assertTrue(resultList.size() >= 1);
	}
	
}
