package com.loantrackingsystem.app.other

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.loantrackingsystem.app.R


class MyDialog(val context: Context) {


    val builder = MaterialAlertDialogBuilder(context)
    lateinit var dialog : androidx.appcompat.app.AlertDialog

    fun showProgressDialog(msg: String? = null, fragment : Fragment) {
        val view =  fragment.layoutInflater.inflate(R.layout.dialog_progress,null,false)
        builder.setCancelable(false)
        if (msg != null) {
            val progressTextview = view.findViewById<TextView>(R.id.tv_progress_text)
            progressTextview.text = msg
            builder.setView(view)
            dialog =  builder.show()
        } else {
            val progressTextview = view.findViewById<TextView>(R.id.tv_progress_text)
            progressTextview.visibility = View.GONE
            builder.setView(view)
            dialog =   builder.show()
        }
    }

    fun showProgressDialogForActivity(msg: String? = null,activity : Activity) {
        val view =  activity.layoutInflater.inflate(R.layout.dialog_progress,null,false)
        builder.setCancelable(false)
        if (msg != null) {
            val progressTextview = view.findViewById<TextView>(R.id.tv_progress_text)
            progressTextview.text = msg
            builder.setView(view)
            dialog =  builder.show()
        } else {
            val progressTextview = view.findViewById<TextView>(R.id.tv_progress_text)
            progressTextview.visibility = View.GONE
            builder.setView(view)
            dialog =   builder.show()
        }
    }


    fun showErrorAlertDialog(msg : String?,listener : DialogInterface.OnClickListener? = null) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Something went wrong")
            .setMessage(msg)
            .setPositiveButton("OK",listener)
            .show()
    }

    fun showPaymentSuccessDialog(msg : String?,listener : DialogInterface.OnClickListener? = null) {
        MaterialAlertDialogBuilder(context)
            .setTitle("SUCCESS")
            .setMessage(msg)
            .setPositiveButton("OK",listener)
            .show()
    }

    fun dismissProgressDialog() {
        dialog.dismiss()
    }







}