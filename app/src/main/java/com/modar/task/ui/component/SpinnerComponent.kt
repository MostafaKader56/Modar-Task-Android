package com.modar.task.ui.component

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.constraintlayout.widget.ConstraintLayout
import com.modar.task.R
import com.modar.task.databinding.LayoutSpinnerComponentBinding
import com.modar.task.utils.Constants.UNSELECTED
import com.modar.task.utils.Utils.readParcelableCompat

class SpinnerComponent(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private val binding = LayoutSpinnerComponentBinding.inflate(LayoutInflater.from(context), this)
    private var savedSelectedItem: SpinnerItemsAdapter.SpinnerItem? = null

    private val itemsAdapter: SpinnerItemsAdapter by lazy {
        SpinnerItemsAdapter(
            context = context,
            viewHintColor = R.color.grey_black,
            viewItemColor = R.color.black,
            dropDownItemColor = R.color.black,
        )
    }

    private var errorMessage: String? = null
        set(value) {
            field = value
            if (value.isNullOrEmpty()) {
                binding.tvError.visibility = View.GONE
            } else {
                binding.tvError.visibility = View.VISIBLE
            }
            binding.tvError.text = value
        }

    init {
        if (!isInEditMode) {
            createView(attrs)
        }
    }

    fun resetComponentSelection() {
        binding.spinner.setSelection(0)
    }

    fun clearItems() {
        itemsAdapter.clearItems()
    }

    private fun createView(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.SpinnerComponent, 0, 0
        )
        val componentLabel = typedArray.getString(R.styleable.SpinnerComponent_sp_text_label)
        val componentHint = typedArray.getString(R.styleable.SpinnerComponent_sp_text_hint)
        val textError = typedArray.getString(R.styleable.SpinnerComponent_sp_text_error)
        val componentClickable =
            typedArray.getBoolean(R.styleable.SpinnerComponent_sp_component_clickable, true)
        typedArray.recycle()

        binding.spinner.adapter = itemsAdapter

        listeners()

        this.errorMessage = textError

        setComponentLabel(componentLabel ?: "")
        setComponentClickable(componentClickable)
        setComponentHint(componentHint)
    }

    private fun setComponentLabel(label: String) {
        binding.txtLabel.text = label
    }

    private fun listeners() {
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (restorationInProgress) {
                    restorationInProgress = false
                    return
                }
                val selected = getSelectedItem()
                if (savedSelectedItem != null && selected == savedSelectedItem) {
                    savedSelectedItem = null
                    return
                }
                handleSelectionChange()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private var restorationInProgress = false

    private fun tryRestoreSelection() {
        savedSelectedItem?.let { item ->
            (0 until itemsAdapter.count).firstOrNull { index ->
                itemsAdapter.getItem(index)?.id == item.id
            }?.let { position ->
                // Temporarily remove the listener before setting the selection.
                val listener = binding.spinner.onItemSelectedListener
                binding.spinner.apply {
                    onItemSelectedListener = null
                    setSelection(position)
                    post {
                        savedSelectedItem = null
                        onItemSelectedListener = listener
                    }
                }
            }
        }
    }

    private fun handleSelectionChange() {
        this.errorMessage = null
        onItemSelectedCallback?.invoke(getSelectedItem())
    }

    private var onItemSelectedCallback: ((SpinnerItemsAdapter.SpinnerItem?) -> Unit)? = null
    fun onChange(onChangeText: (SpinnerItemsAdapter.SpinnerItem?) -> Unit) {
        onItemSelectedCallback = onChangeText
    }

    private fun setComponentClickable(componentClickable: Boolean) {
        if (!componentClickable) {
            disableComponent()
        }
    }

    private fun setComponentHint(componentHint: String?) {
        componentHint?.let {
            itemsAdapter.hint = componentHint
        }
    }

    fun disableComponent() {
        binding.spinner.apply {
            isClickable = false
            isFocusable = false
            isFocusableInTouchMode = false
        }
    }

    private class SavedState : BaseSavedState {
        var selectedItem: SpinnerItemsAdapter.SpinnerItem? = null
        var error: String? = null

        constructor(superState: Parcelable?) : super(superState)
        constructor(source: Parcel) : super(source) {
            selectedItem = source.readParcelableCompat()
            error = source.readString()  // restore error message
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeParcelable(selectedItem, flags)
            out.writeString(error)
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
            selectedItem = getSelectedItem()
            error = this@SpinnerComponent.errorMessage
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        when (state) {
            is SavedState -> {
                super.onRestoreInstanceState(state.superState)
                savedSelectedItem = state.selectedItem
                this.errorMessage = state.error
                restorationInProgress = true
                tryRestoreSelection()
            }

            else -> super.onRestoreInstanceState(state)
        }
    }

    fun getSelectedItem(): SpinnerItemsAdapter.SpinnerItem? {
        val item = binding.spinner.selectedItem as? SpinnerItemsAdapter.SpinnerItem
        return if (item == null || item.id == UNSELECTED) null else item
    }

    fun setItems(items: List<SpinnerItemsAdapter.SpinnerItem>) {
        itemsAdapter.setItems(items)
        tryRestoreSelection()
    }

    fun setError(error: String) {
        this.errorMessage = error
    }
}