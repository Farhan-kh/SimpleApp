package com.fbapicking.model.user;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class PackerListResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("users")
    private List<PackerModel> packerModelList;

    /**
     * @return the packerModelList
     */
    public List<PackerModel> getPackerModelList() {
        return packerModelList;
    }

    /**
     * @param packerModelList the packerModelList to set
     */
    public void setPackerModelList(List<PackerModel> packerModelList) {
        this.packerModelList = packerModelList;
    }
}
