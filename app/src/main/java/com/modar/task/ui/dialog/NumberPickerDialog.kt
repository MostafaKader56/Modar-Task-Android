package com.modar.task.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.modar.task.databinding.LayoutDialogNumberPickerBinding
import com.modar.task.utils.Constants.UNSELECTED

class NumberPickerDialog(
    context: Context,
    title: String,
    private val min: Int,
    private val max: Int,
    private val currentValue: Int,
    private val onNumberSelected: (Int) -> Unit
) {
    private val dialog: Dialog = Dialog(context, android.R.style.Theme_Material_Light_Dialog)
    private val binding: LayoutDialogNumberPickerBinding =
        LayoutDialogNumberPickerBinding.inflate(LayoutInflater.from(context))

    init {
        dialog.setContentView(binding.root)
        dialog.setTitle(title)
        binding.apply {
            numberPicker.minValue = min
            numberPicker.maxValue = max
            numberPicker.value = if (currentValue == UNSELECTED) min else currentValue

            okButton.setOnClickListener {
                onNumberSelected(numberPicker.value)
                dismiss()
            }
        }
    }


    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}