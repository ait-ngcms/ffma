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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for RiskClassification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RiskClassification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="agent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="creationDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="riskFactors">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="riskFactor" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="minValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="maxValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="riskScore" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
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
@XmlType(name = "RiskClassification", propOrder = {
    "weight",
    "agent",
    "creationDate",
    "riskFactors"
})
public class RiskClassification {

    private double weight;
    @XmlElement(required = true)
    private String agent;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    private XMLGregorianCalendar creationDate;
    @XmlElement(required = true)
    private RiskClassification.RiskFactors riskFactors;

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
     * Gets the value of the agent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgent() {
        return agent;
    }

    /**
     * Sets the value of the agent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgent(String value) {
        this.agent = value;
    }

    /**
     * Gets the value of the creationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the value of the creationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreationDate(XMLGregorianCalendar value) {
        this.creationDate = value;
    }

    /**
     * Gets the value of the riskFactors property.
     * 
     * @return
     *     possible object is
     *     {@link RiskClassification.RiskFactors }
     *     
     */
    public RiskClassification.RiskFactors getRiskFactors() {
        return riskFactors;
    }

    /**
     * Sets the value of the riskFactors property.
     * 
     * @param value
     *     allowed object is
     *     {@link RiskClassification.RiskFactors }
     *     
     */
    public void setRiskFactors(RiskClassification.RiskFactors value) {
        this.riskFactors = value;
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
     *         &lt;element name="riskFactor" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="minValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="maxValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="riskScore" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    @XmlType(name = "", propOrder = {
        "riskFactor"
    })
    public static class RiskFactors {

        @XmlElement(required = true)
        private List<RiskClassification.RiskFactors.RiskFactor> riskFactor;

        /**
         * Gets the value of the riskFactor property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the riskFactor property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRiskFactor().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link RiskClassification.RiskFactors.RiskFactor }
         * 
         * 
         */
        public List<RiskClassification.RiskFactors.RiskFactor> getRiskFactor() {
            if (riskFactor == null) {
                riskFactor = new ArrayList<RiskClassification.RiskFactors.RiskFactor>();
            }
            return this.riskFactor;
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
         *         &lt;element name="minValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="maxValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="riskScore" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
            "minValue",
            "maxValue",
            "riskScore"
        })
        public static class RiskFactor {

            @XmlElement(required = true, defaultValue = "", nillable = true)
            private String minValue;
            @XmlElement(required = true, defaultValue = "", nillable = true)
            private String maxValue;
            @XmlElement(defaultValue = "0")
            private int riskScore;

            /**
             * Gets the value of the minValue property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMinValue() {
                return minValue;
            }

            /**
             * Sets the value of the minValue property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMinValue(String value) {
                this.minValue = value;
            }

            /**
             * Gets the value of the maxValue property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMaxValue() {
                return maxValue;
            }

            /**
             * Sets the value of the maxValue property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMaxValue(String value) {
                this.maxValue = value;
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

        }

    }

}