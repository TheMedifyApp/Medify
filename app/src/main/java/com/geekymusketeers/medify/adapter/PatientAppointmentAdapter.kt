package com.geekymusketeers.medify.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.appointment.PatientAppointment
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.mainFragments.DoctorPatient

class PatientAppointmentAdapter(var c: Context, var appointmentList: ArrayList<PatientAppointment>) :
RecyclerView.Adapter<PatientAppointmentAdapter.PatientAppointmentViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientAppointmentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.patient_list,parent,false)
        return PatientAppointmentViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PatientAppointmentViewHolder, position: Int) {
        val currentItem = appointmentList[position]

        val name = currentItem.DoctorName
        val uid = currentItem.DoctorUID
        val disease = currentItem.Disease
        val time = currentItem.Time
        val date = currentItem.Date
        holder.name.text = name
        holder.disease.text = disease
        holder.time.text = time
        holder.date.text = date

        holder.itemView.setOnClickListener {
            /**set Data*/
            val mIntent = Intent(c, DoctorPatient::class.java)
            mIntent.putExtra("uid", uid)
            mIntent.putExtra("date", date)
            mIntent.putExtra("hide", "hide")
            c.startActivity(mIntent)
        }
    }
    class PatientAppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


        val name: TextView = itemView.findViewById(R.id.nameDisplay)
        val disease: TextView = itemView.findViewById(R.id.diseaseDisplay)
        val time:TextView = itemView.findViewById(R.id.timeDisplay)
        val date:TextView = itemView.findViewById(R.id.dateDisplay)
    }
}

