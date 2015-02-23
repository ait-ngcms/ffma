package ait.ffma.preservation.riskmanagement.client;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import ait.ffma.common.exception.client.FfmaClientException;
import ait.ffma.preservation.riskmanagement.PreservationRiskmanagementClientConfiguration;
import ait.ffma.preservation.riskmanagement.PreservationRiskmanagementLodDataAnalysis;

/**
 * This class is a client for LOD analysis requests.
 */
public class PreservationRiskmanagementLodDataAnalysisImpl implements PreservationRiskmanagementLodDataAnalysis {

	private static final String SEE_SERVER_LOGS = ". See server logs.";

	private static final String STARTING_PRESERVATION_STATISTIC_RETRIEVAL_FOR_FILE_EXTENSION = "Starting preservation statistic retrieval for file extension = ";

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(getClass());

	@Autowired
	private PreservationRiskmanagementClientConfiguration configuration;

	/* (non-Javadoc)
	 * @see ait.ffma.preservation.riskmanagement.PreservationRiskmanagementLodDataAnalysis#getConfiguration()
	 */
	public PreservationRiskmanagementClientConfiguration getConfiguration() {
		return configuration;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.preservation.riskmanagement.PreservationRiskmanagementLodDataAnalysis#setConfiguration(ait.ffma.preservation.riskmanagement.PreservationRiskmanagementClientConfiguration)
	 */
	public void setConfiguration(
			PreservationRiskmanagementClientConfiguration configuration) {
		this.configuration = configuration;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.preservation.riskmanagement.PreservationRiskmanagementLodDataAnalysis#checkLodData()
	 */
	public String checkLodData() 
			throws FfmaClientException {
		String resourceUrl = getConfiguration().getServiceUrl()
				+ "/loddataanalysis/checkdataexist";
		WebResource resource = Client.create().resource(resourceUrl);
		resource.accept(MediaType.APPLICATION_JSON);
		String reportString = resource.get(String.class);
		log.info(reportString);
		return reportString;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.preservation.riskmanagement.PreservationRiskmanagementLodDataAnalysis#storeAllExtensions(java.lang.String, boolean, boolean)
	 */
	public String storeAllExtensions(String type,
			boolean overwriteRepositoryFormats, boolean overwriteLodData)
			throws FfmaClientException {
		String resourceUrl = getConfiguration().getServiceUrl()
			+ "/loddataanalysis/storeallextensions/" + type + "/" + overwriteRepositoryFormats + "/" + overwriteLodData;
		WebResource resource = Client.create().resource(resourceUrl);
		resource.accept(MediaType.APPLICATION_JSON);
		log.info("Starting preservation statistic retrieval for repository = " + type
				+ SEE_SERVER_LOGS);
		String reportString = resource.get(String.class);
		log.info(reportString);
		return reportString;
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.preservation.riskmanagement.PreservationRiskmanagementLodDataAnalysis#retrieveSoftware(java.lang.String)
	 */
	public String retrieveSoftware(String ext) throws FfmaClientException {
		String resourceUrl = getConfiguration().getServiceUrl()
			+ "/loddataanalysis/software/" + ext;
		return buildWebRequest(ext, resourceUrl);
	}

	/**
	 * This method creates a web request for passed parameter, processes a call and returns
	 * a server response.
	 * @param ext
	 * @param resourceUrl
	 * @return server response
	 * @throws FfmaClientException
	 */
	private String buildWebRequest(String ext, String resourceUrl)
			throws FfmaClientException {
		WebResource resource = Client.create().resource(resourceUrl);
		resource.accept(MediaType.APPLICATION_JSON);
		log.info(STARTING_PRESERVATION_STATISTIC_RETRIEVAL_FOR_FILE_EXTENSION + ext
				+ SEE_SERVER_LOGS);
		String reportString = resource.get(String.class);
		log.info(reportString);
		return reportString;
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.preservation.riskmanagement.PreservationRiskmanagementLodDataAnalysis#retrieveVendor(java.lang.String)
	 */
	public String retrieveVendor(String ext) throws FfmaClientException {
		String resourceUrl = getConfiguration().getServiceUrl()
			+ "/loddataanalysis/vendor/" + ext;
		return buildWebRequest(ext, resourceUrl);
	}
	
	/* (non-Javadoc)
	 * @see ait.ffma.preservation.riskmanagement.PreservationRiskmanagementLodDataAnalysis#retrievePreservationStatistic(java.lang.String, java.lang.String)
	 */
	public String retrievePreservationStatistic(String type, String ext)
			throws FfmaClientException {
		String resourceUrl = getConfiguration().getServiceUrl()
			+ "/loddataanalysis/csv/" + type + "/" + ext;
		WebResource resource = Client.create().resource(resourceUrl);
		resource.accept(MediaType.APPLICATION_JSON);
		log.info(STARTING_PRESERVATION_STATISTIC_RETRIEVAL_FOR_FILE_EXTENSION + ext
				+ " and report type = " + type + SEE_SERVER_LOGS);
		String reportString = resource.get(String.class);
		log.info(reportString);
		return reportString;
	}

}
