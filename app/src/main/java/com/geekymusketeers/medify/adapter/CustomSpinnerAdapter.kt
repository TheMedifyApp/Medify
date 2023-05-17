package com.geekymusketeers.medify.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.databinding.LayoutSpinnerItemBinding


class CustomSpinnerAdapter(
    private val onClickListener: (item: String) -> Unit
) : RecyclerView.Adapter<CustomSpinnerAdapter.CustomSpinnerViewHolder>() {

    private val items = mutableListOf<String>()

    class CustomSpinnerViewHolder(val binding: LayoutSpinnerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomSpinnerViewHolder {
        return CustomSpinnerViewHolder(
            LayoutSpinnerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomSpinnerViewHolder, position: Int) {
        val item = items[position]

        holder.binding.apply {
            spinnerRecyclerViewItem.text = item
            root.setOnClickListener {
                onClickListener(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItem(list: List<String>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}