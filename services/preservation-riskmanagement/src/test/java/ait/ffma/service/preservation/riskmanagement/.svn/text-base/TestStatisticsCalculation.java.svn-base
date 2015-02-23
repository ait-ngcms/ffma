package ait.ffma.service.preservation.riskmanagement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

import ait.ffma.service.preservation.riskmanagement.api.lod.LODStatisticsUtils;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.policy.ContentClassificationPolicy;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskScoreMeasurement;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskScoreMeasurement.AgentType;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskScoreMeasurementImpl;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.ObjectFactory;

/**
 * This class provides experiments for statistics calculations.
 */
public class TestStatisticsCalculation {

	private static final String PRESERVATION_RISKMANAGEMENT = "preservation-riskmanagement";
	
	/**
	 * Test values
	 */
	private static final String COMMA = ",";
	private static final String TEST3 = "test3";
	private static final String TEST2 = "test2";
	private static final String TEST1 = "test1";
	
	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass().toString());
	
	/**
	 * This method cleans LOD lists.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void clearLists() throws MalformedURLException, URISyntaxException {
		log.info("test lists cleaning");
		LODStatisticsUtils.clearCache();
		assertTrue("Retrieved list must be null", LODStatisticsUtils.getLodFormats().size() == 0);
		assertTrue("Retrieved list must be null", LODStatisticsUtils.getLodSoftware().size() == 0);
		assertTrue("Retrieved list must be null", LODStatisticsUtils.getLodVendors().size() == 0);
	}

	/**
	 * This test initializes the empty file format list from all LOD repositories.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void initEmptyFileFormatListFromAllRepositories() throws MalformedURLException, URISyntaxException {
		log.info("test empty file format list initialization");
		LODStatisticsUtils.initFileFormatListFromAllRepositories(null, null, null, null);
		assertTrue("Retrieved list must be null", LODStatisticsUtils.getDipFormatIdList().size() == 0);
	}

	/**
	 * This test converts list to string with fields separated by comma.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void getStringFromList() throws MalformedURLException, URISyntaxException {
		log.info("test empty file format list initialization");
		List<String> resList = new ArrayList<String>();
		resList.add(TEST1);
		resList.add(TEST2);
		resList.add(TEST3);
		String res = LODStatisticsUtils.getStringFromList(resList);
		assertTrue("Resulted string should contain commas", res.contains(COMMA));
	}

	/**
	 * This test checks the existing array and add new string value to this array.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void addValueToStringArray() throws MalformedURLException, URISyntaxException {
		log.info("test string array extension with additional value");
		String [] initArr = {TEST1, TEST2};
		String [] resArr = LODStatisticsUtils.addValueToStringArray(initArr, TEST3);
		assertTrue("Resulted string array should contain three entries", resArr.length == 3);
	}

	/**
	 * This test checks the profile policies count.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void profilePoliciesCount() throws MalformedURLException, URISyntaxException {
		log.info("test the profile policies count");
		int count = ContentClassificationPolicy.ProfilePolicy.values().length;
		assertTrue("The count should be 4", count == 4);
	}

	/**
	 * This test checks the risk score agent types.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void riskScoreAgentType() throws MalformedURLException, URISyntaxException {
		log.info("test risk score agent types");
		int count = RiskScoreMeasurement.AgentType.values().length;
		assertTrue("The count of the agent type should be two", count == 2);
		RiskScoreMeasurementImpl rs = new RiskScoreMeasurementImpl();		
		assertTrue("The risk score should be null", rs.getRiskScore(null) == null);
		rs.setAgentType(AgentType.Manually);
		assertTrue("The risk score agent type should be AgentType.Manually", rs.getAgentType().equals(AgentType.Manually));
	}

	/**
	 * This test checks PreservationRiskmanagementConfiguration component name.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void checkPreservationRiskmanagementConfiguration() throws MalformedURLException, URISyntaxException {
		log.info("test risk score agent types count");
		PreservationRiskmanagementConfiguration configuration = new PreservationRiskmanagementConfiguration();
		String res = configuration.getComponentName();
		assertTrue("The component name should be preservation-riskmanagement", res.equals(PRESERVATION_RISKMANAGEMENT));
	}

	/**
	 * This test check object factory methods.
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	@Test
	public void objectFactory() throws MalformedURLException, URISyntaxException {
		log.info("test object factory methods");
		ObjectFactory of = new ObjectFactory();
		assertNotNull("The resulting object should not be null", of.createRiskPropertySetPropertySets());
		assertNotNull("The resulting object should not be null", of.createRiskPropertySetPropertySetIDs());
		assertNotNull("The resulting object should not be null", of.createRiskProperty());
		assertNotNull("The resulting object should not be null", of.createRiskPropertySetProperties());
		assertNotNull("The resulting object should not be null", of.createRiskClassification());
		assertNotNull("The resulting object should not be null", of.createRiskPropertySetPropertyIDs());
		assertNotNull("The resulting object should not be null", of.createRiskAnalysis());
		assertNotNull("The resulting object should not be null", of.createRiskClassificationRiskFactorsRiskFactor());
		assertNotNull("The resulting object should not be null", of.createRiskPropertySet());
		assertNotNull("The resulting object should not be null", of.createRiskClassificationRiskFactors());
	}

}
