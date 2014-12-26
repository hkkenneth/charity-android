package com.winginno.charitynow;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import android.content.Context;

public class Utils {

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion() {
        try {
            Context context = ApplicationContextProvider.getContext();
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

}
