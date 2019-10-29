package com.fbapicking.model.user;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class UserRoleModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SerializedName("id")
    private String id;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("deleted_at")
    private String deleted_at;
    @SerializedName("display_name")
    private String display_name;
    @SerializedName("modified_by")
    private String modified_by;
    @SerializedName("name")
    private String name;
    @SerializedName("system")
    private String system;

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
     * @return the created_at
     */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * @param created_at the created_at to set
     */
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /**
     * @return the updated_at
     */
    public String getUpdated_at() {
        return updated_at;
    }

    /**
     * @param updated_at the updated_at to set
     */
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    /**
     * @return the deleted_at
     */
    public String getDeleted_at() {
        return deleted_at;
    }

    /**
     * @param deleted_at the deleted_at to set
     */
    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    /**
     * @return the display_name
     */
    public String getDisplay_name() {
        return display_name;
    }

    /**
     * @param display_name the display_name to set
     */
    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    /**
     * @return the modified_by
     */
    public String getModified_by() {
        return modified_by;
    }

    /**
     * @param modified_by the modified_by to set
     */
    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the system
     */
    public String getSystem() {
        return system;
    }

    /**
     * @param system the system to set
     */
    public void setSystem(String system) {
        this.system = system;
    }
}
