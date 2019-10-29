package com.fbapicking.controller.activity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bugfender.sdk.Bugfender;
import com.fbapicking.utility.ApplicationController;
import com.fbapicking.utility.Commons;
import com.fbapicking.utility.ConnectionDetector;
import com.fbapicking.utility.PreferenceStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.fbapicking.R;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainMenuActivity extends BaseActivity implements OnClickListener {
    Button buttonPickByOrder, buttonPartialPickByOrder, buttonRePickByOrder,
            buttonPickByProducts, buttonPickByLocations, buttonPickByCategory;
    Intent mainIntent;
    ImageButton logoutButton, lblBack;
    TextView lblHeader;
    Typeface font;
    LayoutInflater layoutInflater;
    View promptView;
    AlertDialog logoutAlertDialog;
    ConnectionDetector connectionDetector;
    String pickingCount = "0";
    String rePickingCount = "0";
    String partialPickedCount = "0";
    String pickByProductsCount = "0";
    String pickByLocationsCount = "0";
    String pickByCategoryCount = "0";

    // Default Method: to create the activity (i.e. to start activity)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        initializeComponents();
    }

    public void initializeComponents() {
        connectionDetector = new ConnectionDetector(getApplicationContext());

        setUpActionBar();

        font = Typeface.createFromAsset(getAssets(), "fonts/Museo300Regular.otf");

        layoutInflater = LayoutInflater.from(MainMenuActivity.this);

        logoutButton = (ImageButton) findViewById(R.id.lblSetting);
        if (logoutButton != null) {
            logoutButton.setBackgroundResource(R.drawable.ic_menu);
            logoutButton.setScaleX(0.7f);
            logoutButton.setScaleY(0.8f);
            logoutButton.setOnClickListener(this);
        }

        lblHeader = (TextView) findViewById(R.id.lblHeader);
        if (lblHeader != null) {
            lblHeader.setText(getResources().getString(R.string.appTitle));
            lblHeader.setTypeface(font, Typeface.BOLD);
        }

        lblBack = (ImageButton) findViewById(R.id.lblBack);
        if (lblBack != null) {
            lblBack.setBackgroundResource(R.drawable.ic_refresh);
            lblBack.setScaleX(0.8f);
            lblBack.setScaleY(1.0f);
            lblBack.setOnClickListener(this);
        }

        buttonPickByOrder = (Button) findViewById(R.id.buttonPickByOrder);
        if (buttonPickByOrder != null) {
            buttonPickByOrder.setOnClickListener(this);
            buttonPickByOrder.setTypeface(font, Typeface.BOLD);
            buttonPickByOrder.setText("Pick By Order (0)");
        }

        buttonPartialPickByOrder = (Button) findViewById(R.id.buttonPartialPickByOrder);
        if (buttonPartialPickByOrder != null) {
            buttonPartialPickByOrder.setOnClickListener(this);
            buttonPartialPickByOrder.setTypeface(font, Typeface.BOLD);
            buttonPartialPickByOrder.setText("Partial Pick By Order (0)");
        }

        buttonRePickByOrder = (Button) findViewById(R.id.buttonRePickByOrder);
        if (buttonRePickByOrder != null) {
            buttonRePickByOrder.setOnClickListener(this);
            buttonRePickByOrder.setTypeface(font, Typeface.BOLD);
            buttonRePickByOrder.setText("Re-Pick By Order (0)");
        }

        buttonPickByProducts = (Button) findViewById(R.id.buttonPickByProducts);
        if (buttonPickByProducts != null) {
            buttonPickByProducts.setOnClickListener(this);
            buttonPickByProducts.setTypeface(font, Typeface.BOLD);
            buttonPickByProducts.setText("Pick By Products (0)");
        }

        buttonPickByLocations = (Button) findViewById(R.id.buttonPickByLocations);
        if (buttonPickByLocations != null) {
            buttonPickByLocations.setOnClickListener(this);
            buttonPickByLocations.setTypeface(font, Typeface.BOLD);
            buttonPickByLocations.setText("Pick By Locations (0)");
        }

        buttonPickByCategory = (Button) findViewById(R.id.buttonPickByCategory);
        if (buttonPickByCategory != null) {
            buttonPickByCategory.setOnClickListener(this);
            buttonPickByCategory.setTypeface(font, Typeface.BOLD);
            buttonPickByCategory.setText("Pick By Category (0)");
        }

        getPickingCounts();
    }

    // Method to call to-do list web-service for all picking orders count
    public void getPickingCounts() {
        Bugfender.d("DEBUG", "Picking Count API request submitted : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        String orderUrl = "";
        orderUrl = PreferenceStore.getBaseURL(getApplicationContext())
                + "/order_pickup_lists/pick_orders_count";

        Bugfender.d("DEBUG","Picking Count API URL : " + orderUrl + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, orderUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String errorString = Commons.ResponseValidator
                        .validate(response);
                if (errorString.length() > 0) {
                    Bugfender.d("DEBUG", "Picking Count API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    showCustomToastMessage(
                            errorString, true);
                } else {
                    Bugfender.d("DEBUG", "Picking Count API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    try {
                        pickingCount = response.getString("picking_count");
                        rePickingCount = response.getString("re_picking_count");
                        partialPickedCount = response.getString("partial_picked_count");
                        pickByProductsCount = response.getString("pick_by_products_count");
                        pickByLocationsCount = response.getString("pick_by_locations_count");
                        pickByCategoryCount = response.getString("pick_by_category_count");

                        buttonPickByOrder.setText("Pick By Order (" + pickingCount + ")");
                        buttonPartialPickByOrder.setText("Partial Pick By Order (" + partialPickedCount + ")");
                        buttonRePickByOrder.setText("Re-Pick By Order (" + rePickingCount + ")");
                        buttonPickByProducts.setText("Pick By Products (" + pickByProductsCount + ")");
                        buttonPickByLocations.setText("Pick By Locations (" + pickByLocationsCount + ")");
                        buttonPickByCategory.setText("Pick By Category (" + pickByCategoryCount + ")");
                    } catch (JSONException e) {
                        Bugfender.e("ERROR", "Picking Count API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Picking Count API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

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

    // Method to disable back button pressed event
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void sendToOrderList(String type, String display_type) {
        mainIntent = new Intent(MainMenuActivity.this, PickingOrderListActivity.class);
        mainIntent.putExtra("type", type);
        mainIntent.putExtra("display_type", display_type);
        startActivity(mainIntent);
    }

    private void sendToProcessTypeList(String processType, String option) {
        mainIntent = new Intent(MainMenuActivity.this, ProcessTypeActivity.class);
        mainIntent.putExtra("process_type", processType);
        mainIntent.putExtra("option", option);
        startActivity(mainIntent);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonPickByOrder) {
            if (pickingCount.equalsIgnoreCase("0")) {
                showCustomToastMessage("No Order(s) To Pick", true);
            } else {
                sendToOrderList("assign_for_pickup", "Pick By Order");
            }
        } else if (v == buttonRePickByOrder) {
            if (rePickingCount.equalsIgnoreCase("0")) {
                showCustomToastMessage("No Order(s) To Re-Pick", true);
            } else {
                sendToOrderList("assign_for_repicking", "Re-Pick By Order");
            }
        } else if (v == buttonPartialPickByOrder) {
            if (partialPickedCount.equalsIgnoreCase("0")) {
                showCustomToastMessage("No Order(s) To Partial Pick", true);
            } else {
                sendToOrderList("partial_picked", "Partial Pick By Order");
            }
        } else if (v == buttonPickByProducts) {
            if (pickByProductsCount.equalsIgnoreCase("0")) {
                showCustomToastMessage("No Order(s) To Pick By Products", true);
            } else {
                sendToProcessTypeList("products", "Pick By Products");
            }
        } else if (v == buttonPickByLocations) {
            if (pickByLocationsCount.equalsIgnoreCase("0")) {
                showCustomToastMessage("No Order(s) To Pick By Locations", true);
            } else {
                sendToProcessTypeList("locations", "Pick By Locations");
            }
        } else if (v == buttonPickByCategory) {
            if (pickByCategoryCount.equalsIgnoreCase("0")) {
                showCustomToastMessage("No Order(s) To Pick By Category", true);
            } else {
                sendToProcessTypeList("category", "Pick By Category");
            }
        } else if (v == lblBack) {
            getPickingCounts();
        } else if (v == logoutButton) {
            showMenu(logoutButton);
        }
    }

    public void showMenu(View v) {
        Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenuTheme);
        PopupMenu popup = new PopupMenu(wrapper, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        openProfile();

                        return true;
                    case R.id.logout:
                        popupLogout();

                        return true;
                }
                return false;
            }
        });
        popup.inflate(R.menu.menu_main);
        popup.show();
    }

    private void openProfile() {
        Intent intent = new Intent(MainMenuActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void popupLogout() {
        promptView = layoutInflater.inflate(R.layout.prompt, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainMenuActivity.this);

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
