package com.fbapicking.controller.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bugfender.sdk.Bugfender;
import com.fbapicking.controller.adapter.OrderListAdapter;
import com.fbapicking.model.PaginationModel;
import com.fbapicking.model.order.OrderListResponseModel;
import com.fbapicking.model.order.OrderModel;
import com.fbapicking.utility.ApplicationController;
import com.fbapicking.utility.Commons;
import com.fbapicking.utility.Commons.ResponseValidator;
import com.fbapicking.utility.ConnectionDetector;
import com.fbapicking.utility.PreferenceStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.fbapicking.R;

public class PickingOrderListActivity extends BaseActivity implements
        OnClickListener, OnItemClickListener, SearchView.OnQueryTextListener, View.OnKeyListener, AbsListView.OnScrollListener {
    Typeface font;
    TextView orderListTitleTextView, orderNoTextView, sellerNameTextView,
            emptyWallMessageTextView;
    ImageButton logoutButton, lblBack;
    LinearLayout parentLayoutTodo;
    List<OrderModel> orderModelArrayList;
    ListView orderListView;
    OrderListAdapter orderListAdapter;
    OrderListResponseModel orderResponse;
    LayoutInflater layoutInflater;
    View promptView;
    AlertDialog logoutAlertDialog;
    LinearLayout listLinearLayout, header_todoList;
    ConnectionDetector connectionDetector;
    String type, display_type;
    SearchView searchView;
    int currentPage = 1;
    int perPage = 25;
    int totalPages, totalCount;
    View progressBarFooter;

    // Default Method: to create the fragment (i.e. to start activity)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picking_order_list);

        initializeComponents();
    }

    public void initializeComponents() {
        connectionDetector = new ConnectionDetector(getApplicationContext());

        setUpActionBar();

        font = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "fonts/Museo300Regular.otf");

        layoutInflater = LayoutInflater.from(PickingOrderListActivity.this);

        lblBack = (ImageButton) findViewById(R.id.lblBack);
        if (lblBack != null) {
            lblBack.setVisibility(View.VISIBLE);
            lblBack.setOnClickListener(this);
        }

        logoutButton = (ImageButton) findViewById(R.id.lblSetting);
        if (logoutButton != null)
            logoutButton.setOnClickListener(this);

        header_todoList = (LinearLayout) findViewById(R.id.header_todolist);
        listLinearLayout = (LinearLayout) findViewById(R.id.listLinearLayout);

        orderListTitleTextView = (TextView) findViewById(R.id.lblHeader);
        if (orderListTitleTextView != null) {
            orderListTitleTextView.setTypeface(font, Typeface.BOLD);
            orderListTitleTextView.setSelected(true);
        }

        orderNoTextView = (TextView) findViewById(R.id.lblOrderNo);
        if (orderNoTextView != null) {
            orderNoTextView.setOnClickListener(this);
            orderNoTextView.setTypeface(font);
        }

        sellerNameTextView = (TextView) findViewById(R.id.lblSellerName);
        if (sellerNameTextView != null) {
            sellerNameTextView.setOnClickListener(this);
            sellerNameTextView.setTypeface(font);
        }

        emptyWallMessageTextView = (TextView) findViewById(R.id.wallmessage);
        if (emptyWallMessageTextView != null) {
            emptyWallMessageTextView.setTypeface(font, Typeface.BOLD);
            emptyWallMessageTextView.setOnClickListener(this);
        }

        searchView = (SearchView) findViewById(R.id.search);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
            searchView.setOnKeyListener(this);
            searchView.setActivated(true);
            searchView.setQueryHint("Search By Order # / Company");
            searchView.onActionViewExpanded();
            searchView.setIconified(false);
            searchView.clearFocus();
            TextView searchText = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            if (searchText != null) {
                searchText.setTypeface(font, Typeface.BOLD);
                searchText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_medium));
            }
        }

        parentLayoutTodo = (LinearLayout) findViewById(R.id.parentLayoutTodo);
        orderListView = (ListView) findViewById(R.id.orderTODOListView);
        orderListAdapter = new OrderListAdapter(PickingOrderListActivity.this,
                this);

        progressBarFooter = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.progress_bar_footer, null, false);

        ProgressBar progressBar = progressBarFooter.findViewById(R.id.progressBar);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawableProgress = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(drawableProgress, ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_dark));
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(drawableProgress));

        } else {
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_dark), PorterDuff.Mode.SRC_IN);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("type");
            display_type = extras.getString("display_type");
            orderListTitleTextView.setText(display_type);
        }

        if (!connectionDetector.hasConnection()) {
            showCustomToastMessage(
                    "Please Check Your Internet Connection", true);
        } else {
            getOrderListByType();
        }

        orderListView.setAdapter(orderListAdapter);
        orderListView.setOnItemClickListener(this);
        orderListView.setOnScrollListener(this);
    }

    // Method to disable back button pressed event
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(
                PickingOrderListActivity.this,
                MainMenuActivity.class);
        startActivity(intent);
    }

    // Method to show empty wall message
    private void setEmptyWallMessage(String wallMessage) {
        emptyWallMessageTextView.setVisibility(View.VISIBLE);
        emptyWallMessageTextView.setText(wallMessage);
    }

    // Method to hide empty wall message
    private void hideEmptyWallMessage() {
        emptyWallMessageTextView.setVisibility(View.GONE);
    }

    /**
     * Sort order list by order number ascending
     */
    private void sortByOrderNumberAsc(List<OrderModel> orderList) {
        if (orderListAdapter != null)
            orderListAdapter.clear();

        Comparator<OrderModel> comparator = new Comparator<OrderModel>() {
            @Override
            public int compare(OrderModel object1, OrderModel object2) {
                return object1.getOrder_number().compareToIgnoreCase(
                        object2.getOrder_number());
            }
        };
        Collections.sort(orderList, comparator);

        for (OrderModel orderModel : orderList) {
            orderListAdapter.add(orderModel);
        }
        orderListView.setAdapter(orderListAdapter);

        if (orderListAdapter != null)
            orderListAdapter.notifyDataSetChanged();
    }

    /**
     * Sort order list by company name ascending
     */
    private void sortByCompanyNameAsc(List<OrderModel> orderList) {
        if (orderListAdapter != null)
            orderListAdapter.clear();

        Comparator<OrderModel> comparator = new Comparator<OrderModel>() {
            @Override
            public int compare(OrderModel object1, OrderModel object2) {
                return object1.getCompany_name().compareToIgnoreCase(
                        object2.getCompany_name());
            }
        };
        Collections.sort(orderList, comparator);

        for (OrderModel orderModel : orderList) {
            orderListAdapter.add(orderModel);
        }
        orderListView.setAdapter(orderListAdapter);

        if (orderListAdapter != null)
            orderListAdapter.notifyDataSetChanged();
    }

    private void openOrderDetail(int index) {
        if (orderListAdapter != null && index < orderListAdapter.getCount()) {
            OrderModel orderModel = orderListAdapter.getItem(index);

            if (orderModel != null) {
                fillIntent(orderModel
                        .getTracking_number());
            }
        }
    }

    private void fillIntent(String trackingNumber) {
        Intent in = new Intent(
                PickingOrderListActivity.this,
                PickByOrderActivity.class);
        in.putExtra("is_from_order_list", true);
        in.putExtra("tracking_number", trackingNumber);
        in.putExtra("count", String.valueOf(totalCount));
        in.putExtra("type", type);
        in.putExtra("display_type", display_type);
        startActivity(in);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        openOrderDetail(position);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
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
        } else if (v == emptyWallMessageTextView) {
            if (!connectionDetector.hasConnection()) {
                showCustomToastMessage(
                        "Please Check Your Internet Connection", true);
            } else {
                currentPage = 1;

                getOrderListByType();
            }
        } else if (v == orderNoTextView) {
            if (orderModelArrayList != null && orderModelArrayList.size() > 0)
                sortByOrderNumberAsc(orderModelArrayList);
        } else if (v == sellerNameTextView) {
            if (orderModelArrayList != null && orderModelArrayList.size() > 0)
                sortByCompanyNameAsc(orderModelArrayList);
        }
    }

    // Method to search in adapter
    private void searching(String searchText) {
        if (orderListAdapter != null)
            orderListAdapter.clear();
        if (orderModelArrayList != null && orderModelArrayList.size() > 0) {
            int textLength = searchText.length();
            if (textLength > 0) {
                for (OrderModel orderModel :
                        orderModelArrayList) {
                    if (orderModel != null) {
                        if (textLength <= orderModel.getCompany_name().length() || textLength <= orderModel.getOrder_number().length()) {
                            if (orderModel
                                    .getCompany_name()
                                    .toUpperCase()
                                    .contains(
                                            searchText.toUpperCase()) || orderModel
                                    .getOrder_number()
                                    .toUpperCase()
                                    .contains(
                                            searchText.toUpperCase())) {
                                orderListAdapter
                                        .add(orderModel);
                            }
                        }
                    }
                }

                if (orderListAdapter != null) {
                    if (orderListAdapter.getCount() == 0) {
                        // API call to search order from server and open order detail
                        searchOrder(searchText);
                    }
                }
            } else {
                // by-default sort with company name
                sortByCompanyNameAsc(orderModelArrayList);
            }
        }
        if (orderListAdapter != null)
            orderListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        int action = event.getAction();

        if (action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER
                && view == searchView) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }

            searching(searchView.getQuery().toString());
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searching(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.equalsIgnoreCase("")) {
            searching(newText);
        }
        return false;
    }

    private void clearSearchView() {
        searchView.setQuery("", false);
        searchView.clearFocus();
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && orderListView.getLastVisiblePosition() == orderListAdapter.getCount() - 1) {
            if (currentPage < totalPages) {
                loadNextOrders();
            }
            orderListAdapter.notifyDataSetChanged();
        }
    }

    private void loadNextOrders() {
        currentPage++;

        getOrderListByType();
    }

    // Method to get assigned order list
    public void getOrderListByType() {
        Bugfender.d("DEBUG", "Get Order List API request submitted : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        if (currentPage == 1) {
            clearSearchView();
        }

        if (currentPage == 1) {
            showProgressDialog("Loading assigned orders...");
        } else {
            orderListView.addFooterView(progressBarFooter);
        }

        String orderUrl = "";
        orderUrl = PreferenceStore.getBaseURL(getApplicationContext())
                + "/assigned_orders?type=" + type + "&page=" + currentPage + "&per_page=" + perPage;

        Bugfender.d("DEBUG", "Get Order List API URL : " + orderUrl + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,
                orderUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String errorString = ResponseValidator
                        .validate(response);
                if (errorString.length() > 0) {
                    Bugfender.d("DEBUG", "Get Order List API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    if (currentPage == 1) {
                        closeProgressDialog();
                    } else {
                        orderListView.removeFooterView(progressBarFooter);
                    }

                    if (orderListAdapter != null && currentPage == 1) {
                        orderListAdapter.clear();
                        orderListAdapter.notifyDataSetChanged();

                        parentLayoutTodo.setVisibility(View.GONE);
                        setEmptyWallMessage("No Order(s) Assigned, Please click on Refresh.");
                    }
                } else {
                    Bugfender.d("DEBUG", "Get Order List API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    parseOrderListResponse(response);
                    hideEmptyWallMessage();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Get Order List API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

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
                    if (currentPage == 1) {
                        closeProgressDialog();
                    } else {
                        orderListView.removeFooterView(progressBarFooter);
                    }

                    if (orderListAdapter != null && currentPage == 1) {
                        orderListAdapter.clear();
                        orderListAdapter.notifyDataSetChanged();

                        parentLayoutTodo.setVisibility(View.GONE);
                        setEmptyWallMessage("We're sorry but something went wrong, Please click on Refresh.");
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

    // Method to parse the assigned order list response
    @SuppressLint("SetTextI18n")
    public void parseOrderListResponse(JSONObject response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());

            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.create();

            orderResponse = null;
            orderResponse = gson.fromJson(jsonObject.toString(), OrderListResponseModel.class);

            PaginationModel paginationModel = orderResponse.getPaginationModel();
            currentPage = paginationModel.getCurrent_page();
            totalPages = paginationModel.getTotal_pages();
            totalCount = paginationModel.getTotal_count();

            boolean status = (Boolean) response.get("success");

            if (status) {
                parentLayoutTodo.setVisibility(View.VISIBLE);

                if (orderListAdapter != null && currentPage == 1) {
                    orderListAdapter.clear();
                }

                if (orderResponse.getOrders().size() > 0) {
                    if (currentPage == 1) {
                        orderModelArrayList = new ArrayList<OrderModel>();
                    }

                    for (OrderModel order : orderResponse.getOrders()) {
                        orderListAdapter.add(order);
                        orderModelArrayList.add(order);
                    }

                    if (orderListAdapter.getCount() > 0) {
                        orderListTitleTextView.setText(display_type + " " + "(" + orderModelArrayList.size() + "/" + totalCount + ")");

                        listLinearLayout.setVisibility(View.VISIBLE);
                        header_todoList.setVisibility(View.VISIBLE);

                        Bugfender.d("DEBUG", "Total Orders : " + totalCount + " & " + "Loaded Orders On Device : " + orderModelArrayList.size() + " | " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                    } else {
                        if (currentPage == 1) {
                            listLinearLayout.setVisibility(View.GONE);
                            header_todoList.setVisibility(View.GONE);
                            showCustomToastMessage(
                                    "No Order(s) Assigned!", true);
                        }
                    }
                } else {
                    if (orderListAdapter != null && currentPage == 1) {
                        orderListAdapter.clear();
                        orderListAdapter.notifyDataSetChanged();

                        parentLayoutTodo.setVisibility(View.GONE);
                        setEmptyWallMessage("No Order(s) Assigned, Please click on Refresh.");
                    }
                }
            }

            if (currentPage == 1) {
                closeProgressDialog();
            } else {
                orderListView.removeFooterView(progressBarFooter);
            }
        } catch (JSONException e) {
            Bugfender.e("ERROR", "Get Order List API response got exception - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

            orderResponse = null;

            if (currentPage == 1) {
                closeProgressDialog();
            } else {
                orderListView.removeFooterView(progressBarFooter);
            }

            if (orderListAdapter != null && currentPage == 1) {
                orderListAdapter.clear();
                orderListAdapter.notifyDataSetChanged();

                parentLayoutTodo.setVisibility(View.GONE);
                setEmptyWallMessage("We're sorry but something went wrong, Please click on Refresh.");
            }

            showCustomToastMessage(
                    "Some exception occurred : " + e.getMessage(), true);
            return;
        }

        if (orderListAdapter != null) {
            orderListAdapter.notifyDataSetChanged();
        }
    }

    // Method to search order from assigned orders
    public void searchOrder(String searchQuery) {
        Bugfender.d("DEBUG", "Search Order API request submitted : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        showProgressDialog("Searching...");

        String encodedString = "";
        try {
            encodedString = URLEncoder.encode(searchQuery, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            encodedString = searchQuery;
        }

        String searchOrderUrl = PreferenceStore.getBaseURL(getApplicationContext())
                + "/order_pickup_lists/search_order?type=" + type + "&order_number=" + encodedString;

        Bugfender.d("DEBUG", "Search Order API URL : " + searchOrderUrl + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,
                searchOrderUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String errorString = ResponseValidator
                        .validate(response);

                closeProgressDialog();

                if (errorString.length() > 0) {
                    Bugfender.d("DEBUG", "Search Order API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    showCustomToastMessage(errorString, true);
                } else {
                    Bugfender.d("DEBUG", "Search Order API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    try {
                        fillIntent(response.getString("tracking_number"));
                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bugfender.e("ERROR", "Search Order API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

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
}
