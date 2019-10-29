package com.fbapicking.model.order;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class StartPickResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("data")
    private PickByProcessTypeListResponseModel pickByProcessTypeListResponseModel;

    /**
     * @return the pickByProcessTypeListResponseModel
     */
    public PickByProcessTypeListResponseModel getPickByProcessTypeListResponseModel() {
        return pickByProcessTypeListResponseModel;
    }

    /**
     * @param pickByProcessTypeListResponseModel the pickByProcessTypeListResponseModel to set
     */
    public void setPickByProcessTypeListResponseModel(PickByProcessTypeListResponseModel pickByProcessTypeListResponseModel) {
        this.pickByProcessTypeListResponseModel = pickByProcessTypeListResponseModel;
    }
}
