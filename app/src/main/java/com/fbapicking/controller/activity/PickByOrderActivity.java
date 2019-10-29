package com.fbapicking.controller.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.fbapicking.controller.adapter.PickByOrderAdapter;
import com.fbapicking.model.order.BinModel;
import com.fbapicking.model.order.OrderItemListModel;
import com.fbapicking.model.order.OrderModel;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class PickByOrderActivity extends BaseActivity implements
        View.OnClickListener {
    Typeface font;
    ConnectionDetector connectionDetector;
    LinearLayout parentLinearLayout, actionButtonLayout, clickHereLinearLayout;
    LayoutInflater layoutInflater, actionLayoutInflater;
    EditText pickBinEditText;
    ImageButton logoutButton, lblBack;
    TextView headerTextView, emptyWallMessageTextView,
            orderDeliveryDate, lblCount, carrierTextView, sourceTextView, clickHereTextView;
    ImageView expressDeliveryImageView;
    Button pickButton, skipButton, partialPickButton, pickSkipOrdersButton, checkNewOrdersButton;
    ListView orderItemListView;
    View promptView;
    AlertDialog logoutAlertDialog, skipOrderAlertDialog, pickOrderAlertDialog, alreadyProcessedOrderAlertDialog,
            partialPickOrderAlertDialog, errorResolveOrderAlertDialog;
    boolean is_from_order_list = false;
    Integer total_assigned_orders = 0;
    List<OrderItemListModel> orderItemArray;
    List<OrderItemListModel> pickItemArray;
    ArrayList<Boolean> checkList;
    ArrayList<String> skipOrderIdArray, orderQtyArray, pickedQtyArray, actualPickedQtyArray, allUSNList;
    PickByOrderAdapter pickByOrderAdapter;
    OrderModel orderModel;
    String tracking_number, type, display_type, pickingPreference;

    // Default Method: to create the fragment (i.e. to start activity)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pick_by_order);

        initializeComponents();
    }

    private void initializeComponents() {
        connectionDetector = new ConnectionDetector(getApplicationContext());

        setUpActionBar();

        font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Museo300Regular.otf");

        layoutInflater = LayoutInflater.from(PickByOrderActivity.this);

        parentLinearLayout = findViewById(R.id.parentLayoutTodo);
        actionButtonLayout = findViewById(R.id.actionButtonLayout);
        clickHereLinearLayout = findViewById(R.id.clickHereLinearLayout);

        pickBinEditText = findViewById(R.id.pickBinEditText);
        if (pickBinEditText != null)
            pickBinEditText.setTypeface(font);

        lblBack = findViewById(R.id.lblBack);
        if (lblBack != null) {
            lblBack.setVisibility(View.VISIBLE);
            lblBack.setOnClickListener(this);
            lblBack.setFocusable(false);
        }

        orderDeliveryDate = findViewById(R.id.orderDeliveryDate);
        if (orderDeliveryDate != null)
            orderDeliveryDate.setTypeface(font);

        expressDeliveryImageView = findViewById(R.id.expressDeliveryImageView);

        carrierTextView = findViewById(R.id.carrierTextView);
        if (carrierTextView != null)
            carrierTextView.setTypeface(font);

        sourceTextView = findViewById(R.id.sourceTextView);
        if (sourceTextView != null)
            sourceTextView.setTypeface(font);

        SpannableString ss = new SpannableString("Total Order Item Quantity and Total Item Location Quantity is Mismatched. Click Here to resolve.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                resolveOrder();
            }
        };
        ss.setSpan(clickableSpan, 74, 84, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#0000FF")), 74, 84, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        clickHereTextView = findViewById(R.id.clickHereTextView);
        if (clickHereTextView != null) {
            clickHereTextView.setTypeface(font, Typeface.BOLD);
            clickHereTextView.setText(ss);
            clickHereTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        logoutButton = findViewById(R.id.lblSetting);
        if (logoutButton != null)
            logoutButton.setOnClickListener(this);

        headerTextView = findViewById(R.id.lblHeader);
        if (headerTextView != null) {
            headerTextView.setTypeface(font, Typeface.BOLD);
            headerTextView.setSelected(true);
        }

        lblCount = findViewById(R.id.lblCount);
        if (lblCount != null) {
            lblCount.setTypeface(font, Typeface.BOLD);
        }

        emptyWallMessageTextView = findViewById(R.id.wallMessage);
        if (emptyWallMessageTextView != null) {
            emptyWallMessageTextView.setTypeface(font, Typeface.BOLD);
            emptyWallMessageTextView.setOnClickListener(this);
        }

        pickSkipOrdersButton = findViewById(R.id.pickSkipOrdersButton);
        if (pickSkipOrdersButton != null) {
            pickSkipOrdersButton.setTypeface(font);
            pickSkipOrdersButton.setOnClickListener(this);
        }

        checkNewOrdersButton = findViewById(R.id.checkNewOrdersButton);
        if (checkNewOrdersButton != null) {
            checkNewOrdersButton.setTypeface(font);
            checkNewOrdersButton.setOnClickListener(this);
        }

        pickItemArray = new ArrayList<OrderItemListModel>();
        skipOrderIdArray = new ArrayList<String>();

        orderItemListView = findViewById(R.id.orderItemListView);
        // Below method to clear focus
        orderItemListView.setRecyclerListener(new AbsListView.RecyclerListener() {
            @Override
            public void onMovedToScrapHeap(View view) {
                if (view.hasFocus()) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });

        actionLayoutInflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup) actionLayoutInflater.inflate(R.layout.action_button_layout, orderItemListView, false);

        pickButton = footer.findViewById(R.id.pickButton);
        pickButton.setOnClickListener(this);
        pickButton.setTypeface(font);
        pickButton.setEnabled(false);
        pickButton.setBackgroundResource(R.drawable.button_light_background);

        skipButton = footer.findViewById(R.id.skipButton);
        skipButton.setOnClickListener(this);
        skipButton.setTypeface(font);

        partialPickButton = footer.findViewById(R.id.partialPickButton);
        partialPickButton.setOnClickListener(this);
        partialPickButton.setTypeface(font);
        partialPickButton.setEnabled(false);
        partialPickButton.setBackgroundResource(R.drawable.button_light_background);

        orderItemListView.addFooterView(footer, null, false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tracking_number = bundle.getString("tracking_number");
            is_from_order_list = bundle.getBoolean("is_from_order_list");
            total_assigned_orders = Integer.parseInt(bundle.getString("count"));
            type = bundle.getString("type");
            display_type = bundle.getString("display_type");
        }

        if (is_from_order_list) {
            if (!connectionDetector.hasConnection()) {
                showCustomToastMessage(
                        "Please Check Your Internet Connection", true);
            } else {
                getRequestedOrderDetail(tracking_number);
            }
        } else {
            if (!connectionDetector.hasConnection()) {
                showCustomToastMessage(
                        "Please Check Your Internet Connection", true);
            } else {
                getNextOrderDetail();
            }
        }
    }

    private void setEmptyWallMessage(String text) {
        emptyWallMessageTextView.setVisibility(View.VISIBLE);
        emptyWallMessageTextView.setText(text);
    }

    // Method to call requested order detail web-service
    private void getRequestedOrderDetail(String trackingNumber) {
        Bugfender.d("DEBUG", "Get Requested Order Detail API request submitted : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        showProgressDialog("Loading assigned order details...");
        String requestUrl = PreferenceStore.getBaseURL(getApplicationContext()) + "/pickuporderdetail"
                + "/" + trackingNumber;

        Bugfender.d("DEBUG", "Get Requested Order Detail API URL : " + requestUrl + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,
                requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String errorString = Commons.ResponseValidator
                        .validate(response);

                if (errorString.length() > 0) {
                    Bugfender.d("DEBUG", "Get Requested Order Detail API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    total_assigned_orders = 0;
                    lblCount.setText(""
                            + total_assigned_orders);

                    headerTextView.setText("");
                    parentLinearLayout.setVisibility(View.GONE);
                    if (pickByOrderAdapter != null) {
                        pickByOrderAdapter.clear();
                    }

                    if (total_assigned_orders != 0) {
                        setEmptyWallMessage("You have skipped "
                                + String.valueOf(total_assigned_orders)
                                + " orders");
                        pickSkipOrdersButton
                                .setText("Pick Skipped Orders");
                        checkNewOrdersButton.setVisibility(View.VISIBLE);
                        checkNewOrdersButton.setText("Check New Orders");
                    } else {
                        sendBackToMainMenu();
                    }
                    actionButtonLayout.setVisibility(View.VISIBLE);
                } else {
                    Bugfender.d("DEBUG", "Get Requested Order Detail API response got success : " + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    lblCount.setText(""
                            + total_assigned_orders);

                    parseResponse(response);
                }
                closeProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Get Requested Order Detail API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                closeProgressDialog();

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null
                        && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    showCustomToastMessageWithLength(
                            "Sorry, You Are Not Authorize To Access This Action.",
                            true, "long");
                    if (!connectionDetector.hasConnection()) {
                        showCustomToastMessage("Please Check Your Internet Connection", true);
                    } else {
                        // Do logout while server will say status code 401
                        logoutUser();
                    }
                } else {
                    lblCount.setText("0");
                    headerTextView.setText("");
                    parentLinearLayout.setVisibility(View.GONE);
                    if (pickByOrderAdapter != null) {
                        pickByOrderAdapter.clear();
                    }

                    sendBackToMainMenu();
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

    // Method to call next assigned order detail web-service
    private void getNextOrderDetail() {
        Bugfender.d("DEBUG", "Get Assigned Order Detail API request submitted : " + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        showProgressDialog("Loading assigned order details...");
        String orderUrl = "";
        String ids = "";

        if (skipOrderIdArray.size() != 0) {
            ids = TextUtils.join(",", skipOrderIdArray);
            orderUrl = PreferenceStore.getBaseURL(getApplicationContext())
                    + "/next_assigned_order"
                    + "?skip_assigned_orders_id=" + ids + "&type=" + type;
        } else {
            orderUrl = PreferenceStore.getBaseURL(getApplicationContext())
                    + "/next_assigned_order?type=" + type;
        }

        Bugfender.d("DEBUG", "Get Assigned Order Detail API URL : " + orderUrl + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,
                orderUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String errorString = Commons.ResponseValidator
                        .validate(response);

                if (errorString.length() > 0) {
                    Bugfender.d("DEBUG", "Get Assigned Order Detail API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    try {
                        total_assigned_orders = (Integer) response
                                .get("total_assigned");
                        lblCount.setText(String
                                .valueOf(total_assigned_orders));
                    } catch (JSONException e) {
                        Bugfender.e("ERROR", "Get Assigned Order Detail API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                    }

                    headerTextView.setText("");
                    parentLinearLayout.setVisibility(View.GONE);
                    if (pickByOrderAdapter != null) {
                        pickByOrderAdapter.clear();
                    }

                    if (total_assigned_orders != 0) {
                        setEmptyWallMessage("You have skipped "
                                + String.valueOf(total_assigned_orders)
                                + " orders");
                        pickSkipOrdersButton
                                .setText("Pick Skipped Orders");
                        checkNewOrdersButton.setVisibility(View.VISIBLE);
                        checkNewOrdersButton.setText("Check New Orders");
                    } else {
                        sendBackToMainMenu();
                    }
                    actionButtonLayout.setVisibility(View.VISIBLE);
                } else {
                    Bugfender.d("DEBUG", "Get Assigned Order Detail API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    try {
                        total_assigned_orders = (Integer) response
                                .get("total_assigned");
                        lblCount.setText(String
                                .valueOf(total_assigned_orders));

                        parseResponse(response);
                    } catch (JSONException e) {
                        Bugfender.e("ERROR", "Get Assigned Order Detail API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                        closeProgressDialog();
                    }
                }
                closeProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Get Assigned Order Detail API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

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
                    lblCount.setText("0");
                    headerTextView.setText("");
                    parentLinearLayout.setVisibility(View.GONE);
                    if (pickByOrderAdapter != null) {
                        pickByOrderAdapter.clear();
                    }

                    sendBackToMainMenu();
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

    // Method to parse order detail response
    public void parseResponse(JSONObject response) {
        try {
            boolean status = (Boolean) response.get("success");
            if (status) {
                parentLinearLayout.setVisibility(View.VISIBLE);
                emptyWallMessageTextView.setVisibility(View.GONE);
                actionButtonLayout.setVisibility(View.GONE);

                JSONObject json = new JSONObject(response.toString());

                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();

                orderModel = gson.fromJson(json.getJSONObject("order").toString(), OrderModel.class);

                if (orderModel.getState().equalsIgnoreCase("assign_for_repicking") || orderModel.getState().equalsIgnoreCase("assign_for_pickup") || orderModel.getState().equalsIgnoreCase("partial_picked")) {
                    headerTextView.setText(display_type + " >> " + orderModel.getOrder_number());

                    if (orderModel.getState().equalsIgnoreCase("assign_for_pickup") && orderModel.getIs_count_mismatch().equalsIgnoreCase("true")) {
                        clickHereLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        clickHereLinearLayout.setVisibility(View.GONE);
                    }

                    orderDeliveryDate.setText("Delivery Date : "
                            + Commons.DateConverter.formatedDate(orderModel
                            .getDelivery_date()));

                    if (orderModel.getExpress_delivery().equalsIgnoreCase("true")) {
                        expressDeliveryImageView.setVisibility(View.VISIBLE);
                        expressDeliveryImageView.setImageResource(R
                                .drawable
                                .ic_express);
                    } else {
                        expressDeliveryImageView.setVisibility(View.INVISIBLE);
                    }

                    carrierTextView.setText("Shipping Carrier : " + orderModel.getCarrier());
                    sourceTextView.setText("Source : " + orderModel.getSource());

                    if (orderModel.getState()
                            .equalsIgnoreCase("assign_for_repicking") || orderModel.getState()
                            .equalsIgnoreCase("partial_picked")) {
                        List<BinModel> bins = new ArrayList<BinModel>();
                        List<String> all_bin_numbers = new ArrayList<String>();
                        bins = orderModel.getBins();
                        if (bins.size() != 0) {
                            for (BinModel bin : bins) {
                                all_bin_numbers.add(bin.getBinNumber());
                                if (pickBinEditText != null) {
                                    pickBinEditText.setText(TextUtils.join(", ",
                                            all_bin_numbers));
                                    pickBinEditText.setEnabled(false);
                                }
                            }
                        } else {
                            if (pickBinEditText != null) {
                                pickBinEditText.setText("");
                            }
                        }

                        if (orderModel.getState()
                                .equalsIgnoreCase("assign_for_repicking")) {
                            if (pickBinEditText != null) {
                                pickBinEditText.setEnabled(false);
                            }
                        }
                    } else {
                        if (pickBinEditText != null) {
                            try {
                                pickBinEditText.setEnabled(true);
                                pickBinEditText.setText("");
                                pickBinEditText.requestFocus();
                            } catch (Exception e) {
                                Bugfender.e("ERROR", "Order Detail view got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                            }
                        }
                    }

                    pickingPreference = orderModel.getPickingPreference();

                    orderItemArray = new ArrayList<OrderItemListModel>();
                    if (pickingPreference != null && pickingPreference.equalsIgnoreCase("scan")) {
                        orderQtyArray = new ArrayList<String>();
                        pickedQtyArray = new ArrayList<String>();
                        actualPickedQtyArray = new ArrayList<String>();

                        for (OrderItemListModel orderItemListModel : orderModel
                                .getOrder_item_lists()) {
                            orderItemArray.add(orderItemListModel);
                            orderQtyArray.add(orderItemListModel.getQuantity());
                            String pQty = (orderItemListModel.getPicked_quantity().equalsIgnoreCase("") ? "0" : orderItemListModel.getPicked_quantity());
                            pickedQtyArray.add(pQty);
                            actualPickedQtyArray.add(pQty);
                        }
                    } else {
                        checkList = new ArrayList<Boolean>();
                        for (OrderItemListModel orderItemListModel : orderModel
                                .getOrder_item_lists()) {
                            orderItemArray.add(orderItemListModel);
                            String pQty = (orderItemListModel.getPicked_quantity().equalsIgnoreCase("") ? "0" : orderItemListModel.getPicked_quantity());
                            if (orderItemListModel.getQuantity().equalsIgnoreCase(pQty)) {
                                checkList.add(true);
                            } else {
                                checkList.add(false);
                            }
                        }
                    }

                    if (pickingPreference != null && pickingPreference.equalsIgnoreCase("scan")) {
                        if (orderModel != null && orderModel.getIs_usn_base().equalsIgnoreCase("true")) {
                            pickByOrderAdapter = new PickByOrderAdapter(
                                    PickByOrderActivity.this, this, pickingPreference, true, orderQtyArray, pickedQtyArray);
                        } else {
                            pickByOrderAdapter = new PickByOrderAdapter(
                                    PickByOrderActivity.this, this, pickingPreference, false, orderQtyArray, pickedQtyArray);
                        }
                    } else {
                        if (orderModel != null && orderModel.getIs_usn_base().equalsIgnoreCase("true")) {
                            pickByOrderAdapter = new PickByOrderAdapter(
                                    PickByOrderActivity.this, this, pickingPreference, true, checkList, orderItemArray);
                        } else {
                            pickByOrderAdapter = new PickByOrderAdapter(
                                    PickByOrderActivity.this, this, pickingPreference, false, checkList, orderItemArray);
                        }
                    }

                    orderItemListView.setAdapter(pickByOrderAdapter);

                    for (OrderItemListModel orderItemListModel : orderModel
                            .getOrder_item_lists()) {
                        pickByOrderAdapter.add(orderItemListModel);
                    }

                    pickButton.setEnabled(false);
                    pickButton.setBackgroundResource(R.drawable.button_light_background);

                    partialPickButton.setEnabled(false);
                    partialPickButton.setBackgroundResource(R.drawable.button_light_background);
                } else {
                    closeProgressDialog();

                    clickHereLinearLayout.setVisibility(View.GONE);

                    showErrorDialog("This Order Is Currently Not In Picking Line / Moved Away From Here. Please Put Back The Items & Move To Next Order");
                }
            } else {
                headerTextView.setText("");
                parentLinearLayout.setVisibility(View.GONE);
                clickHereLinearLayout.setVisibility(View.GONE);
                if (pickByOrderAdapter != null) {
                    pickByOrderAdapter.clear();
                }

                if (total_assigned_orders != 0) {
                    setEmptyWallMessage("You have skipped "
                            + String.valueOf(total_assigned_orders) + " orders");
                    pickSkipOrdersButton.setText("Pick Skipped Orders");
                    checkNewOrdersButton.setVisibility(View.VISIBLE);
                    checkNewOrdersButton.setText("Check New Orders");
                } else {
                    sendBackToMainMenu();
                }

                actionButtonLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Bugfender.e("ERROR", "Order Detail API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

            closeProgressDialog();

            lblCount.setText("0");
            headerTextView.setText("");
            parentLinearLayout.setVisibility(View.GONE);
            if (pickByOrderAdapter != null) {
                pickByOrderAdapter.clear();
            }

            onBackPressed();
        }

        if (pickByOrderAdapter != null) {
            pickByOrderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        Intent pickingOrdersIntent = new Intent(
                PickByOrderActivity.this,
                PickingOrderListActivity.class);
        pickingOrdersIntent.putExtra("type", type);
        pickingOrdersIntent.putExtra("display_type", display_type);
        startActivity(pickingOrdersIntent);
    }

    public void sendBackToMainMenu() {
        Intent intent = new Intent(
                PickByOrderActivity.this,
                MainMenuActivity.class);
        startActivity(intent);
    }

    // Method to enable Pick button after adapter method called
    public void enablePickButton() {
        pickButton.setEnabled(true);
        pickButton.setBackgroundResource(R.drawable.button_red_background);
    }

    // Method to disable Pick button after adapter method called
    public void disablePickButton() {
        pickButton.setEnabled(false);
        pickButton.setBackgroundResource(R.drawable.button_light_background);
    }

    // Method to enable Partial Picked button after adapter method called
    public void enablePartialPickButton() {
        partialPickButton.setEnabled(true);
        partialPickButton.setBackgroundResource(R.drawable.button_red_background);
    }

    // Method to disable Partial Picked button after adapter method called
    public void disablePartialPickButton() {
        partialPickButton.setEnabled(false);
        partialPickButton.setBackgroundResource(R.drawable.button_light_background);
    }

    @SuppressLint("SetTextI18n")
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

            final TextView confirmationTextView = promptView
                    .findViewById(R.id.promptTextView);
            confirmationTextView.setTypeface(font);
            confirmationTextView.setText("Do you really want to logout?");

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    if (!connectionDetector.hasConnection()) {
                                        showCustomToastMessage(
                                                "Please Check Your Internet Connection", true);
                                    } else {
                                        // if this button is clicked, close
                                        // and do logout
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
        } else if (v == skipButton) {
            promptView = layoutInflater.inflate(R.layout.prompt, null);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set prompts.xml to alert dialog builder
            alertDialogBuilder.setView(promptView);

            final TextView confirmationTextView = promptView
                    .findViewById(R.id.promptTextView);
            confirmationTextView.setTypeface(font);
            confirmationTextView
                    .setText("Do you want to skip this order for now?");

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    /*
                                     * clear the array list for
                                     * selectedOrderItemListArray when we skip
                                     * the order and add into skipOrderIdArray
                                     */

                                    if (pickByOrderAdapter.selectedOrderItemListArray != null) {
                                        pickByOrderAdapter.selectedOrderItemListArray
                                                .clear();
                                    }

                                    if (!skipOrderIdArray.contains(orderModel
                                            .getId())) {
                                        skipOrderIdArray.add(orderModel.getId());
                                    }

                                    if (!connectionDetector.hasConnection()) {
                                        showCustomToastMessage(
                                                "Please Check Your Internet Connection", true);
                                    } else {
                                        getNextOrderDetail();
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
            skipOrderAlertDialog = alertDialogBuilder.create();

            // show it
            skipOrderAlertDialog.show();

            Button plusBtn = skipOrderAlertDialog
                    .getButton(Dialog.BUTTON_POSITIVE);
            plusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_large));
            plusBtn.setTypeface(font);

            Button minusBtn = skipOrderAlertDialog
                    .getButton(Dialog.BUTTON_NEGATIVE);
            minusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_large));
            minusBtn.setTypeface(font);
        } else if (v == pickSkipOrdersButton) {
            skipOrderIdArray.clear();

            if (!connectionDetector.hasConnection()) {
                showCustomToastMessage(
                        "Please Check Your Internet Connection", true);
            } else {
                getNextOrderDetail();
            }
        } else if (v == checkNewOrdersButton) {
            Intent pickingOrdersIntent = new Intent(
                    PickByOrderActivity.this,
                    PickingOrderListActivity.class);
            pickingOrdersIntent.putExtra("type", type);
            pickingOrdersIntent.putExtra("display_type", display_type);
            startActivity(pickingOrdersIntent);
        } else if (v == partialPickButton) {
            promptView = layoutInflater.inflate(R.layout.prompt, null);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set prompts.xml to alert dialog builder
            alertDialogBuilder.setView(promptView);

            final TextView confirmationTextView = promptView
                    .findViewById(R.id.promptTextView);
            confirmationTextView.setTypeface(font);
            confirmationTextView
                    .setText("Do you want to partial pick this order?");

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    if (pickingPreference != null && pickingPreference.equalsIgnoreCase("scan")) {
                                        if (orderModel != null && orderModel.getIs_usn_base().equalsIgnoreCase("true")) {
                                            getScanDataForUSNBase("Partial Pick");
                                        } else {
                                            getScanDataForSKUBase("Partial Pick");
                                        }
                                    } else {
                                        getInputData("Partial Pick");
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
            partialPickOrderAlertDialog = alertDialogBuilder.create();

            // show it
            partialPickOrderAlertDialog.show();

            Button plusBtn = partialPickOrderAlertDialog
                    .getButton(Dialog.BUTTON_POSITIVE);
            plusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_large));
            plusBtn.setTypeface(font);

            Button minusBtn = partialPickOrderAlertDialog
                    .getButton(Dialog.BUTTON_NEGATIVE);
            minusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_large));
            minusBtn.setTypeface(font);
        } else if (v == pickButton) {
            promptView = layoutInflater.inflate(R.layout.prompt, null);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set prompts.xml to alert dialog builder
            alertDialogBuilder.setView(promptView);

            final TextView confirmationTextView = promptView
                    .findViewById(R.id.promptTextView);
            confirmationTextView.setTypeface(font);
            confirmationTextView
                    .setText("Do you want to pick this order?");

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    if (pickingPreference != null && pickingPreference.equalsIgnoreCase("scan")) {
                                        if (orderModel != null && orderModel.getIs_usn_base().equalsIgnoreCase("true")) {
                                            getScanDataForUSNBase("Pick");
                                        } else {
                                            getScanDataForSKUBase("Pick");
                                        }
                                    } else {
                                        getInputData("Pick");
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
            pickOrderAlertDialog = alertDialogBuilder.create();

            // show it
            pickOrderAlertDialog.show();

            Button plusBtn = pickOrderAlertDialog
                    .getButton(Dialog.BUTTON_POSITIVE);
            plusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_large));
            plusBtn.setTypeface(font);

            Button minusBtn = pickOrderAlertDialog
                    .getButton(Dialog.BUTTON_NEGATIVE);
            minusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                    .getDimension(R.dimen.text_size_large));
            minusBtn.setTypeface(font);
        }
    }

    private void getScanDataForSKUBase(String action) {
        JSONObject pickItemJson = new JSONObject();
        pickedQtyArray = pickByOrderAdapter.pickedQtyArray;

        int loopIndex = 0;
        for (String qty : pickedQtyArray) {
            OrderItemListModel orderItemListModel = orderItemArray.get(loopIndex);

            String oQty = orderQtyArray.get(loopIndex);
            String pQty = actualPickedQtyArray.get(loopIndex);

            if (!oQty.equals(pQty)) {
                try {
                    pickItemJson.put(orderItemListModel.getId(), qty);
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
            loopIndex++;
        }

        JSONObject pickOrderJson = new JSONObject();
        try {
            pickOrderJson.put("items", pickItemJson);
            pickOrderJson.put("state", orderModel.getState());
            pickOrderJson.put("tracking_number", orderModel.getTracking_number());
            pickOrderJson.put("order_id", orderModel.getId());
            pickOrderJson.put("picking_preference", pickingPreference);

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

                pickOrderJson.put("bin_numbers", new JSONArray(
                        uniqueBinNumbersArray));
            }
        } catch (JSONException e) {
            e.getMessage();
        }

        if (!connectionDetector.hasConnection()) {
            showCustomToastMessage(
                    "Please Check Your Internet Connection", true);
        } else {
            // call web-service to update status to Picked
            pickOrderItem(pickOrderJson, action);
        }
    }

    private void getScanDataForUSNBase(String action) {
        JSONObject pickItemJson = new JSONObject();
        pickedQtyArray = pickByOrderAdapter.pickedQtyArray;
        allUSNList = pickByOrderAdapter.allUSNList;

        int loopIndex = 0;
        for (String qty : pickedQtyArray) {
            OrderItemListModel orderItemListModel = orderItemArray.get(loopIndex);

            String oQty = orderQtyArray.get(loopIndex);
            String pQty = actualPickedQtyArray.get(loopIndex);

            if (!oQty.equals(pQty)) {
                try {
                    String USNList = allUSNList.get(loopIndex);
                    pickItemJson.put(orderItemListModel.getId(), USNList);
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
            loopIndex++;
        }

        JSONObject pickOrderJson = new JSONObject();
        try {
            pickOrderJson.put("items", pickItemJson);
            pickOrderJson.put("state", orderModel.getState());
            pickOrderJson.put("tracking_number", orderModel.getTracking_number());
            pickOrderJson.put("order_id", orderModel.getId());
            pickOrderJson.put("picking_preference", pickingPreference);

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

                pickOrderJson.put("bin_numbers", new JSONArray(
                        uniqueBinNumbersArray));
            }
        } catch (JSONException e) {
            e.getMessage();
        }

        if (!connectionDetector.hasConnection()) {
            showCustomToastMessage(
                    "Please Check Your Internet Connection", true);
        } else {
            // call web-service to update status to Picked
            pickOrderItem(pickOrderJson, action);
        }
    }

    private void getInputData(String action) {
        JSONObject pickItemJson = new JSONObject();
        pickItemArray = pickByOrderAdapter.selectedOrderItemListArray;

        if (action.equalsIgnoreCase("Pick")) {
            if (pickItemArray.size() != checkList.size()) {
                showCustomToastMessage("Not All Products Are Picked Yet", true);
                return;
            }
        }

        for (OrderItemListModel orderItemListModel : pickItemArray) {
            try {
                pickItemJson.put(orderItemListModel.getId(), orderItemListModel.getQuantity());
            } catch (JSONException e) {
                e.getMessage();
            }
        }

        JSONObject pickOrderJson = new JSONObject();
        try {
            pickOrderJson.put("items", pickItemJson);
            pickOrderJson.put("state", orderModel.getState());
            pickOrderJson.put("tracking_number", orderModel.getTracking_number());
            pickOrderJson.put("order_id", orderModel.getId());
            pickOrderJson.put("picking_preference", pickingPreference);

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

                pickOrderJson.put("bin_numbers", new JSONArray(
                        uniqueBinNumbersArray));
            }
        } catch (JSONException e) {
            e.getMessage();
        }

        if (!connectionDetector.hasConnection()) {
            showCustomToastMessage(
                    "Please Check Your Internet Connection", true);
        } else {
            // call web-service to update status to Picked
            pickOrderItem(pickOrderJson, action);
        }
    }

    // Method to call pick order item status webservice
    public void pickOrderItem(JSONObject pickedItemJson, String action) {
        Bugfender.d("DEBUG", "Pick Order API request body : " + pickedItemJson + " for " + action + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        Bugfender.d("DEBUG", "Pick Order API request submitted : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        showProgressDialog();
        String requestURL = "";

        if (action.equalsIgnoreCase("Pick")) {
            requestURL = PreferenceStore.getBaseURL(getApplicationContext())
                    + "/order_pickup_lists/picked_up_bulk";
        } else if (action.equalsIgnoreCase("Partial Pick")) {
            requestURL = PreferenceStore.getBaseURL(getApplicationContext())
                    + "/order_pickup_lists/partial_picked_bulk";
        }

        Bugfender.d("DEBUG", "Pick Order API URL : " + requestURL + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST,
                requestURL, pickedItemJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String errorString = Commons.ResponseValidator
                                .validate(response);

                        if (errorString.length() > 0) {
                            Bugfender.d("DEBUG", "Pick Order API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            closeProgressDialog();
                            if (errorString
                                    .equalsIgnoreCase("Order Not Found In Picking Line Or Order " +
                                            "Is Already Picked")) {
                                showErrorDialog("This Order Is Currently Not In Picking Line / Moved Away From Here. Please Put Back The Items & Move To Next Order");
                            } else {
                                showCustomToastMessage(errorString, true);
                            }
                        } else {
                            Bugfender.d("DEBUG", "Pick Order API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            try {
                                boolean status = (Boolean) response
                                        .get("success");
                                if (status) {
                                    closeProgressDialog();
                                    try {
                                        showCustomToastMessage(
                                                response.getString("info"),
                                                false);

                                        if (!connectionDetector.hasConnection()) {
                                            showCustomToastMessage(
                                                    "Please Check Your Internet Connection", true);
                                        } else {
                                            getNextOrderDetail();
                                        }
                                    } catch (JSONException e) {
                                        Bugfender.e("ERROR", "Pick Order API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                                    }
                                }
                            } catch (JSONException e) {
                                Bugfender.e("ERROR", "Pick Order API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                            }
                        }
                        closeProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Pick Order API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

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

    private void showErrorDialog(String message) {
        promptView = layoutInflater.inflate(
                R.layout.prompt, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog
                .Builder(
                PickByOrderActivity.this);

        // set prompts.xml to alert dialog builder
        alertDialogBuilder.setView(promptView);

        final TextView confirmationTextView = promptView
                .findViewById(R.id.promptTextView);
        confirmationTextView.setTypeface(font);
        confirmationTextView
                .setText(message);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int id) {
                                if (!connectionDetector.hasConnection()) {
                                    showCustomToastMessage(
                                            "Please Check Your Internet Connection", true);
                                } else {
                                    getNextOrderDetail();
                                }
                            }
                        });

        // create alert dialog
        alreadyProcessedOrderAlertDialog = alertDialogBuilder
                .create();

        // show it
        alreadyProcessedOrderAlertDialog.show();

        Button plusBtn = alreadyProcessedOrderAlertDialog
                .getButton(Dialog.BUTTON_POSITIVE);
        plusBtn.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(
                        R.dimen.text_size_large));
        plusBtn.setTypeface(font);
    }

    // Method to resolve order item locations
    private void resolveOrder() {
        Bugfender.d("DEBUG", "Resolve Order API request submitted : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        showProgressDialog();
        String requestURL = "";

        requestURL = PreferenceStore.getBaseURL(getApplicationContext())
                + "/order_pickup_lists/resolve_problem_order?order_id=" + orderModel.getId();

        Bugfender.d("DEBUG", "Resolve Order API URL : " + requestURL + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,
                requestURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String errorString = Commons.ResponseValidator.validate(response);

                        if (errorString.length() > 0) {
                            Bugfender.d("DEBUG", "Resolve Order API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            closeProgressDialog();
                            showCustomToastMessage(errorString, true);
                        } else {
                            Bugfender.d("DEBUG", "Resolve Order API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            try {
                                boolean status = (Boolean) response.get("success");
                                if (status) {
                                    closeProgressDialog();

                                    if (!connectionDetector.hasConnection()) {
                                        showCustomToastMessage("Please Check Your Internet Connection", true);
                                    } else {
                                        if (response.getString("info").equalsIgnoreCase("There Is Some Issue With This Order, You Can Skip This Order OR You Can Raise The Ticket To Support Team.")) {
                                            promptView = layoutInflater.inflate(R.layout.prompt, null);

                                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog
                                                    .Builder(
                                                    PickByOrderActivity.this);

                                            // set prompts.xml to alert dialog builder
                                            alertDialogBuilder.setView(promptView);

                                            final TextView confirmationTextView = promptView
                                                    .findViewById(R.id.promptTextView);
                                            confirmationTextView.setTypeface(font);
                                            confirmationTextView
                                                    .setText(response.getString("info"));

                                            // set dialog message
                                            alertDialogBuilder
                                                    .setCancelable(false)
                                                    .setPositiveButton(
                                                            "Skip Order",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(
                                                                        DialogInterface dialog,
                                                                        int id) {
                                                                    if (!connectionDetector.hasConnection()) {
                                                                        showCustomToastMessage(
                                                                                "Please Check Your Internet Connection", true);
                                                                    } else {
                                                                        getNextOrderDetail();
                                                                    }
                                                                }
                                                            })
                                                    .setNeutralButton("Raise Ticket",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog,
                                                                                    int id) {
                                                                    createTicket("Total Order Item Quantity and Total Item Location Quantity is Mismatched.");
                                                                }
                                                            })
                                                    .setNegativeButton("Cancel",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog,
                                                                                    int id) {
                                                                    // if this button is clicked, just close
                                                                    // the dialog box and do nothing
                                                                    dialog.cancel();
                                                                }
                                                            });

                                            // create alert dialog
                                            errorResolveOrderAlertDialog = alertDialogBuilder
                                                    .create();

                                            // show it
                                            errorResolveOrderAlertDialog.show();

                                            Button plusBtn = errorResolveOrderAlertDialog
                                                    .getButton(Dialog.BUTTON_POSITIVE);
                                            plusBtn.setTextSize(
                                                    TypedValue.COMPLEX_UNIT_PX,
                                                    getResources().getDimension(
                                                            R.dimen.text_size_medium));
                                            plusBtn.setTypeface(font);

                                            Button neutralBtn = errorResolveOrderAlertDialog
                                                    .getButton(Dialog.BUTTON_NEUTRAL);
                                            neutralBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                                                    .getDimension(R.dimen.text_size_medium));
                                            neutralBtn.setTypeface(font);

                                            Button minusBtn = errorResolveOrderAlertDialog
                                                    .getButton(Dialog.BUTTON_NEGATIVE);
                                            minusBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                                                    .getDimension(R.dimen.text_size_medium));
                                            minusBtn.setTypeface(font);
                                        } else {
                                            showCustomToastMessage(response.getString("info"), false);

                                            getRequestedOrderDetail(orderModel.getTracking_number());
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                Bugfender.e("ERROR", "Resolve Order API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                            }
                        }
                        closeProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Resolve Order API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                showCustomToastMessage("Error = " + error.getMessage(), true);

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

    // Method to create ticket on help channel
    private void createTicket(String errorMessage) {
        JSONObject json = new JSONObject();
        try {
            json.put("user_email", PreferenceStore.getUserModel(getApplicationContext()).getEmail());
            json.put("error_message", errorMessage);
            json.put("order_id", orderModel.getId());
            json.put("app_name", "eWMS Picking");
        } catch (JSONException e) {
            e.getMessage();
        }

        if (!connectionDetector.hasConnection()) {
            showCustomToastMessage(
                    "Please Check Your Internet Connection", true);
        } else {
            Bugfender.d("DEBUG", "Raise Ticket API request body : " + json + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

            Bugfender.d("DEBUG", "Raise Ticket API request submitted : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

            showProgressDialog();
            String requestURL = "";

            requestURL = PreferenceStore.getBaseURL(getApplicationContext())
                    + "/orders/create_ticket";

            Bugfender.d("DEBUG", "Raise Ticket API URL : " + requestURL + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

            JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST,
                    requestURL, json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String errorString = Commons.ResponseValidator
                                    .validate(response);

                            if (errorString.length() > 0) {
                                Bugfender.d("DEBUG", "Raise Ticket API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                                closeProgressDialog();
                                showCustomToastMessage(errorString, true);
                            } else {
                                Bugfender.d("DEBUG", "Raise Ticket API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                                try {
                                    boolean status = (Boolean) response
                                            .get("success");
                                    if (status) {
                                        closeProgressDialog();
                                        showCustomToastMessage(response.getString("info"), false);

                                        if (!connectionDetector.hasConnection()) {
                                            showCustomToastMessage(
                                                    "Please Check Your Internet Connection", true);
                                        } else {
                                            getNextOrderDetail();
                                        }
                                    }
                                } catch (JSONException e) {
                                    Bugfender.e("ERROR", "Raise Ticket API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                                }
                            }
                            closeProgressDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Bugfender.e("ERROR", "Raise Ticket API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    closeProgressDialog();
                    showCustomToastMessage("Error = " + error.getMessage(), true);

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
}
