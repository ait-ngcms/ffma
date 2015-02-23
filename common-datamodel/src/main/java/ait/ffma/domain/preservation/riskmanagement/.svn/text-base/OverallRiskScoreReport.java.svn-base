package ait.ffma.domain.preservation.riskmanagement;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.ClassDefEnum;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;

@SuppressWarnings("restriction")
@XmlRootElement
public class OverallRiskScoreReport extends BaseFfmaDomainObject {

	private static final long serialVersionUID = -8075809724207226991L;

	@XmlTransient
	public enum FieldsEnum implements FieldDefEnum, ClassDefEnum {
		CollectionId                  { public Class<?> evalType() { return Long.class; } },
		CollectionName                { public Class<?> evalType() { return String.class; } },
		RiskScoreReportsList          { public Class<?> evalType() { return List.class; } };

		public String evalName() { return this.name(); }		

		/**
		 * This method returns the class of the list by passed enumeration field
		 * @return the class of the list
		 */
		public Class<?> evalListType() { 
			return RiskScoreReport.class;
		}	
	}
	
	@XmlTransient
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 * @param FfmaObjectName
	 */
	public OverallRiskScoreReport() {
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

	@SuppressWarnings("unchecked")
	@XmlElement
	public List<RiskScoreReport> getRiskScoreReportsList() {
		return (List<RiskScoreReport>)(get(FieldsEnum.RiskScoreReportsList.name()));
	}

	public void setRiskScoreReportsList(List<RiskScoreReport> value) {
		this.put(FieldsEnum.RiskScoreReportsList.name(), value);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * This method returns risk score report object by passed name
	 * @param riskScoreReportsName
	 *        The name of risk score report
	 * @return risk score report object
	 */
	public RiskScoreReport getRiskScoreReport(String riskScoreReportsName) {
		RiskScoreReport res = null;
		List<RiskScoreReport> riskScoreReportsList = getRiskScoreReportsList();
		Iterator<RiskScoreReport> iterRiskScoreReport = riskScoreReportsList.iterator();
		while (iterRiskScoreReport.hasNext()) {
			RiskScoreReport riskScoreReport = iterRiskScoreReport.next();
			if (riskScoreReport.getRiskPropertyName().equals(riskScoreReportsName)) {
				res = riskScoreReport;
				break;
			}
		}
		return res;
	}
}
