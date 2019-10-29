package com.fbapicking.model.order;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderItemListResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("order_item_list")
    private List<OrderItemListModel> orderItemListModelArray;

    /**
     * @return the orderItemListModelArray
     */
    public List<OrderItemListModel> getOrderItemListModelArray() {
        return orderItemListModelArray;
    }

    /**
     * @param orderItemListModelArray the orderItemListModelArray to set
     */
    public void setOrderItemListModelArray(
            List<OrderItemListModel> orderItemListModelArray) {
        this.orderItemListModelArray = orderItemListModelArray;
    }
}