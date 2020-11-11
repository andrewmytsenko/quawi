package com.test.quawi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel (
    val id: Int = -1,
    val name: String = "",
    val avatar_url: String? = "",
    val is_online: Int = 0,
    val dta_activity: String? = "",
    val dta_since: String? = ""
): Parcelable