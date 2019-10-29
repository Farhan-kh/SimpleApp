package com.fbapicking.model.order;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class BinModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    private String id;
    @SerializedName("number")
    private String number;

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
     * @return the number
     */
    public String getBinNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setBinNumber(String number) {
        this.number = number;
    }
}
