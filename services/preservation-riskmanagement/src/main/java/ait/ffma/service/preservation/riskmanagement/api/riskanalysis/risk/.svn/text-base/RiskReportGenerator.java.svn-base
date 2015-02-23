package ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import ait.ffma.domain.preservation.riskmanagement.FormatRiskAnalysisReport;
import ait.ffma.domain.preservation.riskmanagement.PreservationDimension;
import ait.ffma.domain.preservation.riskmanagement.RiskScoreReport;
import ait.ffma.service.preservation.common.api.measurement.Measurement;
import ait.ffma.service.preservation.common.api.measurement.MeasurementImpl;
import ait.ffma.service.preservation.riskmanagement.PreservationRiskmanagementConfiguration;
import ait.ffma.service.preservation.riskmanagement.api.lod.LODConstants;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.profile.DataItemProfile;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.profile.DataItemProfileImpl;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants.AccessibilityEnum;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants.ContextEnum;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants.CsvEnum;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants.ProvenanceEnum;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants.RiskReportTypesEnum;
import ait.ffma.service.preservation.riskmanagement.dao.PreservationRiskmanagementDao;

/**
 * This class provides file format risk reports generation
 */
public class RiskReportGenerator {

	private static final String H2_A_NAME_CONTEXT_PRESERVATION_DIMENSION_CONTEXT_A_H2 = "<h2><a name=\"context\">Preservation dimension: Context</a></h2>";

	private static final String H2_A_NAME_PROVENANCE_PRESERVATION_DIMENSION_PROVENANCE_A_H2 = "<h2><a name=\"provenance\">Preservation dimension: Provenance</a></h2>";

	private static final String P_A_HREF_OVERVIEW_OVERVIEW_A = "<p><a href=\"#overview\">Overview</a>";

	/**
	 * The data access object for preservation risk management
	 */
	@Autowired
	private PreservationRiskmanagementDao preservationRiskmanagementDao;

	/**
	 * @param preservationRiskmanagementDao
	 */
	public void setPreservationRiskmanagementDao(PreservationRiskmanagementDao preservationRiskmanagementDao) {
		this.preservationRiskmanagementDao = preservationRiskmanagementDao;
	}

	@Autowired
	private PreservationRiskmanagementConfiguration configuration;
	
	/**
	 * @param configuration
	 */
	public void setConfiguration(PreservationRiskmanagementConfiguration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * @return
	 */
	public PreservationRiskmanagementConfiguration getConfiguration() {
		if (configuration == null) {
			configuration = new PreservationRiskmanagementConfiguration();
		}
		return configuration;
	}
		
	/**
	 * A logger object.
	 */
	private Logger log = Logger.getLogger(getClass());

	/**
	 * Defines for risk score calculations
	 */
	private String provenance = ReportConstants.EMPTYSTRING;
	private String context = ReportConstants.EMPTYSTRING;
	private String accessibility = ReportConstants.EMPTYSTRING;
	private String riskAnalysis = ReportConstants.EMPTYSTRING;
	private String avgProvenance = ReportConstants.EMPTYSTRING;
	private String avgContext = ReportConstants.EMPTYSTRING;
	private String avgAccessibility = ReportConstants.EMPTYSTRING;
	private int brokenObjects = 0;
	private int totalObjects = 0;
	private boolean isOverallRiskScoreStored = false;
	private boolean isProvenanceStored = false;
	private boolean isContextStored = false;
	private boolean isAccessibilityStored = false;

	/**
	 * @return
	 */
	public String getProvenance() {
		return provenance;
	}

	/**
	 * @param provenance
	 */
	public void setProvenance(String provenance) {
		this.provenance = provenance;
	}

	/**
	 * @return
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @return
	 */
	public String getAccessibility() {
		return accessibility;
	}

	/**
	 * @param accessibility
	 */
	public void setAccessibility(String accessibility) {
		this.accessibility = accessibility;
	}

	/**
	 * @return
	 */
	public String getRiskAnalysis() {
		return riskAnalysis;
	}

	/**
	 * @param riskAnalysis
	 */
	public void setRiskAnalysis(String riskAnalysis) {
		this.riskAnalysis = riskAnalysis;
	}

	/**
	 * @return
	 */
	public String getAvgProvenance() {
		return avgProvenance;
	}

	/**
	 * @param avgProvenance
	 */
	public void setAvgProvenance(String avgProvenance) {
		this.avgProvenance = avgProvenance;
	}

	/**
	 * @return
	 */
	public String getAvgContext() {
		return avgContext;
	}

	/**
	 * @param avgContext
	 */
	public void setAvgContext(String avgContext) {
		this.avgContext = avgContext;
	}

	/**
	 * @return
	 */
	public String getAvgAccessibility() {
		return avgAccessibility;
	}

	/**
	 * @param avgAccessibility
	 */
	public void setAvgAccessibility(String avgAccessibility) {
		this.avgAccessibility = avgAccessibility;
	}

	/**
	 * @return
	 */
	public int getBrokenObjects() {
		return brokenObjects;
	}

	/**
	 * @param brokenObjects
	 */
	public void setBrokenObjects(int brokenObjects) {
		this.brokenObjects = brokenObjects;
	}

	/**
	 * @return
	 */
	public int getTotalObjects() {
		return totalObjects;
	}

	/**
	 * @param totalObjects
	 */
	public void setTotalObjects(int totalObjects) {
		this.totalObjects = totalObjects;
	}

	/**
	 * @return
	 */
	public boolean isOverallRiskScoreStored() {
		return isOverallRiskScoreStored;
	}

	/**
	 * @param isOverallRiskScoreStored
	 */
	public void setOverallRiskScoreStored(boolean isOverallRiskScoreStored) {
		this.isOverallRiskScoreStored = isOverallRiskScoreStored;
	}

	/**
	 * @return
	 */
	public boolean isProvenanceStored() {
		return isProvenanceStored;
	}

	/**
	 * @param isProvenanceStored
	 */
	public void setProvenanceStored(boolean isProvenanceStored) {
		this.isProvenanceStored = isProvenanceStored;
	}

	/**
	 * @return
	 */
	public boolean isContextStored() {
		return isContextStored;
	}

	/**
	 * @param isContextStored
	 */
	public void setContextStored(boolean isContextStored) {
		this.isContextStored = isContextStored;
	}

	/**
	 * @return
	 */
	public boolean isAccessibilityStored() {
		return isAccessibilityStored;
	}

	/**
	 * @param isAccessibilityStored
	 */
	public void setAccessibilityStored(boolean isAccessibilityStored) {
		this.isAccessibilityStored = isAccessibilityStored;
	}

	/**
	 * This method resets risk report values
	 */
	private void resetReportValues() {
		setProvenance(ReportConstants.EMPTYSTRING);
		setContext(ReportConstants.EMPTYSTRING);
		setAccessibility(ReportConstants.EMPTYSTRING);
		setRiskAnalysis(ReportConstants.EMPTYSTRING);
		setAvgProvenance(ReportConstants.EMPTYSTRING);
		setAvgContext(ReportConstants.EMPTYSTRING);
		setAvgAccessibility(ReportConstants.EMPTYSTRING);
		setBrokenObjects(0);
		setTotalObjects(0);
		setOverallRiskScoreStored(false);
		setProvenanceStored(false);
		setContextStored(false);
		setAccessibilityStored(false);
	}
	
	/**
	 * This method creates preservation dimension object for particular type of report
	 * @param key
	 *        The CsvEnum type of report
	 * @param value
	 *        The CSV formatted content of the report
	 * @param config
	 *        The analysis configuration containing the names of the preservation dimensions
	 * @param reportType
	 *        The type of the risk report
	 * @return preservation dimension object
	 */
	public List<PreservationDimension> generatePreservationDimension(CsvEnum key,
			String value, String config,
			RiskReportTypesEnum reportType, String collectionName) {
		List<PreservationDimension> res = new ArrayList<PreservationDimension>();
		Map<String, String> provenanceMap = null;
		Map<String, String> contextMap = null;
		Map<String, String> accessibilityMap = null;
		
		List<String> configList = RiskUtils.getConfigurationList(config);
		
		if (reportType.equals(RiskReportTypesEnum.RiskScore)
				|| reportType.equals(RiskReportTypesEnum.OverallRiskScore)
				|| reportType.equals(RiskReportTypesEnum.RiskReport)) {
			if (key.equals(CsvEnum.EmptyFieldsObjects)) {
				provenanceMap = generateProvenanceReport(value, configList);
				contextMap = generateContextReport(value, configList);
				accessibilityMap = generateAccessibilityReport(value, configList);
			}
		}
		
		addPreservationDimensions(reportType, collectionName, res,
				provenanceMap, contextMap, accessibilityMap, configList);
		return res;
	}

	/**
	 * @param reportType
	 * @param collectionName
	 * @param res
	 * @param provenanceMap
	 * @param contextMap
	 * @param accessibilityMap
	 * @param configList
	 */
	private void addPreservationDimensions(RiskReportTypesEnum reportType,
			String collectionName, List<PreservationDimension> res,
			Map<String, String> provenanceMap, Map<String, String> contextMap,
			Map<String, String> accessibilityMap, List<String> configList) {
		if (reportType.equals(RiskReportTypesEnum.RiskScore)
				|| reportType.equals(RiskReportTypesEnum.RiskReport)) {
			addPreservationDimension(collectionName, res, provenanceMap, configList, RiskConstants.ProvenanceEnum.class.getSimpleName());
			addPreservationDimension(collectionName, res, contextMap, configList, RiskConstants.ContextEnum.class.getSimpleName());
			addPreservationDimension(collectionName, res, accessibilityMap, configList, RiskConstants.AccessibilityEnum.class.getSimpleName());
		}
	}

	/**
	 * @param value
	 * @param configList
	 * @return
	 */
	private Map<String, String> generateAccessibilityReport(String value, List<String> configList) {
		Map<String, String> accessibilityMapRes = null;
		if (configList == null || configList.contains(RiskConstants.AccessibilityEnum.class.getSimpleName())) {
			accessibilityMapRes = generatePreservationDimensionReport(value, RiskConstants.AccessibilityEnum.class.getSimpleName());
		}
		return accessibilityMapRes;
	}

	/**
	 * @param value
	 * @param configList
	 * @return
	 */
	private Map<String, String> generateContextReport(String value, List<String> configList) {
		Map<String, String> contextMapRes = null;
		if (configList == null || configList.contains(RiskConstants.ContextEnum.class.getSimpleName())) {
			contextMapRes = generatePreservationDimensionReport(value, RiskConstants.ContextEnum.class.getSimpleName());
		}
		return contextMapRes;
	}

	/**
	 * @param value
	 * @param configList
	 * @return
	 */
	private Map<String, String> generateProvenanceReport(String value, List<String> configList) {
		Map<String, String> provenanceMapRes = null;
		if (configList == null || configList.contains(RiskConstants.ProvenanceEnum.class.getSimpleName())) {
			provenanceMapRes = generatePreservationDimensionReport(value, RiskConstants.ProvenanceEnum.class.getSimpleName());
		}
		return provenanceMapRes;
	}

	/**
	 * This method creates measurements list based on given classification and configurations.
	 * @param classification
	 * @param configList
	 * @return
	 */
	private CalculationModel createMeasurementsList(String classification,
			List<String> configList) {
		int id = 0;
		List<Measurement> measurementList = new ArrayList<Measurement>();
		float brokenObjectsCount = 0;
		if (getTotalObjects() != 0) {
			brokenObjectsCount = getBrokenObjects()/(float) getTotalObjects();
		}
		if (configList == null || configList.contains(RiskConstants.BROKEN_OBJECTS_SCORE_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.BROKEN_OBJECTS_SCORE_PROPERTY_ID, Float.toString(brokenObjectsCount))); 
		}
		if (configList == null || configList.contains(RiskConstants.ProvenanceEnum.class.getSimpleName())) {
			measurementList.add(createMeasurement(id++, RiskConstants.PROVENANCE_SCORE_PROPERTY_ID, getAvgProvenance())); 
		}
		if (configList == null || configList.contains(RiskConstants.ContextEnum.class.getSimpleName())) {
			measurementList.add(createMeasurement(id++, RiskConstants.CONTEXT_SCORE_PROPERTY_ID, getAvgContext()));
		}
		if (configList == null || configList.contains(RiskConstants.AccessibilityEnum.class.getSimpleName())) {
			measurementList.add(createMeasurement(id++, RiskConstants.ACCESSIBILITY_SCORE_PROPERTY_ID, getAvgAccessibility()));
		}
		
		// initialize data profile
		DataItemProfile profile = new DataItemProfileImpl(measurementList);	

		// initializations
		CalculationModel calculationModel = RiskUtils.initCalculationModelExt(profile, null, null, classification);
		
		// calculate risk score
		int riskScore = calculationModel.getRiskScore();
		log.info(ReportConstants.RISK_SCORE + riskScore);
		return calculationModel;
	}

	/**
	 * This method creates format measurements list based on given classification and configurations.
	 * @param classification
	 * @param configList
	 * @return
	 */
	private CalculationModel createFormatMeasurementsList(FormatRiskAnalysisReport formatRiskAnalysisReport, String classification,
			List<String> configList) {
		int id = 0;
		List<Measurement> measurementList = new ArrayList<Measurement>();
		if (configList == null || configList.contains(RiskConstants.SOFTWARE_COUNT_SCORE_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.SOFTWARE_COUNT_SCORE_PROPERTY_ID, formatRiskAnalysisReport.getSoftwareCount().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.VENDORS_COUNT_SCORE_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.VENDORS_COUNT_SCORE_PROPERTY_ID, formatRiskAnalysisReport.getVendorsCount().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.VERSIONS_COUNT_SCORE_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.VERSIONS_COUNT_SCORE_PROPERTY_ID, formatRiskAnalysisReport.getVersions().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.HAS_DESCRIPTION_SCORE_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.HAS_DESCRIPTION_SCORE_PROPERTY_ID, formatRiskAnalysisReport.getDescriptionCount().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.HAS_CREATION_DATE_INFORMATION_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.HAS_CREATION_DATE_INFORMATION_PROPERTY_ID, formatRiskAnalysisReport.getHasCreationDate().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.HAS_CREATOR_INFORMATION_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.HAS_CREATOR_INFORMATION_PROPERTY_ID, formatRiskAnalysisReport.getHasCreatorInformation().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.HAS_DIGITAL_RIGHTS_INFORMATION_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.HAS_DIGITAL_RIGHTS_INFORMATION_PROPERTY_ID, formatRiskAnalysisReport.getHasDigitalRightsInformation().toString())); 
		}
//		if (configList == null || configList.contains(RiskConstants.HAS_OBJECT_PREVIEW_SCORE_PROPERTY_ID)) {
//			measurementList.add(createMeasurement(id++, RiskConstants.HAS_OBJECT_PREVIEW_SCORE_PROPERTY_ID, formatRiskAnalysisReport.getHasObjectPreview().toString())); 
//		}
		if (configList == null || configList.contains(RiskConstants.HAS_PUBLISHER_INFORMATION_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.HAS_PUBLISHER_INFORMATION_PROPERTY_ID, formatRiskAnalysisReport.getHasPublisherInformation().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.IS_COMPRESSED_FILE_FORMAT_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.IS_COMPRESSED_FILE_FORMAT_PROPERTY_ID, formatRiskAnalysisReport.getIsCompressedFileFormat().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.IS_FILE_MIGRATION_ALLOWED_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.IS_FILE_MIGRATION_ALLOWED_PROPERTY_ID, formatRiskAnalysisReport.getIsFileMigrationSupported().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.WHICH_VERSION_FREQUENTLY_USED_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.WHICH_VERSION_FREQUENTLY_USED_PROPERTY_ID, formatRiskAnalysisReport.getIsFrequentlyUsedVersion().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.IS_SUPPORTED_BY_IMPORTANT_SOFTWARE_VENDORS_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.IS_SUPPORTED_BY_IMPORTANT_SOFTWARE_VENDORS_PROPERTY_ID, formatRiskAnalysisReport.getIsSupportedByVendor().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.IS_SUPPORTED_BY_WEB_BROWSERS_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.IS_SUPPORTED_BY_WEB_BROWSERS_PROPERTY_ID, formatRiskAnalysisReport.getIsSupportedByWebBrowsers().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.HAS_HOMEPAGE_SCORE_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.HAS_HOMEPAGE_SCORE_PROPERTY_ID, formatRiskAnalysisReport.getHasHomepage().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.HAS_GENRE_SCORE_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.HAS_GENRE_SCORE_PROPERTY_ID, formatRiskAnalysisReport.getHasGenre().toString())); 
		}
//		if (configList == null || configList.contains(RiskConstants.WELL_DOCUMENTED_SCORE_PROPERTY_ID)) {
//			measurementList.add(createMeasurement(id++, RiskConstants.WELL_DOCUMENTED_SCORE_PROPERTY_ID, formatRiskAnalysisReport.getIsWellDocumented().toString())); 
//		}
//		if (configList == null || configList.contains(RiskConstants.LAST_UPDATE_PROPERTY_ID)) {
//			measurementList.add(createMeasurement(id++, RiskConstants.LAST_UPDATE_PROPERTY_ID, formatRiskAnalysisReport.getLastUpdateTime().toString())); 
//		}
		if (configList == null || configList.contains(RiskConstants.HAS_MIME_TYPE_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.HAS_MIME_TYPE_PROPERTY_ID, formatRiskAnalysisReport.getMimeType().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.EXISTENCE_PERIOD_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.EXISTENCE_PERIOD_PROPERTY_ID, formatRiskAnalysisReport.getExistencePeriod().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.COMPLEXITY_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.COMPLEXITY_PROPERTY_ID, formatRiskAnalysisReport.getComplexity().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.IS_DISSEMINATED_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.IS_DISSEMINATED_PROPERTY_ID, formatRiskAnalysisReport.getDissemination().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.IS_STANDARDISED_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.IS_STANDARDISED_PROPERTY_ID, formatRiskAnalysisReport.getStandardisation().toString())); 
		}
		if (configList == null || configList.contains(RiskConstants.IS_OUTDATED_PROPERTY_ID)) {
			measurementList.add(createMeasurement(id++, RiskConstants.IS_OUTDATED_PROPERTY_ID, formatRiskAnalysisReport.getOutdated().toString())); 
		}
		
		// initialize data profile
		DataItemProfile profile = new DataItemProfileImpl(measurementList);	

		// initializations
		CalculationModel calculationModel = RiskUtils.initCalculationModelExt(profile, null, null, classification);
		
		// calculate risk score
		int riskScore = calculationModel.getRiskScore();
		log.info(ReportConstants.RISK_SCORE + riskScore);
		return calculationModel;
	}

	/**
	 * This method evaluates if enum contains given string
	 * @param test
	 * @return true if enum contains given string
	 */
	public boolean dimensionContains(String test, String type) {

		if (type.equals(RiskConstants.ProvenanceEnum.class.getSimpleName())) {
		    for (ProvenanceEnum c : ProvenanceEnum.values()) {
		        if (c.name().equals(test)) {
		            return true;
		        }
		    }
		}
		else if (type.equals(RiskConstants.ContextEnum.class.getSimpleName())) {
		    for (ContextEnum c : ContextEnum.values()) {
		        if (c.name().equals(test)) {
		            return true;
		        }
		    }
		}
		else if (type.equals(RiskConstants.AccessibilityEnum.class.getSimpleName())) {
		    for (AccessibilityEnum c : AccessibilityEnum.values()) {
		        if (c.name().equals(test)) {
		            return true;
		        }
		    }
		}

		return false;
	}

	/**
	 * This method evaluates if enum contains given string
	 * @param test
	 * @return true if enum contains given string
	 */
	public boolean contains(String test) {

	    for (ProvenanceEnum c : ProvenanceEnum.values()) {
	        if (c.name().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}

	/**
	 * This method evaluates if enum contains given string
	 * @param test
	 * @return true if enum contains given string
	 */
	public boolean containsContext(String test) {

	    for (ContextEnum c : ContextEnum.values()) {
	        if (c.name().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}

	/**
	 * This method evaluates if enum contains given string
	 * @param test
	 * @return true if enum contains given string
	 */
	public boolean containsAccessibility(String test) {

	    for (AccessibilityEnum c : AccessibilityEnum.values()) {
	        if (c.name().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}

	/**
	 * This method creates metadata report in HTML table format for particular type of report
	 * @param key
	 *        The CsvEnum type of report
	 * @param value
	 *        The CSV formatted content of the report
	 * @param config
	 *        The analysis configuration containing the names of the preservation dimensions
	 * @param classification
	 *        The path to the XML file that comprises risk analysis classifications like
	 *        weight and risk score depending on value
	 * @param reportType
	 *        The type of the risk report
	 * @return metadata report in HTML format
	 */
	public String generateMetadataReport(CsvEnum key, String value, String classification, String config, RiskReportTypesEnum reportType) {
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);		
		List<String> configList = RiskUtils.getConfigurationList(config);		
		setProvenanceContextAccessibility(key, value, reportType, configList);	
		calculateRiskAnalysis(key, classification, reportType, configList);		
		printMetadataReport(key, value, reportType, buf, configList);		
		filterProvenanceContextAccessibility(reportType, buf, configList);
		filterRiskAnalysis(reportType, buf);
		return buf.toString();
	}

	/**
	 * @param reportType
	 * @param buf
	 */
	private void filterRiskAnalysis(RiskReportTypesEnum reportType,
			StringBuffer buf) {
		if (reportType.equals(RiskReportTypesEnum.OverallRiskScore)
				|| reportType.equals(RiskReportTypesEnum.RiskReport)) {
			if (getRiskAnalysis().length() > 0 && !isOverallRiskScoreStored()) {
				buf.append(getRiskAnalysis());
				setOverallRiskScoreStored(true);
			}
		}
	}

	/**
	 * @param reportType
	 * @param buf
	 * @param configList
	 */
	private void filterProvenanceContextAccessibility(
			RiskReportTypesEnum reportType, StringBuffer buf,
			List<String> configList) {
		if (reportType.equals(RiskReportTypesEnum.RiskScore)
				|| reportType.equals(RiskReportTypesEnum.RiskReport)) {
			printProvenance(buf, configList);
			printContext(buf, configList);
			printAccessibility(buf, configList);
		}
	}

	/**
	 * @param buf
	 * @param configList
	 */
	private void printAccessibility(StringBuffer buf, List<String> configList) {
		if (getAccessibility().length() > 0
				&& !isAccessibilityStored()
				&& (configList == null || configList
						.contains(RiskConstants.AccessibilityEnum.class
								.getSimpleName()))) {
			buf.append(getAccessibility());
			setAccessibilityStored(true);
		}
	}

	/**
	 * @param buf
	 * @param configList
	 */
	private void printContext(StringBuffer buf, List<String> configList) {
		if (getContext().length() > 0
				&& !isContextStored()
				&& (configList == null || configList
						.contains(RiskConstants.ContextEnum.class
								.getSimpleName()))) {
			buf.append(getContext());
			setContextStored(true);
		}
	}

	/**
	 * @param buf
	 * @param configList
	 */
	private void printProvenance(StringBuffer buf, List<String> configList) {
		if (getProvenance().length() > 0
				&& !isProvenanceStored()
				&& (configList == null || configList
						.contains(RiskConstants.ProvenanceEnum.class
								.getSimpleName()))) {
			buf.append(getProvenance());
			setProvenanceStored(true);
		}
	}

	/**
	 * @param key
	 * @param value
	 * @param reportType
	 * @param buf
	 * @param configList
	 */
	private void printMetadataReport(CsvEnum key, String value,
			RiskReportTypesEnum reportType, StringBuffer buf,
			List<String> configList) {
		if (reportType.equals(RiskReportTypesEnum.MetadataAnalysis) || reportType.equals(RiskReportTypesEnum.RiskReport)) {
			if (configList == null || configList.contains(key.name())) {
				buf.append(P_A_HREF_OVERVIEW_OVERVIEW_A);
				buf.append(ReportConstants.H2_A_NAME);
				buf.append(key.name());
				buf.append("\">Metadata Analysis Report: ");
				buf.append(key.name());
				buf.append(ReportConstants.A_H2);
				buf.append(ReportConstants.TABLE_BORDER_1);
			    calculateMetadataReportColumns(value, buf);
				buf.append(ReportConstants.TABLEEND);
			}
		}
	}

	/**
	 * @param value
	 * @param buf
	 */
	private void calculateMetadataReportColumns(String value, StringBuffer buf) {
		int i = 0;
		for (String row : value.split(ReportConstants.NEWLINE)) {
		    log.info(ReportConstants.ROW2 + row);
			buf.append(ReportConstants.TR);
		    for (String col : row.split(ReportConstants.SEMICOLON)) {
		        log.info(ReportConstants.COL2 + col);
		        if (i == 0) {
		    		buf.append(ReportConstants.TH);
		    		buf.append(col);
		    		buf.append(ReportConstants.THEND);
		        } else {
		    		buf.append(ReportConstants.TD);
		    		buf.append(col);
		    		buf.append(ReportConstants.TDEND);
		        }
		    }
			buf.append(ReportConstants.TREND);
		    i++;
		}
		appendEmptyRow(buf, i);
	}

	/**
	 * @param configList
	 * @return
	 */
	public boolean isPreservationDimension(List<String> configList) {
		boolean res = false;
		if (configList.contains(RiskConstants.ProvenanceEnum.class.getSimpleName())
				|| configList.contains(RiskConstants.ContextEnum.class.getSimpleName())
				|| configList.contains(RiskConstants.AccessibilityEnum.class.getSimpleName())) {
			res = true;
		}
		return res;
	}
	
	/**
	 * @param key
	 * @param classification
	 * @param reportType
	 * @param configList
	 */
	private void calculateRiskAnalysis(CsvEnum key, String classification,
			RiskReportTypesEnum reportType, List<String> configList) {
		if (reportType.equals(RiskReportTypesEnum.OverallRiskScore) || reportType.equals(RiskReportTypesEnum.RiskReport)) {
			if (key.equals(CsvEnum.EmptyFieldsObjects)) {
				if (configList == null
						|| configList.contains(RiskConstants.RISK_SCORE_REPORT)
						|| isPreservationDimension(configList)) {
					setRiskAnalysis(generateRiskScoreReport(classification, configList));
				}
			}
		}
	}

	/**
	 * @param key
	 * @param value
	 * @param reportType
	 * @param configList
	 */
	private void setProvenanceContextAccessibility(CsvEnum key, String value,
			RiskReportTypesEnum reportType, List<String> configList) {
		if (reportType.equals(RiskReportTypesEnum.RiskScore)
				|| reportType.equals(RiskReportTypesEnum.OverallRiskScore)
				|| reportType.equals(RiskReportTypesEnum.RiskReport)) {
			if (key.equals(CsvEnum.EmptyFieldsObjects)) {
				evaluateProvenance(value, configList);
				evaluateContext(value, configList);
				evaluateAccessibility(value, configList);
			}
		}
	}

	/**
	 * @param value
	 * @param configList
	 */
	private void evaluateAccessibility(String value, List<String> configList) {
		if (configList == null || configList.contains(RiskConstants.AccessibilityEnum.class.getSimpleName())) {
			setAccessibility(generateAccessibilityReport(value));
		}
	}

	/**
	 * @param value
	 * @param configList
	 */
	private void evaluateContext(String value, List<String> configList) {
		if (configList == null || configList.contains(RiskConstants.ContextEnum.class.getSimpleName())) {
			setContext(generateContextReport(value));
		}
	}

	/**
	 * @param value
	 * @param configList
	 */
	private void evaluateProvenance(String value, List<String> configList) {
		if (configList == null || configList.contains(RiskConstants.ProvenanceEnum.class.getSimpleName())) {
			setProvenance(generateProvenanceReportHtml(value));
		}
	}

	/**
	 * This method creates metadata report in HTML table format for particular type of report
	 * @param value
	 *        The CSV formatted content of the report
	 * @return metadata report in HTML format
	 */
	public  String generateProvenanceReportHtml(String value) {
		String res = ReportConstants.EMPTYSTRING;
		StringBuffer buf = new StringBuffer();
		buf.append(P_A_HREF_OVERVIEW_OVERVIEW_A);
		buf.append(H2_A_NAME_PROVENANCE_PRESERVATION_DIMENSION_PROVENANCE_A_H2);
		int i = 0;
		double avgView = 0;
		int avgCount = 0;
		buf.append(ReportConstants.TABLE_BORDER_1);
	    for (String row : value.split(ReportConstants.NEWLINE)) {
	        log.info(ReportConstants.ROW2 + row);
			buf.append(ReportConstants.TR);
			int colCounter = 0;
			boolean storeValue = false;
		    for (String col : row.split(ReportConstants.SEMICOLON)) {
		        log.info(ReportConstants.COL2 + col);
		        if (colCounter > ReportConstants.INIT_VAL) {
		        	break; // only filling level is interesting
		        }
		        if (i == 0) {
		    		buf.append(ReportConstants.TH);
		    		buf.append(col);
		    		buf.append(ReportConstants.THEND);
		        } else {
	    			if (colCounter == 0 && contains(col)) {
	    				buf.append(ReportConstants.TD);
	    				buf.append(col);
	    				buf.append(ReportConstants.TDEND);
	    				storeValue = true;
	    			}
	    			if (colCounter == ReportConstants.INIT_VAL && storeValue) {
	    				double viewValue = getDoubleViewValue(col);
	    				avgView = avgView + viewValue;
	    				avgCount++;
	    				buf.append(ReportConstants.TD);
	    				buf.append(viewValue);
	    				buf.append(ReportConstants.TDEND);
	    				storeValue = false;
	    			}
		        }
		        colCounter++;
		    }
	    	buf.append(ReportConstants.TREND);
		    i++;
	    }
	    res = calculateAvgProvenance(buf, i, avgView, avgCount);
		return res;
	}

	/**
	 * @param buf
	 * @param i
	 * @param avgView
	 * @param avgCount
	 * @return
	 */
	private String calculateAvgProvenance(StringBuffer buf, int i,
			double avgView, int avgCount) {
		String res;
		appendEmptyRow(buf, i);		
	    buf.append(ReportConstants.TABLEEND);
	    res = buf.toString();
    	res = res.replaceAll(ReportConstants.TR_TR, ReportConstants.EMPTYSTRING);
		computeAvgProvenance(avgView, avgCount);
		return res;
	}
	
	/**
	 * This method generates header for HTML risk report
	 * @return HTML report header
	 */
	public String generateHtmlReportBegin() {
		
		StringBuffer buf = new StringBuffer();
		String path = getConfiguration().getServiceUrl().replace("rest", "");
		buf.append(LODConstants.HTML_HEADER);
		buf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" + 
  	               "<title>Format Risk Analysis Report</title>" +
  	               "<link href=\"" + path + "ffmastyle.css\" rel=\"stylesheet\" type=\"text/css\" />");
		buf.append("<div id=\"wrapper\">" +  
	"<div id=\"container\">" + 
		"<div id=\"mainblock\">" + 
					"<div id=\"content\">" + 
						"<div class=\"box\">" + 
							"<h2>Aggregated File Format Description</h2>");
	    return buf.toString();
	}

	/**
	 * This method generates end for HTML risk report
	 * @return HTML report end
	 */
	public String generateHtmlReportEnd() {
		StringBuffer buf = new StringBuffer();
		buf.append("</div></div></div><hr/></div>");
	    return buf.toString();
	}

	/**
	 * This method creates preservation risk score report object for particular type of report
	 * @param key
	 *        The CsvEnum type of report
	 * @param value
	 *        The CSV formatted content of the report
	 * @param config
	 *        The analysis configuration containing the names of the preservation dimensions
	 * @param classification
	 *        The path to the XML file that comprises risk analysis classifications like
	 *        weight and risk score depending on value
	 * @param reportType
	 *        The type of the risk report
	 * @return preservation dimension object
	 */
	public List<RiskScoreReport> generateRiskScoreReport(CsvEnum key,
			String value, String classification, String config,
			RiskReportTypesEnum reportType) {
		
		List<RiskScoreReport> res = new ArrayList<RiskScoreReport>();		
		List<String> configList = RiskUtils.getConfigurationList(config);		
		setProvenanceContextAccessibility(key, value, reportType, configList);
		
		if (reportType.equals(RiskReportTypesEnum.OverallRiskScore) || reportType.equals(RiskReportTypesEnum.RiskReport)) {
			if (key.equals(CsvEnum.EmptyFieldsObjects)) {
				if (configList == null
						|| configList.contains(RiskConstants.RISK_SCORE_REPORT)
						|| isPreservationDimension(configList)) {					
					CalculationModel calculationModel = createMeasurementsList(
							classification, configList);
					res = calculationModel.getRiskScoreBreakdownList();
				}
			}
		}
		return res;
	}

	/**
	 * This method creates preservation dimension report in map format
	 * @param value
	 *        The CSV formatted content of the report
	 * @return preservation dimension report in map format
	 */
	public  Map<String, String> generatePreservationDimensionReport(String value, String dimensionType) {
		Map<String, String> res = new HashMap<String, String>();
		int i = 0;
		double avgView = 0;
		int avgCount = 0;
	    for (String row : value.split(ReportConstants.NEWLINE)) {
	        log.info(ReportConstants.ROW2 + row);
			int colCounter = 0;
			boolean storeValue = false;
			String key = ReportConstants.EMPTYSTRING;
		    for (String col : row.split(ReportConstants.SEMICOLON)) {
		        log.info(ReportConstants.COL2 + col);
		        if (colCounter > ReportConstants.INIT_VAL) {
		        	break; // only filling level is interesting
		        }
		        if (i > 0) {
		        	double fillingLevel = -ReportConstants.INIT_VAL;
	    			if (colCounter == 0 && dimensionContains(col, dimensionType)) {
	    				storeValue = true;
	    				key = col;
	    			}
	    			if (colCounter == ReportConstants.INIT_VAL && storeValue) {
	    				fillingLevel = ReportConstants.FLOATHUNDRED - Math.round((Float.valueOf(col)) * ReportConstants.INTHUNDRED)/ReportConstants.FLOATHUNDRED;
	    				avgView = avgView + fillingLevel;
	    				avgCount++;
	    				storeValue = false;
	    			}
	    			computeAvgProvenanceFillingLevel(res, key, col,
							fillingLevel);
		        }
		        colCounter++;
		    }
		    i++;
	    }
		computeAvgProvenance(avgView, avgCount);
		return res;
	}

	/**
	 * @param res
	 * @param key
	 * @param col
	 * @param fillingLevel
	 */
	private void computeAvgProvenanceFillingLevel(Map<String, String> res,
			String key, String col, double fillingLevel) {
		if (col != null && col.length() > 0 && fillingLevel != -ReportConstants.INIT_VAL) {
			res.put(key, Double.toString(fillingLevel));
		}
	}

	/**
	 * @param avgView
	 * @param avgCount
	 */
	private void computeAvgProvenance(double avgView, int avgCount) {
		if (avgCount > 0) {
			setAvgProvenance(Double.toString(Math.round((avgView/avgCount))/ReportConstants.FLOATHUNDRED));
		}
	}
	
	/**
	 * This method creates metadata report in HTML table format for particular type of report
	 * @param value
	 *        The CSV formatted content of the report
	 * @return metadata report in HTML format
	 */
	public  String generateContextReport(String value) {
		String res = ReportConstants.EMPTYSTRING;
		StringBuffer buf = new StringBuffer();
		buf.append(P_A_HREF_OVERVIEW_OVERVIEW_A);
		buf.append(H2_A_NAME_CONTEXT_PRESERVATION_DIMENSION_CONTEXT_A_H2);
		int i = 0;
		double avgView = 0;
		int avgCount = 0;
		buf.append(ReportConstants.TABLE_BORDER_1);
	    for (String row : value.split(ReportConstants.NEWLINE)) {
	        log.info(ReportConstants.ROW2 + row);
			buf.append(ReportConstants.TR);
			int colCounter = 0;
			boolean storeValue = false;
		    for (String col : row.split(ReportConstants.SEMICOLON)) {
		        log.info(ReportConstants.COL2 + col);
		        if (colCounter > ReportConstants.INIT_VAL) {
		        	break; // only filling level is interesting
		        }
		        if (i == 0) {
		    		buf.append(ReportConstants.TH);
		    		buf.append(col);
		    		buf.append(ReportConstants.THEND);
		        } else {
	    			if (colCounter == 0 && containsContext(col)) {
	    				buf.append(ReportConstants.TD);
	    				buf.append(col);
	    				buf.append(ReportConstants.TDEND);
	    				storeValue = true;
	    			}
	    			if (colCounter == ReportConstants.INIT_VAL && storeValue) {
	    				double viewValue = getDoubleViewValue(col);
	    				avgView = avgView + viewValue;
	    				avgCount++;
	    				buf.append(ReportConstants.TD);
	    				buf.append(viewValue);
	    				buf.append(ReportConstants.TDEND);
	    				storeValue = false;
	    			}
		        }
		        colCounter++;
		    }
	    	buf.append(ReportConstants.TREND);
		    i++;
	    }
	    res = calculateAvgContext(buf, i, avgView, avgCount);
		return res;
	}

	/**
	 * @param buf
	 * @param i
	 * @param avgView
	 * @param avgCount
	 * @return
	 */
	private String calculateAvgContext(StringBuffer buf, int i, double avgView,
			int avgCount) {
		String res;
		appendEmptyRow(buf, i);		
	    buf.append(ReportConstants.TABLEEND);
	    res = buf.toString();
    	res = res.replaceAll(ReportConstants.TR_TR, ReportConstants.EMPTYSTRING);
    	if (avgCount > 0) {
			setAvgContext(Double.toString(Math.round((avgView/avgCount))/ReportConstants.FLOATHUNDRED));
		}
		return res;
	}
	
	/**
	 * This method creates metadata report in HTML table format for particular type of report
	 * @param value
	 *        The CSV formatted content of the report
	 * @return metadata report in HTML format
	 */
	public  String generateAccessibilityReport(String value) {
		String res = ReportConstants.EMPTYSTRING;
		StringBuffer buf = new StringBuffer();
		buf.append(P_A_HREF_OVERVIEW_OVERVIEW_A);
		buf.append("<h2><a name=\"accessibility\">Preservation dimension: Accessibility</a></h2>");
		int i = 0;
		double avgView = 0;
		int avgCount = 0;
		buf.append(ReportConstants.TABLE_BORDER_1);
	    for (String row : value.split(ReportConstants.NEWLINE)) {
	        log.info(ReportConstants.ROW2 + row);
			buf.append(ReportConstants.TR);
			int colCounter = 0;
			boolean storeValue = false;
		    for (String col : row.split(ReportConstants.SEMICOLON)) {
		        log.info(ReportConstants.COL2 + col);
		        if (colCounter > ReportConstants.INIT_VAL) {
		        	break; // only filling level is interesting
		        }
		        if (i == 0) {
		    		buf.append(ReportConstants.TH);
		    		buf.append(col);
		    		buf.append(ReportConstants.THEND);
		        } else {
	    			storeValue = computeAccessibilityValue(buf, colCounter, storeValue, col);
	    			if (colCounter == ReportConstants.INIT_VAL && storeValue) {
	    				double viewValue = getDoubleViewValue(col);
	    				avgView = avgView + viewValue;
	    				avgCount++;
	    				buf.append(ReportConstants.TD);
	    				buf.append(viewValue);
	    				buf.append(ReportConstants.TDEND);
	    				storeValue = false;
	    			}
		        }
		        colCounter++;
		    }
	    	buf.append(ReportConstants.TREND);
		    i++;
	    }
	    appendEmptyRow(buf, i);
		buf.append(ReportConstants.TABLEEND);
		res = buf.toString();
    	res = res.replaceAll(ReportConstants.TR_TR, ReportConstants.EMPTYSTRING);
    	computeAvgAccessibility(avgView, avgCount);
		return res;
	}

	/**
	 * @param avgView
	 * @param avgCount
	 */
	private void computeAvgAccessibility(double avgView, int avgCount) {
		if (avgCount > 0) {
			setAvgAccessibility(Double.toString(Math.round((avgView/avgCount))/ReportConstants.FLOATHUNDRED));
		}
	}

	/**
	 * @param buf
	 * @param i
	 */
	private void appendEmptyRow(StringBuffer buf, int i) {
		if (i == ReportConstants.INIT_VAL) { // table is empty
	    	buf.append(ReportConstants.TR_TD_EMPTY_TD_TR);
	    }
	}

	/**
	 * @param buf
	 * @param colCounter
	 * @param storeValue
	 * @param col
	 * @return
	 */
	private boolean computeAccessibilityValue(StringBuffer buf, int colCounter,
			boolean storeValue, String col) {
		boolean storeValueRes = storeValue;
		if (colCounter == 0 && containsAccessibility(col)) {
			buf.append(ReportConstants.TD);
			buf.append(col);
			buf.append(ReportConstants.TDEND);
			storeValueRes = true;
		}
		return storeValueRes;
	}

	/**
	 * @param col
	 * @return
	 */
	private double getDoubleViewValue(String col) {
		double viewValue = ReportConstants.FLOATHUNDRED
		- Math.round((Float.valueOf(col)) * ReportConstants.INTHUNDRED)
				/ ReportConstants.FLOATHUNDRED;
		return viewValue;
	}
	
	/**
	 * This method creates measurement for particular property.
	 * @param id
	 * @param propertyName
	 * @param value
	 * @return measurement
	 */
	public Measurement createMeasurement(int id, String propertyName, String value) {
		MeasurementImpl measurement = new MeasurementImpl();
		measurement.setId(id);
		measurement.setIdentifier(propertyName); 
		measurement.setValue(value);
		return measurement;
	}	

	/**
	 * This method creates risk score report in HTML table
	 * @return risk score report in HTML format
	 */
	public  String generateRiskScoreReport(String classification, List<String> configList) {
		String res = ReportConstants.EMPTYSTRING;
		
		CalculationModel calculationModel = createMeasurementsList(
				classification, configList);
		
		String htmlRiskBreakdown = calculationModel.getRiskScoreBreakdownHtml();

		res = res + P_A_HREF_OVERVIEW_OVERVIEW_A;
		res = res + htmlRiskBreakdown;
		return res;
	}
	
	/**
	 * This method creates risk score report for format analysis in HTML table
	 * @return risk score report in HTML format
	 */
	public String generateFormatRiskScoreReport(FormatRiskAnalysisReport formatRiskAnalysisReport, String classification, String config) {
		String res = ReportConstants.EMPTYSTRING;
		
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);		
		List<String> configList = RiskUtils.getConfigurationList(config);		

		CalculationModel calculationModel = createFormatMeasurementsList(
				formatRiskAnalysisReport, classification, configList);
		if (formatRiskAnalysisReport.getSoftwareCount() == 0 && formatRiskAnalysisReport.getVendorsCount() == 0 && formatRiskAnalysisReport.getVersions() == 0 && formatRiskAnalysisReport.getDescriptionCount() == 0) {
			res = res + calculationModel.printEmptyInfoReport(formatRiskAnalysisReport.getExtension());
		} else {
			res = res + calculationModel.getRiskScoreTotalHtml(formatRiskAnalysisReport.getExtension());
			res = res + calculationModel.getRiskScoreBreakdownHtmlExt();
		}
		buf.append(res);
		return buf.toString();
	}
	
	/**
	 * This method checks if preservation dimension already exists in the list
	 * @param dimensionsList
	 * @param dimensionName
	 * @return true if preservation dimension already exists
	 */
	public  boolean preservationDimensionExists(List<PreservationDimension> dimensionsList, String dimensionName) {
		boolean res = false;
		Iterator<PreservationDimension> iter = dimensionsList.iterator();
		while (iter.hasNext()) {
			PreservationDimension dimension = iter.next();
			if (dimension.getName().equals(dimensionName)) {
				res = true;
				break;
			}
		}
		return res;
	}

	/**
	 * This method adds preservation dimension to the preservation dimensions list
	 * if dimension map has content
	 * @param collectionName
	 * @param res
	 * @param dimensionMap
	 * @param configList
	 * @param dimensionName
	 */
	public void addPreservationDimension(String collectionName,
			List<PreservationDimension> res, Map<String, String> dimensionMap,
			List<String> configList, String dimensionName) {
		if (dimensionMap != null && dimensionMap.size() > 0
				&& (configList == null || configList.contains(dimensionName))) {
			if (!preservationDimensionExists(res, dimensionName)) {
				PreservationDimension preservationDimension = new PreservationDimension();
				preservationDimension.setCollectionName(collectionName);
				preservationDimension.setContent(dimensionMap);
				preservationDimension.setName(dimensionName);
				res.add(preservationDimension);
			}
		}
	}
	
	/**
	 * @param totalCount
	 * @param buf
	 * @param europeanaUri
	 */
	public void appendEuropeanaUri(int totalCount, StringBuffer buf,
			String europeanaUri) {
		buf.append(europeanaUri);
		buf.append(RiskConstants.CSV_SEPARATOR);
		buf.append(totalCount);
		buf.append(RiskConstants.CSV_SEPARATOR);
		buf.append(RiskConstants.LINE_END);
	}

	/**
	 * This method verifies if value fits in interval from begin to end. If value
	 * fits increase objects count in map and insert new map value.  
	 * @param map
	 *        The map holding objects count for particular interval
	 * @param begin
	 *        The begin of interval
	 * @param end
	 *        The end of interval
	 * @param value
	 *        The value to verify
	 */
	public void checkInterval(Map<Integer, Integer> map, Integer begin, Integer end, int value) {
		if (begin == null) {
			if (value <= end) {
				map.put(end, map.get(end) + ReportConstants.INIT_VAL);
			} 
		} else {
			if (value > begin && value <= end) {
				map.put(end, map.get(end) + ReportConstants.INIT_VAL);
			} 
		}
	}
	
	/**
	 * This method updates values count for particular dissemination.
	 * @param map
	 *        The map containing dissemination raster values as keys and corresponding entries 
	 *        count as values
	 * @param value
	 *        The string values array
	 * @return
	 */
	public int fillDisseminationMap(Map<String, Integer> map, String[] value) {
		int totalFields = 0;
		if (value != null) {
			for (int i = 0; i < value.length; i++) {
				String normalizedValue = value[i].replaceAll(ReportConstants.NEWLINE, ReportConstants.EMPTYSTRING);
				normalizedValue = normalizedValue.replaceAll(ReportConstants.SEMICOLON, ReportConstants.COMMA);
				if (normalizedValue != null && !normalizedValue.equals(RiskConstants.EMPTY_STRING)) {
					if (!map.containsKey(normalizedValue)) {
						map.put(normalizedValue, ReportConstants.INIT_VAL);
						totalFields++;
					} else {
						map.put(normalizedValue, map.get(normalizedValue) + ReportConstants.INIT_VAL);
						totalFields++;
					}
				}
			} 
		}
		return totalFields;
	}
	
	/**
	 * This method updates values count for particular dissemination.
	 * @param map
	 *        The map containing dissemination raster values as keys and corresponding entries 
	 *        count as values
	 * @param value
	 *        The string value
	 * @return
	 */
	public int fillDisseminationMap(Map<String, Integer> map, String valueStr) {
		int totalFields = 0;
		String valueStr2 = valueStr.replaceAll(ReportConstants.NEWLINE, ReportConstants.EMPTYSTRING);
		String value = valueStr2.replaceAll(ReportConstants.SEMICOLON, ReportConstants.COMMA);

		if (value != null && !value.equals(RiskConstants.EMPTY_STRING)) {
			if (!map.containsKey(value)) {
				map.put(value, ReportConstants.INIT_VAL);
				totalFields++;
			} else {
				map.put(value, map.get(value) + ReportConstants.INIT_VAL);
				totalFields++;
			}
		}
		return totalFields;
	}

	/**
	 * This method creates preservation risk score report object for particular type of report
	 * @param key
	 *        The CsvEnum type of report
	 * @param value
	 *        The CSV formatted content of the report
	 * @param config
	 *        The analysis configuration containing the names of the preservation dimensions
	 * @param classification
	 *        The path to the XML file that comprises risk analysis classifications like
	 *        weight and risk score depending on value
	 * @param reportType
	 *        The type of the risk report
	 * @return preservation dimension object
	 */
	public String generateHtmlReport(//CsvEnum key,
			String id, String config, String classification, 
			RiskReportTypesEnum reportType) {
		
		StringBuffer buf = new StringBuffer();
		buf.append(ReportConstants.EMPTYSTRING);
		try {
			resetReportValues();
			if (id == null || id.length() == 0 || id.equals(" ")) {
				buf.append(LODConstants.HTML_HEADER);
				buf.append("<title>Risk analysis error report</title></head><body><h1>Format ID for extension ");
				buf.append(id);
				buf.append(" not found. Please check the format id. </h1>");
			} else {
				FormatRiskAnalysisReport formatRiskAnalysisReport = 
					preservationRiskmanagementDao.getFormatRiskAnalysisReport(id);
				log.info(ReportConstants.RETRIEVED_FORMAT_ANALYSIS_REPORT + formatRiskAnalysisReport);
				buf.append(generateHtmlReportBegin());				
				buf.append(generateFormatRiskScoreReport(formatRiskAnalysisReport, classification, config));
				buf.append(generateHtmlReportEnd());				
			}
			buf.append(ReportConstants.BODYEND);
			buf.append(ReportConstants.HTMLEND);
		} catch (Exception e) {
			log.info(ReportConstants.ANALYSE_METADATA_ERROR + e);
		}
		return buf.toString();
	}

}
