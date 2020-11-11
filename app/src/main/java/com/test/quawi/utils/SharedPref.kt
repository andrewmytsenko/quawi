package com.test.quawi.utils

import android.content.Context

class SharedPref {
    fun saveToPref(context: Context, key: String, value: String) {
        val sharedPref = context.getSharedPreferences("userData", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getFromPref(context: Context, key: String, value: String): String {
        val sharedPref = context.getSharedPreferences("userData", Context.MODE_PRIVATE)
        return sharedPref.getString(key, value).toString()
    }

    fun clearPrefs(context: Context) {
        val sharedPref = context.getSharedPreferences("userData", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }
}