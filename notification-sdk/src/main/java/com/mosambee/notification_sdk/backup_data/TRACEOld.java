package com.mosambee.notification_sdk.backup_data;

import android.util.Log;

public class TRACEOld {

    private static String AppName = "Notification_SDK";

    public static Boolean isTesting = true;

    public static void i(String string) {
        if (isTesting) {
            Log.i(AppName, string);

        }
    }

    public static void w(String string) {
        if (isTesting) {
            Log.e(AppName, string);
        }
    }

    public static void e(Exception exception) {
        if (isTesting) {
            Log.e(AppName, exception.toString());
        }
    }

    public static void d(String string) {
        if (isTesting) {
            Log.d(AppName, string);
        }
    }

    public static void a(int num) {
        if (isTesting) {
            Log.d(AppName, Integer.toString(num));
        }
    }
}