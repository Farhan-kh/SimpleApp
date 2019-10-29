package com.fbapicking.model.order;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class PickByProcessTypeListResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("picking_process_id")
    private String picking_process_id;
    @SerializedName("product_data")
    private List<PickByProcessTypeModel> pickByProcessTypeModelListArray;

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
     * @return the pickByProcessTypeModelListArray
     */
    public List<PickByProcessTypeModel> getPickByProcessTypeModelListArray() {
        return pickByProcessTypeModelListArray;
    }

    /**
     * @param pickByProcessTypeModelListArray the pickByProcessTypeModelListArray to set
     */
    public void setPickByProcessTypeModelListArray(
            List<PickByProcessTypeModel> pickByProcessTypeModelListArray) {
        this.pickByProcessTypeModelListArray = pickByProcessTypeModelListArray;
    }
}
