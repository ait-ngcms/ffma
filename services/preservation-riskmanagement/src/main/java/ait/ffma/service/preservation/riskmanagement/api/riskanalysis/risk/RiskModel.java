package ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk;

import java.io.File;

import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskAnalysis;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskPropertySet;

public interface RiskModel {
	
	/**
	 * This method analyzes passed file using calculation model retrieved from 
	 * passed property set XML and classification XML files. If XML files have 
	 * no valid path - default files will be used.
	 * @param file
	 *        The file to analyze
	 * @param propertySetXml
	 *        The XML file that defines properties, property sets and their dependencies
	 * @param classificationXml
	 *        The XML file that defines risk score classifications for risk properties
	 * @return risk analysis evaluated for passed file
	 */
	public RiskAnalysis analyze(File file, String propertySetXml, String classificationXml);
	
	/**
	 * This method retrieves risk analysis structure.
	 * @return
	 */
	public RiskAnalysis getRiskAnalysis();
	
	/**
	 * This method initializes a risk analysis structure.
	 * @param riskAnalysis
	 */
	public void setRiskAnalysis(RiskAnalysis riskAnalysis);

	/**
	 * This method initializes property value.
	 * @param set
	 * @param propertyId
	 * @param propertyValue
	 */
	public void setPropertyValue(RiskPropertySet set, String propertyId, String propertyValue);
}
