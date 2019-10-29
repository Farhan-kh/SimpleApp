package com.fbapicking.controller.activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bugfender.sdk.Bugfender;
import com.fbapicking.utility.ApplicationController;
import com.fbapicking.utility.Commons;
import com.fbapicking.utility.PreferenceStore;
import com.fbapicking.utility.ProgressHUD;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.fbapicking.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements OnCancelListener {
    ProgressHUD progressDialog;
    Typeface font;

    // Default Method: to create the activity (i.e. to start activity)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        font = Typeface.createFromAsset(getAssets(),
                "fonts/Museo300Regular.otf");
    }

    // Method for showing toast message
    public void showShortToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
                .show();
    }

    // Method for getting long String
    public String longInfo(String str) {
        if (str.length() > 4000) {
            Log.i("", str.substring(0, 4000));
            longInfo(str.substring(4000));
            return str;
        } else
            Log.i("", str);
        return str;
    }

    // Method to validate email
    public boolean isEmailValid(String email) {
        CharSequence target = email;
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    // Method to validate the password
    public static boolean isPasswordValid(String password) {
        int length = password.length();
        if (length < 6)
            return false;
        for (int i = 0; i < length; i++) {
            char c = password.charAt(i);
            if (Character.isWhitespace(c))
                return false;
        }
        return true;
    }

    // Method to start showing progress dialog with static message while calling
    // web-service
    public void showProgressDialog() {
        try {
            progressDialog = ProgressHUD.show(this, "Please wait...", true, true,
                    this);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    // Method to stop showing progress dialog while getting response from
    // web-service
    public void closeProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    // Method to start showing progress dialog with dynamic message while
    // calling web-service
    public void showProgressDialog(String message) {
        try {
            progressDialog = ProgressHUD.show(this, message, true, true, this);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    // Method to cancel progress dialog
    @Override
    public void onCancel(DialogInterface dialog) {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    // Method to setup the action bar
    public void setUpActionBar() {
        getSupportActionBar().hide();
    }

    // Method to setup the empty wall message
    public void setEmptyWallMessage() {
        TextView wallMessage = (TextView) findViewById(R.id.wallmessage);
        wallMessage.setVisibility(View.VISIBLE);
        Typeface font = Typeface.createFromAsset(getAssets(),
                "fonts/Museo300Regular.otf");
        wallMessage.setTypeface(font);
    }

    public void showCustomToastMessage(String message, boolean error) {
        if (error) {
            if (checkErrorAcknowledgementSetting().equalsIgnoreCase("true")) {
                acknowledgementError(message);
            } else {
                flashMessage(message, true, "short");
            }
        } else {
            flashMessage(message, false, "short");
        }
    }

    public void showCustomToastMessageWithLength(String message, boolean error,
                                                 String length) {
        if (error) {
            if (checkErrorAcknowledgementSetting().equalsIgnoreCase("true")) {
                acknowledgementError(message);
            } else {
                flashMessage(message, true, length);
            }
        } else {
            flashMessage(message, false, length);
        }
    }

    private String checkErrorAcknowledgementSetting() {
        return PreferenceStore.getPrefEnableErrorAcknowledgement(getApplicationContext());
    }

    private void flashMessage(String message, boolean error, String length) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView textView = view.findViewById(R.id.text);
        textView.setText(message);
        textView.setTypeface(font, Typeface.BOLD);

        if (error) {
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundColor(Color.RED);
        } else {
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.color.fba_color);
        }

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        if (length.equalsIgnoreCase("short")) {
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.setView(view);
        toast.show();
    }

    private void acknowledgementError(String message) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(
                R.layout.error_prompt, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.errorDialog);

        // set prompts.xml to alert dialog builder
        alertDialogBuilder.setView(promptView);

        final TextView confirmationTextView = promptView
                .findViewById(R.id.promptTextView);
        confirmationTextView.setTypeface(font, Typeface.BOLD);
        confirmationTextView.setText(message);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog errorAlertDialog = alertDialogBuilder.create();

        // show it
        errorAlertDialog.show();

        Button minusBtn = errorAlertDialog
                .getButton(Dialog.BUTTON_NEGATIVE);
        minusBtn.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(
                        R.dimen.text_size_large));
        minusBtn.setTypeface(font, Typeface.BOLD);
        minusBtn.setTextColor(getResources().getColor(R.color.text_white));
    }

    // Method to call logout web-service
    public void logoutUser() {
        Bugfender.d("DEBUG", "Logout API request submitted : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.DELETE,
                PreferenceStore.getBaseURL(getApplicationContext())
                        + "/logout", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String errorString = Commons.ResponseValidator
                        .validate(response);

                if (errorString.length() > 0) {
                    Bugfender.d("DEBUG", "Logout API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    showCustomToastMessage(errorString, true);
                } else {
                    Bugfender.d("DEBUG", "Logout API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                    try {
                        if (response.getString("info")
                                .equalsIgnoreCase("Logged out")) {
                            showCustomToastMessage(
                                    "You have been logged out successfully.",
                                    false);
                            PreferenceStore
                                    .clearDetails(getApplicationContext());
                            Intent intent = new Intent(
                                    getApplicationContext(),
                                    LoginActivity.class);
                            startActivity(intent);

                            // Clear Cache of application
                            Commons.trimCache(getApplicationContext());
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
}
