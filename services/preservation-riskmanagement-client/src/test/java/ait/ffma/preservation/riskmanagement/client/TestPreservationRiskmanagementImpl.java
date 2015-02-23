package ait.ffma.preservation.riskmanagement.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ait.ffma.common.exception.client.FfmaClientException;
import ait.ffma.preservation.riskmanagement.PreservationRiskmanagement;
import ait.ffma.preservation.riskmanagement.PreservationRiskmanagementClientConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/Ffma-preservation-riskmanagement-client-application-context.xml" })
public class TestPreservationRiskmanagementImpl {

	@Autowired
	PreservationRiskmanagement preservationRiskmanagement;  
	
	@Autowired
	private PreservationRiskmanagementClientConfiguration configuration;

//	@Test
	public void getComponentNameFromRest(){
		
		String restComponentName = preservationRiskmanagement.getComponentNameFromRest();
		assertEquals(configuration.getComponentName(), restComponentName);
	}
	
	/**
	 * Test the risk management configuration.
	 */
	@Test
	public void testConfiguration() throws MalformedURLException, URISyntaxException, FfmaClientException {
		preservationRiskmanagement.setConfiguration(new PreservationRiskmanagementClientConfiguration());
		assertNotNull("Risk management object must be null", preservationRiskmanagement);	
	}

}
