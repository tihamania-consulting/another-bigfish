//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.21 at 10:11:32 AM EDT 
//


package com.osafe.feeds.osafefeeds;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PlpSwatchType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PlpSwatchType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}Url"/>
 *         &lt;element ref="{}ThruDate" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TranslatedLabelType", propOrder = {

})
public class TranslatedLabelType {

    @XmlElement(name = "labelkey", required = true, defaultValue = "")
    protected String labelkey;
    @XmlElement(name = "labelValue", defaultValue = "")
    protected String labelValue;
    @XmlElement(name = "labelLang", defaultValue = "")
    protected String labelLang;


    public String getLabelkey() {
        return labelkey;
    }

    public void setLabelkey(String labelkey) {
        this.labelkey = labelkey;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }

    public String getLabelLang() {
        return labelLang;
    }

    public void setLabelLang(String labelLang) {
        this.labelLang = labelLang;
    }
}
