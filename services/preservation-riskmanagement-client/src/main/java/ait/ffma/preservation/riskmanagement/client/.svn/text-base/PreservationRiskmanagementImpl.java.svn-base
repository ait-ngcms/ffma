package ait.ffma.preservation.riskmanagement.client;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import ait.ffma.common.exception.client.FfmaClientException;
import ait.ffma.preservation.riskmanagement.PreservationRiskmanagement;
import ait.ffma.preservation.riskmanagement.PreservationRiskmanagementClientConfiguration;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class PreservationRiskmanagementImpl implements PreservationRiskmanagement {

	private static final String STARTING_METADATA_ANALYSIS_FOR_COLLECTION_NAME = "Starting metadata analysis for collection name = ";

	private static final String SEE_SERVER_LOGS = ". See server logs.";

	private static final String STARTING_METADATA_ANALYSIS_FOR_COLLECTION_ID = "Starting metadata analysis for collection ID=";

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass());

	@Autowired
	private PreservationRiskmanagementClientConfiguration configuration;

	/**
	 * @return
	 */
	public PreservationRiskmanagementClientConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration
	 */
	public void setConfiguration(
			PreservationRiskmanagementClientConfiguration configuration) {
		this.configuration = configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ait.ffma.preservation.riskmanagement.PreservationRiskmanagement
	 * #getComponentNameFromRest()
	 */
	public String getComponentNameFromRest() {
		String resourceUrl = getConfiguration().getServiceUrl() + "/component";
		WebResource resource = Client.create().resource(resourceUrl);
		resource.accept(MediaType.TEXT_HTML);

		return resource.get(String.class);
	}

	/**
	 * This method creates a web request for passed parameter, processes a call and returns
	 * a server response.
	 * @param id
	 * @param resourceUrl
	 * @return server response
	 * @throws FfmaClientException
	 */
	private String buildWebRequestById(Long id, String resourceUrl)
			throws FfmaClientException {
		WebResource resource = Client.create().resource(resourceUrl);
		resource.accept(MediaType.APPLICATION_JSON);
		log.info(STARTING_METADATA_ANALYSIS_FOR_COLLECTION_ID + id
				+ SEE_SERVER_LOGS);
		String riskScoreReportString = resource.get(String.class);
		log.info(riskScoreReportString);
		return riskScoreReportString;
	}

	/**
	 * This method creates a web request for passed parameter, processes a call and returns
	 * a server response.
	 * @param name
	 * @param resourceUrl
	 * @return server response
	 * @throws FfmaClientException
	 */
	private String buildWebRequestByName(String name, String resourceUrl)
			throws FfmaClientException {
		WebResource resource = Client.create().resource(resourceUrl);
		resource.accept(MediaType.APPLICATION_JSON);
		log.info(STARTING_METADATA_ANALYSIS_FOR_COLLECTION_NAME + name
				+ SEE_SERVER_LOGS);
		String riskScoreReportString = resource.get(String.class);
		log.info(riskScoreReportString);
		return riskScoreReportString;
	}

}
