package com.fbapicking.controller.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bugfender.sdk.Bugfender;
import com.fbapicking.R;
import com.fbapicking.controller.adapter.RoleAdapter;
import com.fbapicking.model.user.UserModel;
import com.fbapicking.model.user.UserRoleModel;
import com.fbapicking.utility.ApplicationController;
import com.fbapicking.utility.Commons;
import com.fbapicking.utility.ConnectionDetector;
import com.fbapicking.utility.PreferenceStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    ImageButton logoutButton, lblBack;
    TextView lblHeader, lblFirstNameKey, lblFirstNameColon, lblFirstName,
            lblLastNameKey, lblLastNameColon, lblLastName,
            lblEmailKey, lblEmailColon, lblEmail,
            lblRolesKey, lblRolesColon;
    Typeface font;
    LayoutInflater layoutInflater;
    View promptView;
    AlertDialog logoutAlertDialog;
    ConnectionDetector connectionDetector;
    ListView rolesListView;
    List<UserRoleModel> roleModelArrayList;
    RoleAdapter roleAdapter;
    UserModel userModel;

    // Default Method: to create the activity (i.e. to start activity)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        initializeComponents();
    }

    public void initializeComponents() {
        connectionDetector = new ConnectionDetector(getApplicationContext());

        setUpActionBar();

        font = Typeface.createFromAsset(getAssets(), "fonts/Museo300Regular.otf");

        layoutInflater = LayoutInflater.from(ProfileActivity.this);

        logoutButton = (ImageButton) findViewById(R.id.lblSetting);
        if (logoutButton != null) {
            logoutButton.setOnClickListener(this);
        }

        lblHeader = (TextView) findViewById(R.id.lblHeader);
        if (lblHeader != null) {
            lblHeader.setText(getResources().getString(R.string.profile));
            lblHeader.setTypeface(font, Typeface.BOLD);
        }

        lblBack = (ImageButton) findViewById(R.id.lblBack);
        if (lblBack != null) {
            lblBack.setOnClickListener(this);
        }

        lblFirstNameKey = (TextView) findViewById(R.id.lblFirstNameKey);
        if (lblFirstNameKey != null)
            lblFirstNameKey.setTypeface(font);

        lblFirstNameColon = (TextView) findViewById(R.id.lblFirstNameColon);
        if (lblFirstNameColon != null)
            lblFirstNameColon.setTypeface(font);

        lblFirstName = (TextView) findViewById(R.id.lblFirstName);
        if (lblFirstName != null) {
            lblFirstName.setTypeface(font);
        }

        lblLastNameKey = (TextView) findViewById(R.id.lblLastNameKey);
        if (lblLastNameKey != null)
            lblLastNameKey.setTypeface(font);

        lblLastNameColon = (TextView) findViewById(R.id.lblLastNameColon);
        if (lblLastNameColon != null)
            lblLastNameColon.setTypeface(font);

        lblLastName = (TextView) findViewById(R.id.lblLastName);
        if (lblLastName != null) {
            lblLastName.setTypeface(font);
        }

        lblEmailKey = (TextView) findViewById(R.id.lblEmailKey);
        if (lblEmailKey != null)
            lblEmailKey.setTypeface(font);

        lblEmailColon = (TextView) findViewById(R.id.lblEmailColon);
        if (lblEmailColon != null)
            lblEmailColon.setTypeface(font);

        lblEmail = (TextView) findViewById(R.id.lblEmail);
        if (lblEmail != null) {
            lblEmail.setTypeface(font);
        }

        lblRolesKey = (TextView) findViewById(R.id.lblRolesKey);
        if (lblRolesKey != null)
            lblRolesKey.setTypeface(font);

        lblRolesColon = (TextView) findViewById(R.id.lblRolesColon);
        if (lblRolesColon != null)
            lblRolesColon.setTypeface(font);

        rolesListView = (ListView) findViewById(R.id.rolesListView);
        roleAdapter = new RoleAdapter(ProfileActivity.this, this);

        getUserDetails();
    }

    public void getUserDetails() {
        Bugfender.d("DEBUG", "Get User Detail API request submitted : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        String userUrl = "";
        showProgressDialog("Loading user details...");
        userUrl = PreferenceStore.getBaseURL(getApplicationContext())
                + "/users/" + PreferenceStore.getUserModel(getApplicationContext()).getId();

        Bugfender.d("DEBUG","Get User Detail API URL : " + userUrl + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,
                userUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String errorString = Commons.ResponseValidator
                        .validate(response);
                if (errorString.length() > 0) {
                    showCustomToastMessage(errorString, true);

                    Bugfender.d("DEBUG", "Get User Detail API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    closeProgressDialog();
                    if (roleAdapter != null) {
                        roleAdapter.clear();
                        roleAdapter.notifyDataSetChanged();
                    }
                } else {
                    Bugfender.d("DEBUG", "Get User Detail API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    parseUserResponse(response);
                }
                closeProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Get User Detail API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null
                        && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    showCustomToastMessageWithLength(
                            "Sorry, You Are Not Authorize To Access This Action.",
                            true, "long");
                    if (!connectionDetector.hasConnection()) {
                        showCustomToastMessage(
                                "Please Check Your Internet Connection", true);
                    } else {
                        // Do logout while server will say status code 401
                        logoutUser();
                    }
                } else {
                    if (roleAdapter != null) {
                        roleAdapter.clear();
                        roleAdapter.notifyDataSetChanged();
                    }
                    closeProgressDialog();
                }
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

    public void parseUserResponse(JSONObject response) {
        try {
            String str = response.toString();
            JSONObject jsn = new JSONObject(str);

            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.create();

            userModel = null;
            userModel = gson.fromJson(jsn.getJSONObject("data").getJSONObject("user").toString(), UserModel.class);

            boolean status = (Boolean) response.get("success");
            if (status) {
                lblFirstName.setText(userModel.getFirst_name());
                lblLastName.setText(userModel.getLast_name());
                lblEmail.setText(userModel.getEmail());

                if (roleAdapter != null) {
                    roleAdapter.clear();
                }
                if (userModel.getRoles().size() != 0) {
                    roleModelArrayList = new ArrayList<UserRoleModel>();
                    for (UserRoleModel userRoleModel : userModel.getRoles()) {
                        roleAdapter.add(userRoleModel);
                        roleModelArrayList.add(userRoleModel);
                    }

                    rolesListView.setAdapter(roleAdapter);

                    if (roleAdapter.getCount() == 0) {
                        showCustomToastMessage("No roles mapped to this user", true);
                    }
                } else {
                    if (roleAdapter != null) {
                        roleAdapter.clear();
                        roleAdapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (JSONException e) {
            Bugfender.e("ERROR", "Get User Detail API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
        }

        if (roleAdapter != null) {
            roleAdapter.notifyDataSetChanged();
        }
    }

    // Method to disable back button pressed event
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == lblBack) {
            onBackPressed();
        } else if (v == logoutButton) {
            promptView = layoutInflater.inflate(R.layout.prompt, null);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);

            // set prompts.xml to alert dialog builder
            alertDialogBuilder.setView(promptView);

            final TextView confirmationTextView = (TextView) promptView
                    .findViewById(R.id.promptTextView);
            confirmationTextView.setTypeface(font);
            confirmationTextView.setText("Do you really want to logout?");

            // set dialog message
            alertDialogBuilder
                    // .setMessage("Do you really want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // if this button is clicked, close
                                    // and do logout
                                    if (!connectionDetector.hasConnection()) {
                                        showCustomToastMessage(
                                                "Please Check Your Internet Connection", true);
                                    } else {
                                        logoutUser();
                                    }
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            logoutAlertDialog = alertDialogBuilder.create();

            // show it
            logoutAlertDialog.show();

            Button plusBtn = logoutAlertDialog
                    .getButton(Dialog.BUTTON_POSITIVE);
            plusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_large));
            plusBtn.setTypeface(font);

            Button minusBtn = logoutAlertDialog
                    .getButton(Dialog.BUTTON_NEGATIVE);
            minusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_large));
            minusBtn.setTypeface(font);
        }
    }
}
