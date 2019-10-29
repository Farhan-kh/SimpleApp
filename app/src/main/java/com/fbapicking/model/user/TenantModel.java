package com.fbapicking.model.user;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class TenantModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("contact_person_name")
    private String contact_person_name;
    @SerializedName("contact_email")
    private String contact_email;
    @SerializedName("contact_number")
    private String contact_number;
    @SerializedName("short_code")
    private String short_code;
    @SerializedName("time_zone")
    private String time_zone;
    @SerializedName("optimal_inbound_flow")
    private String optimal_inbound_flow;
    @SerializedName("split_po_to_shipments")
    private String split_po_to_shipments;
    @SerializedName("enable_error_acknowledgement")
    private String enable_error_acknowledgement;

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
     * @return the contact_person_name
     */
    public String getContactPersonName() {
        return contact_person_name;
    }

    /**
     * @param contact_person_name the contact_person_name to set
     */
    public void setContactPersonName(String contact_person_name) {
        this.contact_person_name = contact_person_name;
    }

    /**
     * @return the contact_email
     */
    public String getContactEmail() {
        return contact_email;
    }

    /**
     * @param contact_email the contact_email to set
     */
    public void setContactEmail(String contact_email) {
        this.contact_email = contact_email;
    }

    /**
     * @return the contact_number
     */
    public String getContactNumber() {
        return contact_number;
    }

    /**
     * @param contact_number the contact_number to set
     */
    public void setContactNumber(String contact_number) {
        this.contact_number = contact_number;
    }

    /**
     * @return the short_code
     */
    public String getShortCode() {
        return short_code;
    }

    /**
     * @param short_code the short_code to set
     */
    public void setShortCode(String short_code) {
        this.short_code = short_code;
    }

    /**
     * @return the time_zone
     */
    public String getTimezone() {
        return time_zone;
    }

    /**
     * @param time_zone the time_zone to set
     */
    public void setTimezone(String time_zone) {
        this.time_zone = time_zone;
    }

    /**
     * @return the optimal_inbound_flow
     */
    public String getOptimalInboundFlow() {
        return optimal_inbound_flow;
    }

    /**
     * @param optimal_inbound_flow the optimal_inbound_flow to set
     */
    public void setOptimalInboundFlow(String optimal_inbound_flow) {
        this.optimal_inbound_flow = optimal_inbound_flow;
    }

    /**
     * @return the split_po_to_shipments
     */
    public String getSplitPoToShipments() {
        return split_po_to_shipments;
    }

    /**
     * @param split_po_to_shipments the split_po_to_shipments to set
     */
    public void setSplitPoToShipments(String split_po_to_shipments) {
        this.split_po_to_shipments = split_po_to_shipments;
    }

    /**
     * @return the enable_error_acknowledgement
     */
    public String getEnableErrorAcknowledgement() {
        return enable_error_acknowledgement;
    }

    /**
     * @param enable_error_acknowledgement the enable_error_acknowledgement to set
     */
    public void setEnableErrorAcknowledgement(String enable_error_acknowledgement) {
        this.enable_error_acknowledgement = enable_error_acknowledgement;
    }
}
