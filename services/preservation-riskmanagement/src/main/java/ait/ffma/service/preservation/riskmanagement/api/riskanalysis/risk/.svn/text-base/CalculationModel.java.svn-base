package ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ait.ffma.domain.preservation.riskmanagement.RiskScoreReport;
import ait.ffma.service.preservation.common.api.measurement.Measurement;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.profile.DataItemProfile;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskConstants.RiskLevelEnum;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskAnalysis;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskClassification.RiskFactors.RiskFactor;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskProperty;
import ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskPropertySet;

/**
 * Calculation model manages risk analysis activities and comprises
 * risk analysis structure, risk properties and score break down 
 * analysis. 
 */
public class CalculationModel implements RiskModel, RiskScore {
	
    private static final String EMPTYSTRING = "";

	private static final String TR2 = "</tr>";

	private static final String TD2 = "</td>";

	private static final String TD = "<td>";

	private static final String TR = "<tr>";

	/**
     * A logger object.
     */
    private final Logger log = Logger.getLogger(getClass().getName());
    
    /**
     * Measurements with property IDs and their values.
     */
    private DataItemProfile profile = null;
    
    /**
     * Risk analysis structure that contains property sets, properties
     * and correspondent risk classifications extracted from XML files.
     */
    private RiskAnalysis ra = null;
    
    /**
     * The score break down analysis describes properties involved in
     * score calculation. These properties contains values evaluated 
     * during the risk analysis.
     */
    private RiskPropertySet riskScoreBreakdown = null;
    
    /**
     * The list of defined in XML file risk properties.
     */
    private List<RiskProperty> propertyList;

	/**
	 * This is a regular way to initialize calculation model.
	 */
	public CalculationModel() {
	}

	/**
	 * Parameter profile contains measurements with property IDs and their values.
	 * @param profile
	 */
	public CalculationModel(DataItemProfile profile) {
		this.profile = profile;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskScore#getRiskScoreBreakdown()
	 */
	public RiskPropertySet getRiskScoreBreakdown() {
		return riskScoreBreakdown;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskScore#getRiskProperties()
	 */
	public RiskPropertySet getRiskProperties() {
		ra = RiskUtils.loadRiskAnalysis();
		if (ra == null) {
			log.log(Level.SEVERE, "File riskanalysis.xml could not be loaded!");
		}
		return ra.getRiskFactors();
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskScore#getRiskScore()
	 */
	public Integer getRiskScore() {
		// fill tree with values in DataItemProfile
		if (profile != null) {
			for (Measurement m : profile.getMeasurements()) {
				setPropertyValue(ra.getRiskFactors(), m.getIdentifier(), m.getValue());
			}
		}
		Integer riskScore = calculateRiskScore(ra.getRiskFactors());
		// create risk score breakdown tree:
		createRiskScoreBreakdown();
		return riskScore;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskModel#analyze(java.io.File, java.lang.String, java.lang.String)
	 */
	public RiskAnalysis analyze(File file, String propertySetXml, String classificationXml) {
		RiskAnalysis riskAnalysis = null;
		String propertySetXmlRes = propertySetXml; 
		String classificationXmlRes = classificationXml;
		
		// load risk analysis properties from XML file
		if (propertySetXml == null) {
			propertySetXmlRes = RiskConstants.DEFAULT_PROPERTY_SET_XML;
		}
		if (classificationXml == null || classificationXml.equals(EMPTYSTRING)) {
			classificationXmlRes = RiskConstants.DEFAULT_CLASSIFICATION_XML;
		}

		RiskAnalysis propertiesRiskAnalysis = RiskUtils.loadRiskPropertiesFromXML(RiskConstants.PROPERTIES_XML);
		if (propertiesRiskAnalysis == null) {
			log.log(Level.SEVERE, "Risk analysis properties xml could not be loaded!");
		}
		
		// evaluate property list
		setPropertyId(propertiesRiskAnalysis.getRiskFactors());
		log.info("properties count: " + propertiesRiskAnalysis.getRiskFactors().getProperties().getProperty().size());
		propertyList = propertiesRiskAnalysis.getRiskFactors().getProperties().getProperty();
		RiskUtils.setRiskPropertiesMap(propertyList);
		
		// load default property set containing weights from XML file
		riskAnalysis = RiskUtils.loadRiskPropertiesFromXML(propertySetXmlRes);
		setPropertySetId(riskAnalysis.getRiskFactors());
		
		// load classifications from XML file
		RiskAnalysis classifications = RiskUtils.loadRiskPropertiesFromXML(classificationXmlRes);
		
		// attach full properties and customized classifications to the base risk analysis
		addFullProperties(riskAnalysis.getRiskFactors());
		addClassifications(riskAnalysis.getRiskFactors(), classifications.getRiskFactors());
		
		return riskAnalysis;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskModel#getRiskAnalysis()
	 */
	public RiskAnalysis getRiskAnalysis() {
		return ra;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskModel#setRiskAnalysis(ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskAnalysis)
	 */
	public void setRiskAnalysis(RiskAnalysis ra) {
		this.ra = ra;
	}

	/* (non-Javadoc)
	 * @see ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.RiskModel#setPropertyValue(ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel.RiskPropertySet, java.lang.String, java.lang.String)
	 */
	public void setPropertyValue(RiskPropertySet set, String propertyId, String propertyValue) {
		for (RiskProperty p : set.getProperties().getProperty()) {
			if (p.getId().equals(propertyId)) {
				p.setValue(propertyValue);
				return;
			}
		}
		for (RiskPropertySet ps : set.getPropertySets().getPropertySet()) {
			setPropertyValue(ps, propertyId, propertyValue);
		}
	}
	
	/**
	 * This method adds full risk properties to the risk model provided by
	 * property set XML file
	 * @param set
	 *        The risk model
	 */
	private void addFullProperties(RiskPropertySet set) {
		List<RiskProperty> riskPropertyList = new ArrayList<RiskProperty>();
		for (RiskProperty p : set.getProperties().getProperty()) {
			riskPropertyList.add(RiskUtils.getRiskPropertyById(p.getId()));
		}
		set.getProperties().setProperty(riskPropertyList);
		
		for (RiskPropertySet ps : set.getPropertySets().getPropertySet()) {
			addFullProperties(ps);
		}
	}

	/**
	 * @param set
	 * @return
	 */
	private boolean checkSet(RiskPropertySet set) {
		boolean res = false;
		if (set != null && set.getProperties() != null && set.getProperties().getProperty() != null) {
			if (set.getPropertySets() != null && set.getPropertySets().getPropertySet() != null) {
				res = true;
			}
		}
		return res;
	}
	
	/**
	 * This method adds customized classifications provided by
	 * classifications XML file to risk properties in the risk model 
	 * @param set
	 *        The risk model
	 * @param classifications
	 */
	private void addClassifications(RiskPropertySet set, RiskPropertySet classifications) {
		try {
			if (checkSet(set)) {
				for (RiskProperty p : set.getProperties().getProperty()) {
					computeRiskProperties(classifications, p);
				}
			
				for (RiskPropertySet ps : set.getPropertySets().getPropertySet()) {
//					if (ps.getId().equals("FORMAT_PROPERTY_SET_ID")) {
//						int ii = 0;
//					}
					addClassifications(ps, classifications);
				}
			} else {
				log.info("property set is empty.");
			}
		} catch (Exception e) {
			log.info("Error adding classifications: " + e);
		}
	}

	/**
	 * @param classifications
	 * @param p
	 */
	private void computeRiskProperties(RiskPropertySet classifications,
			RiskProperty p) {
		if (p != null && classifications != null && classifications.getProperties() != null) {
			for (RiskProperty classificationProperty : classifications.getProperties().getProperty()) {
				if (p.getId().equals(classificationProperty.getId())) {
					p.setRiskClassification(classificationProperty.getRiskClassification());
				}
			}
		} else {
			log.info("classifications are empty.");
		}
	}
	
	/**
	 * This method generates property IDs if they are not exist
	 * @param set
	 *        The property set
	 */
	private void setPropertyId(RiskPropertySet set) {
		for (RiskProperty riskProperty : set.getProperties().getProperty()) {
			if (riskProperty.getId() == null || riskProperty.getId().equals(EMPTYSTRING)) {
				riskProperty.setId(RiskUtils.generateID());
				log.info("property: " + riskProperty.getName() + ", id: " + riskProperty.getId());
			}
		}
		for (RiskPropertySet riskPropertySet : set.getPropertySets().getPropertySet()) {
			setPropertyId(riskPropertySet);
		}
	}
	
	/**
	 * This method generates property set IDs if they are not exist
	 * @param set
	 *        The property set
	 */
	private void setPropertySetId(RiskPropertySet set) {
		for (RiskPropertySet riskPropertySet : set.getPropertySets().getPropertySet()) {
			if (riskPropertySet.getId() == null || riskPropertySet.getId().equals(EMPTYSTRING)) {
				riskPropertySet.setId(RiskUtils.generateID());
				log.info("property set: " + riskPropertySet.getName() + ", id: " + riskPropertySet.getId());
			}
			setPropertySetId(riskPropertySet);
		}
	}
	
	/**
	 * This method evaluates risk score for the risk model.
	 * @param set
	 *        The risk model instance
	 * @return risk score
	 */
	private Integer calculateRiskScore(RiskPropertySet set) {
		computePropertySets(set);		
		Integer riskScore = -1;
		for (RiskProperty p : set.getProperties().getProperty()) {
			if (p.getValue() != null && !p.getValue().isEmpty()) {
				if (riskScore == -1) {
					riskScore = 0;
				}
				riskScore += getPropertyRisk(p);
			}
		}
		
		if (set.getPropertySets() != null) {
			for (RiskPropertySet ps : set.getPropertySets().getPropertySet()) {
				Integer psRiskScore = ps.getRiskScore();
				if (psRiskScore > -1) {
					psRiskScore = (int)(psRiskScore * ps.getWeight());
					if (riskScore == -1) {
						riskScore = 0;
					}
					riskScore += psRiskScore;
				}
			}
		}
		set.setRiskScore(riskScore);
		return riskScore;
	}

	/**
	 * @param set
	 */
	private void computePropertySets(RiskPropertySet set) {
		if (set.getPropertySets() != null) {
			for (RiskPropertySet ps : set.getPropertySets().getPropertySet()) {
				calculateRiskScore(ps);
			}	
		}
	}
	
	/**
	 * This method evaluates property risk accordingly to its data type
	 * @param p
	 *        The risk property
	 * @return risk score
	 */
	private Integer getPropertyRisk(RiskProperty p) {
		Integer riskScore = -1;
		switch (p.getMetric()) {
		case STRING:
			riskScore = getStringRiskScore(p, riskScore);
			break;
		case INTEGER:
			riskScore = getIntegerRiskScore(p, riskScore);
			break;
		case DOUBLE:
			riskScore = getDoubleRiskScore(p, riskScore);
			break;
		case DATE:
			riskScore = getDateRiskScore(p, riskScore);
			break;
		case BOOLEAN:
			riskScore = getBooleanRiskScore(p, riskScore);
			break;
		default:
			break;
		} 
		return riskScore;
	}

	/**
	 * @param p
	 * @param riskScore
	 * @return
	 */
	private Integer getBooleanRiskScore(RiskProperty p, Integer riskScore) {
		Integer riskScoreRes = riskScore;
		Boolean booleanValue = getBooleanValue(p.getValue());
		for (RiskFactor rf : p.getRiskClassification().getRiskFactors().getRiskFactor()) {
			Boolean rfVal = false;
			riskScoreRes = handleBooleanRiskValue(riskScore, booleanValue, rf, rfVal);
			if (riskScoreRes != -1) {
				break;
			}
		}
		return riskScoreRes;
	}

	/**
	 * @param p
	 * @param riskScore
	 * @return
	 */
	private Integer getDateRiskScore(RiskProperty p, Integer riskScore) {
		Integer riskScoreRes = riskScore;
		Date dateValue = getDateValue(p.getValue());
		if (dateValue != null) {
			for (RiskFactor rf : p.getRiskClassification().getRiskFactors().getRiskFactor()) {
				Date minVal = getDateValue(rf.getMinValue());
				Date maxVal = getDateValue(rf.getMaxValue());
				riskScoreRes = handleDateRiskValue(riskScore, dateValue, rf, minVal, maxVal);
				if (riskScoreRes != -1) {
					break;
				}
			}				
		} else {
			riskScoreRes = 1;
		}
		
		return riskScoreRes;
	}

	/**
	 * @param p
	 * @param riskScore
	 * @return
	 */
	private Integer getDoubleRiskScore(RiskProperty p, Integer riskScore) {
		Integer riskScoreRes = riskScore;
		Double doubleValue = getDoubleValue(p.getValue());
		for (RiskFactor rf : p.getRiskClassification().getRiskFactors().getRiskFactor()) {
			Double minVal = getDoubleValue(rf.getMinValue());
			Double maxVal = getDoubleValue(rf.getMaxValue());
			riskScoreRes = handleDoubleRiskValue(riskScore, doubleValue, rf, minVal, maxVal);
			if (riskScoreRes != -1) {
				break;
			}
		}
		return riskScoreRes;
	}

	/**
	 * @param p
	 * @param riskScore
	 * @return
	 */
	private Integer getIntegerRiskScore(RiskProperty p, Integer riskScore) {
		Integer riskScoreRes = riskScore;
		Integer intValue = getIntValue(p.getValue());
		for (RiskFactor rf : p.getRiskClassification().getRiskFactors().getRiskFactor()) {
			Integer minVal = getIntValue(rf.getMinValue());
			Integer maxVal = getIntValue(rf.getMaxValue());
			riskScoreRes = handleIntegerRiskValue(riskScore, intValue, rf, minVal, maxVal);
			if (riskScoreRes != -1) {
				break;
			}
		}
		return riskScoreRes;
	}

	/**
	 * @param p
	 * @param riskScore
	 * @return
	 */
	private Integer getStringRiskScore(RiskProperty p, Integer riskScore) {
		Integer riskScoreRes = riskScore;
		String strValue = p.getValue();
		for (RiskFactor rf : p.getRiskClassification().getRiskFactors().getRiskFactor()) {
			String minVal = rf.getMinValue();
			String maxVal = rf.getMaxValue();
			riskScoreRes = handleStringRiskValue(riskScore, strValue, rf, minVal, maxVal);
			if (riskScoreRes != -1) {
				break;
			}
		}
		return riskScoreRes;
	}

	/**
	 * @param riskScore
	 * @param booleanValue
	 * @param rf
	 * @param rfVal
	 * @return
	 */
	private Integer handleBooleanRiskValue(Integer riskScore,
			Boolean booleanValue, RiskFactor rf, Boolean rfVal) {
		Integer riskScoreRes = riskScore;
		Boolean rfValRes = rfVal;
		if (rf.getMinValue() != null && !rf.getMinValue().isEmpty()) {
			rfValRes = getBooleanValue(rf.getMinValue());
		}
		else if (rf.getMaxValue() != null && !rf.getMaxValue().isEmpty()) {
			rfValRes = getBooleanValue(rf.getMaxValue());
		}
		if (rfValRes.equals(booleanValue)) {
			riskScoreRes = rf.getRiskScore();
		}
		return riskScoreRes;
	}

	/**
	 * @param riskScore
	 * @param dateValue
	 * @param rf
	 * @param minVal
	 * @param maxVal
	 * @return
	 */
	private Integer handleDateRiskValue(Integer riskScore, Date dateValue,
			RiskFactor rf, Date minVal, Date maxVal) {
		Integer riskScoreRes = riskScore;
		if (minVal == null) {
			if (dateValue.equals(maxVal)) {
				riskScoreRes = rf.getRiskScore();
			}
		}
		else if (maxVal == null) {
			if (dateValue.equals(minVal)) {
				riskScoreRes = rf.getRiskScore();
			}
		}
		else if (minVal.before(dateValue) && maxVal.after(dateValue)) {
			riskScoreRes = rf.getRiskScore();
		}
		return riskScoreRes;
	}

	/**
	 * @param riskScore
	 * @param doubleValue
	 * @param rf
	 * @param minVal
	 * @param maxVal
	 * @return
	 */
	private Integer handleDoubleRiskValue(Integer riskScore,
			Double doubleValue, RiskFactor rf, Double minVal, Double maxVal) {
		Integer riskScoreRes = riskScore;
		if (minVal == null) {
			if (doubleValue.equals(maxVal)) {
				riskScoreRes = rf.getRiskScore();
			}
		}
		else if (maxVal == null) {
			if (doubleValue.equals(minVal)) {
				riskScoreRes = rf.getRiskScore();
			}
		}
		else if (minVal <= doubleValue && maxVal >= doubleValue) {
			riskScoreRes = rf.getRiskScore();
		}
		return riskScoreRes;
	}

	/**
	 * @param riskScore
	 * @param intValue
	 * @param rf
	 * @param minVal
	 * @param maxVal
	 * @return
	 */
	private Integer handleIntegerRiskValue(Integer riskScore, Integer intValue,
			RiskFactor rf, Integer minVal, Integer maxVal) {
		Integer riskScoreRes = riskScore;
		if (minVal == null) {
			if (intValue.equals(maxVal)) {
				riskScoreRes = rf.getRiskScore();
			}
		}
		else if (maxVal == null) {
			if (intValue.equals(minVal)) {
				riskScoreRes = rf.getRiskScore();
			}
		}
		else if (minVal <= intValue && maxVal >= intValue) {
			riskScoreRes = rf.getRiskScore();
		}
		return riskScoreRes;
	}

	/**
	 * @param riskScore
	 * @param strValue
	 * @param rf
	 * @param minVal
	 * @param maxVal
	 * @return
	 */
	private Integer handleStringRiskValue(Integer riskScore, String strValue,
			RiskFactor rf, String minVal, String maxVal) {
		Integer riskScoreRes = riskScore;
		if (minVal == null || minVal.equals(EMPTYSTRING)) {
			if (strValue.equals(maxVal)) {
				riskScoreRes = rf.getRiskScore();
			}
		}
		else if (maxVal == null || maxVal.equals(EMPTYSTRING)) {
			if (strValue.equals(minVal)) {
				riskScoreRes = rf.getRiskScore();
			}
		}
		return riskScoreRes;
	}
	
	/**
	 * @param s
	 * @return
	 */
	private Integer getIntValue(String s) {
		int res = 0;
		if (s != null && !s.equals("")) {
		   res = Integer.valueOf(s);
		}
		return res;
	}
	
	/**
	 * @param s
	 * @return
	 */
	private Double getDoubleValue(String s) {
		return Double.valueOf(s);
	}
	
	/**
	 * @param s
	 * @return
	 */
	private Boolean getBooleanValue(String s) {
		return (s!= null && s.equalsIgnoreCase("true")) ? true : false;
	}
	
	/**
	 * @param s
	 * @return
	 */
	private Date getDateValue(String s) {
		Date dateValue = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			dateValue = df.parse(s);
		} catch (ParseException e) {
			return null;
		}
		return dateValue;
	}
	
	/**
	 * This method stores risk score break down result in a variable.
	 */
	private void createRiskScoreBreakdown() {
		riskScoreBreakdown = buildBreakdown(ra.getRiskFactors());
	}
	
	/**
	 * This method adds color to the HTML text
	 * @param color
	 * @param value
	 * @return colored value
	 */
	public String addColor(RiskLevelEnum color, String value) {
		return "<font color=\"" + color.getColorCode() + "\">" + value + "</font>";
	}
	
	/**
	 * This method evaluates risk level depending from score
	 * @param score
	 * @return
	 */
	public RiskLevelEnum evaluateRiskRange(int score) {
		RiskLevelEnum res = RiskLevelEnum.High;
		if (score != -1 && score >= 0) {
			if (score >= 0 && score <= RiskConstants.RISK_SCORE_LOW_RANGE) {
				res = RiskLevelEnum.Low;
			}
			if (score > RiskConstants.RISK_SCORE_LOW_RANGE && score <= RiskConstants.RISK_SCORE_MIDDLE_RANGE) {
				res = RiskLevelEnum.Middle;
			}
			if (score > RiskConstants.RISK_SCORE_MIDDLE_RANGE) {
				res = RiskLevelEnum.High;
			}
		}
		return res;
	}
	
	public RiskLevelEnum evaluateRiskRangeExt(double score) {
		RiskLevelEnum res = RiskLevelEnum.High;
		if (score != -1 && score >= 0) {
			if (score >= 0 && score <= RiskConstants.RISK_SCORE_LOW_RANGE) {
				res = RiskLevelEnum.Low;
			}
			if (score > RiskConstants.RISK_SCORE_LOW_RANGE && score <= RiskConstants.RISK_SCORE_MIDDLE_RANGE) {
				res = RiskLevelEnum.Middle;
			}
			if (score > RiskConstants.RISK_SCORE_MIDDLE_RANGE) {
				res = RiskLevelEnum.High;
			}
		}
		return res;
	}
	
	/**
	 * This method evaluates risk level depending from score
	 * @param score
	 * @return
	 */
	public String evaluateRiskLevelFromScore(int score, boolean isColored) {
		String res = null;
		RiskLevelEnum range = evaluateRiskRange(score);
		if (isColored) {
			res = addColor(range, range.name());
		} else {
			res = range.name();
		}
		return res;
	}
	
	public String evaluateRiskLevelFromScoreExt(double score, boolean isColored) {
		String res = null;
		RiskLevelEnum range = evaluateRiskRangeExt(score);
		if (isColored) {
			res = addColor(range, range.name());
		} else {
			res = range.name();
		}
		return res;
	}
	
	/**
	 * This method evaluates risk score color depending from score range
	 * @param score
	 * @return
	 */
	public String evaluateRiskScore(int score, boolean isColored) {
		String res = null;
		RiskLevelEnum range = evaluateRiskRange(score);
		Double dblScore = Math.round((Double.valueOf(score)))/100.0;
		if (isColored) {
			res = addColor(range, dblScore.toString());
		} else {
			res = dblScore.toString();
		}
		return res;
	}
	
	public String evaluateRiskScoreExt(double score, boolean isColored) {
		String res = null;
		RiskLevelEnum range = evaluateRiskRangeExt(score);
		Double dblScore = Math.round((score))/100.0;
		if (isColored) {
			res = addColor(range, dblScore.toString());
		} else {
			res = dblScore.toString();
		}
		return res;
	}
	
	public String evaluateWeightedRiskScoreExt(double score, double weight, boolean isColored) {
		String res = null;
		RiskLevelEnum range = evaluateRiskRangeExt(score);
		Double dblScore = weight*Math.round((score))/100.0;
		if (isColored) {
			res = addColor(range, dblScore.toString());
		} else {
			res = dblScore.toString();
		}
		return res;
	}
	
	/**
	 * This method stores risk score break down result in a variable.
	 */
	public String getRiskScoreBreakdownHtml() {
		StringBuffer buf = new StringBuffer();
		printFormatRiskAnalysisHeader(buf);

		int totalScore = 0;
		int counter = 0;
		RiskPropertySet source = ra.getRiskFactors();
		if (source.getRiskScore() > -1) {
			for (RiskProperty p : source.getProperties().getProperty()) {
				if (p.getValue()!= null && !p.getValue().isEmpty()) {
					if (p.getValue() != null) {
						int score = getPropertyRisk(p);
						totalScore = totalScore + score;
						counter++;
					}
				}
			}
		}
		if (source.getRiskScore() > -1) {
			printRiskScores(buf, totalScore, counter, source);
		}
		buf.append("</table>");
		return buf.toString();
	}

	/**
	 * This method stores risk score break down result in a variable including weight value for each 
	 * risk factor.
	 */
	public String getRiskScoreBreakdownHtmlExt() {
		StringBuffer buf = new StringBuffer();
		printFormatRiskAnalysisHeader(buf);

		double totalScore = 0;
		int counter = 0;
		RiskPropertySet source = ra.getRiskFactors();
		if (source.getRiskScore() > -1) {
			for (RiskProperty p : source.getProperties().getProperty()) {
				if (p.getValue()!= null && !p.getValue().isEmpty()) {
					if (p.getValue() != null) {
						int score = getPropertyRisk(p);
						totalScore = totalScore + score*p.getRiskClassification().getWeight();
//						totalScore = totalScore + score;
						counter++;
					}
				}
			}
		}
		if (source.getRiskScore() > -1) {
			printRiskScoresExt(buf, totalScore, counter, source);
		}
		buf.append("</table>");
		return buf.toString();
	}

	/**
	 * This method demonstrates the total risk score result in HTML format.
	 */
	public String getRiskScoreTotalHtml(String id) {
		StringBuffer buf = new StringBuffer();
		printFormatTotalRiskAnalysisHeader(buf);

		double totalScore = 0;
		int counter = 0;
		RiskPropertySet source = ra.getRiskFactors();
		if (source.getRiskScore() > -1) {
			for (RiskProperty p : source.getProperties().getProperty()) {
				if (p.getValue()!= null && !p.getValue().isEmpty()) {
					if (p.getValue() != null) {
						int score = getPropertyRisk(p);
						totalScore = totalScore + score*p.getRiskClassification().getWeight();
//						totalScore = totalScore + score;
						counter++;
					}
				}
			}
		}
		if (source.getRiskScore() > -1) {
			printTotalRiskScore(buf, totalScore, counter, source, id);
		}
		buf.append("</table>");
		return buf.toString();
	}

	/**
	 * @param buf
	 * @param totalScore
	 * @param counter
	 * @param source
	 */
	private void printRiskScores(StringBuffer buf, int totalScore, int counter,
			RiskPropertySet source) {
		for (RiskProperty p : source.getProperties().getProperty()) {
			if (p.getValue()!= null && !p.getValue().isEmpty()) {
				if (p.getValue() != null) {
					int score = getPropertyRisk(p);
					printRiskScoreRowExt(buf, totalScore, counter, p, score);
				}
			}
		}
	}

	/**
	 * @param buf
	 * @param totalScore
	 * @param counter
	 * @param source
	 */
	private void printRiskScoresExt(StringBuffer buf, double totalScore, int counter,
			RiskPropertySet source) {
		for (RiskProperty p : source.getProperties().getProperty()) {
			if (p.getValue()!= null && !p.getValue().isEmpty()) {
				if (p.getValue() != null) {
					int score = getPropertyRisk(p);
					printRiskScoreRowExt(buf, totalScore, counter, p, score);
				}
			}
		}
	}

	private void printTotalRiskScore(StringBuffer buf, double totalScore, int counter,
			RiskPropertySet source, String id) {
		buf.append(TR);
		buf.append(TD);
    	buf.append(id);
		buf.append(TD2);
		buf.append(TD);
    	buf.append(evaluateRiskScoreExt(totalScore/counter, true));
		buf.append(TD2);
		buf.append(TD);
		buf.append(evaluateRiskLevelFromScoreExt(totalScore/counter, true));
		buf.append(TD2);
		buf.append(TR2);
	}

	/**
	 * @param buf
	 * @param totalScore
	 * @param counter
	 * @param p
	 * @param score
	 */
	private void printRiskScoreRow(StringBuffer buf, int totalScore,
			int counter, RiskProperty p, int score) {
		buf.append(TR);
		buf.append(TD);
		buf.append(p.getName());
		buf.append(TD2);
		buf.append(TD);
		buf.append(p.getValue());
		buf.append(TD2);
		buf.append(TD);
		buf.append(evaluateRiskScore(score, true));
		buf.append(TD2);
		buf.append(TD);
		buf.append(evaluateRiskLevelFromScore(score, true));
		buf.append(TD2);
		buf.append(TD);
		buf.append(evaluateRiskScore(totalScore/counter, true));
		buf.append(TD2);
		buf.append(TD);
		buf.append(evaluateRiskLevelFromScore(totalScore/counter, true));
		buf.append(TD2);
		buf.append(TR2);
	}

	/**
	 * @param buf
	 * @param totalScore
	 * @param counter
	 * @param p
	 * @param score
	 */
	private void printRiskScoreRowExt(StringBuffer buf, double totalScore,
			double counter, RiskProperty p, int score) {
		buf.append(TR);
		buf.append(TD);
		buf.append(p.getName());
		buf.append(TD2);
		buf.append(TD);
		buf.append(p.getValue());
		buf.append(TD2);
		double weight = p.getRiskClassification().getWeight();
		buf.append(TD);
		buf.append(evaluateRiskScoreExt(score, true));
		buf.append(TD2);
		buf.append(TD);
		buf.append(weight);
		buf.append(TD2);
		buf.append(TD);
		buf.append(evaluateWeightedRiskScoreExt(score, weight, true));
		buf.append(TD2);
		buf.append(TD);
		buf.append(evaluateRiskLevelFromScore(score, true));
		buf.append(TD2);
//		buf.append(TD);
//		buf.append(evaluateRiskScoreExt(totalScore/counter, true));
//		buf.append(TD2);
//		buf.append(TD);
//		buf.append(evaluateRiskLevelFromScoreExt(totalScore/counter, true));
//		buf.append(TD2);
		buf.append(TR2);
	}

	/**
	 * @param buf
	 */
	private void printRiskAnalysisHeader(StringBuffer buf) {
		buf.append(EMPTYSTRING);
		buf.append("<h2><a name=\"risk_score\">Preservation Score: RiskAnalysis</a></h2>");
		buf.append("<table border=\"1\">");
		buf.append(TR);
		buf.append("<th>Preservation Dimension</th><th>Average Risk Value</th><th>Risk Score (min=0.0, max=1.0)</th><th>Risk Level</th><th>Total Risk Score</th><th>Total Risk Level</th>");
		buf.append(TR2);
	}
	
	/**
	 * @param buf
	 */
	private void printFormatRiskAnalysisHeader(StringBuffer buf) {
		buf.append(EMPTYSTRING);
		buf.append("<div class=\"box mezzi\">" + 
			 	"<h3>Detailed List of Format Risk Scores</h3>" +
			    "</div>");
//		buf.append("<h2><a name=\"risk_score\">Detailed List of Format Risk Scores</a></h2>");
		buf.append("<table width=\"100%\">"); //"<table border=\"1\">");
		buf.append(TR);
		buf.append("<th>Risk Factor</th><th>Property Value</th><th>Risk Score</th><th>Weight</th><th>Weighted Risk Score</th><th>Risk Level</th>");
//		buf.append("<th>Risk Factor</th><th>Risk Value</th><th>Weight</th><th>Risk Score</th><th>Weighted Risk Score</th><th>Risk Level</th>");
//		buf.append("<th>Risk Factor</th><th>Risk Value</th><th>Weight (min=0.0, max=1.0)</th><th>Risk Score (min=0.0, max=1.0)</th><th>Weighted Risk Score (min=0.0, max=1.0)</th><th>Risk Level</th>");
		buf.append(TR2);
	}
	
	public String printEmptyInfoReport(String ext) {
		StringBuffer buf = new StringBuffer();
		buf.append(EMPTYSTRING);
		buf.append("<div class=\"box mezzi\">" + 
			 	"<h3>Currently no entries found in LOD repositories for given file format '" + ext + "'</h3>" +
			    "</div>");
//		buf.append("<h2><a name=\"empty_report\">Currently no entries found in LOD repositories for given file format: " + ext + "</a></h2>");
		return buf.toString();
	}
	
	private void printFormatTotalRiskAnalysisHeader(StringBuffer buf) {
//		buf.append(EMPTYSTRING);
////		buf.append("<h2><a name=\"risk_score\">Preservation Format Total Risk Score</a></h2>");
//		buf.append("<table border=\"1\">");
//		buf.append(TR);
//		buf.append("<th>File Format Extension</th><th>Total Risk Score (min=0.0, max=1.0)</th><th>Total Risk Level</th>");
//		buf.append(TR2);
		buf.append("<div class=\"box mezzi\">" + 
	 	"<h3>File Format Risk Analysis Report</h3>" +
			"<p>A total risk score valuea a ranged from min=0.0 to max=1.0, where 1.0 means the highest risk. To use the service the retrieval of overall format information from Linked Open Data (LOD) repositories like Freebase, Pronom, DBPedia and AIT should be done. " +
			"<a href=\"/ffma/preservation-riskmanagement/rest/loddataanalysis/checkdataexist\" target=\"_new\">Check database content.</a>" +
	"</div>" +														
	"<table width=\"100%\">" +
		"<tr>" +
			"<th>File Format</th>" +
			"<th>Overall Risk Score</th>" +
			"<th>Overall Risk Level</th>" +
		"</tr>");
	}
	
	/**
	 * This method stores risk score break down result in a risk score report list.
	 */
	public List<RiskScoreReport> getRiskScoreBreakdownList() {
		List<RiskScoreReport> res = new ArrayList<RiskScoreReport>();
		int totalScore = 0;
		int counter = 0;
		RiskPropertySet source = ra.getRiskFactors();
		if (source.getRiskScore() > -1) {
			for (RiskProperty p : source.getProperties().getProperty()) {
				if (p.getValue()!= null && !p.getValue().isEmpty()) {
					if (p.getValue() != null) {
						int score = getPropertyRisk(p);
						totalScore = totalScore + score;
						counter++;
					}
				}
			}
		}
		if (source.getRiskScore() > -1) {
			printRiskScoreReport(res, totalScore, counter, source);
		}
		return res;
	}

	/**
	 * @param res
	 * @param totalScore
	 * @param counter
	 * @param source
	 */
	private void printRiskScoreReport(List<RiskScoreReport> res,
			int totalScore, int counter, RiskPropertySet source) {
		for (RiskProperty p : source.getProperties().getProperty()) {
			if (p.getValue()!= null && !p.getValue().isEmpty()) {
				if (p.getValue() != null) {
					int score = getPropertyRisk(p);
					RiskScoreReport riskScoreReport = new RiskScoreReport();
					riskScoreReport.setRiskPropertyName(p.getName());
					riskScoreReport.setAverageRiskValue(p.getValue());
					riskScoreReport.setRiskScore(evaluateRiskScore(score, false));
					riskScoreReport.setRiskLevel(evaluateRiskLevelFromScore(score, false));
					riskScoreReport.setTotalRiskScore(evaluateRiskScore(totalScore/counter, false));
					riskScoreReport.setTotalRiskLevel(evaluateRiskLevelFromScore(totalScore/counter, false));
					res.add(riskScoreReport);
				}
			}
		}
	}
	
	/**
	 * This method builds break down analysis that describes properties involved in
     * score calculation. These properties contains values evaluated 
     * during the risk analysis.
	 * @param source 
	 *        The risk analysis structure
	 * @return properties involved in risk score calculation
	 */
	private RiskPropertySet buildBreakdown(RiskPropertySet source) {
		RiskPropertySet newSet = null;
		List<RiskProperty> scorePropertyList = new ArrayList<RiskProperty>();
		List<RiskPropertySet> scorePropertySetList = new ArrayList<RiskPropertySet>();
		RiskPropertySet.Properties scoreProperties = new RiskPropertySet.Properties();
		RiskPropertySet.PropertySets scorePropertySets = new RiskPropertySet.PropertySets();
		if (source.getRiskScore() > -1) {
			newSet = new RiskPropertySet();
			newSet.setName(source.getName());
			newSet.setRiskScore(source.getRiskScore());
			newSet.setWeight(source.getWeight());
			for (RiskProperty p : source.getProperties().getProperty()) {
				if (p.getValue()!= null && !p.getValue().isEmpty()) {
					RiskProperty newProperty = new RiskProperty();
					newProperty.setDescription(p.getDescription());
					newProperty.setId(p.getId());
					newProperty.setMetric(p.getMetric());
					newProperty.setName(p.getName());
					newProperty.setRiskClassification(p.getRiskClassification());
					if (p.getValue() != null) {
						log.info("Add score property: " + newProperty.getName() + ", with value: " + p.getValue());
						newProperty.setValue(p.getValue());
						scorePropertyList.add(newProperty);
					}
				}
			}
			scoreProperties.setProperty(scorePropertyList);
			newSet.setProperties(scoreProperties);
			
			for (RiskPropertySet s : source.getPropertySets().getPropertySet()) {
				RiskPropertySet child = buildBreakdown(s);
				if (child != null) {
					if (newSet.getPropertySets() != null) {
						newSet.getPropertySets().getPropertySet().add(child);
					} else {
						scorePropertySetList.add(child);
						scorePropertySets.setPropertySet(scorePropertySetList);
						newSet.setPropertySets(scorePropertySets);
					}
				}
			}
		}
		return newSet;
	}

}
