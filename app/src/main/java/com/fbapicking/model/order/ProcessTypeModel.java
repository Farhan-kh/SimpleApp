package com.fbapicking.model.order;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@SuppressWarnings("serial")
public class ProcessTypeModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("picking_type")
    private String picking_type;
    @SerializedName("display_picking_type")
    private String display_picking_type;
    @SerializedName("no_of_orders")
    private String no_of_orders;
    @SerializedName("units")
    private String units;
    @SerializedName("order_ids")
    private String[] order_ids;

    /**
     * @return the picking_type
     */
    public String getPicking_type() {
        return picking_type;
    }

    /**
     * @param picking_type the picking_type to set
     */
    public void setPicking_type(String picking_type) {
        this.picking_type = picking_type;
    }

    /**
     * @return the display_picking_type
     */
    public String getDisplay_picking_type() {
        return display_picking_type;
    }

    /**
     * @param display_picking_type the display_picking_type to set
     */
    public void setDisplay_picking_type(String display_picking_type) {
        this.display_picking_type = display_picking_type;
    }

    /**
     * @return the no_of_orders
     */
    public String getNo_of_orders() {
        return no_of_orders;
    }

    /**
     * @param no_of_orders the no_of_orders to set
     */
    public void setNo_of_orders(String no_of_orders) {
        this.no_of_orders = no_of_orders;
    }

    /**
     * @return the units
     */
    public String getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * @return the order_ids
     */
    public String[] getOrder_ids() {
        return order_ids;
    }

    /**
     * @param order_ids the order_ids to set
     */
    public void setOrder_ids(String[] order_ids) {
        this.order_ids = order_ids;
    }
}
