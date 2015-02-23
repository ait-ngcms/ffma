package ait.ffma.domain.preservation;


import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;


/**
 * <p>Java class for agentIdentifierComplexType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="agentIdentifierComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{info:lc/xmlns/premis-v2}agentIdentifierType"/>
 *         &lt;element ref="{info:lc/xmlns/premis-v2}agentIdentifierValue"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.w3.org/1999/xlink}simpleLink"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

public class AgentIdentifierComplexType extends BaseFfmaDomainObject {

	private static final long serialVersionUID = 6885192154180369608L;
	
	public enum FieldsEnum implements FieldDefEnum {
		AgentIdentifierType   { public Class<?> evalType() { return String.class; } },
		AgentIdentifierValue  { public Class<?> evalType() { return String.class; } };

		public String evalName() { return this.name(); }
	}

	public FieldDefEnum[] getFieldsEnum() {
		return FieldsEnum.values();
	}

	

	public AgentIdentifierComplexType () {
		setFfmaObjectName(AgentIdentifierComplexType.class.getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
	}
	
	
    
	/**
     * Gets the value of the agentIdentifierType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentIdentifierType() {
        return 	getString(FieldsEnum.AgentIdentifierType.name());

    }

    /**
     * Sets the value of the agentIdentifierType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentIdentifierType(String value) {
		put(FieldsEnum.AgentIdentifierType.name(), value);
    }

    /**
     * Gets the value of the agentIdentifierValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentIdentifierValue() {
        return getString(FieldsEnum.AgentIdentifierValue.name());
    }

    /**
     * Sets the value of the agentIdentifierValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentIdentifierValue(String value) {
		put(FieldsEnum.AgentIdentifierValue.name(), value);
    }

	

}
