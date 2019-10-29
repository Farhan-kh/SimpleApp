package com.fbapicking.model.order;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class ProcessTypeListResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("data")
    private List<ProcessTypeModel> processTypeModelListArray;

    /**
     * @return the processTypeModelListArray
     */
    public List<ProcessTypeModel> getProcessTypeModelListArray() {
        return processTypeModelListArray;
    }

    /**
     * @param processTypeModelListArray the processTypeModelListArray to set
     */
    public void setProcessTypeModelListArray(
            List<ProcessTypeModel> processTypeModelListArray) {
        this.processTypeModelListArray = processTypeModelListArray;
    }
}
