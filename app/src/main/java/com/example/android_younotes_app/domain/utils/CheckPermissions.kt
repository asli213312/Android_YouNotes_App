package com.example.android_younotes_app.domain.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object CheckPermissions {
    fun hasPermission(PERMISSION_REQUEST: Int, permission: String, context: Context, activity: Activity): Boolean {
        if (ContextCompat.checkSelfPermission(context,
                permission)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission) &&
                ContextCompat.checkSelfPermission(context,
                    permission)
                != PackageManager.PERMISSION_GRANTED) {

                return false;

            } else {

                ActivityCompat.requestPermissions(activity, arrayOf(permission),
                    PERMISSION_REQUEST);

            }

            return false;
        } else {
            return true;


        }
    }
}