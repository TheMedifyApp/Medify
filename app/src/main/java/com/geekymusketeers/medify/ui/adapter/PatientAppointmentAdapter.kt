package com.geekymusketeers.medify.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.model.PatientAppointment
import com.geekymusketeers.medify.R

class PatientAppointmentAdapter(
    val listener: (PatientAppointment) -> Unit
) :
    RecyclerView.Adapter<PatientAppointmentAdapter.PatientAppointmentViewHolder>() {

    private var appointmentList = mutableListOf<PatientAppointment>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PatientAppointmentViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.patient_list, parent, false)
        return PatientAppointmentViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PatientAppointmentViewHolder, position: Int) {
        val currentItem = appointmentList[position]

        holder.apply {
            name.text = currentItem.DoctorName
            disease.text = currentItem.Disease
            time.text = currentItem.Time
            date.text = currentItem.Date
            itemView.setOnClickListener {
                listener(currentItem)
            }
        }
    }

    class PatientAppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameDisplay)
        val disease: TextView = itemView.findViewById(R.id.diseaseDisplay)
        val time: TextView = itemView.findViewById(R.id.timeDisplay)
        val date: TextView = itemView.findViewById(R.id.dateDisplay)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(appointment: List<PatientAppointment>) {
        this.appointmentList.clear()
        this.appointmentList.addAll(appointment)
        notifyDataSetChanged()
    }
}

