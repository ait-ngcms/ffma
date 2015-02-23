package ait.ffma.preservation.riskmanagement;

import ait.ffma.common.exception.client.FfmaClientException;
import ait.ffma.domain.preservation.riskmanagement.OverallRiskScoreReport;
import ait.ffma.domain.preservation.riskmanagement.PreservationDimensions;

public interface PreservationRiskmanagement {

	/**
	 * This method retrieves component name from the rest API
	 * @return
	 */
	public String getComponentNameFromRest();

	/**
	 * This method computes risk score for particular collection applying analysis configuration
	 * and customized classification
	 * @param id
	 *        The Europeana collection ID
	 * @param config
	 *        The analysis configuration containing the names of the preservation dimensions
	 * @param classification
	 *        The path to the XML file that comprises risk analysis classifications like
	 *        weight and risk score depending on value
	 * @return risk score report
	 * @throws FfmaClientException
	 */
//	public PreservationDimensions computeRiskScore(
//			Long id, String config, String classification)
//			throws FfmaClientException;
	
	/**
	 * This method computes risk score for particular collection applying analysis configuration
	 * and customized classification
	 * @param collectionName
	 *        The Europeana collection name
	 * @param config
	 *        The analysis configuration containing the names of the preservation dimensions
	 * @param classification
	 *        The path to the XML file that comprises risk analysis classifications like
	 *        weight and risk score depending on value
	 * @return risk score report
	 * @throws FfmaClientException
	 */
//	public PreservationDimensions computeRiskScore(
//			String collectionName, String config, String classification)
//			throws FfmaClientException;
	
	/**
	 * @param configuration
	 */
	public void setConfiguration(PreservationRiskmanagementClientConfiguration configuration);
}
