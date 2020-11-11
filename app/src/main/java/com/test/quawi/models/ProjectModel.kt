package com.test.quawi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProjectModel (
    val id: Int = -1,
    val name: String = "",
    val uid: String = "",
    val logo_url: String? = null,
    val position: Int = -1,
    val is_active: Int = -1,
    val is_owner_watcher: Int = -1,
    val dta_user_since: String? = null,
    val users: ArrayList<UserModel> = arrayListOf()
): Parcelable