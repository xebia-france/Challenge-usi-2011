//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.2-hudson-jaxb-ri-2.2-63- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.03.27 at 03:13:16 PM CEST 
//


package com.usi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for sessiontype complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sessiontype">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="questions" type="{http://www.usi.com}questiontype"/>
 *         &lt;element name="parameters" type="{http://www.usi.com}parametertype"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sessiontype", propOrder = {
    "questions",
    "parameters"
})
public class Sessiontype implements Serializable {

    @XmlElement(required = true)
    protected Questiontype questions;
    @XmlElement(required = true)
    protected Parametertype parameters;

    /**
     * Gets the value of the questions property.
     * 
     * @return
     *     possible object is
     *     {@link Questiontype }
     *     
     */
    public Questiontype getQuestions() {
        return questions;
    }

    /**
     * Sets the value of the questions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Questiontype }
     *     
     */
    public void setQuestions(Questiontype value) {
        this.questions = value;
    }

    /**
     * Gets the value of the parameters property.
     * 
     * @return
     *     possible object is
     *     {@link Parametertype }
     *     
     */
    public Parametertype getParameters() {
        return parameters;
    }

    /**
     * Sets the value of the parameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parametertype }
     *     
     */
    public void setParameters(Parametertype value) {
        this.parameters = value;
    }

}
