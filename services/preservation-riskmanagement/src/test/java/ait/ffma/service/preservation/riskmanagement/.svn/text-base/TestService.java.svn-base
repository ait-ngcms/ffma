package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.junit.Test;

import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODUtils;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.Datatype;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODProperty;
import ait.ffma.service.preservation.riskmanagement.api.lod.lodmodel.LODType;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.DBPediaConnector;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.FreebaseConnector;
import ait.ffma.service.preservation.riskmanagement.api.preservationwatch.PronomConnector;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskUtils;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.Metric;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskClassification;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskProperty;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskPropertySet;

/**
 * This class provides experiments for server methods.
 */
public class TestService {

	private static final double TESTDOUBLE = 12.0;
	/**
	 * Test values
	 */
	private static final String COMMA = ",";
	private static final String TEST2 = "test2";
	private static final String TEST1 = "test1";
	private static final String VALUE = "value";
	private static final String PROPERTY_NAME = "propertyName";
	
	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());
	
	/**
	 * This test examines the methods of LODProperty object.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void lodProperty() throws MalformedURLException, URISyntaxException {
		log.info("test methods of LODProperty object");
		LODProperty ma = new LODProperty();
		ma.setId(VALUE);
		assertTrue("Result should be value", ma.getId().equals(VALUE));
		ma.setName(VALUE);
		assertTrue("Result should be value", ma.getName().equals(VALUE));
		ma.setLodClass(VALUE);
		assertTrue("Result should be value", ma.getLodClass().equals(VALUE));
		ma.setQualifiedName(VALUE);
		assertTrue("Result should be value", ma.getQualifiedName().equals(VALUE));
		ma.setVersion(VALUE);
		assertTrue("Result should be value", ma.getVersion().equals(VALUE));
		ma.setDescription(VALUE);
		assertTrue("Result should be value", ma.getDescription().equals(VALUE));
	}

	/**
	 * This test checks the risk level count.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void riskLevel() throws MalformedURLException, URISyntaxException {
		log.info("test the risk levels count");
		int count = RiskConstants.RiskLevelEnum.values().length;
		assertTrue("The count should be 3", count == 3);
	}
	
	/**
	 * This test checks the csv enumeration types count.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void csvEnumTypes() throws MalformedURLException, URISyntaxException {
		log.info("test the csv enumeration count");
		int count = RiskConstants.CsvEnum.values().length;
		assertTrue("The count should be 15", count == 15);
	}
	
	/**
	 * This test checks the provenance types count.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void provenanceTypes() throws MalformedURLException, URISyntaxException {
		log.info("test the provenance types count");
		int count = RiskConstants.ProvenanceEnum.values().length;
		assertTrue("The count should be 11", count == 11);
	}
	
	/**
	 * This test checks the context types count.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void contextTypes() throws MalformedURLException, URISyntaxException {
		log.info("test the context types count");
		int count = RiskConstants.ContextEnum.values().length;
		assertTrue("The count should be 17", count == 17);
	}
	
	/**
	 * This test checks the accessibility types count.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void accessibilityTypes() throws MalformedURLException, URISyntaxException {
		log.info("test the accessibility types count");
		int count = RiskConstants.AccessibilityEnum.values().length;
		assertTrue("The count should be 7", count == 7);
	}
	
	/**
	 * This test checks the risk report types count.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void riskReportTypes() throws MalformedURLException, URISyntaxException {
		log.info("test the risk report types count");
		int count = RiskConstants.RiskReportTypesEnum.values().length;
		assertTrue("The count should be 4", count == 4);
	}
	
	/**
	 * This test checks the LOD repositories types count.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void lodRepositoriesTypes() throws MalformedURLException, URISyntaxException {
		log.info("test the LOD repositories types count");
		int count = LODConstants.LODRepositories.values().length;
		assertTrue("The count should be 5", count == 5);
	}
	
	/**
	 * This test checks the preservation statistics types count.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void preservationStatisticsTypes() throws MalformedURLException, URISyntaxException {
		log.info("test the LOD repositories types count");
		int count = LODConstants.PreservationStatisticsTypes.values().length;
		assertTrue("The count should be 4", count == 4);
	}
	
	/**
	 * This test checks the format CSV types count.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void foramtCsvTypes() throws MalformedURLException, URISyntaxException {
		log.info("test the format CSV types count");
		int count = LODConstants.FormatCsvTypes.values().length;
		assertTrue("The count should be 16", count == 16);
	}
	
	/**
	 * This test checks the software CSV types count.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void softwareCsvTypes() throws MalformedURLException, URISyntaxException {
		log.info("test the software CSV types count");
		int count = LODConstants.SoftwareCsvTypes.values().length;
		assertTrue("The count should be 9", count == 9);
	}
	
	/**
	 * This test checks the vendors CSV types count.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void vendorsCsvTypes() throws MalformedURLException, URISyntaxException {
		log.info("test the vendors CSV types count");
		int count = LODConstants.VendorsCsvTypes.values().length;
		assertTrue("The count should be 9", count == 9);
	}
	
	/**
	 * This test initializes a Freebase connector object
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void freebaseConnector() throws MalformedURLException, URISyntaxException {
		log.info("test the freebase connector");
		FreebaseConnector fc = new FreebaseConnector();
		assertNotNull("The resulting object should not be null", fc);		
		assertNull("The resulting object should be null", fc.getConnector());
		assertTrue("The result string length should be > 0", fc.getID().length() > 0);
		assertTrue("The result string length should be > 0", fc.getName().length() > 0);
		assertTrue("The result string length should be > 0", fc.getLocation().toString().length() > 0);
		assertTrue("The result string length should be > 0", fc.getProtocol().length() > 0);
		assertNull("The resulting object should be null", fc.getSoftwareVendors());
		assertNull("The resulting object should be null", fc.getNameSpace());
	}
	
	/**
	 * This test initializes a Pronom connector object
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void pronomConnector() throws MalformedURLException, URISyntaxException {
		log.info("test the pronom connector");
		PronomConnector pc = new PronomConnector();
		assertNotNull("The resulting object should not be null", pc);		
		assertNull("The resulting object should be null", pc.getConnector());
		assertTrue("The result string length should be > 0", pc.getID().length() > 0);
		assertTrue("The result string length should be > 0", pc.getName().length() > 0);
		assertTrue("The result string length should be > 0", pc.getLocation().toString().length() > 0);
		assertTrue("The result string length should be > 0", pc.getProtocol().length() > 0);
		assertNull("The resulting object should be null", pc.getSoftwareVendors());
		assertNull("The resulting object should be null", pc.getNameSpace());
	}
	
	/**
	 * This test initializes a DBPedia connector object
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void dbpediaConnector() throws MalformedURLException, URISyntaxException {
		log.info("test the DBPedia connector");
		DBPediaConnector dc = new DBPediaConnector();
		assertNotNull("The resulting object should not be null", dc);		
		assertNull("The resulting object should be null", dc.getConnector());
		assertTrue("The result string length should be > 0", dc.getID().length() > 0);
		assertTrue("The result string length should be > 0", dc.getName().length() > 0);
		assertTrue("The result string length should be > 0", dc.getLocation().toString().length() > 0);
		assertTrue("The result string length should be > 0", dc.getProtocol().length() > 0);
		assertNull("The resulting object should be null", dc.getSoftwareVendors());
		assertNull("The resulting object should be null", dc.getNameSpace());
	}
	
	/**
	 * This test initializes a risk classification object
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void riskClassification() throws MalformedURLException, URISyntaxException {
		log.info("test the risk classification");
		RiskClassification rc = new RiskClassification();
		assertNotNull("The resulting object should not be null", rc);		
		rc.setWeight(TESTDOUBLE);
		assertNotNull("The resulting object should not be null", rc.getWeight());
		assertTrue("The result string length should be > 0", rc.getWeight() == TESTDOUBLE);
	}
	
	/**
	 * This test initializes a risk property object
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void riskProperty() throws MalformedURLException, URISyntaxException {
		log.info("test the risk property");
		RiskProperty rp = new RiskProperty();
		assertNotNull("The resulting object should not be null", rp);		
	    rp.setVersion(VALUE);
		assertTrue("Result should be value", rp.getVersion().equals(VALUE));
		rp.setGroup(VALUE);
		assertTrue("Result should be value", rp.getGroup().equals(VALUE));
	}
	
	/**
	 * This test initializes a risk property set object
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void riskPropertySet() throws MalformedURLException, URISyntaxException {
		log.info("test the risk property set");
		RiskPropertySet rps = new RiskPropertySet();
		assertNotNull("The resulting object should not be null", rps);		
	    rps.setId(VALUE);
		assertTrue("Result should be value", rps.getId().equals(VALUE));
		rps.setVersion(VALUE);
		assertTrue("Result should be value", rps.getVersion().equals(VALUE));
	}
	
	/**
	 * This test examines the LODUtils class methods.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void lodUtils() throws MalformedURLException, URISyntaxException {
		log.info("test the LODUtils class methods");
		assertTrue("The result string length should be > 0", LODUtils.generateID().length() > 0);
		String initStr = TEST1 + COMMA + TEST2;
		assertTrue("Resulted string list should contain two entries", LODUtils.splitString(initStr, COMMA).size() == 2);
	}
	
	/**
	 * This test examines the RiskUtils class methods.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void riskUtils() throws MalformedURLException, URISyntaxException {
		log.info("test the RiskUtils class methods");
		assertTrue("The result string length should be > 0", RiskUtils.generateID().length() > 0);
		String initStr = TEST1 + COMMA + TEST2;
		assertTrue("Resulted string list should contain two entries", RiskUtils.splitString(initStr, COMMA).size() == 2);
	}
	
	/**
	 * This test checks the LODType enumeration.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void lodType() throws MalformedURLException, URISyntaxException {
		log.info("test the format LOD types enumeration");		
		int count = LODType.values().length;
		assertTrue("The count should be 3", count == 3);
		assertTrue("The value should be AGG", LODType.AGG.value().equals(LODType.AGG.name()));
	}
	
	/**
	 * This test checks the Datatype enumeration.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void checkDatatype() throws MalformedURLException, URISyntaxException {
		log.info("test the datatype enumeration");		
		int count = Datatype.values().length;
		assertTrue("The count should be 5", count == 5);
		assertTrue("The value should be String", Datatype.STRING.value().equals(Datatype.STRING.name()));
	}
	
	/**
	 * This test checks the Metric enumeration.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void checkMetric() throws MalformedURLException, URISyntaxException {
		log.info("test the metric enumeration");		
		int count = Metric.values().length;
		assertTrue("The count should be 5", count == 5);
		assertTrue("The value should be String", Metric.STRING.value().equals(Metric.STRING.name()));
	}
	
}
