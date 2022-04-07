package com.paylivre.sdk.gateway.android.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File

fun getMimeType(file: File?, context: Context): String? {
    val uri: Uri = Uri.fromFile(file)
    val cR: ContentResolver = context.contentResolver
    val mime = MimeTypeMap.getSingleton()
    return mime.getExtensionFromMimeType(cR.getType(uri))
}