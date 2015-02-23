package ait.ffma.domain.preservation;

import java.util.List;

import ait.ffma.domain.BaseFfmaDomainObject;
import ait.ffma.domain.FieldDefEnum;
import ait.ffma.factory.ComponentNameConstants;


/**
 * <p>Java class for agentComplexType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="agentComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{info:lc/xmlns/premis-v2}AgentIdentifier" maxOccurs="unbounded"/>
 *         &lt;element ref="{info:lc/xmlns/premis-v2}AgentName" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{info:lc/xmlns/premis-v2}AgentType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="xmlID" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="version" type="{info:lc/xmlns/premis-v2}versionSimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

/**
 * This class should be a generated class.
 */
public class AgentComplexType extends BaseFfmaDomainObject {

	private static final long serialVersionUID = -1023310180589500099L;
	
	public enum FieldsEnum implements FieldDefEnum {
		AgentIdentifier     { public Class<?> evalType() { return List.class; } },
		AgentName           { public Class<?> evalType() { return String.class; } },
		AgentType           { public Class<?> evalType() { return String.class; } };

		public String evalName() { return this.name(); }
	}
	
	public FieldDefEnum[] getFieldsEnum(){
		return FieldsEnum.values();
	}
	
	/**
	 * Constructor by component name
	 * @param FfmaObjectName
	 */
	public AgentComplexType () {
		setFfmaObjectName(AgentComplexType.class.getSimpleName());
		setComponentName(ComponentNameConstants.COMPONENT_PRESERVATION_RISKMANAGEMENT);
	}
	
	/**
     * Gets the value of the AgentIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the AgentIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAgentIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AgentIdentifierComplexType }
     * 
     * 
     */
    @SuppressWarnings("unchecked")
	public List<AgentIdentifierComplexType> getAgentIdentifier() {
        return (List<AgentIdentifierComplexType>) get(FieldsEnum.AgentIdentifier.name());
    }

    /**
	 * @param AgentIdentifier the AgentIdentifier to set
	 */
	public void setAgentIdentifier(List<AgentIdentifierComplexType> agentIdentifier) {
		put(FieldsEnum.AgentIdentifier.name(), agentIdentifier);
	}

    /**
     * Gets the value of the AgentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentType() {
    	return getString(FieldsEnum.AgentType.name());
    }

    /**
     * Sets the value of the AgentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentType(String value) {
    	put(FieldsEnum.AgentType.name(), value);
    }

}
