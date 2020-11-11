package com.test.quawi.utils

import android.content.Context
import android.widget.EditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.quawi.R

class EditProjectNamePopup(
    context: Context,
    private val name: String,
    make: (name: String) -> Unit
) {
    private val wrapContent: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT

    private var projectName: EditText? = null

    private val dialog = MaterialAlertDialogBuilder(context).apply {
        setTitle(R.string.change_ticket_subject)
        setView(R.layout.popup_view)

        setNegativeButton(R.string.close) { _, _ ->
            dismiss()
        }

        setPositiveButton(R.string.change) { _, _ ->
            make(projectName?.text.toString())
        }

    }.create()

    private fun prepareDialog() {
        projectName = dialog.findViewById(R.id.popup_edit_text)
        projectName?.setText(name)
        projectName?.setSelection(projectName?.text!!.length)
    }

    fun show() {
        dialog.window?.setLayout(wrapContent, wrapContent)
        dialog.show()
        prepareDialog()
    }

    private fun dismiss() {
        dialog.dismiss()
    }
}