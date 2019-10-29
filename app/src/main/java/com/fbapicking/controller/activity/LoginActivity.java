package com.fbapicking.controller.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bugfender.sdk.Bugfender;
import com.fbapicking.model.user.TenantModel;
import com.fbapicking.model.user.UserModel;
import com.fbapicking.model.user.UserRoleModel;
import com.fbapicking.utility.ApplicationController;
import com.fbapicking.utility.Commons;
import com.fbapicking.utility.Commons.ResponseValidator;
import com.fbapicking.utility.ConnectionDetector;
import com.fbapicking.utility.PreferenceStore;
import com.fbapicking.utility.Utils;
import com.google.gson.Gson;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.fbapicking.R;

public class LoginActivity extends BaseActivity implements OnClickListener {
    ConnectionDetector connectionDetector;
    TextView titleTextView, rememberMeTV,
            releaseDateTextView, versionTextView, tagLineTextView;
    EditText userEmailEditText, passwordEditText;
    Button loginButton;
    CheckBox rememberMeCheckBox;
    JSONObject data;
    UserModel userModel;
    ArrayList<String> ROLE_NAME;
    Intent loginIntent;
    TenantModel tenantModel;

    // Default Method: to create the activity (i.e. to start activity)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initializeComponents();

        // for auto login
        if (PreferenceStore.getEmail(getApplicationContext())
                .length() > 0
                && PreferenceStore.getPassword(getApplicationContext())
                .length() > 0) {
            userEmailEditText.setText(PreferenceStore
                    .getEmail(getApplicationContext()));
            passwordEditText.setText(PreferenceStore
                    .getPassword(getApplicationContext()));

            rememberMeCheckBox.setChecked(true);

            loginUserData();
        }
    }

    public void initializeComponents() {
        connectionDetector = new ConnectionDetector(getApplicationContext());

        setUpActionBar();

        Typeface font1 = Typeface.createFromAsset(getAssets(),
                "fonts/HelveticaNeueLight.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(),
                "fonts/Museo300Regular.otf");
        Typeface font3 = Typeface.createFromAsset(getAssets(),
                "fonts/DroidSerif-Bold.ttf");

        titleTextView = (TextView) findViewById(R.id.title);
        if (titleTextView != null) {
            titleTextView.setTypeface(font3);
            titleTextView.setText("eWMS" + "\n" + "PICKING");
        }

        rememberMeTV = (TextView) findViewById(R.id.rememberMeTV);
        if (rememberMeTV != null)
            rememberMeTV.setTypeface(font2);

        userEmailEditText = (EditText) findViewById(R.id.userEmail);
        passwordEditText = (EditText) findViewById(R.id.userPassword);

        userEmailEditText.setTypeface(font2);
        passwordEditText.setTypeface(font2);

        loginButton = (Button) findViewById(R.id.loginBtn);

        rememberMeCheckBox = (CheckBox) findViewById(R.id.rememberMeCB);
        if (rememberMeCheckBox != null)
            rememberMeCheckBox.setChecked(false);

        loginButton.setOnClickListener(this);
        loginButton.setTypeface(font2, Typeface.BOLD);

        releaseDateTextView = (TextView) findViewById(R.id.releaseDateTextView);
        if (releaseDateTextView != null) {
            releaseDateTextView.setTypeface(font2);
            releaseDateTextView.setText("Release On: "
                    + Utils.getAppBuildDate(getApplicationContext()));
        }

        versionTextView = (TextView) findViewById(R.id.versionTextView);
        if (versionTextView != null) {
            versionTextView.setTypeface(font2);
            versionTextView.setText("Version: "
                    + Utils.getAppVersionName(getApplicationContext()));
        }

        tagLineTextView = (TextView) findViewById(R.id.tagLineTextView);
        if (tagLineTextView != null)
            tagLineTextView.setTypeface(font2);
    }

    // Method to handle back button pressed event
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    // Method to call appropriate button click event
    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            if (userEmailEditText.getText().toString().equalsIgnoreCase("")
                    || passwordEditText.getText().toString()
                    .equalsIgnoreCase("")) {
                userEmailEditText.setText("");
                userEmailEditText.requestFocus();
                passwordEditText.setText("");
                showCustomToastMessage("Please Enter Valid Email And Password",
                        true);
            } else {
                if (!isEmailValid(userEmailEditText.getText().toString())) {
                    userEmailEditText.setText("");
                    userEmailEditText.requestFocus();
                    passwordEditText.setText("");
                    showCustomToastMessage("Please Enter Valid Email", true);
                } else if (!isPasswordValid(passwordEditText.getText()
                        .toString())) {
                    userEmailEditText.setText("");
                    userEmailEditText.requestFocus();
                    passwordEditText.setText("");
                    showCustomToastMessage(
                            "Password Must Be At Least 6 Char Long", true);
                } else {
                    if (!connectionDetector.hasConnection()) {
                        showCustomToastMessage(
                                "Please Check Your Internet Connection", true);
                    } else {
                        PreferenceStore.setMode(getApplicationContext(),
                                "Production");
                        PreferenceStore.setBaseURL(getApplicationContext(),
                                "http://stgfba.anchanto.com:3010/api/v1");
                        System.out.println("Mode : "
                                + PreferenceStore
                                .getMode(getApplicationContext()));
                        System.out.println("Base URL : "
                                + PreferenceStore
                                .getBaseURL(getApplicationContext()));
                        loginUserData();
                    }
                }
            }
        }
    }

    // Method to fill the login details into jsonObject
    public void loginUserData() {
        Bugfender.d("DEBUG", "Login User email - " + userEmailEditText.getText().toString() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JSONObject user = new JSONObject();
        try {
            user.putOpt(
                    "user",
                    getLoginDetails(userEmailEditText.getText().toString(),
                            passwordEditText.getText().toString()));
        } catch (JSONException e) {
            e.getMessage();
        }

        loginUser(user);
    }

    // Method to get login details i.e. email and password
    public JSONObject getLoginDetails(String userEmail, String userPassword) {
        JSONObject loginDetails = new JSONObject();
        try {
            loginDetails.put("app_name", "FBAPicking");
            loginDetails.put("email", userEmail);
            loginDetails.put("password", userPassword);
        } catch (JSONException e) {
            e.getMessage();
        }
        return loginDetails;
    }

    // Method to call login web-service
    public void loginUser(JSONObject userJson) {
        Bugfender.d("DEBUG", "Login API request submitted : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        showProgressDialog();

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST,
                PreferenceStore.getBaseURL(getApplicationContext())
                        + "/login", userJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String errorString = ResponseValidator
                                .validate(response);

                        if (errorString.length() > 0) {
                            Bugfender.d("DEBUG", "Login API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            closeProgressDialog();
                            PreferenceStore
                                    .clearDetails(getApplicationContext());
                            rememberMeCheckBox.setChecked(false);
                            userEmailEditText.setText("");
                            userEmailEditText.requestFocus();
                            passwordEditText.setText("");
                            showCustomToastMessage(errorString, true);
                        } else {
                            Bugfender.d("DEBUG", "Login API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            try {
                                data = response.getJSONObject("data");
                                userModel = new Gson().fromJson(data
                                                .getJSONObject("user").toString(),
                                        UserModel.class);

                                if (userModel != null) {
                                    PreferenceStore.setUserModel(
                                            getApplicationContext(), userModel);
                                    tenantModel = userModel.getTenantModel();

                                    if (rememberMeCheckBox.isChecked()) {
                                        PreferenceStore.setEmail(
                                                getApplicationContext(),
                                                userEmailEditText.getText()
                                                        .toString());
                                        PreferenceStore.setPassword(
                                                getApplicationContext(),
                                                passwordEditText.getText()
                                                        .toString());
                                    }

                                    getROLE_NAME();

                                    if (ROLE_NAME.size() == 0) {
                                        showCustomToastMessage(
                                                "This User Doesn't Have Any Role, Please Check With Warehouse Admin.",
                                                true);
                                        userEmailEditText.setText("");
                                        userEmailEditText.requestFocus();
                                        passwordEditText.setText("");
                                        rememberMeCheckBox.setChecked(false);
                                        if (!connectionDetector.hasConnection()) {
                                            showCustomToastMessage(
                                                    "Please Check Your Internet Connection", true);
                                        } else {
                                            logoutUser();
                                        }
                                    } else {
                                        Bugfender.d("DEBUG", "Login User Tenant : " + userModel.getTenantModel().getName() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                                        // Delete internal files of application
                                        Commons.deleteInternalStorageFiles(LoginActivity.this);

                                        showCustomToastMessage(
                                                "You are logged in as "
                                                        + PreferenceStore
                                                        .getUserModel(
                                                                getApplicationContext())
                                                        .getFirst_name(),
                                                false);

                                        PreferenceStore.setWarehouseId(
                                                getApplicationContext(),
                                                userModel.getWarehouse_id());

                                        Bugfender.d("DEBUG", "Login User Warehouse ID : "
                                                + PreferenceStore
                                                .getWarehouseId(getApplicationContext()) + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                                        PreferenceStore.setPrefEnableErrorAcknowledgement(
                                                getApplicationContext(),
                                                tenantModel.getEnableErrorAcknowledgement());

                                        loginIntent = new Intent(
                                                LoginActivity.this,
                                                MainMenuActivity.class);
                                        startActivity(loginIntent);
                                    }
                                }
                            } catch (Exception e) {
                                Bugfender.e("ERROR", "Login API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                                userModel = null;
                                PreferenceStore
                                        .clearDetails(getApplicationContext());
                            }
                            closeProgressDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Login API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                closeProgressDialog();

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null
                        && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    showCustomToastMessageWithLength(
                            "Invalid Email Or Password.",
                            true, "long");
                    userEmailEditText.setText("");
                    userEmailEditText.requestFocus();
                    passwordEditText.setText("");
                    rememberMeCheckBox.setChecked(false);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Commons.getAuth());
                params.put("Content-Type", String.format("application/json"));
                params.put("Accept", String.format("application/json"));
                return params;
            }
        };
        ApplicationController.getInstance().getRequestQueue().add(jr);
    }

    // Logout user API
    public void logoutUser() {
        Bugfender.d("DEBUG", "Logout API request submitted : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.DELETE,
                PreferenceStore.getBaseURL(getApplicationContext())
                        + "/logout", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String errorString = ResponseValidator
                        .validate(response);

                if (errorString.length() > 0) {
                    Bugfender.d("DEBUG", "Logout API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    showCustomToastMessage(errorString, true);
                } else {
                    Bugfender.d("DEBUG", "Logout API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    try {
                        if (response.getString("info")
                                .equalsIgnoreCase("Logged out")) {
                            PreferenceStore
                                    .clearDetails(getApplicationContext());
                            // Clear Cache of application
                            Commons.trimCache(LoginActivity.this);
                        }
                    } catch (JSONException e) {
                        Bugfender.e("ERROR", "Logout API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                    }
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Logout API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                showCustomToastMessage("Error : " + error.getMessage(),
                        true);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", String.format("application/json"));
                params.put("Accept", String.format("application/json"));
                params.put("Authorization", Commons.getAuth());
                params.put("Authorization", "Token token="
                        + PreferenceStore.getUserModel(getApplicationContext())
                        .getAuth_token());
                return params;
            }
        };
        ApplicationController.getInstance().getRequestQueue().add(jr);
    }

    // Method to return roles name array list of type String
    public ArrayList<String> getROLE_NAME() {
        ROLE_NAME = new ArrayList<String>();
        for (UserRoleModel roleModel : userModel.getRoles()) {
            ROLE_NAME.add(roleModel.getName());
        }
        return ROLE_NAME;
    }
}
