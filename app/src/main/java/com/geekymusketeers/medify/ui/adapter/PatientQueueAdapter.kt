package com.geekymusketeers.medify.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.geekymusketeers.medify.model.DoctorAppointment
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.utils.Logger
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


class PatientQueueAdapter(
    private val userID: String,
    val listener: (DoctorAppointment) -> Unit,
) : RecyclerView.Adapter<PatientQueueAdapter.DoctorAppointmentViewHolder>() {

    private var appointmentList: ArrayList<DoctorAppointment> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorAppointmentViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.appointment_list, parent, false)
        return DoctorAppointmentViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: DoctorAppointmentViewHolder, position: Int) {

        val currentItem = appointmentList[position]

        if (currentItem.PatientPhone == "" || currentItem.PatientPhone!!.isEmpty()) {
            holder.name.text = currentItem.PatientName
        } else {
            holder.name.text = currentItem.PatientName + " (" + currentItem.PatientPhone + ")"
        }

        val userAndDoctorAreSamePerson = currentItem.DoctorUID == userID

        Logger.debugLog("CurrentUser: $userID and DoctorID: ${currentItem.DoctorUID} are same person: $userAndDoctorAreSamePerson")

        if (currentItem.DoctorUID == userID) {
            val datesList: List<Date?> = getDates(currentItem.Date, currentItem.Time)
            /** 0th Index = dateNow, 1st Index = dateAppointment */

            Logger.debugLog("Current Date Time: ${datesList[0]}")
            Logger.debugLog("Appointment Date Time: ${datesList[1]}")
            datesList[1]?.let {
                val isBefore = it.before(datesList[0])
                if (isBefore) {
                    Logger.debugLog("Appointment Date Time is after Current Date Time")
                    holder.rate.visibility = View.VISIBLE
                } else {
                    Logger.debugLog("Appointment Date Time is before Current Date Time")
                    holder.rate.visibility = View.GONE
                }
            }
        }



        holder.disease.text = currentItem.Disease + " - " + currentItem.PatientCondition
        holder.button.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(currentItem.Prescription.toString().trim()))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            holder.button.context.startActivity(intent)
        }

        holder.rate.setOnClickListener {
            listener(currentItem)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDates(date: String?, time: String?): List<Date?> {
        val currentDateTime = LocalDateTime.now()
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val formattedDateTime = currentDateTime.format(formatter)
        val dateNow = format.parse(formattedDateTime)
        val appointmentDateTime = date + " " + time!!.substring(0, 5)
        val dateAppointment = format.parse(appointmentDateTime)
        return listOf(dateNow, dateAppointment)
    }

    override fun getItemCount() = appointmentList.size

    class DoctorAppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.nameDisplay)
        val disease: TextView = itemView.findViewById(R.id.diseaseDisplay)
        val button: ImageView = itemView.findViewById(R.id.downloadPrescription)
        val rate: ImageView = itemView.findViewById(R.id.rateUser)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(appointmentList: ArrayList<DoctorAppointment>) {
        this.appointmentList.clear()
        this.appointmentList = appointmentList
        notifyDataSetChanged()
    }
}