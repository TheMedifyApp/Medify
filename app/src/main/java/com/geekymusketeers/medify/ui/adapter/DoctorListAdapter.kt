package com.geekymusketeers.medify.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.model.User

class DoctorListAdapter(val listener: (User)-> Unit) : RecyclerView.Adapter<DoctorListAdapter.DoctorListViewModel>() {

    private var doctorList : MutableList<User> = mutableListOf()

    class DoctorListViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val doctorName : TextView = itemView.findViewById(R.id.nameDisplay)
        val category : TextView = itemView.findViewById(R.id.categoryDisplay)
        val bookButton : Button = itemView.findViewById(R.id.book_button)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorListViewModel {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doctor_card_layout,parent,false)
        return DoctorListViewModel(itemView)
    }

    override fun onBindViewHolder(holder: DoctorListViewModel, position: Int) {

        val currentItem = doctorList[position]
        Log.d("", "Doctors are: $currentItem")

        holder.itemView.setOnClickListener {
            listener(currentItem)
        }

        holder.bookButton.setOnClickListener {
            listener(currentItem)
        }

        holder.doctorName.text = currentItem.Name
        holder.category.text = currentItem.Specialist
    }

    override fun getItemCount(): Int {
        return doctorList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(list: List<User>) {
        doctorList.clear()
        doctorList.addAll(list)
        Log.d("", "Doctor List size: ${list.size} and lists items are : $list")
        notifyDataSetChanged()
    }

}