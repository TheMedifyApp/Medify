package com.geekymusketeers.medify.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.databinding.StatiscticsCardListBinding
import com.geekymusketeers.medify.model.HealthData

private var testList: List<HealthData> = ArrayList()

class StatsListAdapter(
    private val listener: (HealthData) -> Unit
) : RecyclerView.Adapter<StatsListAdapter.StatsViewHolder>() {


    class StatsViewHolder(val binding: StatiscticsCardListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        return StatsViewHolder(
            StatiscticsCardListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return testList.size
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val test = testList[position]

        holder.binding.apply {
            testName.text = test.name
//            testDataRange.text = test.testDataRange
//            testDateTime.text = test.testDate_Time

            root.setOnClickListener {
                listener(test)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(list: List<HealthData>) {
        testList = list
        notifyDataSetChanged()
    }
}
