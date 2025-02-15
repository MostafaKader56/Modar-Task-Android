package com.modar.task.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.modar.task.R
import com.modar.task.base.BaseItemUIState
import com.modar.task.databinding.LayoutItemUserBinding
import com.modar.task.model.main.Gender
import com.modar.task.model.main.User

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.Holder>() {

    private val items = mutableListOf<User>()

    var listener: UsersAdapterListener? = null
        set(value) {
            field = value
            value?.checkIfIsEmpty(items.isEmpty())
        }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<User>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
        listener?.checkIfIsEmpty(items.isEmpty())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
        listener?.checkIfIsEmpty(true)
    }

    fun setItemRemoved(uiState: BaseItemUIState<User>) {
        if (uiState.item == null || uiState.position > items.size - 1) return
        items.removeAt(uiState.position)
        notifyItemRemoved(uiState.position)
        listener?.checkIfIsEmpty(items.isEmpty())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutItemUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    inner class Holder(private val binding: LayoutItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(value: User) {
            binding.apply {
                txtTitle.text = value.name
                txtSubtitle.text = value.jobTitle
                txtAge.text = root.context.getString(
                    R.string.age_with_suffix,
                    value.age.toString(),
                    root.context.getString(R.string.year)
                )
                txtGender.text = root.context.getString(value.gender.labelRes)

                txtGender.setCompoundDrawablesWithIntrinsicBounds(
                    when (value.gender) {
                        Gender.MALE -> R.drawable.ic_male
                        Gender.FEMALE -> R.drawable.ic_female
                    },
                    0,
                    0,
                    0,
                )

                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener?.onItemClicked(value, position)
                    }
                }

                btnDelete.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener?.onDeleteItemClicked(value, position)
                    }
                }
            }
        }
    }

    interface UsersAdapterListener {
        fun onItemClicked(value: User, position: Int)
        fun onDeleteItemClicked(value: User, position: Int)
        fun checkIfIsEmpty(isEmpty: Boolean)
    }
}