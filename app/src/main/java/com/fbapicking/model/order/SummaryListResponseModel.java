package com.fbapicking.model.order;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class SummaryListResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("picking_process_id")
    private String picking_process_id;
    @SerializedName("fulfillable_orders")
    private List<PickByProcessTypeModel> fulfillableOrdersListArray;
    @SerializedName("non_fulfillable_orders")
    private List<PickByProcessTypeModel> nonFulfillableOrdersListArray;
    @SerializedName("total_fulfillable_orders_count")
    private String total_fulfillable_orders_count;
    @SerializedName("total_fulfillable_skus_count")
    private String total_fulfillable_skus_count;
    @SerializedName("total_fulfillable_units_count")
    private String total_fulfillable_units_count;
    @SerializedName("total_non_fulfillable_orders_count")
    private String total_non_fulfillable_orders_count;
    @SerializedName("total_non_fulfillable_skus_count")
    private String total_non_fulfillable_skus_count;
    @SerializedName("total_non_fulfillable_units_count")
    private String total_non_fulfillable_units_count;

    /**
     * @return the picking_process_id
     */
    public String getPicking_process_id() {
        return picking_process_id;
    }

    /**
     * @param picking_process_id the picking_process_id to set
     */
    public void setPicking_process_id(String picking_process_id) {
        this.picking_process_id = picking_process_id;
    }

    /**
     * @return the fulfillableOrdersListArray
     */
    public List<PickByProcessTypeModel> getFulfillableOrdersListArray() {
        return fulfillableOrdersListArray;
    }

    /**
     * @param fulfillableOrdersListArray the fulfillableOrdersListArray to set
     */
    public void setFulfillableOrdersListArray(
            List<PickByProcessTypeModel> fulfillableOrdersListArray) {
        this.fulfillableOrdersListArray = fulfillableOrdersListArray;
    }

    /**
     * @return the nonFulfillableOrdersListArray
     */
    public List<PickByProcessTypeModel> getNonFulfillableOrdersListArray() {
        return nonFulfillableOrdersListArray;
    }

    /**
     * @param nonFulfillableOrdersListArray the nonFulfillableOrdersListArray to set
     */
    public void setNonFulfillableOrdersListArray(
            List<PickByProcessTypeModel> nonFulfillableOrdersListArray) {
        this.nonFulfillableOrdersListArray = nonFulfillableOrdersListArray;
    }

    /**
     * @return the total_fulfillable_orders_count
     */
    public String getTotal_fulfillable_orders_count() {
        return total_fulfillable_orders_count;
    }

    /**
     * @param total_fulfillable_orders_count the total_fulfillable_orders_count to set
     */
    public void setTotal_fulfillable_orders_count(String total_fulfillable_orders_count) {
        this.total_fulfillable_orders_count = total_fulfillable_orders_count;
    }

    /**
     * @return the total_fulfillable_skus_count
     */
    public String getTotal_fulfillable_skus_count() {
        return total_fulfillable_skus_count;
    }

    /**
     * @param total_fulfillable_skus_count the total_fulfillable_skus_count to set
     */
    public void setTotal_fulfillable_skus_count(String total_fulfillable_skus_count) {
        this.total_fulfillable_skus_count = total_fulfillable_skus_count;
    }

    /**
     * @return the total_fulfillable_units_count
     */
    public String getTotal_fulfillable_units_count() {
        return total_fulfillable_units_count;
    }

    /**
     * @param total_fulfillable_units_count the total_fulfillable_units_count to set
     */
    public void setTotal_fulfillable_units_count(String total_fulfillable_units_count) {
        this.total_fulfillable_units_count = total_fulfillable_units_count;
    }

    /**
     * @return the total_non_fulfillable_orders_count
     */
    public String getTotal_non_fulfillable_orders_count() {
        return total_non_fulfillable_orders_count;
    }

    /**
     * @param total_non_fulfillable_orders_count the total_non_fulfillable_orders_count to set
     */
    public void setTotal_non_fulfillable_orders_count(String total_non_fulfillable_orders_count) {
        this.total_non_fulfillable_orders_count = total_non_fulfillable_orders_count;
    }

    /**
     * @return the total_non_fulfillable_skus_count
     */
    public String getTotal_non_fulfillable_skus_count() {
        return total_non_fulfillable_skus_count;
    }

    /**
     * @param total_non_fulfillable_skus_count the total_non_fulfillable_skus_count to set
     */
    public void setTotal_non_fulfillable_skus_count(String total_non_fulfillable_skus_count) {
        this.total_non_fulfillable_skus_count = total_non_fulfillable_skus_count;
    }

    /**
     * @return the total_non_fulfillable_units_count
     */
    public String getTotal_non_fulfillable_units_count() {
        return total_non_fulfillable_units_count;
    }

    /**
     * @param total_non_fulfillable_units_count the total_non_fulfillable_units_count to set
     */
    public void setTotal_non_fulfillable_units_count(String total_non_fulfillable_units_count) {
        this.total_non_fulfillable_units_count = total_non_fulfillable_units_count;
    }
}
