package fr.free.nrw.commons.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import javax.annotation.Nullable;

import fr.free.nrw.commons.utils.model.NetworkConnectionType;

public class NetworkUtils {

    public static boolean branchCoverage[] = new boolean[21];

    /**
     * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring#java
     * Check if internet connection is established.
     *
     * @param context context passed to this method could be null.
     * @return Returns current internet connection status. Returns false if null context was passed.
     */
    @SuppressLint("MissingPermission")
    public static boolean isInternetConnectionEstablished(@Nullable Context context) {
        if (context == null) {
            return false;
        }

        NetworkInfo activeNetwork = getNetworkInfo(context);
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Detect network connection type
     */
    static NetworkConnectionType getNetworkType(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            branchCoverage[0] = true;
            return NetworkConnectionType.UNKNOWN;
        }
        else{
            branchCoverage[1] = true;
        }

        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo == null) {
            branchCoverage[2] = true;
            return NetworkConnectionType.UNKNOWN;
        }
        else{
            branchCoverage[3] = true;
        }

        int network = networkInfo.getType();
        if (network == ConnectivityManager.TYPE_WIFI) {
            branchCoverage[4] = true;
            return NetworkConnectionType.WIFI;
        }
        else{
            branchCoverage[5] = true;
        }

        int mobileNetwork = telephonyManager.getNetworkType();
        switch (mobileNetwork) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                branchCoverage[6] = true;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                branchCoverage[7] = true;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                branchCoverage[8] = true;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                branchCoverage[9] = true;
                return NetworkConnectionType.TWO_G;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                branchCoverage[10] = true;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                branchCoverage[11] = true;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                branchCoverage[12] = true;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                branchCoverage[13] = true;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                branchCoverage[14] = true;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                branchCoverage[15] = true;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                branchCoverage[16] = true;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                branchCoverage[17] = true;
                return NetworkConnectionType.THREE_G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                branchCoverage[18] = true;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                branchCoverage[19] = true;
                return NetworkConnectionType.FOUR_G;
            default:
                branchCoverage[20] = true;
                return NetworkConnectionType.UNKNOWN;
        }
    }

    /**
     * Extracted private method to get nullable network info
     */
    @Nullable
    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return null;
        }

        return connectivityManager.getActiveNetworkInfo();
    }
}
