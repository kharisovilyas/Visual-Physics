package com.example.visualphysics10.net;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetConnection {
    public static boolean checkConnection(Context context){
        return ((ConnectivityManager) context.getSystemService
                ((Context.CONNECTIVITY_SERVICE))).getActiveNetwork() != null;
    }
}
