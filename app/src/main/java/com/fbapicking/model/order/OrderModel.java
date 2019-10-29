package com.fbapicking.model.order;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SerializedName("id")
    private String id;
    @SerializedName("order_number")
    private String order_number;
    @SerializedName("express_delivery")
    private String express_delivery;
    @SerializedName("tracking_number")
    private String tracking_number;
    @SerializedName("order_delivery_date")
    private String order_delivery_date;
    @SerializedName("state")
    private String state;
    @SerializedName("company_name")
    private String company_name;
    @SerializedName("order_item_lists")
    private List<OrderItemListModel> order_item_lists;
    @SerializedName("carrier_code")
    private String carrier_code;
    @SerializedName("bins")
    private List<BinModel> bins;
    @SerializedName("is_usn_base")
    private String is_usn_base;
    @SerializedName("originated_from")
    private String originated_from;
    @SerializedName("picking_preference")
    private String picking_preference;
    @SerializedName("is_count_mismatch")
    private String is_count_mismatch;

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
     * @return the express_delivery
     */
    public String getExpress_delivery() {
        return express_delivery;
    }

    /**
     * @param express_delivery the express_delivery to set
     */
    public void setExpress_delivery(String express_delivery) {
        this.express_delivery = express_delivery;
    }

    /**
     * @return the tracking_number
     */
    public String getTracking_number() {
        return tracking_number;
    }

    /**
     * @param tracking_number the tracking_number to set
     */
    public void setTracking_number(String tracking_number) {
        this.tracking_number = tracking_number;
    }

    /**
     * @return the order_delivery_date
     */
    public String getDelivery_date() {
        return order_delivery_date;
    }

    /**
     * @param order_delivery_date the order_delivery_date to set
     */
    public void setDelivery_date(String order_delivery_date) {
        this.order_delivery_date = order_delivery_date;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the company_name
     */
    public String getCompany_name() {
        return company_name;
    }

    /**
     * @param company_name the company_name to set
     */
    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    /**
     * @return the order_item_lists
     */
    public List<OrderItemListModel> getOrder_item_lists() {
        return order_item_lists;
    }

    /**
     * @param order_item_lists the order_item_lists to set
     */
    public void setOrder_item_lists(List<OrderItemListModel> order_item_lists) {
        this.order_item_lists = order_item_lists;
    }

    /**
     * @return the carrier_code
     */
    public String getCarrier() {
        return carrier_code;
    }

    /**
     * @param carrier_code the carrier_code to set
     */
    public void setCarrier(String carrier_code) {
        this.carrier_code = carrier_code;
    }

    /**
     * @return the bins
     */
    public List<BinModel> getBins() {
        return bins;
    }

    /**
     * @param bins the bins to set
     */
    public void setBins(List<BinModel> bins) {
        this.bins = bins;
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
     * @return the originated_from
     */
    public String getSource() {
        return originated_from;
    }

    /**
     * @param originated_from the originated_from to set
     */
    public void setSource(String originated_from) {
        this.originated_from = originated_from;
    }

    /**
     * @return the picking_preference
     */
    public String getPickingPreference() {
        return picking_preference;
    }

    /**
     * @param picking_preference the picking_preference to set
     */
    public void setPickingPreference(String picking_preference) {
        this.picking_preference = picking_preference;
    }

    /**
     * @return the is_count_mismatch
     */
    public String getIs_count_mismatch() {
        return is_count_mismatch;
    }

    /**
     * @param is_count_mismatch the is_count_mismatch to set
     */
    public void setIs_count_mismatch(String is_count_mismatch) {
        this.is_count_mismatch = is_count_mismatch;
    }
}
