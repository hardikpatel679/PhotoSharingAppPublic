package com.photo.sharing.extensions

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import java.util.*

fun createUUID(): String {
    return UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ROOT)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun Context.displaySingleChoiceDialog(
    title: String,
    options: Array<String?>,
    currentSelection: Int,
    operation: (which: Int) -> Unit
) {
    val optionsDialog = AlertDialog.Builder(this)
    optionsDialog.setTitle(title)
    optionsDialog.setSingleChoiceItems(options, currentSelection) { dialog, which ->
        run {
            operation(which)
            dialog.dismiss()
        }
    }
    optionsDialog.create().show()
}