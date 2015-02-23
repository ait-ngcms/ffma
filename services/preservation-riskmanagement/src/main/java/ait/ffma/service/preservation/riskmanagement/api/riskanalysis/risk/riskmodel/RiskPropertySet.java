//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.03.14 at 10:27:03 AM MEZ 
//


package ait.ffma.service.preservation.riskmanagement.api.riskanalysis.risk.riskmodel;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RiskPropertySet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RiskPropertySet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="riskScore" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="propertyIDs">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="propertyIDs" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="propertySetIDs">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="propertySetIDs" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="properties">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="property" type="{http://www.example.org/RiskAnalysis}RiskProperty" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="propertySets">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="propertySet" type="{http://www.example.org/RiskAnalysis}RiskPropertySet" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RiskPropertySet", propOrder = {
    "id",
    "name",
    "version",
    "weight",
    "riskScore",
    "propertyIDs",
    "propertySetIDs",
    "properties",
    "propertySets"
})
@XmlRootElement(name = "RiskPropertySet")
public class RiskPropertySet {

    @XmlElement(required = true, defaultValue = "")
    private String id;
    @XmlElement(required = true, defaultValue = "empty")
    private String name;
    @XmlElement(required = true, defaultValue = "1.0")
    private String version;
    @XmlElement(defaultValue = "1.0")
    private double weight;
    @XmlElement(defaultValue = "0")
    private int riskScore;
    @XmlElement(required = true)
    private RiskPropertySet.PropertyIDs propertyIDs;
    @XmlElement(required = true)
    private RiskPropertySet.PropertySetIDs propertySetIDs;
    @XmlElement(required = true)
    private RiskPropertySet.Properties properties;
    @XmlElement(required = true)
    private RiskPropertySet.PropertySets propertySets;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the weight property.
     * 
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     */
    public void setWeight(double value) {
        this.weight = value;
    }

    /**
     * Gets the value of the riskScore property.
     * 
     */
    public int getRiskScore() {
        return riskScore;
    }

    /**
     * Sets the value of the riskScore property.
     * 
     */
    public void setRiskScore(int value) {
        this.riskScore = value;
    }

    /**
     * Gets the value of the propertyIDs property.
     * 
     * @return
     *     possible object is
     *     {@link RiskPropertySet.PropertyIDs }
     *     
     */
    public RiskPropertySet.PropertyIDs getPropertyIDs() {
        return propertyIDs;
    }

    /**
     * Sets the value of the propertyIDs property.
     * 
     * @param value
     *     allowed object is
     *     {@link RiskPropertySet.PropertyIDs }
     *     
     */
    public void setPropertyIDs(RiskPropertySet.PropertyIDs value) {
        this.propertyIDs = value;
    }

    /**
     * Gets the value of the propertySetIDs property.
     * 
     * @return
     *     possible object is
     *     {@link RiskPropertySet.PropertySetIDs }
     *     
     */
    public RiskPropertySet.PropertySetIDs getPropertySetIDs() {
        return propertySetIDs;
    }

    /**
     * Sets the value of the propertySetIDs property.
     * 
     * @param value
     *     allowed object is
     *     {@link RiskPropertySet.PropertySetIDs }
     *     
     */
    public void setPropertySetIDs(RiskPropertySet.PropertySetIDs value) {
        this.propertySetIDs = value;
    }

    /**
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link RiskPropertySet.Properties }
     *     
     */
    public RiskPropertySet.Properties getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link RiskPropertySet.Properties }
     *     
     */
    public void setProperties(RiskPropertySet.Properties value) {
        this.properties = value;
    }

    /**
     * Gets the value of the propertySets property.
     * 
     * @return
     *     possible object is
     *     {@link RiskPropertySet.PropertySets }
     *     
     */
    public RiskPropertySet.PropertySets getPropertySets() {
        return propertySets;
    }

    /**
     * Sets the value of the propertySets property.
     * 
     * @param value
     *     allowed object is
     *     {@link RiskPropertySet.PropertySets }
     *     
     */
    public void setPropertySets(RiskPropertySet.PropertySets value) {
        this.propertySets = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="property" type="{http://www.example.org/RiskAnalysis}RiskProperty" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "property"
    })
    public static class Properties {

        @XmlElement(required = true)
        private List<RiskProperty> property;

        /**
         * Gets the value of the property property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the property property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProperty().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link RiskProperty }
         * 
         * 
         */
        public List<RiskProperty> getProperty() {
            if (property == null) {
                property = new ArrayList<RiskProperty>();
            }
            return this.property;
        }

        /**
         * This method rewrites risk analysis properties
         * @param properties
         */
        public void setProperty(List<RiskProperty> properties) {
            property = properties;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="propertyIDs" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "propertyIDs"
    })
    public static class PropertyIDs {

        @XmlElement(required = true)
        private List<String> propertyIDs;

        /**
         * Gets the value of the propertyIDs property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the propertyIDs property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPropertyIDs().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getPropertyIDs() {
            if (propertyIDs == null) {
                propertyIDs = new ArrayList<String>();
            }
            return this.propertyIDs;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="propertySetIDs" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "propertySetIDs"
    })
    public static class PropertySetIDs {

        @XmlElement(required = true)
        private List<String> propertySetIDs;

        /**
         * Gets the value of the propertySetIDs property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the propertySetIDs property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPropertySetIDs().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getPropertySetIDs() {
            if (propertySetIDs == null) {
                propertySetIDs = new ArrayList<String>();
            }
            return this.propertySetIDs;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="propertySet" type="{http://www.example.org/RiskAnalysis}RiskPropertySet" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "propertySet"
    })
    public static class PropertySets {

        private List<RiskPropertySet> propertySet;

        /**
         * Gets the value of the propertySet property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the propertySet property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPropertySet().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link RiskPropertySet }
         * 
         * 
         */
        public List<RiskPropertySet> getPropertySet() {
            if (propertySet == null) {
                propertySet = new ArrayList<RiskPropertySet>();
            }
            return this.propertySet;
        }

        /**
         * This method rewrites risk analysis property set
         * @param property set
         */
        public void setPropertySet(List<RiskPropertySet> propertySet) {
        	this.propertySet = propertySet;
        }


    }

}
