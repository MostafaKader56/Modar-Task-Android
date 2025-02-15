package com.modar.task.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.modar.task.R
import com.modar.task.databinding.LayoutEdittextComponentBinding
import com.modar.task.utils.Constants.UNSELECTED
import com.modar.task.utils.Utils.addFocusListener
import com.modar.task.utils.Utils.onChange
import com.modar.task.utils.Utils.setMaxLength
import com.google.android.material.textfield.TextInputLayout

class EditTextComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutEdittextComponentBinding =
        LayoutEdittextComponentBinding.inflate(LayoutInflater.from(context), this)
    private var textWatcher: TextWatcher? = null
    private var errorMessage: String? = null
        set(value) {
            field = value
            binding.apply {
                tvError.text = value
                tvError.visibility = if (value.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        }

    init {
        if (!isInEditMode) {
            binding.editText.id = View.generateViewId()
            createView(attrs)
        }
    }

    fun getComponentText(): String = binding.editText.text.toString().trim()
    fun componentRequestFocus() = binding.editText.requestFocus()
    fun setOnTextChangeListener(listener: (String) -> Unit) {
        binding.editText.removeTextChangedListener(textWatcher)
        textWatcher = binding.editText.onChange { text ->
            errorMessage = null
            listener(text)
        }
        binding.editText.addTextChangedListener(textWatcher)
    }

    fun setError(error: String?) {
        errorMessage = error
    }

    fun resetComponent() {
        binding.editText.setText("")
    }

    private fun createView(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.EditTextComponent).apply {
            binding.apply {
                txtLabel.text = getString(R.styleable.EditTextComponent_text_label) ?: ""
                editText.hint = getString(R.styleable.EditTextComponent_text_hint)
                val minLines = getInteger(R.styleable.EditTextComponent_text_lines, 1)
                InputTypeComponent.fromStyleable(
                    getInt(R.styleable.EditTextComponent_text_inputType, 0)
                ).configure(editText, textInputLayout, minLines > 1)
                editText.minLines = minLines
                editText.maxLines = if (minLines > 1) minLines + 5 else minLines
                if (minLines > 1) {
                    editText.isVerticalScrollBarEnabled = true
                    editText.setHorizontallyScrolling(false)
                    editText.gravity = android.view.Gravity.TOP or android.view.Gravity.START
                    editText.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    editText.isNestedScrollingEnabled = true
                    editText.overScrollMode = View.OVER_SCROLL_ALWAYS
                    editText.scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
                    createEditTextVerticalScrollable()
                }
                if (getInt(
                        R.styleable.EditTextComponent_text_max_length, UNSELECTED
                    ) != UNSELECTED
                ) {
                    editText.filters = arrayOf(
                        InputFilter.LengthFilter(
                            getInt(R.styleable.EditTextComponent_text_max_length, UNSELECTED)
                        )
                    )
                }
                if (!getBoolean(R.styleable.EditTextComponent_component_clickable, true)) {
                    editText.isClickable = false
                    editText.isFocusable = false
                    editText.isFocusableInTouchMode = false
                }
                errorMessage = getString(R.styleable.EditTextComponent_text_error)
                addRemoveErrorListener()
            }
        }.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createEditTextVerticalScrollable() {
        binding.editText.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.parent.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_UP -> {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                    // Call performClick() to satisfy accessibility requirements
                    view.performClick()
                }
            }
            false
        }
    }

    private fun addRemoveErrorListener() {
        textWatcher = binding.editText.onChange {
            errorMessage = null
        }
        binding.editText.addTextChangedListener(textWatcher)
    }

    private sealed class InputTypeComponent {
        abstract fun configure(editText: EditText, layout: TextInputLayout?, isMultiLine: Boolean)
        data object Text : InputTypeComponent() {
            override fun configure(
                editText: EditText,
                layout: TextInputLayout?,
                isMultiLine: Boolean,
            ) {
                editText.inputType =
                    if (isMultiLine) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    else InputType.TYPE_CLASS_TEXT
            }
        }
        data object Email : InputTypeComponent() {
            override fun configure(
                editText: EditText,
                layout: TextInputLayout?,
                isMultiLine: Boolean,
            ) {
                editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
        }
        data object Password : InputTypeComponent() {
            override fun configure(
                editText: EditText,
                layout: TextInputLayout?,
                isMultiLine: Boolean,
            ) {
                layout?.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                editText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
        data object Phone : InputTypeComponent() {
            override fun configure(
                editText: EditText,
                layout: TextInputLayout?,
                isMultiLine: Boolean,
            ) {
                editText.inputType = InputType.TYPE_CLASS_PHONE
            }
        }
        data object CardExpiryDate : InputTypeComponent() {
            override fun configure(
                editText: EditText, layout: TextInputLayout?, isMultiLine: Boolean
            ) {
                editText.apply {
                    inputType = InputType.TYPE_CLASS_NUMBER
                    setMaxLength(5)
                    var previousText = ""
                    addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?, start: Int, count: Int, after: Int
                        ) {
                            previousText = s.toString()
                        }

                        override fun onTextChanged(
                            s: CharSequence?, start: Int, before: Int, count: Int
                        ) {
                            if ((s?.length ?: 0) > previousText.length) {
                                if (s?.length == 2) {
                                    val newText = "$s/"
                                    setText(newText)
                                    setSelection(newText.length)
                                }
                            }
                        }

                        override fun afterTextChanged(s: Editable?) {}
                    })
                }
            }
        }
        data object BankCardNumber : InputTypeComponent() {
            override fun configure(
                editText: EditText,
                layout: TextInputLayout?,
                isMultiLine: Boolean,
            ) {
                editText.apply {
                    inputType = InputType.TYPE_CLASS_NUMBER
                    addFocusListener(onFocused = {
                        setText(
                            text.trim().toString().replace(" ", "")
                        )
                        setMaxLength(16)
                    }, onRemoveFocus = {
                        setText(text.trim())
                        if (text.length == 16) setMaxLength(19)
                        setText(text.chunked(4).joinToString(" "))
                    })
                }
            }
        }
        data object Currency : InputTypeComponent() {
            override fun configure(
                editText: EditText,
                layout: TextInputLayout?,
                isMultiLine: Boolean,
            ) {
                editText.inputType =
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            }
        }
        data object Number : InputTypeComponent() {
            override fun configure(
                editText: EditText,
                layout: TextInputLayout?,
                isMultiLine: Boolean,
            ) {
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
        companion object {
            fun fromStyleable(value: Int): InputTypeComponent {
                return when (value) {
                    1 -> Email
                    2 -> Password
                    3 -> Phone
                    4 -> Currency
                    5 -> Number
                    6 -> BankCardNumber
                    7 -> CardExpiryDate
                    else -> Text
                }
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.text = binding.editText.text.toString()
        savedState.errorMessage = errorMessage
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            val listener = textWatcher
            binding.editText.removeTextChangedListener(listener)
            post {
                binding.editText.setText(state.text)
                this.errorMessage = state.errorMessage
                textWatcher = listener
                textWatcher?.let {
                    binding.editText.addTextChangedListener(it)
                }
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private class SavedState : BaseSavedState {
        var text: String? = null
        var errorMessage: String? = null

        constructor(superState: Parcelable?) : super(superState)
        private constructor(parcel: Parcel) : super(parcel) {
            text = parcel.readString()
            errorMessage = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeString(text)
            parcel.writeString(errorMessage)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}