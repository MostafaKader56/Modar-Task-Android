package com.modar.task.ui.component

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.modar.task.R
import com.modar.task.databinding.LayoutSpinnerItemBinding
import com.modar.task.databinding.LayoutSpinnerItemDropdownBinding
import com.modar.task.utils.Constants.UNSELECTED
import kotlinx.parcelize.Parcelize

class SpinnerItemsAdapter(
    private val context: Context,
    initialHint: String = "",
    private val viewHintColor: Int,
    private val viewItemColor: Int,
    private val dropDownItemColor: Int,
) : ArrayAdapter<SpinnerItemsAdapter.SpinnerItem>(context, R.layout.layout_spinner_item, mutableListOf()) {

    // Holds the hint text
    var hint: String = initialHint
        set(value) {
            field = value
            // Update the adapter when the hint changes.
            setItems(currentItemsWithoutHint)
        }

    // Store the "real" items separately so they are not mixed with the hint.
    private var currentItemsWithoutHint: List<SpinnerItem> = emptyList()

    /**
     * Call this method to update the spinner items (excluding the hint).
     * The adapter is rebuilt with the hint as the first item.
     */
    fun setItems(newItems: List<SpinnerItem>) {
        currentItemsWithoutHint = newItems
        // Clear and repopulate the adapter's data.
        clear()
        add(SpinnerItem(UNSELECTED, hint))  // Always add the hint first.
        addAll(newItems)
        notifyDataSetChanged()
    }

    fun clearItems() {
        clear()
        add(SpinnerItem(UNSELECTED, hint))
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: LayoutSpinnerItemBinding
        if (convertView == null) {
            binding = LayoutSpinnerItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
            binding.root.tag = binding
        } else {
            binding = convertView.tag as LayoutSpinnerItemBinding
        }

        val spinnerItem = getItem(position)
        binding.itemText.text = spinnerItem?.text
        binding.itemText.setTextColor(
            ContextCompat.getColor(
                context,
                if (position == 0) viewHintColor else viewItemColor
            ),
        )

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: LayoutSpinnerItemDropdownBinding
        if (convertView == null) {
            binding = LayoutSpinnerItemDropdownBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
            binding.root.tag = binding
        } else {
            binding = convertView.tag as LayoutSpinnerItemDropdownBinding
        }

        val spinnerItem = getItem(position)
        binding.itemText.text = spinnerItem?.text
        binding.itemText.setTextColor(
            ContextCompat.getColor(
                context,
                if (position == 0) viewHintColor else dropDownItemColor
            ),
        )

        return binding.root
    }

    @Parcelize
    class SpinnerItem(val id: Int, val text: String) : Parcelable
}

