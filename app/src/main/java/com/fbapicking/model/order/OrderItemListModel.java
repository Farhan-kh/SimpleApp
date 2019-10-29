package com.fbapicking.model.order;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderItemListModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    private String id;
    @SerializedName("sku")
    private String sku;
    @SerializedName("shelf")
    private String shelf;
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("picked_quantity")
    private String picked_quantity;
    @SerializedName("product_image_url")
    private String product_image_url;
    @SerializedName("product_code")
    private String product_code;
    @SerializedName("usns")
    private List<String> usns;
    @SerializedName("possible_usns")
    private List<String> possible_usns;
    @SerializedName("is_serial_number_base")
    private String is_serial_number_base;
    @SerializedName("serial_numbers")
    private List<String> serial_numbers;
    @SerializedName("possible_serial_numbers")
    private List<String> possible_serial_numbers;
    @SerializedName("enable_batch_management")
    private String enable_batch_management;
    @SerializedName("enable_expiry_management")
    private String enable_expiry_management;
    @SerializedName("batch_number")
    private String batch_number;
    @SerializedName("expiry_date")
    private String expiry_date;

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
     * @return the shelf
     */
    public String getShelf() {
        return shelf;
    }

    /**
     * @param shelf the shelf to set
     */
    public void setShelf(String shelf) {
        this.shelf = shelf;
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
     * @return the product_image_url
     */
    public String getProductImageURL() {
        return product_image_url;
    }

    /**
     * @param product_image_url the product_image_url to set
     */
    public void setProductImageURL(String product_image_url) {
        this.product_image_url = product_image_url;
    }

    /**
     * @return the product_code
     */
    public String getUPC() {
        return product_code;
    }

    /**
     * @param product_code the product_code to set
     */
    public void setUPC(String product_code) {
        this.product_code = product_code;
    }

    /**
     * @return the usns
     */
    public List<String> getUsns() {
        return usns;
    }

    /**
     * @param usns the usns to set
     */
    public void setUsns(List<String> usns) {
        this.usns = usns;
    }

    /**
     * @return the possible_usns
     */
    public List<String> getPossible_usns() {
        return possible_usns;
    }

    /**
     * @param possible_usns the possible_usns to set
     */
    public void setPossible_usns(List<String> possible_usns) {
        this.possible_usns = possible_usns;
    }

    /**
     * @return the is_serial_number_base
     */
    public String getIs_serial_number_base() {
        return is_serial_number_base;
    }

    /**
     * @param is_serial_number_base the is_serial_number_base to set
     */
    public void setIs_serial_number_base(String is_serial_number_base) {
        this.is_serial_number_base = is_serial_number_base;
    }

    /**
     * @return the serial_numbers
     */
    public List<String> getSerial_numbers() {
        return serial_numbers;
    }

    /**
     * @param serial_numbers the serial_numbers to set
     */
    public void setSerial_numbers(List<String> serial_numbers) {
        this.serial_numbers = serial_numbers;
    }

    /**
     * @return the possible_serial_numbers
     */
    public List<String> getPossible_serial_numbers() {
        return possible_serial_numbers;
    }

    /**
     * @param possible_serial_numbers the possible_serial_numbers to set
     */
    public void setPossible_serial_numbers(List<String> possible_serial_numbers) {
        this.possible_serial_numbers = possible_serial_numbers;
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
     * @return the batch_number
     */
    public String getBatchNumber() {
        return batch_number;
    }

    /**
     * @param batch_number the batch_number to set
     */
    public void setBatchNumber(String batch_number) {
        this.batch_number = batch_number;
    }

    /**
     * @return the expiry_date
     */
    public String getExpiryDate() {
        return expiry_date;
    }

    /**
     * @param expiry_date the expiry_date to set
     */
    public void setExpiryDate(String expiry_date) {
        this.expiry_date = expiry_date;
    }
}
