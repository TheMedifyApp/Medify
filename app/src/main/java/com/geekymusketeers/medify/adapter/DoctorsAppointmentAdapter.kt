package com.geekymusketeers.medify.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.appointment.DoctorAppointment
import com.geekymusketeers.medify.R


class DoctorsAppointmentAdapter(var appointmentList: ArrayList<DoctorAppointment>) : RecyclerView.Adapter<DoctorsAppointmentAdapter.DoctorAppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorAppointmentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.appointment_list,parent,false)
        return DoctorAppointmentViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DoctorAppointmentViewHolder, position: Int) {

        val currentItem = appointmentList[position]

        if (currentItem.PatientPhone == "" || currentItem.PatientPhone!!.isEmpty()) {
            holder.name.text = currentItem.PatientName
        } else {
            holder.name.text = currentItem.PatientName + " (" + currentItem.PatientPhone + ")"
        }
        holder.disease.text = currentItem.Disease + " - " + currentItem.PatientCondition
        holder.button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentItem.Prescription.toString().trim()))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            holder.button.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }
    class DoctorAppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.nameDisplay)
        val disease: TextView = itemView.findViewById(R.id.diseaseDisplay)
        val button: ImageView = itemView.findViewById(R.id.downloadPrescription)
    }
}