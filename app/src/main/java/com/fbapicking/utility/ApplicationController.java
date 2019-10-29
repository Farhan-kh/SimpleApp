package com.fbapicking.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bugfender.sdk.Bugfender;
import com.crittercism.app.Crittercism;
import com.fbapicking.BuildConfig;
import com.fbapicking.R;
import com.fbapicking.utility.Commons.ResponseValidator;

@SuppressLint("InflateParams")
public class ApplicationController extends Application {
    String message = "";
    String mFileName, mFileUrl;
    File folder;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    Typeface font;
    AlertDialog newAppUpdateAlertDialog;
    LayoutInflater layoutInflater;
    View promptView;
    ConnectionDetector connectionDetector;

    /**
     * Log or request TAG
     */
    public static final String TAG = ApplicationController.class.getSimpleName();

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * A singleton instance of the application class for easy access in other
     * places
     */
    private static ApplicationController sInstance;

    public ApplicationController() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        sInstance = this;

        // Initialize criticism
        String CRITICISM_ID = "562a16508d4d8c0a00d07f1c";
        Crittercism.initialize(getApplicationContext(), CRITICISM_ID);

        Bugfender.init(this, "mq0gkziFH7uq7eAIzcZR8kbKYK10x57D", BuildConfig.DEBUG);
        Bugfender.enableCrashReporting();

        connectionDetector = new ConnectionDetector(getApplicationContext());

        if (!connectionDetector.hasConnection()) {
            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            /* API call to check new app update available or not */
            checkApplicationUpdate();
        }
    }

    public class APKDownloader extends AsyncTask<String, String, String> {
        private static final int MEGABYTE = 1024 * 1024;
        private File pdfDocument;

        APKDownloader() {
        }

        @Override
        protected String doInBackground(String... strings) {
            pdfDocument = new File(folder, mFileName);

            try {
                URL url = new URL(mFileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();
                urlConnection.connect();
                int lengthOfFile = urlConnection.getContentLength();

                FileOutputStream fileOutputStream = new FileOutputStream(
                        pdfDocument.getPath());
                InputStream inputStream = urlConnection.getInputStream();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                long total = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    total += bufferLength;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    fileOutputStream.write(buffer, 0, bufferLength);
                }

                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri apkURI = FileProvider.getUriForFile(
                            getApplicationContext(),
                            getApplicationContext()
                                    .getPackageName() + ".provider", pdfDocument);
                    intent.setDataAndType(apkURI, "application/vnd.android.package-archive");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    pdfDocument.setReadable(true, false);
                    intent.setDataAndType(
                            Uri.fromFile(pdfDocument),
                            "application/vnd.android.package-archive");
                }
                getApplicationContext().startActivity(intent);
            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }
    }

    /**
     * Showing Dialog
     **/
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(ApplicationController.getInstance());
                pDialog.setMessage("Downloading App Update, Please Wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    pDialog.getWindow().setType(
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pDialog.getWindow().setType(
                            WindowManager.LayoutParams.TYPE_TOAST);
                } else {
                    pDialog.getWindow().setType(
                            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                }

                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    // Method to check app update
    public void checkApplicationUpdate() {
        Bugfender.d("DEBUG", "Auto Update API request submitted" + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        String checkAppUpdateURL = "";
        checkAppUpdateURL = "http://stgfba.anchanto.com:3010/api/v1/orders/get_ota_update?version=" + Utils.getAppVersionName(getApplicationContext()) + "&name=FBAPicking";

        Bugfender.d("DEBUG", "Auto Update API URL : " + checkAppUpdateURL + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET, checkAppUpdateURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String errorString = ResponseValidator
                                .validate(response);

                        if (errorString.length() > 0) {
                            Bugfender.d("DEBUG", "Auto Update API response got success - " + errorString + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                        } else {
                            Bugfender.d("DEBUG", "Auto Update API response got success : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());

                            try {
                                /* This below code is for Android OS 9.0 : Pie : API Level 28 */
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !Settings.canDrawOverlays(getApplicationContext())) {
                                    Intent i = new Intent();
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    i.setData(uri);
                                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    getApplicationContext().startActivity(i);

                                    for (int k = 0; k < 10; k++) {
                                        Toast.makeText(getApplicationContext(), "Please Do Allow 'Display Over Other Apps' Permission & Relaunch Application Again To Use", Toast.LENGTH_LONG)
                                                .show();
                                    }

                                    return;
                                }

                                layoutInflater = LayoutInflater
                                        .from(ApplicationController
                                                .getInstance());

                                folder = getFilesDir();

                                mFileUrl = response.getJSONObject("ota")
                                        .getString("ota_url");
                                mFileName = response.getJSONObject("ota")
                                        .getString("name") + ".apk";
                                message = response.getString("info");

                                promptView = layoutInflater.inflate(
                                        R.layout.prompt, null);
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        ApplicationController.getInstance());

                                // set prompts.xml to alert dialog builder
                                alertDialogBuilder.setView(promptView);

                                TextView confirmationTextView = (TextView) promptView
                                        .findViewById(R.id.promptTextView);
                                confirmationTextView.setTypeface(font);
                                confirmationTextView.setText(message);

                                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    confirmationTextView.setTextColor(Color.WHITE);
                                }

                                // set dialog message
                                alertDialogBuilder.setCancelable(false)
                                        .setPositiveButton("Download App", null)
                                        .setNeutralButton("No, Thanks",
                                                null);

                                // create alert dialog
                                newAppUpdateAlertDialog = alertDialogBuilder
                                        .create();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    newAppUpdateAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    newAppUpdateAlertDialog
                                            .getWindow()
                                            .setType(
                                                    WindowManager.LayoutParams.TYPE_TOAST);
                                } else {
                                    newAppUpdateAlertDialog
                                            .getWindow()
                                            .setType(
                                                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                }

                                // show it
                                newAppUpdateAlertDialog.show();

                                Button plusBtn = newAppUpdateAlertDialog
                                        .getButton(Dialog.BUTTON_POSITIVE);
                                plusBtn.setTextSize(
                                        TypedValue.COMPLEX_UNIT_PX,
                                        getResources().getDimension(
                                                R.dimen.text_size_large));
                                plusBtn.setTypeface(font);
                                plusBtn.setTransformationMethod(null);

                                plusBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // TODO Do something
                                        newAppUpdateAlertDialog.dismiss();
                                        onCreateDialog(progress_bar_type);

                                        /* API call to download apk from s3 bucket */
                                        new APKDownloader().execute();
                                    }
                                });

                                Button minusBtn = newAppUpdateAlertDialog
                                        .getButton(Dialog.BUTTON_NEUTRAL);
                                minusBtn.setTextSize(
                                        TypedValue.COMPLEX_UNIT_PX,
                                        getResources().getDimension(
                                                R.dimen.text_size_large));
                                minusBtn.setTypeface(font);
                                minusBtn.setTransformationMethod(null);

                                minusBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // TODO Do something
                                        newAppUpdateAlertDialog.dismiss();
                                    }
                                });
                            } catch (Exception e) {
                                Bugfender.e("ERROR", "Auto Update API response got failure - " + e.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Bugfender.e("ERROR", "Auto Update API response got failure - " + error.getMessage() + " : " + Commons.getAndroidID(getApplicationContext()) + " : " + Commons.currentDateTime());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", String.format("application/json"));
                params.put("Accept", String.format("application/json"));
                params.put("Authorization", Commons.getAuth());
                return params;
            }
        };
        ApplicationController.getInstance().getRequestQueue().add(jsonRequest);
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized ApplicationController getInstance() {
        return sInstance;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified then
     * it is used else Default TAG is used.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
