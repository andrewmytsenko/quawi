package com.test.quawi.utils

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern


class Validation {
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && !TextUtils.isEmpty(email)
    }

    fun isPasswordValid(target: String): Boolean {
        return target.length >=6 && !TextUtils.isEmpty(target)
    }
}