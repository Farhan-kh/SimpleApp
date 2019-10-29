package com.fbapicking.model.order;

import java.io.Serializable;
import java.util.List;
import com.fbapicking.model.PaginationModel;
import com.google.gson.annotations.SerializedName;

public class OrderListResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("orders")
    private List<OrderModel> orders;
    @SerializedName("meta")
    private PaginationModel paginationModel;

    /**
     * @return the orders
     */
    public List<OrderModel> getOrders() {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
    }

    /**
     * @return the paginationModel
     */
    public PaginationModel getPaginationModel() {
        return paginationModel;
    }

    /**
     * @param paginationModel the paginationModel to set
     */
    public void setPaginationModel(PaginationModel paginationModel) {
        this.paginationModel = paginationModel;
    }
}
