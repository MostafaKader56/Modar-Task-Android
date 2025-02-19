package com.modar.task.utils

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.modar.task.ModarTaskApplication
import com.modar.task.R

object Utils {
    fun EditText.onFocused(onFocused: () -> Unit) {
        this.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onFocused.invoke()
            }
        }
    }

    fun EditText.addFocusListener(onFocused: () -> Unit, onRemoveFocus: () -> Unit) {
        this.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onFocused.invoke()
            } else {
                onRemoveFocus.invoke()
            }
        }
    }

    fun EditText.setMaxLength(maxLength: Int) {
        val filter = InputFilter.LengthFilter(maxLength)
        this.filters = arrayOf(filter)
    }

    fun EditText.onChange(onChange: (String) -> Unit): TextWatcher {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                onChange.invoke(s?.toString() ?: "")
            }

        }
        this.addTextChangedListener(watcher)
        return watcher
    }

    inline fun <reified T : Parcelable> Parcel.readParcelableCompat(): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            readParcelable(T::class.java.classLoader, T::class.java)
        } else {
            @Suppress("DEPRECATION") readParcelable(T::class.java.classLoader)
        }
    }

    fun Any?.toast() {
        Toast.makeText(ModarTaskApplication.instance, this.toString(), Toast.LENGTH_LONG).show()
    }


    fun Context.showAlert(
        title: String,
        message: String,
        canCancel: Boolean = true,
        textPositiveButton: String = getString(R.string.okay),
        textNegativeButton: String = getString(R.string.cancel),
        onPositiveClicked: (() -> Unit)? = null,
        onNegativeClicked: (() -> Unit)? = null,
    ) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(canCancel)
            .setNegativeButton(textNegativeButton) { _, _ ->
                onNegativeClicked?.invoke()
            }
            .setPositiveButton(textPositiveButton) { _, _ ->
                onPositiveClicked?.invoke()
            }
            .show()
    }
}