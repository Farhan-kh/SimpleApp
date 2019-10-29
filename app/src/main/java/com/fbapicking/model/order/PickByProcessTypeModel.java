package com.fbapicking.model.order;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class PickByProcessTypeModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    private String id;
    @SerializedName("batch_id")
    private String batch_id;
    @SerializedName("batch_number")
    private String batch_number;
    @SerializedName("expiry_date")
    private String expiry_date;
    @SerializedName("is_usn_base")
    private String is_usn_base;
    @SerializedName("location")
    private String location;
    @SerializedName("product_code")
    private String product_code;
    @SerializedName("product_id")
    private String product_id;
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("shelf_id")
    private String shelf_id;
    @SerializedName("sku")
    private String sku;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("enable_batch_management")
    private String enable_batch_management;
    @SerializedName("enable_expiry_management")
    private String enable_expiry_management;
    @SerializedName("order_bucket_id")
    private String order_bucket_id;
    @SerializedName("order_number")
    private String order_number;
    @SerializedName("picked_quantity")
    private String picked_quantity;
    @SerializedName("order_picking_product_data_id")
    private String order_picking_product_data_id;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the batch_id
     */
    public String getBatch_id() {
        return batch_id;
    }

    /**
     * @param batch_id the batch_id to set
     */
    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    /**
     * @return the batch_number
     */
    public String getBatch_number() {
        return batch_number;
    }

    /**
     * @param batch_number the batch_number to set
     */
    public void setBatch_number(String batch_number) {
        this.batch_number = batch_number;
    }

    /**
     * @return the expiry_date
     */
    public String getExpiry_date() {
        return expiry_date;
    }

    /**
     * @param expiry_date the expiry_date to set
     */
    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    /**
     * @return the is_usn_base
     */
    public String getIs_usn_base() {
        return is_usn_base;
    }

    /**
     * @param is_usn_base the is_usn_base to set
     */
    public void setIs_usn_base(String is_usn_base) {
        this.is_usn_base = is_usn_base;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the product_code
     */
    public String getProduct_code() {
        return product_code;
    }

    /**
     * @param product_code the product_code to set
     */
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    /**
     * @return the product_id
     */
    public String getProduct_id() {
        return product_id;
    }

    /**
     * @param product_id the product_id to set
     */
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    /**
     * @return the product_name
     */
    public String getProduct_name() {
        return product_name;
    }

    /**
     * @param product_name the product_name to set
     */
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    /**
     * @return the shelf_id
     */
    public String getShelf_id() {
        return shelf_id;
    }

    /**
     * @param shelf_id the shelf_id to set
     */
    public void setShelf_id(String shelf_id) {
        this.shelf_id = shelf_id;
    }

    /**
     * @return the sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * @param sku the sku to set
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * @return the quantity
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the enable_batch_management
     */
    public String getEnable_batch_management() {
        return enable_batch_management;
    }

    /**
     * @param enable_batch_management the enable_batch_management to set
     */
    public void setEnable_batch_management(String enable_batch_management) {
        this.enable_batch_management = enable_batch_management;
    }

    /**
     * @return the enable_expiry_management
     */
    public String getEnable_expiry_management() {
        return enable_expiry_management;
    }

    /**
     * @param enable_expiry_management the enable_expiry_management to set
     */
    public void setEnable_expiry_management(String enable_expiry_management) {
        this.enable_expiry_management = enable_expiry_management;
    }

    /**
     * @return the order_bucket_id
     */
    public String getOrder_bucket_id() {
        return order_bucket_id;
    }

    /**
     * @param order_bucket_id the order_bucket_id to set
     */
    public void setOrder_bucket_id(String order_bucket_id) {
        this.order_bucket_id = order_bucket_id;
    }

    /**
     * @return the order_number
     */
    public String getOrder_number() {
        return order_number;
    }

    /**
     * @param order_number the order_number to set
     */
    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    /**
     * @return the picked_quantity
     */
    public String getPicked_quantity() {
        return picked_quantity;
    }

    /**
     * @param picked_quantity the picked_quantity to set
     */
    public void setPicked_quantity(String picked_quantity) {
        this.picked_quantity = picked_quantity;
    }

    /**
     * @return the order_picking_product_data_id
     */
    public String getOrder_picking_product_data_id() {
        return order_picking_product_data_id;
    }

    /**
     * @param order_picking_product_data_id the order_picking_product_data_id to set
     */
    public void setOrder_picking_product_data_id(String order_picking_product_data_id) {
        this.order_picking_product_data_id = order_picking_product_data_id;
    }
}
