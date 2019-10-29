package com.fbapicking.model.user;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SerializedName("id")
    private String id;
    @SerializedName("auth_token")
    private String auth_token;
    @SerializedName("api_key")
    private String api_key;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("is_older_than_21")
    private Boolean is_older_than_21;
    @SerializedName("phone")
    private String phone;
    @SerializedName("company")
    private String company;
    @SerializedName("roles")
    private List<UserRoleModel> roles;
    @SerializedName("permissions")
    private List<PermissionModel> permissions;
    @SerializedName("company_category")
    private CompanyCategoryModel companyCategoryModel;
    @SerializedName("tenant")
    private TenantModel tenantModel;
    @SerializedName("warehouse_id")
    private String warehouse_id;

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
     * @return the auth_token
     */
    public String getAuth_token() {
        return auth_token;
    }

    /**
     * @param auth_token the auth_token to set
     */
    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    /**
     * @return the api_key
     */
    public String getApi_key() {
        return api_key;
    }

    /**
     * @param api_key the api_key to set
     */
    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return the is_older_than_21
     */
    public Boolean getIs_older_than_21() {
        return is_older_than_21;
    }

    /**
     * @param is_older_than_21 the is_older_than_21 to set
     */
    public void setIs_older_than_21(Boolean is_older_than_21) {
        this.is_older_than_21 = is_older_than_21;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the roles
     */
    public List<UserRoleModel> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<UserRoleModel> roles) {
        this.roles = roles;
    }

    /**
     * @return the permissions
     */
    public List<PermissionModel> getPermissions() {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(List<PermissionModel> permissions) {
        this.permissions = permissions;
    }

    /**
     * @return the companyCategoryModel
     */
    public CompanyCategoryModel getCompanyCategoryModel() {
        return companyCategoryModel;
    }

    /**
     * @param companyCategoryModel the companyCategoryModel to set
     */
    public void setCompanyCategoryModel(CompanyCategoryModel companyCategoryModel) {
        this.companyCategoryModel = companyCategoryModel;
    }

    /**
     * @return the tenantModel
     */
    public TenantModel getTenantModel() {
        return tenantModel;
    }

    /**
     * @param tenantModel the tenantModel to set
     */
    public void setTenantModel(TenantModel tenantModel) {
        this.tenantModel = tenantModel;
    }

    /**
     * @return the warehouse_id
     */
    public String getWarehouse_id() {
        return warehouse_id;
    }

    /**
     * @param warehouse_id the warehouse_id to set
     */
    public void setWarehouse_id(String warehouse_id) {
        this.warehouse_id = warehouse_id;
    }
}