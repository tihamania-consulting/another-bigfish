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
 * <p>Java class for StoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StoreType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProductStoreId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StoreId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StoreCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StoreName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StoreAddress" type="{}StoreAddressType"/>
 *         &lt;element name="OpeningHours" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StoreNotice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StoreContentSpot" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GeoCodeLong" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GeoCodeLat" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StoreType", propOrder = {
    "productStoreId",
    "storeId",
    "storeCode",
    "storeName",
    "storeAddress",
    "openingHours",
    "storeNotice",
    "storeContentSpot",
    "status",
    "geoCodeLong",
    "geoCodeLat"
})
public class StoreType {

    @XmlElement(name = "ProductStoreId", required = true, defaultValue = "")
    protected String productStoreId;
    @XmlElement(name = "StoreId", required = true, defaultValue = "")
    protected String storeId;
    @XmlElement(name = "StoreCode", required = true, defaultValue = "")
    protected String storeCode;
    @XmlElement(name = "StoreName", required = true, defaultValue = "")
    protected String storeName;
    @XmlElement(name = "StoreAddress", required = true)
    protected StoreAddressType storeAddress;
    @XmlElement(name = "OpeningHours", required = true, defaultValue = "")
    protected String openingHours;
    @XmlElement(name = "StoreNotice", required = true, defaultValue = "")
    protected String storeNotice;
    @XmlElement(name = "StoreContentSpot", required = true, defaultValue = "")
    protected String storeContentSpot;
    @XmlElement(name = "Status", required = true, defaultValue = "")
    protected String status;
    @XmlElement(name = "GeoCodeLong", required = true, defaultValue = "")
    protected String geoCodeLong;
    @XmlElement(name = "GeoCodeLat", required = true, defaultValue = "")
    protected String geoCodeLat;

    /**
     * Gets the value of the productStoreId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductStoreId() {
        return productStoreId;
    }

    /**
     * Sets the value of the productStoreId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductStoreId(String value) {
        this.productStoreId = value;
    }

    /**
     * Gets the value of the storeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * Sets the value of the storeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoreId(String value) {
        this.storeId = value;
    }

    /**
     * Gets the value of the storeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoreCode() {
        return storeCode;
    }

    /**
     * Sets the value of the storeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoreCode(String value) {
        this.storeCode = value;
    }

    /**
     * Gets the value of the storeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * Sets the value of the storeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoreName(String value) {
        this.storeName = value;
    }

    /**
     * Gets the value of the storeAddress property.
     * 
     * @return
     *     possible object is
     *     {@link StoreAddressType }
     *     
     */
    public StoreAddressType getStoreAddress() {
        return storeAddress;
    }

    /**
     * Sets the value of the storeAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link StoreAddressType }
     *     
     */
    public void setStoreAddress(StoreAddressType value) {
        this.storeAddress = value;
    }

    /**
     * Gets the value of the openingHours property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpeningHours() {
        return openingHours;
    }

    /**
     * Sets the value of the openingHours property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpeningHours(String value) {
        this.openingHours = value;
    }

    /**
     * Gets the value of the storeNotice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoreNotice() {
        return storeNotice;
    }

    /**
     * Sets the value of the storeNotice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoreNotice(String value) {
        this.storeNotice = value;
    }

    /**
     * Gets the value of the storeContentSpot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoreContentSpot() {
        return storeContentSpot;
    }

    /**
     * Sets the value of the storeContentSpot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoreContentSpot(String value) {
        this.storeContentSpot = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the geoCodeLong property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeoCodeLong() {
        return geoCodeLong;
    }

    /**
     * Sets the value of the geoCodeLong property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeoCodeLong(String value) {
        this.geoCodeLong = value;
    }

    /**
     * Gets the value of the geoCodeLat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeoCodeLat() {
        return geoCodeLat;
    }

    /**
     * Sets the value of the geoCodeLat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeoCodeLat(String value) {
        this.geoCodeLat = value;
    }

}
