package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskUtils;
import ait.ffma.service.preservation.riskmanagement.mappingregistry.DBPediaRegistry;
import ait.ffma.service.preservation.riskmanagement.mappingregistry.MappingRegistry.RegistryTypesEnum;

/**
 * This class provides vendor searching experiments.
 */
public class TestSearchVendor {

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());

	@Test
	public void searchSoftwareForVendor() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates software for given vendor name");
		DBPediaRegistry dbPediaRegistry = new DBPediaRegistry();
		String searchVendorName = dbPediaRegistry.getValue(RegistryTypesEnum.SoftwareVendor, RiskConstants.REG_ADOBE);
		log.info("dbpedia://softwarevendor/adobe: " + searchVendorName);	
		List<String> vendors = RiskUtils.splitString(searchVendorName, RiskConstants.STRING_SEPARATOR);
		List<String> softwareList = null;
		boolean found = false;
		Iterator<String> vendorsIter = vendors.iterator();
		while (vendorsIter.hasNext()) {
			searchVendorName = vendorsIter.next();
			log.info("searchVendorName: " + searchVendorName);
			if (found) {
				break;
			}

			RiskUtils.initCalculationModel();
			softwareList = RiskUtils.searchSoftwareForVendor(searchVendorName);
	
			Iterator<String> resultIter = softwareList.iterator();
			log.info("Software list: " + softwareList.size());
			if (softwareList.size() > 0) {
				found = true;
			}
			while (resultIter.hasNext()) {
				String softwareStr = resultIter.next();
				log.info("Software found: " + softwareStr);
			}
		}

		assertNotNull("Retrieved extensionList must not be null", softwareList);
		assertTrue(softwareList.size() >= 1);
	}
	
	@Test
	public void searchVendorForSoftware() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates vendor for given software name");
		String searchSoftwareName = "Adobe Photoshop";
//		String searchSoftwareName = "SimpleText";
//		String searchSoftwareName = "Seashore";
//		String searchSoftwareName = "QuickView";
//		String searchSoftwareName = "Dcraw";
		
		RiskUtils.initCalculationModel();
		List<String> vendorList = RiskUtils.searchVendorForSoftware(searchSoftwareName);

		Iterator<String> resultIter = vendorList.iterator();
		log.info("Vendor list: " + vendorList.size());
		while (resultIter.hasNext()) {
			String vendorStr = resultIter.next();
			log.info("Vendor found: " + vendorStr);
		}

		assertNotNull("Retrieved extensionList must not be null", vendorList);
		assertTrue(vendorList.size() >= 1);
	}
	
	@Test
	public void searchFileExtensionsForVendor() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates file extensions for given vendor");
		String searchVendorName = "Adobe Systems";
		List<String> extensionsList = new ArrayList<String>();
		RiskUtils.initCalculationModel();
		List<String> extensionsListTmp = RiskUtils.searchFileExtensionsForVendor(searchVendorName);
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

		assertNotNull("Retrieved list must not be null", extensionsList);
//		assertTrue(extensionsList.size() >= 1);
	}
	
	@Test
	public void searchFileFormatsForVendor() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates file formats for given vendor name");
		String searchVendorName = "Adobe Systems";
		List<String> fileFormatsList = new ArrayList<String>();
		RiskUtils.initCalculationModel();
		List<String> fileFormatsListTmp = RiskUtils.searchFileFormatsForVendor(searchVendorName);
		Iterator<String> fileFormatsTmpIter = fileFormatsListTmp.iterator();
		while (fileFormatsTmpIter.hasNext()) {
			String extensionsTmpStr = fileFormatsTmpIter.next();
			fileFormatsList.add(extensionsTmpStr);
		}

		Iterator<String> extensionsIter = fileFormatsList.iterator();
		log.info("file formats count: " + fileFormatsList.size());
		while (extensionsIter.hasNext()) {
			log.info("file format found: " + extensionsIter.next());
		}

		assertNotNull("Retrieved list must not be null", fileFormatsList);
//		assertTrue(fileFormatsList.size() >= 1);
	}
	
	@Test
	public void searchVendorForFileExtension() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates vendor for given file extension");
		String searchExtension = "TIF";
//		String searchExtension = ".pdf";
		RiskUtils.initCalculationModel();
		List<String> vendorsList = RiskUtils.searchVendorForFileExtension(searchExtension);
		Iterator<String> vendorsIter = vendorsList.iterator();
		log.info("compatible vendors count: " + vendorsList.size());
		while (vendorsIter.hasNext()) {
			log.info("vendor found: " + vendorsIter.next());
		}

		assertNotNull("Retrieved list must not be null", vendorsList);
//		assertTrue(vendorsList.size() >= 1);
	}
	
	@Test
	public void searchVendorForFormatName() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates vendor for given format name");
		String formatName = "Portable Document Format";
		RiskUtils.initCalculationModel();
		List<String> vendorsList = RiskUtils.searchVendorForFormatName(formatName);
		Iterator<String> vendorsIter = vendorsList.iterator();
		log.info("compatible vendors count: " + vendorsList.size());
		while (vendorsIter.hasNext()) {
			log.info("vendor found: " + vendorsIter.next());
		}

		assertNotNull("Retrieved list must not be null", vendorsList);
		assertTrue(vendorsList.size() >= 1);
	}
	
	@Test
	public void searchVendorForFormatCode() throws MalformedURLException, URISyntaxException {
		log.info("test evaluates vendor for given format code");
		String formatCode = "GIF";
		RiskUtils.initCalculationModel();
		List<String> vendorsList = RiskUtils.searchVendorForFormatCode(formatCode);
		Iterator<String> vendorsIter = vendorsList.iterator();
		log.info("compatible vendors count: " + vendorsList.size());
		while (vendorsIter.hasNext()) {
			log.info("vendor found: " + vendorsIter.next());
		}

		assertNotNull("Retrieved list must not be null", vendorsList);
		assertTrue(vendorsList.size() >= 1);
	}
	
}
