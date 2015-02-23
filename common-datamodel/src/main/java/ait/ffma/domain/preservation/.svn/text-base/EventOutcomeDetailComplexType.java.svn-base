package ait.ffma.domain.preservation;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;


/**
 * <p>Java class for eventOutcomeDetailComplexType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eventOutcomeDetailComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element ref="{info:lc/xmlns/premis-v2}eventOutcomeDetailNote"/>
 *           &lt;element ref="{info:lc/xmlns/premis-v2}eventOutcomeDetailExtension" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element ref="{info:lc/xmlns/premis-v2}eventOutcomeDetailExtension"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public class EventOutcomeDetailComplexType extends BaseFfmaDomainObject {

	private static final long serialVersionUID = 3815540824455739168L;

	public enum FieldsEnum implements FieldDefEnum {
		EventOutcomeDetailNote        { public Class<?> evalType() { return String.class; } },
		EventOutcomeDetailExtension   { public Class<?> evalType() { return String.class; } };

		public String evalName() { return this.name(); }
	}

	public EventOutcomeDetailComplexType () {
		setFfmaObjectName(EventOutcomeDetailComplexType.class.getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
	}	

	public FieldDefEnum[] getFieldsEnum() {
		return FieldsEnum.values();
	}
	
	/**
	 * @return the eventOutcomeDetailNote
	 */
	public String getEventOutcomeDetailNote() {
		return getString(FieldsEnum.EventOutcomeDetailNote.name());
	}
	
	/**
	 * @param eventOutcomeDetailNote the eventOutcomeDetailNote to set
	 */
	public void setEventOutcomeDetailNote(String value) {
		put(FieldsEnum.EventOutcomeDetailNote.name(), value);
	}
	
	/**
	 * @return the eventOutcomeDetailExtension
	 */
	public String getEventOutcomeDetailExtension() {
		return getString(FieldsEnum.EventOutcomeDetailExtension.name());
	}
	
	/**
	 * @param eventOutcomeDetailExtension the eventOutcomeDetailExtension to set
	 */
	public void setEventOutcomeDetailExtension(String value) {
		put(FieldsEnum.EventOutcomeDetailExtension.name(), value);
	}

}
