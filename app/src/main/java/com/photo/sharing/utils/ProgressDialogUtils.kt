package com.photo.sharing.utils

import android.app.Dialog
import android.content.Context
import android.view.View
import com.photo.sharing.R

class ProgressDialogUtils {

    companion object {
        private var mAlertDialog: Dialog? = null

        fun showProgressDialog(context: Context) {
            mAlertDialog = Dialog(context)
            val dialogView = View.inflate(context, R.layout.layout_progress_bar_with_text, null)
            mAlertDialog?.setContentView(dialogView)
            mAlertDialog?.show()
        }

        fun dismissDialog() {
            mAlertDialog?.dismiss()
        }
    }
}