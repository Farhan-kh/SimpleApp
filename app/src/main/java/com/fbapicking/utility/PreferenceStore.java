package com.fbapicking.utility;

import com.fbapicking.model.user.UserModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceStore {
    /**
     * User Data
     */
    private static final String PREF_USER_ID = "id";
    private static final String PREF_USER_name = "firstname";
    private static final String PREF_USER_last_name = "lastname";
    private static final String PREF_USER_email = "email";
    private static final String PREF_USER_password = "password";
    private static final String PREF_USER_api_key = "api_key";
    private static final String PREF_USER_authentication_token = "authentication_token";
    private static final String PREF_MODE = "mode";
    private static final String PREF_BASE_URL = "base_url";
    private static final String PREF_WAREHOUSE_ID = "warehouse_id";
    private static final String PREF_ENABLE_ERROR_ACKNOWLEDGEMENT = "enable_error_acknowledgement";

    // Method to get shared preferences
    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // Method to set password to shared preferences
    public static void setPassword(Context context, String password) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_password, password);
        editor.apply();
    }

    // Method get password from shared preferences
    public static String getPassword(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_password, "");
    }

    // Method to set email into shared preferences
    public static void setEmail(Context context, String email) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_email, email);
        editor.apply();
    }

    // Method to get email from shared preferences
    public static String getEmail(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_email, "");
    }

    /**
     * set user profile
     */
    public static void setUserModel(Context context, UserModel userModel) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_ID, userModel.getId());
        editor.putString(PREF_USER_password, userModel.getPassword());
        editor.putString(PREF_USER_api_key, userModel.getApi_key());
        editor.putString(PREF_USER_authentication_token, userModel.getAuth_token());
        editor.putString(PREF_USER_name, userModel.getFirst_name());
        editor.putString(PREF_USER_last_name, (userModel.getLast_name() != null ? userModel.getLast_name() : ""));
        editor.putString(PREF_USER_email, userModel.getEmail());
        editor.apply();
    }

    /**
     * get user profile model
     */
    public static UserModel getUserModel(Context context) {
        UserModel profileModel = new UserModel();
        profileModel.setId(getSharedPreferences(context).getString(PREF_USER_ID, ""));
        profileModel.setApi_key(getSharedPreferences(context).getString(PREF_USER_api_key, ""));
        profileModel.setAuth_token(getSharedPreferences(context).getString
                (PREF_USER_authentication_token, ""));
        profileModel.setFirst_name(getSharedPreferences(context).getString(PREF_USER_name, ""));
        profileModel.setLast_name(getSharedPreferences(context).getString(PREF_USER_last_name, ""));
        profileModel.setEmail(getSharedPreferences(context).getString(PREF_USER_email, ""));
        return profileModel;
    }

    /**
     * set mode
     */
    public static void setMode(Context context, String environment) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_MODE, environment);
        editor.apply();
    }

    /**
     * get mode
     */
    public static String getMode(Context context) {
        return getSharedPreferences(context).getString(PREF_MODE, "");
    }

    /**
     * set base url
     */
    public static void setBaseURL(Context context, String url) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_BASE_URL, url);
        editor.apply();
    }

    /**
     * get base url
     */
    public static String getBaseURL(Context context) {
        return getSharedPreferences(context).getString(PREF_BASE_URL, "");
    }

    /**
     * set warehouse id
     */
    public static void setWarehouseId(Context context, String warehouseId) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_WAREHOUSE_ID, warehouseId);
        editor.apply();
    }

    /**
     * get warehouse id
     */
    public static String getWarehouseId(Context context) {
        return getSharedPreferences(context).getString(PREF_WAREHOUSE_ID, "");
    }

    /**
     * set enable_error_acknowledgement
     */
    public static void setPrefEnableErrorAcknowledgement(Context context, String enable_error_acknowledgement) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_ENABLE_ERROR_ACKNOWLEDGEMENT, enable_error_acknowledgement);
        editor.apply();
    }

    /**
     * get enable_error_acknowledgement
     */
    public static String getPrefEnableErrorAcknowledgement(Context context) {
        return getSharedPreferences(context).getString(PREF_ENABLE_ERROR_ACKNOWLEDGEMENT, "");
    }

    /**
     * Clear all preferences
     */
    public static void clearDetails(Context context) {
        Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }
}
