package com.fbapicking.utility;

import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class Utils {
    public static boolean checkForInstalledApp(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            pm.getPackageInfo(getAppPackageName(ctx), 0);
            return true;
        } catch (NameNotFoundException e) {
            e.getMessage();
        }
        return false;
    }

    public static String getAppPackageName(Context ctx) {
        String packageName = "";
        try {
            packageName = ctx.getPackageName();
        } catch (Exception e) {
            e.getMessage();
        }
        return packageName;
    }

    public static String getAppName(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(
                    getAppPackageName(ctx), 0);
            return pm.getApplicationLabel(appInfo).toString();
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    public static String getAppVersionName(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pkgInfo = pm.getPackageInfo(getAppPackageName(ctx), 0);
            return pkgInfo.versionName;
        } catch (NameNotFoundException e) {
            return "0";
        }
    }

    public static int getAppVersionCode(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pkgInfo = pm.getPackageInfo(getAppPackageName(ctx), 0);
            return pkgInfo.versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    public static String getAppBuildDate(Context ctx) {
        String buildDate = "";
        try {
            ApplicationInfo appInfo = ctx.getPackageManager()
                    .getApplicationInfo(getAppPackageName(ctx), 0);
            ZipFile zipFile = new ZipFile(appInfo.sourceDir);
            ZipEntry zipEntry = zipFile.getEntry("classes.dex");
            long time = zipEntry.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
            buildDate = formatter.format(time);
            zipFile.close();
        } catch (Exception e) {
            e.getMessage();
        }
        return buildDate;
    }

    public void DoSleep(Context c, int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e1) {
            e1.getMessage();
        }
    }
}
