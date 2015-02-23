package ait.ffma.domain.preservation.riskmanagement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

@SuppressWarnings("restriction")
@XmlRootElement
public class RiskScoreReport extends BaseFfmaDomainObject {

	private static final long serialVersionUID = 2785545641647496353L;
	 
	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum {
		CollectionId                  { public Class<?> evalType() { return Long.class; } },
		CollectionName                { public Class<?> evalType() { return String.class; } },
		RiskPropertyName              { public Class<?> evalType() { return String.class; } },
		AverageRiskValue              { public Class<?> evalType() { return String.class; } },
		RiskScore                     { public Class<?> evalType() { return String.class; } }, // min 0.0 - max 1.0
		RiskLevel                     { public Class<?> evalType() { return String.class; } }, // low, middle, high
		TotalRiskScore                { public Class<?> evalType() { return String.class; } }, // min 0.0 - max 1.0
		TotalRiskLevel                { public Class<?> evalType() { return String.class; } }; // low, middle, high

		public String evalName() { return this.name(); }		
	}
	
	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 * @param FfmaObjectName
	 */
	public RiskScoreReport() {
		setFfmaObjectName(getClass().getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
	}
	
	/**
	 * @return the collectionId
	 */
	@XmlElement
	public Long getCollectionId() {
		return Long.valueOf((String) get(FieldsEnum.CollectionId.name()));
	}

	public void setCollectionId(String collectionId) {
		this.put(FieldsEnum.CollectionId.name(), collectionId);
	}

	/**
	 * @return the collectionName
	 */
	@XmlElement
	public String getCollectionName() {
		return getString(FieldsEnum.CollectionName.name());
	}

	public void setCollectionName(String collectionName) {
		this.put(FieldsEnum.CollectionName.name(), collectionName);
	}

	@XmlElement
	public String getRiskPropertyName() {
		return getString(FieldsEnum.RiskPropertyName.name());
	}

	public void setRiskPropertyName(String riskPropertyName) {
		this.put(FieldsEnum.RiskPropertyName.name(), riskPropertyName);
	}

	@XmlElement
	public String getAverageRiskValue() {
		return getString(FieldsEnum.AverageRiskValue.name());
	}

	public void setAverageRiskValue(String averageRiskValue) {
		this.put(FieldsEnum.AverageRiskValue.name(), averageRiskValue);
	}

	@XmlElement
	public String getRiskScore() {
		return getString(FieldsEnum.RiskScore.name());
	}

	public void setRiskScore(String riskScore) {
		this.put(FieldsEnum.RiskScore.name(), riskScore);
	}

	@XmlElement
	public String getRiskLevel() {
		return getString(FieldsEnum.RiskLevel.name());
	}

	public void setRiskLevel(String riskLevel) {
		this.put(FieldsEnum.RiskLevel.name(), riskLevel);
	}

	@XmlElement
	public String getTotalRiskScore() {
		return getString(FieldsEnum.TotalRiskScore.name());
	}

	public void setTotalRiskScore(String totalRiskScore) {
		this.put(FieldsEnum.TotalRiskScore.name(), totalRiskScore);
	}

	@XmlElement
	public String getTotalRiskLevel() {
		return getString(FieldsEnum.TotalRiskLevel.name());
	}

	public void setTotalRiskLevel(String totalRiskLevel) {
		this.put(FieldsEnum.TotalRiskLevel.name(), totalRiskLevel);
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
