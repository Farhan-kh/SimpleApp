package com.fbapicking.model.user;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class PackerModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    /**
     * @return the id
     */
    public String getUserId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setUserId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getUserName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setUserName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
