package com.fbapicking.controller.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bugfender.sdk.Bugfender;
import com.fbapicking.R;
import com.fbapicking.controller.adapter.PickByProcessTypeAdapter;
import com.fbapicking.controller.adapter.SummaryAdapter;
import com.fbapicking.model.order.PickByProcessTypeModel;
import com.fbapicking.model.order.PickItemResponseModel;
import com.fbapicking.model.order.ProcessTypeModel;
import com.fbapicking.model.order.StartPickResponseModel;
import com.fbapicking.model.order.SummaryListResponseModel;
import com.fbapicking.model.user.PackerListResponseModel;
import com.fbapicking.model.user.PackerModel;
import com.fbapicking.utility.ApplicationController;
import com.fbapicking.utility.Commons;
import com.fbapicking.utility.ConnectionDetector;
import com.fbapicking.utility.PreferenceStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Nitin on 19/09/18.
 */
public class PickByProcessTypeActivity extends BaseActivity implements View.OnClickListener {
    ImageButton logoutButton, lblBack;
    TextView lblHeader, titleHeaderPopup, handoverToKey, handoverToColon, lblTotalOrder, lblTotalSKU, lblTotalUnits;
    Spinner handoverTo;
    EditText pickBinEditText;
    Button confirmButton, cancelButton;
    Typeface font;
    LayoutInflater layoutInflater, actionLayoutInflater;
    View promptView;
    ListView itemListView, orderListView;
    AlertDialog logoutAlertDialog, cancelAlertDialog, confirmAlertDialog, pickConfirmAlertDialog;
    ConnectionDetector connectionDetector;
    Bundle bundle;
    String processType, option;
    ProcessTypeModel selectedProcessTypeModel;
    StartPickResponseModel startPickResponseModel;
    List<PickByProcessTypeModel> pickByProcessTypeModelList, fulfillOrderModelList, nonFulfillOrderModelList;
    PickByProcessTypeAdapter pickByProcessTypeAdapter;
    ArrayList<String> qtyArray, pickedQtyArray;
    List<PickByProcessTypeModel> itemArray;
    String pickingProcessId;
    Dialog summaryDialog;
    SummaryAdapter summaryAdapter;
    List<PackerModel> packerModelList;
    ArrayAdapter<PackerModel> packersArrayAdapter;
    PickItemResponseModel pickItemResponseModel;

    // Default Method: to create the activity (i.e. to start activity)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pick_by_process_type);

        initializeComponents();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initializeComponents() {
        connectionDetector = new ConnectionDetector(getApplicationContext());

        setUpActionBar();

        font = Typeface.createFromAsset(getAssets(), "fonts/Museo300Regular.otf");

        layoutInflater = LayoutInflater.from(PickByProcessTypeActivity.this);

        logoutButton = findViewById(R.id.lblSetting);
        if (logoutButton != null)
            logoutButton.setOnClickListener(this);

        lblHeader = findViewById(R.id.lblHeader);
        if (lblHeader != null) {
            lblHeader.setTypeface(font, Typeface.BOLD);
            lblHeader.setSelected(true);
        }

        lblBack = findViewById(R.id.lblBack);
        if (lblBack != null) {
            lblBack.setVisibility(View.VISIBLE);
            lblBack.setOnClickListener(this);
        }

        pickBinEditText = findViewById(R.id.pickBinEditText);
        if (pickBinEditText != null)
            pickBinEditText.setTypeface(font);

        itemListView = findViewById(R.id.itemListView);
        itemListView.setRecyclerListener(new AbsListView.RecyclerListener() {
            @Override
            public void onMovedToScrapHeap(View view) {
                if (view.hasFocus()) {
                    view.clearFocus();

                    hideKeyBoard(view);
                }
            }
        });

        itemListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.clearFocus();

                hideKeyBoard(v);
                return false;
            }
        });

        actionLayoutInflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup) actionLayoutInflater.inflate(R.layout.confirm_button_layout, itemListView, false);

        cancelButton = footer.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        cancelButton.setTypeface(font);

        confirmButton = footer.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);
        confirmButton.setTypeface(font);
        confirmButton.setEnabled(false);
        confirmButton.setBackgroundResource(R.drawable.button_light_background);

        itemListView.addFooterView(footer, null, false);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            processType = bundle.getString("process_type");
            option = bundle.getString("option");

            if (processType != null) {
                selectedProcessTypeModel = (ProcessTypeModel) getIntent().getSerializableExtra("selected_process_type_model");
                if (selectedProcessTypeModel != null) {
                    try {
                        lblHeader.setText(selectedProcessTypeModel.getDisplay_picking_type());

                        JSONObject startPickJson = new JSONObject();
                        startPickJson.put("picking_type", selectedProcessTypeModel.getPicking_type());
                        startPickJson.put("no_of_orders", selectedProcessTypeModel.getNo_of_orders());
                        startPickJson.put("units", selectedProcessTypeModel.getUnits());
                        startPickJson.put("order_ids", new JSONArray(Arrays.asList(selectedProcessTypeModel.getOrder_ids())));

                        startPick(processType, startPickJson);
                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }
            }
        }
    }

    // Method to hide KeyBoard
    private void hideKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    // API Method to start pick
    @SuppressLint("LongLogTag")
    public void startPick(final String processType, JSONObject requestObject) {
        Bugfender.d("DEBUG", "Start Pick API request body " + requestObject + " for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        Bugfender.d("DEBUG", "Start Pick API request submitted for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        showProgressDialog();
        String startPickURL = PreferenceStore.getBaseURL(getApplicationContext()) + "/order_pickup_lists/pick_by/" + processType + "/start";

        Bugfender.d("DEBUG", "Start Pick API URL : " + startPickURL + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST, startPickURL, requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO Auto-generated method stub
                        String errorString = Commons.ResponseValidator
                                .validate(response);

                        if (errorString.length() > 0) {
                            Bugfender.d("DEBUG", "Start Pick API response got success for " + processType + " - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            showCustomToastMessage(
                                    errorString, true);
                        } else {
                            Bugfender.d("DEBUG", "Start Pick API response got success for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            parseStartPickResponse(processType, response);
                        }
                        closeProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Start Pick API response got failure for " + processType + " - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                closeProgressDialog();

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
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", Commons.getAuth());
                params.put("Authorization", "Token token="
                        + PreferenceStore.getUserModel(getApplicationContext())
                        .getAuth_token());
                return params;
            }
        };
        ApplicationController.getInstance().getRequestQueue().add(jsonRequest);
    }

    public void parseStartPickResponse(String processType, JSONObject response) {
        try {
            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.create();

            startPickResponseModel = null;
            startPickResponseModel = gson.fromJson(response.toString(), StartPickResponseModel.class);

            boolean status = (Boolean) response.get("success");
            if (status) {
                if (pickBinEditText != null) {
                    pickBinEditText.setEnabled(true);
                    pickBinEditText.setText("");
                    pickBinEditText.requestFocus();
                }

                pickByProcessTypeModelList = new ArrayList<PickByProcessTypeModel>();
                pickByProcessTypeModelList = startPickResponseModel.getPickByProcessTypeListResponseModel().getPickByProcessTypeModelListArray();

                pickingProcessId = startPickResponseModel.getPickByProcessTypeListResponseModel().getPicking_process_id();

                if (pickByProcessTypeModelList != null && pickByProcessTypeModelList.size() > 0) {
                    itemArray = new ArrayList<PickByProcessTypeModel>();
                    qtyArray = new ArrayList<String>();
                    pickedQtyArray = new ArrayList<String>();

                    for (PickByProcessTypeModel pickByProcessTypeModel : pickByProcessTypeModelList) {
                        itemArray.add(pickByProcessTypeModel);
                        qtyArray.add(pickByProcessTypeModel.getQuantity());
                        pickedQtyArray.add("0");
                    }

                    pickByProcessTypeAdapter = new PickByProcessTypeAdapter(PickByProcessTypeActivity.this, this, qtyArray, pickedQtyArray, processType);

                    for (PickByProcessTypeModel pickByProcessTypeModel : pickByProcessTypeModelList) {
                        pickByProcessTypeAdapter.add(pickByProcessTypeModel);
                    }

                    itemListView.setAdapter(pickByProcessTypeAdapter);

                    confirmButton.setEnabled(false);
                    confirmButton.setBackgroundResource(R.drawable.button_light_background);
                } else {
                    showCustomToastMessage("No Data Found", true);

                    checkReleaseAPI();
                }
            }
        } catch (Exception e) {
            Bugfender.e("ERROR", "Start Pick API response got exception for " + processType + " - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

            closeProgressDialog();

            showCustomToastMessage(e.getMessage(), true);
        }
    }

    @Override
    public void onBackPressed() {
        checkReleaseAPI();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            checkReleaseAPI();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void checkReleaseAPI() {
        if (connectionDetector != null && pickingProcessId != null) {
            if (!connectionDetector.hasConnection()) {
                showCustomToastMessage(
                        "Please Check Your Internet Connection", true);
            } else {
                releaseOrders();
            }
        } else {
            backToProcessTypeActivity();
        }
    }

    // API Method to release blocked orders
    @SuppressLint("LongLogTag")
    public void releaseOrders() {
        JSONObject reqJson = new JSONObject();
        try {
            reqJson.put("picking_process_id", pickingProcessId);
        } catch (JSONException e) {
            e.getMessage();
        }

        Bugfender.d("DEBUG", "Release Orders API request body : " + reqJson + " for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        if (!connectionDetector.hasConnection()) {
            showCustomToastMessage(
                    "Please Check Your Internet Connection", true);
        } else {
            Bugfender.d("DEBUG", "Release Orders API request submitted for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

            String requestURL = "";

            requestURL = PreferenceStore.getBaseURL(getApplicationContext()) + "/order_pickup_lists/release_blocked_orders";

            Bugfender.d("DEBUG", "Release Orders API URL : " + requestURL + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

            JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST,
                    requestURL, reqJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String errorString = Commons.ResponseValidator
                                    .validate(response);

                            backToProcessTypeActivity();

                            if (errorString.length() > 0) {
                                Bugfender.d("DEBUG", "Release Orders API response got success for " + processType + " - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                                showCustomToastMessage(errorString, true);
                            } else {
                                Bugfender.d("DEBUG", "Release Orders API response got success for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                                try {
                                    boolean status = (Boolean) response
                                            .get("success");
                                    if (status) {
                                        showCustomToastMessage(
                                                response.getString("info"),
                                                false);
                                    }
                                } catch (JSONException e) {
                                    Bugfender.e("ERROR", "Release Orders API response got exception for " + processType + " - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Bugfender.e("ERROR", "Release Orders API response got failure for " + processType + " - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    showCustomToastMessage("Error = " + error.getMessage(),
                            true);

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
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Accept", "application/json");
                    params.put("Authorization", Commons.getAuth());
                    params.put("Authorization", "Token token="
                            + PreferenceStore.getUserModel(getApplicationContext())
                            .getAuth_token());
                    return params;
                }
            };
            ApplicationController.getInstance().getRequestQueue().add(jr);
        }
    }

    // Method to enable confirm button after adapter method called
    public void enableConfirmButton() {
        confirmButton.setEnabled(true);
        confirmButton.setBackgroundResource(R.drawable.button_red_background);
    }

    // Method to disable confirm button after adapter method called
    public void disableConfirmButton() {
        confirmButton.setEnabled(false);
        confirmButton.setBackgroundResource(R.drawable.button_light_background);
    }

    private void backToProcessTypeActivity() {
        Intent mainIntent = new Intent(PickByProcessTypeActivity.this, ProcessTypeActivity.class);
        mainIntent.putExtra("process_type", processType);
        mainIntent.putExtra("option", option);
        startActivity(mainIntent);
    }

    @SuppressLint("SetTextI18n")
    private void takeConfirmation(String action) {
        promptView = layoutInflater.inflate(R.layout.prompt, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alert dialog builder
        alertDialogBuilder.setView(promptView);

        final TextView confirmationTextView = (TextView) promptView
                .findViewById(R.id.promptTextView);
        confirmationTextView.setTypeface(font);

        if (action.equalsIgnoreCase("cancel")) {
            confirmationTextView
                    .setText("Do you want to cancel this picking process?");
        } else {
            confirmationTextView
                    .setText("Do you want to go back from this picking process?");
        }

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                checkReleaseAPI();
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
        cancelAlertDialog = alertDialogBuilder.create();

        // show it
        cancelAlertDialog.show();

        Button plusBtn = cancelAlertDialog
                .getButton(Dialog.BUTTON_POSITIVE);
        plusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                .getDimension(R.dimen.text_size_large));
        plusBtn.setTypeface(font);

        Button minusBtn = cancelAlertDialog
                .getButton(Dialog.BUTTON_NEGATIVE);
        minusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                .getDimension(R.dimen.text_size_large));
        minusBtn.setTypeface(font);
    }

    @Override
    public void onClick(View v) {
        if (v == lblBack) {
            takeConfirmation("back");
        } else if (v == cancelButton) {
            takeConfirmation("cancel");
        } else if (v == confirmButton) {
            promptView = layoutInflater.inflate(R.layout.prompt, null);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set prompts.xml to alert dialog builder
            alertDialogBuilder.setView(promptView);

            final TextView confirmationTextView = (TextView) promptView
                    .findViewById(R.id.promptTextView);
            confirmationTextView.setTypeface(font);
            confirmationTextView
                    .setText("Do you want to pick this product(s)?");

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    getPickingData();
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
            confirmAlertDialog = alertDialogBuilder.create();

            // show it
            confirmAlertDialog.show();

            Button plusBtn = confirmAlertDialog
                    .getButton(Dialog.BUTTON_POSITIVE);
            plusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_large));
            plusBtn.setTypeface(font);

            Button minusBtn = confirmAlertDialog
                    .getButton(Dialog.BUTTON_NEGATIVE);
            minusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_large));
            minusBtn.setTypeface(font);
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

    // Method to get picking data
    private void getPickingData() {
        JSONArray pickItemArray = new JSONArray();
        pickedQtyArray = pickByProcessTypeAdapter.pickedQtyArray;

        int loopIndex = 0;
        for (PickByProcessTypeModel pModel : itemArray) {
            JSONObject pickItemJson = new JSONObject();

            String qty = pickedQtyArray.get(loopIndex);

            if (pModel != null) {
                try {
                    pickItemJson.put("picked_qty", Integer.parseInt(qty));
                    pickItemJson.put("id", Integer.parseInt(pModel.getId()));
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
            pickItemArray.put(pickItemJson);
            loopIndex++;
        }

        JSONObject pickJson = new JSONObject();
        try {
            pickJson.put("picking_products", pickItemArray);
            pickJson.put("picking_process_id", pickingProcessId);
            pickJson.put("process_type", processType);

            if (pickBinEditText.getText().toString().length() > 0) {
                List<String> binNumbersArray = new ArrayList<String>();
                binNumbersArray = Arrays.asList(pickBinEditText.getText()
                        .toString().split("\\s*,\\s*"));
                List<String> tempArray = new ArrayList<String>();
                for (String str : binNumbersArray) {
                    tempArray.add(str.toUpperCase());
                }
                List<String> uniqueBinNumbersArray = new ArrayList<String>(
                        new HashSet<String>(tempArray));

                pickJson.put("bins", new JSONArray(
                        uniqueBinNumbersArray));
            }
        } catch (JSONException e) {
            e.getMessage();
        }

        if (!connectionDetector.hasConnection()) {
            showCustomToastMessage(
                    "Please Check Your Internet Connection", true);
        } else {
            pickItem(pickJson);
        }
    }

    // API Method to pick item
    @SuppressLint("LongLogTag")
    public void pickItem(JSONObject pickedItemJson) {
        Bugfender.d("DEBUG", "Pick Item API request body : " + pickedItemJson + " for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        Bugfender.d("DEBUG", "Pick Item API request submitted for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        showProgressDialog();
        String requestURL = "";

        requestURL = PreferenceStore.getBaseURL(getApplicationContext())
                + "/order_pickup_lists/pick_by/" + processType + "/confirm";

        Bugfender.d("DEBUG", "Pick Item API URL : " + requestURL + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST,
                requestURL, pickedItemJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String errorString = Commons.ResponseValidator
                                .validate(response);

                        if (errorString.length() > 0) {
                            Bugfender.d("DEBUG", "Pick Item API response got success for " + processType + " - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            closeProgressDialog();
                            showCustomToastMessage(errorString, true);
                        } else {
                            Bugfender.d("DEBUG", "Pick Item API response got success for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            try {
                                boolean status = (Boolean) response
                                        .get("success");
                                if (status) {
                                    closeProgressDialog();

                                    final GsonBuilder builder = new GsonBuilder();
                                    final Gson gson = builder.create();

                                    pickItemResponseModel = null;
                                    pickItemResponseModel = gson.fromJson(response.toString(), PickItemResponseModel.class);

                                    getPackers();
                                }
                            } catch (JSONException e) {
                                Bugfender.e("ERROR", "Pick Item API response got exception for " + processType + " - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                            }
                        }
                        closeProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Pick Item API response got failure for " + processType + " - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                showCustomToastMessage("Error = " + error.getMessage(),
                        true);
                closeProgressDialog();

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
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", Commons.getAuth());
                params.put("Authorization", "Token token="
                        + PreferenceStore.getUserModel(getApplicationContext())
                        .getAuth_token());
                return params;
            }
        };
        ApplicationController.getInstance().getRequestQueue().add(jr);
    }

    // API Method to fetch list of all packing users
    @SuppressLint("LongLogTag")
    public void getPackers() {
        Bugfender.d("DEBUG", "Get Packer List API request submitted for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        String requestURL = "";
        requestURL = PreferenceStore.getBaseURL(getApplicationContext())
                + "/order_packing_lists/get_warehouse_packer_list?warehouse_id=" + PreferenceStore.getWarehouseId(getApplicationContext());

        Bugfender.d("DEBUG", "Get Packer List API URL : " + requestURL + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET, requestURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO Auto-generated method stub
                        String errorString = Commons.ResponseValidator
                                .validate(response);

                        if (errorString.length() > 0) {
                            Bugfender.d("DEBUG", "Get Packer List API response got success for " + processType + " - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            showCustomToastMessage(errorString, true);
                        } else {
                            Bugfender.d("DEBUG", "Get Packer List API response got success for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            parsePackersListResponse(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Bugfender.e("ERROR", "Get Packer List API response got failure for " + processType + " - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null
                        && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    showCustomToastMessageWithLength(
                            "Sorry, You Are Not Authorize To Access This Action.",
                            true, "long");
                    // Do logout while server will say status code 401
                    logoutUser();
                } else {
                    showCustomToastMessage(error.getMessage(), true);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", Commons.getAuth());
                params.put("Authorization", "Token token="
                        + PreferenceStore.getUserModel(getApplicationContext())
                        .getAuth_token());
                return params;
            }
        };
        ApplicationController.getInstance().getRequestQueue().add(jsonRequest);
    }

    // Parse Packers List Response
    @SuppressLint("LongLogTag")
    public void parsePackersListResponse(JSONObject response) {
        try {
            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.create();

            PackerListResponseModel packerListResponseModel;

            packerListResponseModel = gson.fromJson(
                    response.toString(),
                    PackerListResponseModel.class);

            boolean status = (Boolean) response.get("success");
            if (status) {
                if (packerListResponseModel.getPackerModelList()
                        .size() > 0) {
                    packerModelList = new ArrayList<PackerModel>();

                    PackerModel packerModel = new PackerModel();
                    packerModel.setUserId("0");
                    packerModel.setUserName("-- Select Packer --");
                    packerModelList.add(packerModel);

                    packerModelList.addAll(packerListResponseModel
                            .getPackerModelList());

                    // by-default sort with user name
                    Comparator<PackerModel> comparator = new Comparator<PackerModel>() {
                        @Override
                        public int compare(PackerModel object1,
                                           PackerModel object2) {
                            return object1.getUserName().compareToIgnoreCase(
                                    object2.getUserName());
                        }
                    };
                    Collections.sort(packerModelList, comparator);

                    if (pickItemResponseModel != null)
                        summaryPopup(pickItemResponseModel);
                }
            }
        } catch (Exception e) {
            Bugfender.e("ERROR", "Get Packer List response got exception for " + processType + " - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

            showCustomToastMessage(e.getMessage(), true);
        }
    }

    // API Method to cancel handover
    @SuppressLint("LongLogTag")
    public void cancelHandOver() {
        JSONObject reqJson = new JSONObject();
        try {
            reqJson.put("picking_process_id", pickingProcessId);
        } catch (JSONException e) {
            e.getMessage();
        }

        Bugfender.d("DEBUG", "Cancel HandOver API request body : " + reqJson + " for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        if (!connectionDetector.hasConnection()) {
            showCustomToastMessage(
                    "Please Check Your Internet Connection", true);
        } else {
            Bugfender.d("DEBUG", "Cancel HandOver API request submitted for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

            String requestURL = "";

            requestURL = PreferenceStore.getBaseURL(getApplicationContext())
                    + "/order_pickup_lists/pick_by/" + processType + "/cancel_handover";

            Bugfender.d("DEBUG", "Cancel HandOver API URL : " + requestURL + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

            JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST,
                    requestURL, reqJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String errorString = Commons.ResponseValidator
                                    .validate(response);

                            if (errorString.length() > 0) {
                                Bugfender.d("DEBUG", "Cancel HandOver API response got success for " + processType + " - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                                showCustomToastMessage(errorString, true);
                            } else {
                                Bugfender.d("DEBUG", "Cancel HandOver API response got success for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                                try {
                                    boolean status = (Boolean) response
                                            .get("success");
                                    if (status) {
                                        showCustomToastMessage(
                                                response.getString("info"),
                                                false);
                                    }
                                } catch (JSONException e) {
                                    Bugfender.e("ERROR", "Cancel HandOver API response got exception for " + processType + " - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Bugfender.e("ERROR", "Cancel HandOver API response got failure for " + processType + " - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    showCustomToastMessage("Error = " + error.getMessage(),
                            true);

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
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Accept", "application/json");
                    params.put("Authorization", Commons.getAuth());
                    params.put("Authorization", "Token token="
                            + PreferenceStore.getUserModel(getApplicationContext())
                            .getAuth_token());
                    return params;
                }
            };
            ApplicationController.getInstance().getRequestQueue().add(jr);
        }
    }

    // Method to show summary popup
    private void summaryPopup(PickItemResponseModel pickItemRespModel) {
        summaryDialog = new Dialog(PickByProcessTypeActivity.this);
        summaryDialog.setContentView(R.layout.summary_popup);
        summaryDialog.setCancelable(false);
        summaryDialog.setCanceledOnTouchOutside(false);

        titleHeaderPopup = summaryDialog.findViewById(R.id.titleHeaderPopup);
        if (titleHeaderPopup != null) {
            titleHeaderPopup.setTypeface(font);
        }

        TextView lblTotalOrderKey = summaryDialog.findViewById(R.id.lblTotalOrderKey);
        if (lblTotalOrderKey != null)
            lblTotalOrderKey.setTypeface(font, Typeface.BOLD);

        TextView lblTotalOrderColon = summaryDialog.findViewById(R.id.lblTotalOrderColon);
        if (lblTotalOrderColon != null)
            lblTotalOrderColon.setTypeface(font);

        lblTotalOrder = summaryDialog.findViewById(R.id.lblTotalOrder);
        if (lblTotalOrder != null)
            lblTotalOrder.setTypeface(font, Typeface.BOLD);

        TextView lblTotalSKUKey = summaryDialog.findViewById(R.id.lblTotalSKUKey);
        if (lblTotalSKUKey != null)
            lblTotalSKUKey.setTypeface(font, Typeface.BOLD);

        TextView lblTotalSKUColon = summaryDialog.findViewById(R.id.lblTotalSKUColon);
        if (lblTotalSKUColon != null)
            lblTotalSKUColon.setTypeface(font);

        lblTotalSKU = summaryDialog.findViewById(R.id.lblTotalSKU);
        if (lblTotalSKU != null)
            lblTotalSKU.setTypeface(font, Typeface.BOLD);

        TextView lblTotalUnitsKey = summaryDialog.findViewById(R.id.lblTotalUnitsKey);
        if (lblTotalUnitsKey != null)
            lblTotalUnitsKey.setTypeface(font, Typeface.BOLD);

        TextView lblTotalUnitsColon = summaryDialog.findViewById(R.id.lblTotalUnitsColon);
        if (lblTotalUnitsColon != null)
            lblTotalUnitsColon.setTypeface(font);

        lblTotalUnits = summaryDialog.findViewById(R.id.lblTotalUnits);
        if (lblTotalUnits != null)
            lblTotalUnits.setTypeface(font, Typeface.BOLD);

        final SummaryListResponseModel summaryListResponseModel = pickItemRespModel.getSummaryListResponseModel();

        handoverToKey = summaryDialog.findViewById(R.id.handoverToKey);
        if (handoverToKey != null)
            handoverToKey.setTypeface(font);

        handoverToColon = summaryDialog.findViewById(R.id.handoverToColon);
        if (handoverToColon != null)
            handoverToColon.setTypeface(font, Typeface.BOLD);

        handoverTo = summaryDialog.findViewById(R.id.handoverTo);
        if (packerModelList != null) {
            packersArrayAdapter = new ArrayAdapter<PackerModel>(
                    getApplicationContext(),
                    R.layout.spinner_with_background,
                    packerModelList) {
                public View getView(int position, View convertView,
                                    android.view.ViewGroup parent) {
                    TextView v = (TextView) super.getView(position,
                            convertView, parent);
                    v.setTypeface(font, Typeface.BOLD);
                    return v;
                }

                public View getDropDownView(int position,
                                            View convertView, android.view.ViewGroup
                                                    parent) {
                    TextView v = (TextView) super.getView(position,
                            convertView, parent);
                    v.setTypeface(font, Typeface.BOLD);
                    return v;
                }
            };

            packersArrayAdapter
                    .setDropDownViewResource(R.layout.spinner_with_background);
            handoverTo.setAdapter(packersArrayAdapter);
            handoverTo.setSelection(0);
        }

        orderListView = summaryDialog.findViewById(R.id.orderListView);

        if (summaryListResponseModel != null) {
            fulfillOrderModelList = summaryListResponseModel.getFulfillableOrdersListArray();
            nonFulfillOrderModelList = summaryListResponseModel.getNonFulfillableOrdersListArray();
        }

        final Button failedButton = summaryDialog.findViewById(R.id.failedButton);
        failedButton.setTypeface(font, Typeface.BOLD);
        final Button successfulButton = summaryDialog.findViewById(R.id.successfulButton);
        successfulButton.setTypeface(font);

        failedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successfulButton
                        .setBackgroundResource(R.drawable.light_button_border);
                successfulButton.setTypeface(font);
                failedButton
                        .setBackgroundResource(R.drawable.dark_button_border);
                failedButton.setTypeface(font, Typeface.BOLD);

                initializeConstructor(true);

                if (nonFulfillOrderModelList != null && nonFulfillOrderModelList.size() > 0) {
                    for (PickByProcessTypeModel pickByProcessTypeModel : nonFulfillOrderModelList) {
                        summaryAdapter.add(pickByProcessTypeModel);
                    }
                }

                showSummaryCounts(false, summaryListResponseModel);

                if (orderListView != null)
                    orderListView.setAdapter(summaryAdapter);

                if (summaryAdapter != null)
                    summaryAdapter.notifyDataSetChanged();
            }
        });

        successfulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failedButton
                        .setBackgroundResource(R.drawable.light_button_border);
                failedButton.setTypeface(font);
                successfulButton
                        .setBackgroundResource(R.drawable.dark_button_border);
                successfulButton.setTypeface(font, Typeface.BOLD);

                initializeConstructor(false);

                if (fulfillOrderModelList != null && fulfillOrderModelList.size() > 0) {
                    for (PickByProcessTypeModel pickByProcessTypeModel : fulfillOrderModelList) {
                        summaryAdapter.add(pickByProcessTypeModel);
                    }
                }

                showSummaryCounts(true, summaryListResponseModel);

                if (orderListView != null)
                    orderListView.setAdapter(summaryAdapter);

                if (summaryAdapter != null)
                    summaryAdapter.notifyDataSetChanged();
            }
        });

        if (nonFulfillOrderModelList != null && nonFulfillOrderModelList.size() > 0) {
            successfulButton
                    .setBackgroundResource(R.drawable.light_button_border);
            successfulButton.setTypeface(font);
            failedButton
                    .setBackgroundResource(R.drawable.dark_button_border);
            failedButton.setTypeface(font, Typeface.BOLD);

            initializeConstructor(true);

            for (PickByProcessTypeModel pickByProcessTypeModel : nonFulfillOrderModelList) {
                summaryAdapter.add(pickByProcessTypeModel);
            }

            showSummaryCounts(false, summaryListResponseModel);
        } else {
            if (fulfillOrderModelList != null && fulfillOrderModelList.size() > 0) {
                failedButton
                        .setBackgroundResource(R.drawable.light_button_border);
                failedButton.setTypeface(font);
                successfulButton
                        .setBackgroundResource(R.drawable.dark_button_border);
                successfulButton.setTypeface(font, Typeface.BOLD);

                initializeConstructor(false);

                for (PickByProcessTypeModel pickByProcessTypeModel : fulfillOrderModelList) {
                    summaryAdapter.add(pickByProcessTypeModel);
                }

                showSummaryCounts(true, summaryListResponseModel);
            }
        }

        if (orderListView != null)
            orderListView.setAdapter(summaryAdapter);

        if (summaryAdapter != null)
            summaryAdapter.notifyDataSetChanged();

        summaryDialog.show();

        Button dialogButtonCancel = summaryDialog.findViewById(R.id.dialogButtonCancel);
        dialogButtonCancel.setTypeface(font, Typeface.BOLD);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryDialog.dismiss();

                cancelHandOver();
            }
        });

        Button dialogButtonConfirm = summaryDialog.findViewById(R.id.dialogButtonConfirm);
        dialogButtonConfirm.setTypeface(font, Typeface.BOLD);
        dialogButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackerModel pModel = (PackerModel) handoverTo
                        .getSelectedItem();

                if (pModel != null) {
                    if (pModel.getUserId().equalsIgnoreCase("0")) {
                        showCustomToastMessage("Please Select Packer", true);
                    } else {
                        summaryDialog.dismiss();

                        setConfirmHandover(pModel.getUserId());
                    }
                }
            }
        });

        if (fulfillOrderModelList != null && fulfillOrderModelList.size() > 0) {
            dialogButtonConfirm.setEnabled(true);
            dialogButtonConfirm.setBackgroundResource(R.drawable.button_red_background);
        } else {
            dialogButtonConfirm.setEnabled(false);
            dialogButtonConfirm.setBackgroundResource(R.drawable.button_light_background);
        }
    }

    private void setConfirmHandover(String userId) {
        ArrayList<String> fulfillOrderIds = new ArrayList<String>();
        if (fulfillOrderModelList != null && fulfillOrderModelList.size() > 0) {
            for (PickByProcessTypeModel pickByProcessTypeModel : fulfillOrderModelList) {
                fulfillOrderIds.add(pickByProcessTypeModel.getOrder_bucket_id());
            }
        }

        ArrayList<String> nonFulfillOrderIds = new ArrayList<String>();
        if (nonFulfillOrderModelList != null && nonFulfillOrderModelList.size() > 0) {
            for (PickByProcessTypeModel pickByProcessTypeModel : nonFulfillOrderModelList) {
                nonFulfillOrderIds.add(pickByProcessTypeModel.getOrder_bucket_id());
            }
        }

        JSONObject confirmHandoverJson = new JSONObject();
        try {
            confirmHandoverJson.put("fulfillable_order_ids", new JSONArray(
                    fulfillOrderIds));
            confirmHandoverJson.put("non_fulfillable_order_ids", new JSONArray(
                    nonFulfillOrderIds));
            confirmHandoverJson.put("packer_id", Integer.parseInt(userId));
            confirmHandoverJson.put("picking_process_id", pickingProcessId);
            confirmHandoverJson.put("process_type", processType);
        } catch (JSONException e) {
            e.getMessage();
        }

        if (!connectionDetector.hasConnection()) {
            showCustomToastMessage(
                    "Please Check Your Internet Connection", true);
        } else {
            confirmHandover(confirmHandoverJson);
        }
    }

    // API Method to confirm handover
    @SuppressLint("LongLogTag")
    public void confirmHandover(JSONObject reqJson) {
        Bugfender.d("DEBUG", "Confirm Handover API request body : " + reqJson + " for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        Bugfender.d("DEBUG", "Confirm Handover API request submitted for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        showProgressDialog();
        String requestURL = "";

        requestURL = PreferenceStore.getBaseURL(getApplicationContext())
                + "/order_pickup_lists/pick_by/" + processType + "/handover";

        Bugfender.d("DEBUG", "Confirm Handover API URL : " + requestURL + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST,
                requestURL, reqJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String errorString = Commons.ResponseValidator
                                .validate(response);

                        if (errorString.length() > 0) {
                            Bugfender.d("DEBUG", "Confirm Handover API response got success for " + processType + " - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            closeProgressDialog();
                            showCustomToastMessage(errorString, true);
                        } else {
                            Bugfender.d("DEBUG", "Confirm Handover API response got success for " + processType + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            try {
                                boolean status = (Boolean) response
                                        .get("success");
                                if (status) {
                                    closeProgressDialog();
                                    showCustomToastMessage(
                                            response.getString("info"),
                                            false);

                                    Intent intent = new Intent(
                                            PickByProcessTypeActivity.this,
                                            MainMenuActivity.class);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                Bugfender.e("ERROR", "Confirm Handover API response got exception for " + processType + " - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                            }
                        }
                        closeProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Confirm Handover API response got failure for " + processType + " - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                showCustomToastMessage("Error = " + error.getMessage(),
                        true);
                closeProgressDialog();

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
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", Commons.getAuth());
                params.put("Authorization", "Token token="
                        + PreferenceStore.getUserModel(getApplicationContext())
                        .getAuth_token());
                return params;
            }
        };
        ApplicationController.getInstance().getRequestQueue().add(jr);
    }

    public void showErrorMessage(String message, boolean isError) {
        showCustomToastMessage(message, isError);
    }

    // Method to show summary counts
    private void showSummaryCounts(boolean isSuccessTab, SummaryListResponseModel summaryListRespModel) {
        if (summaryListRespModel != null) {
            if (isSuccessTab) {
                lblTotalOrder.setText(summaryListRespModel.getTotal_fulfillable_orders_count());
                lblTotalSKU.setText(summaryListRespModel.getTotal_fulfillable_skus_count());
                lblTotalUnits.setText(summaryListRespModel.getTotal_fulfillable_units_count());
            } else {
                lblTotalOrder.setText(summaryListRespModel.getTotal_non_fulfillable_orders_count());
                lblTotalSKU.setText(summaryListRespModel.getTotal_non_fulfillable_skus_count());
                lblTotalUnits.setText(summaryListRespModel.getTotal_non_fulfillable_units_count());
            }
        }
    }

    // Method to pick confirm from failed order list
    @SuppressLint("SetTextI18n")
    public void pickConfirm(final PickByProcessTypeModel pickByProcessTypeModel) {
        promptView = layoutInflater.inflate(R.layout.prompt, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alert dialog builder
        alertDialogBuilder.setView(promptView);

        final TextView confirmationTextView = (TextView) promptView
                .findViewById(R.id.promptTextView);
        confirmationTextView.setTypeface(font);
        confirmationTextView
                .setText("Do you want to pick confirm this?");

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                summaryDialog.dismiss();

                                getPickConfirmData(pickByProcessTypeModel);
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
        pickConfirmAlertDialog = alertDialogBuilder.create();

        // show it
        pickConfirmAlertDialog.show();

        Button plusBtn = pickConfirmAlertDialog
                .getButton(Dialog.BUTTON_POSITIVE);
        plusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                .getDimension(R.dimen.text_size_large));
        plusBtn.setTypeface(font);

        Button minusBtn = pickConfirmAlertDialog
                .getButton(Dialog.BUTTON_NEGATIVE);
        minusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                .getDimension(R.dimen.text_size_large));
        minusBtn.setTypeface(font);
    }

    // Method to get picking data
    private void getPickConfirmData(PickByProcessTypeModel pickByProcessTypeModel) {
        JSONArray pickItemArray = new JSONArray();
        if (pickByProcessTypeModel != null) {
            try {
                JSONObject pickItemJson = new JSONObject();

                int remainingQty = (Integer.parseInt(pickByProcessTypeModel.getQuantity()) - Integer.parseInt(pickByProcessTypeModel.getPicked_quantity()));
                pickItemJson.put("picked_qty", remainingQty);
                pickItemJson.put("id", Integer.parseInt(pickByProcessTypeModel.getOrder_picking_product_data_id()));

                pickItemArray.put(pickItemJson);
            } catch (JSONException e) {
                e.getMessage();
            }
        }

        JSONObject pickJson = new JSONObject();
        try {
            pickJson.put("picking_products", pickItemArray);
            pickJson.put("picking_process_id", pickingProcessId);
            pickJson.put("process_type", processType);
            pickJson.put("is_failed_pick", "true");

            if (pickBinEditText.getText().toString().length() > 0) {
                List<String> binNumbersArray = new ArrayList<String>();
                binNumbersArray = Arrays.asList(pickBinEditText.getText()
                        .toString().split("\\s*,\\s*"));
                List<String> tempArray = new ArrayList<String>();
                for (String str : binNumbersArray) {
                    tempArray.add(str.toUpperCase());
                }
                List<String> uniqueBinNumbersArray = new ArrayList<String>(
                        new HashSet<String>(tempArray));

                pickJson.put("bins", new JSONArray(
                        uniqueBinNumbersArray));
            }
        } catch (JSONException e) {
            e.getMessage();
        }

        if (!connectionDetector.hasConnection()) {
            showCustomToastMessage(
                    "Please Check Your Internet Connection", true);
        } else {
            pickItem(pickJson);
        }
    }

    // Method to initialize constructor
    private void initializeConstructor(boolean failedTab) {
        summaryAdapter = new SummaryAdapter(
                PickByProcessTypeActivity.this, this, failedTab);
        summaryAdapter.clear();
    }
}
