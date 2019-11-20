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
 * <p>Java class for OrderHeaderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderHeaderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OrderDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EntryDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StatusId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CreatedBy" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProductStoreId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OrderSubTotal" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OrderTotalItem" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OrderTotalAdjustment" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OrderShippingCharge" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OrderTax" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OrderTotalAmount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Currency" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderHeaderType", propOrder = {
    "orderId",
    "orderDate",
    "entryDate",
    "statusId",
    "createdBy",
    "productStoreId",
    "orderSubTotal",
    "orderTotalItem",
    "orderTotalAdjustment",
    "orderShippingCharge",
    "orderTax",
    "orderTotalAmount",
    "currency"
})
public class OrderHeaderType {

    @XmlElement(name = "OrderId", required = true)
    protected String orderId;
    @XmlElement(name = "OrderDate", required = true)
    protected String orderDate;
    @XmlElement(name = "EntryDate", required = true)
    protected String entryDate;
    @XmlElement(name = "StatusId", required = true)
    protected String statusId;
    @XmlElement(name = "CreatedBy", required = true)
    protected String createdBy;
    @XmlElement(name = "ProductStoreId", required = true)
    protected String productStoreId;
    @XmlElement(name = "OrderSubTotal", required = true)
    protected String orderSubTotal;
    @XmlElement(name = "OrderTotalItem", required = true)
    protected String orderTotalItem;
    @XmlElement(name = "OrderTotalAdjustment", required = true)
    protected String orderTotalAdjustment;
    @XmlElement(name = "OrderShippingCharge", required = true)
    protected String orderShippingCharge;
    @XmlElement(name = "OrderTax", required = true)
    protected String orderTax;
    @XmlElement(name = "OrderTotalAmount", required = true)
    protected String orderTotalAmount;
    @XmlElement(name = "Currency", required = true)
    protected String currency;

    /**
     * Gets the value of the orderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderId(String value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the orderDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the value of the orderDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderDate(String value) {
        this.orderDate = value;
    }

    /**
     * Gets the value of the entryDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryDate() {
        return entryDate;
    }

    /**
     * Sets the value of the entryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryDate(String value) {
        this.entryDate = value;
    }

    /**
     * Gets the value of the statusId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusId() {
        return statusId;
    }

    /**
     * Sets the value of the statusId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusId(String value) {
        this.statusId = value;
    }

    /**
     * Gets the value of the createdBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

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
     * Gets the value of the orderSubTotal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderSubTotal() {
        return orderSubTotal;
    }

    /**
     * Sets the value of the orderSubTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderSubTotal(String value) {
        this.orderSubTotal = value;
    }

    /**
     * Gets the value of the orderTotalItem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderTotalItem() {
        return orderTotalItem;
    }

    /**
     * Sets the value of the orderTotalItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderTotalItem(String value) {
        this.orderTotalItem = value;
    }

    /**
     * Gets the value of the orderTotalAdjustment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderTotalAdjustment() {
        return orderTotalAdjustment;
    }

    /**
     * Sets the value of the orderTotalAdjustment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderTotalAdjustment(String value) {
        this.orderTotalAdjustment = value;
    }

    /**
     * Gets the value of the orderShippingCharge property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderShippingCharge() {
        return orderShippingCharge;
    }

    /**
     * Sets the value of the orderShippingCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderShippingCharge(String value) {
        this.orderShippingCharge = value;
    }

    /**
     * Gets the value of the orderTax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderTax() {
        return orderTax;
    }

    /**
     * Sets the value of the orderTax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderTax(String value) {
        this.orderTax = value;
    }

    /**
     * Gets the value of the orderTotalAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderTotalAmount() {
        return orderTotalAmount;
    }

    /**
     * Sets the value of the orderTotalAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderTotalAmount(String value) {
        this.orderTotalAmount = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

}
