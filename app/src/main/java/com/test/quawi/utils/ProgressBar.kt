package com.test.quawi.utils

import android.view.View
import android.widget.ProgressBar

class ProgressBar {
    fun hide(pb: ProgressBar) {
        pb.visibility = View.GONE
    }

    fun show(pb: ProgressBar) {
        pb.visibility = View.VISIBLE
    }
}