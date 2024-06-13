package com.example.android_younotes_app.data.utils

import android.net.Uri
import androidx.room.TypeConverter

class DatabaseConverters {
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }
}