package com.example.oompaloompahr.api;

import android.util.Log;

public abstract class ArgumentCallback<T> {
    private static final String TAG = "ArgCb";

    public abstract void done(T arg);

    public void error(String message, Throwable error){
        if(message != null && error != null) {
            Log.e(TAG, message, error);
        }
    }
}
