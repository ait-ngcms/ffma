package ait.ffma.domain.preservation;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;


/**
 * <p>Java class for eventOutcomeInformationComplexType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eventOutcomeInformationComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element ref="{info:lc/xmlns/premis-v2}eventOutcome"/>
 *           &lt;element ref="{info:lc/xmlns/premis-v2}eventOutcomeDetail" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element ref="{info:lc/xmlns/premis-v2}eventOutcomeDetail" maxOccurs="unbounded"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public class EventOutcomeInformationComplexType extends BaseFfmaDomainObject {

	private static final long serialVersionUID = 8569462356276865631L;

	public enum FieldsEnum implements FieldDefEnum {
		EventOutcome           { public Class<?> evalType() { return String.class; } },
		EventOutcomeDetail     { public Class<?> evalType() { return EventOutcomeDetailComplexType.class; } };

		public String evalName() { return this.name(); }
	}

	public EventOutcomeInformationComplexType () {
		setFfmaObjectName(EventOutcomeInformationComplexType.class.getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
	}	

	public FieldDefEnum[] getFieldsEnum() {
		return FieldsEnum.values();
	}
	
	/**
	 * @return the eventOutcomeDetailComplexType
	 */
	public EventOutcomeDetailComplexType getEventOutcomeDetailComplexType() {
		return (EventOutcomeDetailComplexType) get(FieldsEnum.EventOutcomeDetail.name());
	}

	/**
	 * @param eventOutcomeDetailComplexType the eventOutcomeDetailComplexType to set
	 */
	public void setEventOutcomeDetailComplexType(EventOutcomeDetailComplexType value) {
		put(FieldsEnum.EventOutcomeDetail.name(), value);
	}

	/**
	 * @return the eventOutcome
	 */
	public String getEventOutcome() {
		return getString(FieldsEnum.EventOutcome.name());
	}

	/**
	 * @param eventOutcome the eventOutcome to set
	 */
	public void setEventOutcome(String value) {
		put(FieldsEnum.EventOutcome.name(), value);
	}
	
}
