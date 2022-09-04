package com.geekymusketeers.medify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.DoctorAppointment
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.ListItemsBinding

class DoctorsAppointmentAdapter(var c:Context, var appointmentList : ArrayList<DoctorAppointment>) : RecyclerView.Adapter<DoctorsAppointmentAdapter.DoctorAppointmentViewHolder>() {

    inner class AnimalViewHolder(var v:ListItemsBinding): RecyclerView.ViewHolder(v.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorAppointmentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.appointment_list,parent,false)
        return DoctorAppointmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DoctorAppointmentViewHolder, position: Int) {

        val currentitem = appointmentList[position]

        holder.name.text = currentitem.DoctorName
        holder.disease.text = currentitem.Disease
        holder.time.text = currentitem.Time
        holder.date.text = currentitem.Date
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }
    class DoctorAppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.nameDisplay)
        val disease: TextView = itemView.findViewById(R.id.diseaseDisplay)
        val time:TextView = itemView.findViewById(R.id.timeDisplay)
        val date:TextView = itemView.findViewById(R.id.dateDisplay)
    }
}