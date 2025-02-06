package com.example.a_connect

import android.app.Activity
import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class ProgressDialogHelper(private val context: Context) {

    private var dialog: AlertDialog? = null

    fun showProgressDialog(message: String = "Uploading...") {
        val builder = AlertDialog.Builder(context)
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.progress_bar_dialogbox, null)

        val progressText = view.findViewById<TextView>(R.id.loadingMessage)
        progressText.text = message

        builder.setView(view)
        builder.setCancelable(false)

        dialog = builder.create()
        dialog?.show()
    }

    fun dismissProgressDialog() {
        dialog?.dismiss()
    }
}
