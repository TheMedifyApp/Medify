package com.geekymusketeers.medify.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.SettingsItemBinding
import com.geekymusketeers.medify.model.SettingsItem

class SettingsAdapter(
    val context : Context,
    private val settingsItemList: ArrayList<SettingsItem>,
    private val listener: (Int) -> Unit
    ) :
    RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    class SettingsViewHolder(val binding: SettingsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SettingsViewHolder {
        return SettingsViewHolder(
            SettingsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        val settingsList = settingsItemList[position]
        holder.binding.apply {
            settingsItemName.apply {
                text = settingsList.itemName
                if (settingsList.itemID == 6) {
                     setTextColor(ResourcesCompat.getColor(resources, R.color.red, null))
                     setTypeface(null, Typeface.BOLD)
                }
            }
        }
        holder.binding.root.setOnClickListener {
                listener(position)
        }

        holder.binding.settingsItemIcon.setImageResource(settingsList.drawableInt)
    }

    override fun getItemCount() = settingsItemList.size
}