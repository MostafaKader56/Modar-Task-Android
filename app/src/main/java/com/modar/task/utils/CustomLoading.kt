package com.modar.task.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.modar.task.R
import com.modar.task.databinding.LayoutProgressDialogBinding

class CustomLoading(context: Context) {

    private var dialog: Dialog
    private var binding: LayoutProgressDialogBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = LayoutProgressDialogBinding.inflate(inflater)

        binding.cpBgView.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.colorLoadingBackground
            )
        )
        //Background Color
        dialog = Dialog(context, R.style.CustomProgressBarTheme).apply {
            setContentView(binding.root)
            setCancelable(false)
        }
    }

    fun show() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun dismiss() {
        dialog.dismiss()
    }

    fun isLoading(): Boolean = dialog.isShowing

}