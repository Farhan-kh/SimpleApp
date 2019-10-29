package com.fbapicking.controller.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bugfender.sdk.Bugfender;
import com.fbapicking.R;
import com.fbapicking.model.order.ProcessTypeListResponseModel;
import com.fbapicking.model.order.ProcessTypeModel;
import com.fbapicking.utility.ApplicationController;
import com.fbapicking.utility.Commons;
import com.fbapicking.utility.ConnectionDetector;
import com.fbapicking.utility.PreferenceStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nitin on 17/09/18.
 */
public class ProcessTypeActivity extends BaseActivity implements View.OnClickListener {
    ImageButton logoutButton, lblBack;
    TextView lblHeader;
    Typeface font;
    LayoutInflater layoutInflater;
    View promptView;
    AlertDialog logoutAlertDialog;
    ConnectionDetector connectionDetector;
    LinearLayout holderLayout;
    Bundle bundle;

    String processType, option;
    ProcessTypeListResponseModel processTypeListResponseModel;
    List<ProcessTypeModel> processTypeModelList;

    // Default Method: to create the activity (i.e. to start activity)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_process_type);

        initializeComponents();
    }

    public void initializeComponents() {
        connectionDetector = new ConnectionDetector(getApplicationContext());

        setUpActionBar();

        font = Typeface.createFromAsset(getAssets(), "fonts/Museo300Regular.otf");

        layoutInflater = LayoutInflater.from(ProcessTypeActivity.this);

        holderLayout = (LinearLayout) findViewById(R.id.holderLayout);

        logoutButton = (ImageButton) findViewById(R.id.lblSetting);
        if (logoutButton != null)
            logoutButton.setOnClickListener(this);

        lblHeader = (TextView) findViewById(R.id.lblHeader);
        if (lblHeader != null) {
            lblHeader.setTypeface(font, Typeface.BOLD);
        }

        lblBack = (ImageButton) findViewById(R.id.lblBack);
        if (lblBack != null) {
            lblBack.setVisibility(View.VISIBLE);
            lblBack.setOnClickListener(this);
        }

        bundle = getIntent().getExtras();
        if (bundle != null) {
            processType = bundle.getString("process_type");
            option = bundle.getString("option");

            if (option != null) {
                lblHeader.setText(option);
            }

            if (processType != null) {
                getProcessType(processType);
            }
        }
    }

    public void getProcessType(String processType) {
        Bugfender.d("DEBUG", "Process Type Count API request submitted : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        String processTypeURL = PreferenceStore.getBaseURL(getApplicationContext()) + "/order_pickup_lists/pick_by/" + processType;

        Bugfender.d("DEBUG","Process Type Count API URL : " + processTypeURL + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET, processTypeURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO Auto-generated method stub
                        String errorString = Commons.ResponseValidator
                                .validate(response);

                        if (errorString.length() > 0) {
                            Bugfender.d("DEBUG", "Process Type Count API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            showCustomToastMessage(
                                    errorString, true);
                        } else {
                            Bugfender.d("DEBUG", "Process Type Count API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            parseProcessTypeResponse(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Process Type Count API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

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
                    showCustomToastMessage(
                            error.getMessage(), true);
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
        ApplicationController.getInstance().getRequestQueue().add(jsonRequest);
    }

    public void parseProcessTypeResponse(JSONObject response) {
        try {
            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.create();

            processTypeListResponseModel = null;
            processTypeListResponseModel = gson.fromJson(response.toString(), ProcessTypeListResponseModel.class);

            boolean status = (Boolean) response.get("success");
            if (status) {
                processTypeModelList = new ArrayList<ProcessTypeModel>();
                processTypeModelList = processTypeListResponseModel.getProcessTypeModelListArray();

                showProcessTypeList(processTypeModelList);
            }
        } catch (Exception e) {
            Bugfender.e("ERROR", "Process Type Count API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

            showCustomToastMessage(e.getMessage(), true);
        }
    }

    public void showProcessTypeList(final List<ProcessTypeModel> processTypeModelList) {
        if (processTypeModelList != null && processTypeModelList.size() > 0) {
            int newControlHeight = (int) getResources().getDimension(
                    R.dimen.new_control_height);

            int controlHeight = (int) getResources().getDimension(
                    R.dimen.control_height);

            int buttonHeight = (int) getResources().getDimension(
                    R.dimen.button_height);

            LinearLayout headerLinearLayout = new LinearLayout(this);
            headerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            headerLinearLayout.setGravity(Gravity.CENTER_VERTICAL);

            final LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(0, controlHeight);
            p1.weight = 0.35f;
            p1.setMargins(10, 10, 0, 5);

            TextView typeTV = new TextView(this);
            typeTV.setLayoutParams(p1);
            typeTV.setText(getResources().getString(R.string.types));
            typeTV.setTextColor(Color.BLACK);
            typeTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_normal));
            typeTV.setPadding(5, 5, 5, 5);
            typeTV.setTypeface(font, Typeface.BOLD);
            typeTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            typeTV.setBackgroundColor(getResources().getColor(R.color.lavender));
            headerLinearLayout.addView(typeTV);

            final LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(0, controlHeight);
            p2.weight = 0.20f;
            p2.setMargins(5, 10, 0, 5);

            TextView noOfOrdersTV = new TextView(this);
            noOfOrdersTV.setLayoutParams(p2);
            noOfOrdersTV.setText(getResources().getString(R.string.noOfOrders));
            noOfOrdersTV.setTextColor(Color.BLACK);
            noOfOrdersTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_normal));
            noOfOrdersTV.setPadding(5, 5, 5, 5);
            noOfOrdersTV.setTypeface(font, Typeface.BOLD);
            noOfOrdersTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            noOfOrdersTV.setBackgroundColor(getResources().getColor(R.color.lavender));
            headerLinearLayout.addView(noOfOrdersTV);

            final LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(
                    0, controlHeight);
            p3.weight = 0.20f;
            p3.setMargins(5, 10, 0, 5);

            TextView unitsTV = new TextView(this);
            unitsTV.setLayoutParams(p3);
            unitsTV.setText(getResources().getString(R.string.units));
            unitsTV.setTextColor(Color.BLACK);
            unitsTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_normal));
            unitsTV.setPadding(5, 5, 5, 5);
            unitsTV.setTypeface(font, Typeface.BOLD);
            unitsTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            unitsTV.setBackgroundColor(getResources().getColor(R.color.lavender));
            headerLinearLayout.addView(unitsTV);

            final LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(
                    0, controlHeight);
            p4.weight = 0.25f;
            p4.setMargins(5, 5, 10, 0);

            TextView actionTV = new TextView(this);
            actionTV.setLayoutParams(p4);
            actionTV.setText(getResources().getString(R.string.action));
            actionTV.setTextColor(Color.BLACK);
            actionTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_normal));
            actionTV.setPadding(5, 5, 5, 5);
            actionTV.setTypeface(font, Typeface.BOLD);
            actionTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            actionTV.setBackgroundColor(getResources().getColor(R.color.lavender));
            headerLinearLayout.addView(actionTV);

            try {
                holderLayout.addView(headerLinearLayout);
            } catch (Exception e) {
                Bugfender.e("ERROR", "Parsing Process Type Activity got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
            }

            int count = 0;

            for (ProcessTypeModel processTypeModel : processTypeModelList) {
                LinearLayout childLinearLayout = new LinearLayout(this);
                childLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                childLinearLayout.setGravity(Gravity.CENTER_VERTICAL);

                final LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, newControlHeight);
                params1.weight = 0.35f;
                params1.setMargins(10, 0, 0, 0);

                TextView typeTextView = new TextView(this);
                typeTextView.setLayoutParams(params1);
                typeTextView.setText(processTypeModel.getDisplay_picking_type());
                typeTextView.setTextColor(Color.BLACK);
                typeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                        .getDimension(R.dimen.text_size_small));
                typeTextView.setPadding(5, 5, 5, 5);
                typeTextView.setTypeface(font);
                typeTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                typeTextView.setBackgroundColor(getResources().getColor(R.color.lavender));
                childLinearLayout.addView(typeTextView);

                final LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        0, newControlHeight);
                params2.weight = 0.20f;
                params2.setMargins(5, 0, 0, 0);

                TextView noOfOrdersTextView = new TextView(this);
                noOfOrdersTextView.setLayoutParams(params2);
                noOfOrdersTextView.setText(processTypeModel.getNo_of_orders());
                noOfOrdersTextView.setTextColor(Color.BLACK);
                noOfOrdersTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                        .getDimension(R.dimen.text_size_new));
                noOfOrdersTextView.setPadding(5, 5, 5, 5);
                noOfOrdersTextView.setTypeface(font, Typeface.BOLD);
                noOfOrdersTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                noOfOrdersTextView.setBackgroundColor(getResources().getColor(R.color.lavender));
                childLinearLayout.addView(noOfOrdersTextView);

                final LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                        0, newControlHeight);
                params3.weight = 0.20f;
                params3.setMargins(5, 0, 0, 0);

                TextView unitsTextView = new TextView(this);
                unitsTextView.setLayoutParams(params3);
                unitsTextView.setText(processTypeModel.getUnits());
                unitsTextView.setTextColor(Color.BLACK);
                unitsTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                        .getDimension(R.dimen.text_size_new));
                unitsTextView.setPadding(5, 5, 5, 5);
                unitsTextView.setTypeface(font, Typeface.BOLD);
                unitsTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                unitsTextView.setBackgroundColor(getResources().getColor(R.color.lavender));
                childLinearLayout.addView(unitsTextView);

                final LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(
                        0, newControlHeight);
                params4.weight = 0.25f;
                params4.setMargins(5, 5, 10, 5);

                LinearLayout actionLinearLayout = new LinearLayout(this);
                actionLinearLayout.setOrientation(LinearLayout.VERTICAL);
                actionLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
                actionLinearLayout.setBackgroundColor(getResources().getColor(R.color.lavender));
                actionLinearLayout.setLayoutParams(params4);

                final LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, buttonHeight);
                params5.setMargins(10, 0, 10, 0);

                final Button button = new Button(this);
                button.setLayoutParams(params5);
                button.setTag(count);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                        .getDimension(R.dimen.text_size_small));
                button.setBackgroundColor(getResources().getColor(R.color.light_orange));
                button.setText(getResources().getString(R.string.startPick));
                button.setPadding(5, 5, 5, 5);
                button.setTypeface(font, Typeface.BOLD);
                button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

                if (processTypeModel.getNo_of_orders().equalsIgnoreCase("0") && processTypeModel.getUnits().equalsIgnoreCase("0")) {
                    button.setVisibility(View.INVISIBLE);
                } else {
                    button.setVisibility(View.VISIBLE);
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("InflateParams")
                    public void onClick(View v) {
                        int selectedIndex = (Integer) v.getTag();

                        Intent i = new Intent(ProcessTypeActivity.this, PickByProcessTypeActivity.class);
                        i.putExtra("process_type", processType);
                        i.putExtra("option", option);
                        i.putExtra("selected_process_type_model", processTypeModelList.get(selectedIndex));
                        startActivity(i);
                    }
                });
                actionLinearLayout.addView(button);
                childLinearLayout.addView(actionLinearLayout);

                try {
                    holderLayout.addView(childLinearLayout);
                } catch (Exception e) {
                    Bugfender.e("ERROR", "Parsing Process Type Activity got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                }
                count++;
            }
        } else {
            showCustomToastMessage("No Product(s) Found", true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(ProcessTypeActivity.this, MainMenuActivity.class);
        startActivity(mainIntent);
    }

    @Override
    public void onClick(View v) {
        if (v == lblBack) {
            onBackPressed();
        } else if (v == logoutButton) {
            promptView = layoutInflater.inflate(R.layout.prompt, null);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

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
