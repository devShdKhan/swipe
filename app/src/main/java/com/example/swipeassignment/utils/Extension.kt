package com.example.swipeassignment.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.swipeassignment.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

fun String.toSafeDouble(): Double {
    return try {
        toDouble()
    } catch (e: Exception) {
        0.0
    }
}

fun String.toRequestBody(): RequestBody =
    RequestBody.create(MediaType.parse("text/plain"), this)

fun File.toMultipartBody(): MultipartBody.Part {
    val body = RequestBody.create(MediaType.parse("image/*"), this)
    return MultipartBody.Part.createFormData("files[]", name, body)
}

fun ImageView.load(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.product_placeholder)
        .into(this)
}