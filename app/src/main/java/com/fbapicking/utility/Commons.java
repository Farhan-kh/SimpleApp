package com.fbapicking.utility;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;

public class Commons {
    /**
     * The version.
     */
    public static String version = "";
    private static PackageInfo pInfo;
    boolean mExternalStorageAvailable = false;
    boolean mExternalStorageWritable = false;

    void updateExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWritable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWritable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWritable = false;
        }
    }

    /**
     * Store current version. This method stores the application's current
     * version in a variable call this method once every time application
     * starts.
     */
    public static void storeCurrentVersion(Context context) {
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.getMessage();
        }
        version = pInfo.versionName;
    }

    /**
     * Gets the current version. Returns the current version of application
     * installed
     */
    public static String getCurrentVersion() {
        return version;
    }

    // Method to convert ASCII to nonASCII & vice-versa
    public static String escapeNonAscii(String str) {
        StringBuilder retStr = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int cp = Character.codePointAt(str, i);
            int charCount = Character.charCount(cp);
            if (charCount > 1) {
                i += charCount - 1;
                if (i >= str.length()) {
                    throw new IllegalArgumentException("truncated unexpectedly");
                }
            }

            if (cp < 128) {
                retStr.appendCodePoint(cp);
            } else {
                retStr.append(String.format("\\u%x", cp));
            }
        }
        String res = "";
        if (retStr.toString().contains("\\"))
            res = "\\" + retStr.toString();
        else
            res = retStr.toString();
        return res;
    }

    // Method to convert from unicode to UTF & vice-versa
    public static String unicodeToUTF(String unicode) {
        try {
            // Convert from Unicode to UTF-8
            byte[] utf8 = unicode.getBytes("UTF-8");

            // Convert from UTF-8 to Unicode
            unicode = new String(utf8, "UTF-8");
        } catch (Exception e) {
            e.getMessage();
        }
        return unicode;
    }

    // Method which returns authentication
    public static String getAuth() {
        return "Basic " + Base64.encodeToString(("fulflmnt:@nch@t0").getBytes(), Base64.NO_WRAP);
    }

    /**
     * The Class <code>FULFLMNT Error ResponseValidator</code>.
     */
    public static final class ResponseValidator {
        public static String validate(JSONObject response) {
            if (response.has("error")) {
                try {
                    return response.getString("error");
                } catch (JSONException e) {
                    e.getMessage();
                }
            } else if (response.has("exception")) {
                try {
                    return response.getString("exception");
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
            return "";
        }
    }

    // Method to convert date
    @SuppressLint("SimpleDateFormat")
    public static final class DateConverter {
        // Method to format date
        @SuppressLint("SimpleDateFormat")
        public static String formatedDate(String strDate) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd'T'HH:mm:ss
            SimpleDateFormat output = new SimpleDateFormat("dd MMM, yyyy");
            try {
                Date d = sdf.parse(strDate);
                return output.format(d);
            } catch (ParseException e) {
                e.getMessage();
            }
            return null;
        }

        // Method to format date with diff. style
        @SuppressLint("SimpleDateFormat")
        public static String toFormatDate(String strDate) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yy");
            try {
                Date d = sdf.parse(strDate);
                return output.format(d);
            } catch (ParseException e) {
                e.getMessage();
            }
            return null;
        }
    }

    // Method to capitalize every word's first char
    public static String toTitleCase(String givenString) {
        char[] c = givenString.toCharArray();
        boolean b = true;
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                b = true;
                continue;
            }
            if (b && Character.isLetter(c[i])) {
                c[i] = Character.toUpperCase(c[i]);
                b = false;
            }
        }
        return new String(c);
    }

    // Method to get current date
    public String getTodayDate() {
        final Calendar c = Calendar.getInstance();
        int todayDate = (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        return (String.valueOf(todayDate));
    }

    // Method to get current time
    public String getCurrentTime() {
        final Calendar c = Calendar.getInstance();
        int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        return (String.valueOf(currentTime));
    }

    // Method to get Current Date & Time
    public static String getCurrentDateAndTime() {
        Date now = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss" + " +0000");
        return sdf.format(now);
    }

    // Method to clear cache of app
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    // Method to delete cache directory
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static String currentDateTime() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return df.format(c.getTime());
    }

    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    // Method to delete files from internal storage if present
    public static void deleteInternalStorageFiles(Context context) {
        try {
            File[] files = context.getFilesDir().listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    f.delete();
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
