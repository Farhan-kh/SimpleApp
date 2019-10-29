package com.fbapicking.model.order;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class PickItemResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("data")
    private SummaryListResponseModel summaryListResponseModel;

    /**
     * @return the summaryListResponseModel
     */
    public SummaryListResponseModel getSummaryListResponseModel() {
        return summaryListResponseModel;
    }

    /**
     * @param summaryListResponseModel the summaryListResponseModel to set
     */
    public void setSummaryListResponseModel(SummaryListResponseModel summaryListResponseModel) {
        this.summaryListResponseModel = summaryListResponseModel;
    }
}
