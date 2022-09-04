package com.geekymusketeers.medify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.DoctorAppointment
import com.geekymusketeers.medify.R

class DoctorsAppointmentAdapter(private val appointmentList : ArrayList<DoctorAppointment>) : RecyclerView.Adapter<DoctorsAppointmentAdapter.DoctorAppointmentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorAppointmentViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.appointment_list,parent,false)
        return DoctorAppointmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DoctorAppointmentViewHolder, position: Int) {

        val currentitem = appointmentList[position]

        holder.patientname.text = currentitem.PatientName
        holder.disease.text = currentitem.Disease
        holder.time.text = currentitem.Time
        holder.date.text = currentitem.Date
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }
    class DoctorAppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val patientname: TextView = itemView.findViewById(R.id.nameDisplay)
        val disease: TextView = itemView.findViewById(R.id.diseaseDisplay)
        val time:TextView = itemView.findViewById(R.id.timeDisplay)
        val date:TextView = itemView.findViewById(R.id.dateDisplay)
    }
}