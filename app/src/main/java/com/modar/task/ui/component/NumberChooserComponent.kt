package com.modar.task.ui.component

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.modar.task.R
import com.modar.task.databinding.LayoutNumberChooserComponentBinding
import com.modar.task.ui.dialog.NumberPickerDialog

class NumberChooserComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding =
        LayoutNumberChooserComponentBinding.inflate(LayoutInflater.from(context), this)

    private var componentSelectedNumber: Int? = null
        set(value) {
            if (field == value) return
            field = value
            setError("")
            updateDisplay()
            onNumberChanged?.invoke(value)
        }

    private var componentHint: String = ""
    private var suffix: String = ""
    private var prefix: String = ""

    var onNumberChanged: ((number: Int?) -> Unit)? = null

    var min: Int = DEFAULT_MIN
        set(value) {
            field = value.coerceAtMost(max)
            validateSelection()
        }

    var max: Int = DEFAULT_MAX
        set(value) {
            field = value.coerceAtLeast(min)
            validateSelection()
        }

    init {
        if (!isInEditMode) {
            initAttributes(attrs)
            setupClickListeners()
        }
    }

    private fun initAttributes(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.NumberChooserComponent).apply {
            min = getInt(R.styleable.NumberChooserComponent_nc_min, DEFAULT_MIN)
            max = getInt(R.styleable.NumberChooserComponent_nc_max, DEFAULT_MAX)
            componentHint = getString(R.styleable.NumberChooserComponent_nc_text_hint) ?: ""
            suffix = getString(R.styleable.NumberChooserComponent_nc_suffix) ?: ""
            prefix = getString(R.styleable.NumberChooserComponent_nc_prefix) ?: ""

            setLabel(getString(R.styleable.NumberChooserComponent_nc_text_label))
            setError(getString(R.styleable.NumberChooserComponent_nc_text_error) ?: "")
            isEnabled = getBoolean(R.styleable.NumberChooserComponent_nc_enabled, true)

            recycle()
        }
        validateMinMax()
        updateDisplay()
    }

    private fun validateMinMax() {
        if (min > max) {
            throw IllegalArgumentException("Min value cannot be greater than max value")
        }
    }

    private fun setupClickListeners() {
        binding.container.setOnClickListener {
            if (isEnabled) showNumberPickerDialog()
        }
    }

    private fun showNumberPickerDialog() {
        NumberPickerDialog(
            context = context,
            title = binding.txtLabel.text.toString(),
            min = min,
            max = max,
            currentValue = componentSelectedNumber ?: min,
            onNumberSelected = { componentSelectedNumber = it }
        ).show()
    }

    private fun updateDisplay() {
        binding.txtSelectedValue.apply {
            text = when (componentSelectedNumber) {
                null -> componentHint
                else -> "$prefix$componentSelectedNumber$suffix"
            }
            setTextColor(
                ContextCompat.getColor(
                    context,
                    if (componentSelectedNumber == null) R.color.grey_black else R.color.black
                )
            )
        }
    }

    fun getSelectedNumber(): Int? = componentSelectedNumber

    fun setSelectedNumber(number: Int?) {
        if (number == null) {
            componentSelectedNumber = null
            return
        }
        componentSelectedNumber = when {
            number < min -> {
                setError(context.getString(R.string.error_number_too_low, "$min"))
                null
            }

            number > max -> {
                setError(context.getString(R.string.error_number_too_high, "$max"))
                null
            }

            else -> number
        }
    }

    private fun validateSelection() {
        componentSelectedNumber?.let {
            if (it < min || it > max) {
                componentSelectedNumber = null
            }
        }
    }

    fun setLabel(label: String?) {
        binding.txtLabel.text = label ?: ""
    }

    fun setError(errorMessage: String) {
        binding.tvError.apply {
            visibility = if (errorMessage.isNotEmpty()) View.VISIBLE else View.GONE
            text = errorMessage
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.container.apply {
            isClickable = enabled
            isFocusable = enabled
            alpha = if (enabled) 1f else DISABLED_ALPHA
        }
        binding.txtSelectedValue.setTextColor(
            ContextCompat.getColor(
                context,
                if (enabled) R.color.grey_black else R.color.grey_black
            )
        )
    }

    fun resetComponent() {
        componentSelectedNumber = null
        setError("")
    }

    private class SavedState : BaseSavedState {
        var selectedNumber: Int? = null
        var min: Int = DEFAULT_MIN
        var max: Int = DEFAULT_MAX
        var errorMessage: String = ""

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel) : super(source) {
            selectedNumber = if (source.readInt() == 1) source.readInt() else null
            min = source.readInt()
            max = source.readInt()
            errorMessage = source.readString() ?: ""
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(if (selectedNumber != null) 1 else 0)
            selectedNumber?.let { out.writeInt(it) }
            out.writeInt(min)
            out.writeInt(max)
            out.writeString(errorMessage)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return SavedState(superState).apply {
            selectedNumber = componentSelectedNumber
            min = this@NumberChooserComponent.min
            max = this@NumberChooserComponent.max
            errorMessage = binding.tvError.text.toString()
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        when (state) {
            is SavedState -> {
                super.onRestoreInstanceState(state.superState)
                val listener = this.onNumberChanged
                this.onNumberChanged = null
                componentSelectedNumber = state.selectedNumber
                min = state.min
                max = state.max
                setError(state.errorMessage)
                post {
                    this.onNumberChanged = listener
                }
            }

            else -> super.onRestoreInstanceState(state)
        }
    }

    companion object {
        private const val DEFAULT_MIN = 0
        private const val DEFAULT_MAX = 100
        private const val DISABLED_ALPHA = 0.5f
    }
}