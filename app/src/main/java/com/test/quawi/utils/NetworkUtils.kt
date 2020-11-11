package com.test.quawi.utils

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class NetworkUtils {
    fun multipartText(name: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData("name", name)
    }
}