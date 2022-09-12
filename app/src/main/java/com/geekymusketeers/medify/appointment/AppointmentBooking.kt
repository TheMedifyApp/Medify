package com.geekymusketeers.medify.appointment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.ActivityAppointmentBookingBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.FirebaseDatabase
import com.ncorti.slidetoact.SlideToActView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AppointmentBooking : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBookingBinding
    private lateinit var sharedPreference : SharedPreferences
    //"Cardiologist", "Dentist", "ENT specialist", "Obstetrician/Gynaecologist", "Orthopaedic surgeon","Psychiatrists",
    // "Radiologist", "Pulmonologist", "Neurologist", "Allergists", "Gastroenterologists",
    // "Urologists", "Otolaryngologists", "Rheumatologists", "Anesthesiologists"
    private lateinit var mapOfDiseasesList: HashMap<String, ArrayList<String>>
    private lateinit var cardiologistList: ArrayList<String>
    private lateinit var dentistList: ArrayList<String>
    private lateinit var entList: ArrayList<String>
    private lateinit var gynaecologistList: ArrayList<String>
    private lateinit var orthopaedicList: ArrayList<String>
    private lateinit var psychiatristsList: ArrayList<String>
    private lateinit var radiologistList: ArrayList<String>
    private lateinit var pulmonologistList: ArrayList<String>
    private lateinit var neurologistList: ArrayList<String>
    private lateinit var allergistsList: ArrayList<String>
    private lateinit var gastroenterologistsList: ArrayList<String>
    private lateinit var urologistsList: ArrayList<String>
    private lateinit var otolaryngologistsList: ArrayList<String>
    private lateinit var rheumatologistsList: ArrayList<String>
    private lateinit var anesthesiologistsList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        sharedPreference = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)

        val diseaseValue = HashMap<String, Float>()
        setDiseaseValues(diseaseValue)

        val conditionValue = HashMap<String, Float>()
        setConditionValue(conditionValue)

        initializeSpecializationWithDiseasesLists()

        var totalPoint = 0

        val doctorUid = intent.extras!!.getString("Duid")
        val doctorName = intent.extras!!.getString("Dname")
        val doctorEmail = intent.extras!!.getString("Demail")
        val doctorPhone = intent.extras!!.getString("Dphone")
        val doctorType = intent.extras!!.getString("Dtype")

        // Date Picker
        binding.selectDate.setOnClickListener {
            // Initiation date picker with
            // MaterialDatePicker.Builder.datePicker()
            // and building it using build()
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormatter.format(Date(it))
                binding.selectDate.setText(date)

            }

            // Setting up the event for when cancelled is clicked
            datePicker.addOnNegativeButtonClickListener {
                Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
            }

            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {
                Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
            }
        }




        // Booking Appointment
        binding.btnFinalbook.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener{

            override fun onSlideComplete(view: SlideToActView) {
                val userName = sharedPreference.getString("name","").toString()
                val userPhone = sharedPreference.getString("phone","").toString()
                val userid = sharedPreference.getString("uid","").toString()
                val userPrescription = sharedPreference.getString("prescription", "").toString()

                val date = binding.selectDate.text.toString()
                val time = binding.timeDropdown.text.toString()
                val disease = binding.diseaseDropdown.text.toString()
                val situation = binding.situationDropdown.text.toString()
                val rightNow = Calendar.getInstance()
                val currentHourIn24Format: Int =rightNow.get(Calendar.HOUR_OF_DAY)
                val firstComeFirstServe = 1 + (0.1 * ((currentHourIn24Format / 10) + 1))

                var temp = diseaseValue[disease]!!
                temp += conditionValue[situation]!!
                totalPoint = (temp * firstComeFirstServe).toInt()

                val appointmentD:HashMap<String,String> = HashMap() //define empty hashmap
                appointmentD["PatientName"] = userName
                appointmentD["PatientPhone"] = userPhone
                appointmentD["Time"] = time
                appointmentD["Date"] = date
                appointmentD["Disease"] = disease
                appointmentD["PatientCondition"] = situation
                appointmentD["Prescription"] = userPrescription
                appointmentD["TotalPoints"] = totalPoint.toString().trim()

                val appointmentP : HashMap<String, String> = HashMap() //define empty hashmap
                appointmentP["DoctorUID"] = doctorUid.toString()
                appointmentP["DoctorName"] = doctorName.toString()
                appointmentP["DoctorPhone"] = doctorPhone.toString()
                appointmentP["Date"] = date
                appointmentP["Time"] = time
                appointmentP["Disease"] = disease
                appointmentP["PatientCondition"] = situation
                appointmentP["Prescription"] = userPrescription

                val appointmentDB_Doctor = FirebaseDatabase.getInstance().getReference("Doctor").child(doctorUid!!).child("DoctorsAppointments").child(date)
                appointmentDB_Doctor.child(userid).setValue(appointmentD)

                val appointmentDB_User_Doctor = FirebaseDatabase.getInstance().getReference("Users").child(doctorUid).child("DoctorsAppointments").child(date)
                appointmentDB_User_Doctor.child(userid).setValue(appointmentD)


                val appointmentDB_Patient = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("PatientsAppointments").child(date)
                appointmentDB_Patient.child(doctorUid).setValue(appointmentP)

                startActivity(Intent(baseContext, BookingDoneActivity::class.java))
                finish()
            }
        }


        // Disease List
        ////        val items = listOf("Not sure", "Fever", "Cold & Flu", "Diarrhea","Headache", "Allergies", "Stomach Aches","Conjunctivitis", "Dehydration", "Tooth ache", "Ear ache", "Food poisoning")
        val items: List<String> = mapOfDiseasesList[doctorType]!!
        val adapter = ArrayAdapter(this, R.layout.list_items, items)
        binding.diseaseDropdown.setAdapter(adapter)

        // Situation List
        val situationItems = listOf("Severe Pain", "Mild Pain", "No Pain")
        val situationAdapter = ArrayAdapter(this, R.layout.list_items, situationItems)
        binding.situationDropdown.setAdapter(situationAdapter)

        val timeItems = listOf("9:00 AM - 11:00 AM","11:00 AM - 13:00 PM", "17:00 PM - 19:00 PM","19:00 PM - 22:OO PM")
        val timeAdapter = ArrayAdapter(this, R.layout.list_items, timeItems)
        binding.timeDropdown.setAdapter(timeAdapter)
    }

    private fun initializeSpecializationWithDiseasesLists() {
        mapOfDiseasesList = HashMap()
        cardiologistList = ArrayList()
        dentistList = ArrayList()
        entList = ArrayList()
        gynaecologistList = ArrayList()
        orthopaedicList = ArrayList()
        psychiatristsList = ArrayList()
        radiologistList = ArrayList()
        pulmonologistList = ArrayList()
        neurologistList = ArrayList()
        allergistsList = ArrayList()
        gastroenterologistsList = ArrayList()
        urologistsList = ArrayList()
        otolaryngologistsList = ArrayList()
        rheumatologistsList = ArrayList()
        anesthesiologistsList = ArrayList()

        //List initializing for Cardiac diseases
        cardiologistList.add("Not sure")
        cardiologistList.add("High blood pressure")
        cardiologistList.add("High cholesterol")
        cardiologistList.add("Angina (chest pain)")
        cardiologistList.add("Heart rhythm disorders")
        cardiologistList.add("Atrial fibrillation")

        //List initializing for Dental diseases
        dentistList.add("Not sure")
        dentistList.add("Tooth Decay/Cavities")
        dentistList.add("Gum Disease")
        dentistList.add("Cracked or Broken Teeth")
        dentistList.add("Root Infection")
        dentistList.add("Tooth Loss")

        //List initializing for ENT diseases
        entList.add("Not sure")
        entList.add("Hearing problems")
        entList.add("Allergies")
        entList.add("Nasal congestion")
        entList.add("Tonsil infections")
        entList.add("Enlarged tonsils")

        //List initializing for ENT diseases
        gynaecologistList.add("Not sure")
        gynaecologistList.add("Bleeding during pregnancy")
        gynaecologistList.add("Female infertility")
        gynaecologistList.add("Heart disease in pregnancy")
        gynaecologistList.add("Menopause")
        gynaecologistList.add("Menstrual cramps")
        gynaecologistList.add("Miscarriage")
        gynaecologistList.add("Ovarian cysts")
        gynaecologistList.add("Vaginal bleeding")

        //List initializing for Orthopaedic diseases
        orthopaedicList.add("Not sure")
        orthopaedicList.add("Bone fractures")
        orthopaedicList.add("Muscle strains")
        orthopaedicList.add("Joint or back pain")
        orthopaedicList.add("Injuries to tendons or ligaments")
        orthopaedicList.add("Limb abnormalities")
        orthopaedicList.add("Bone cancer")

        //List initializing for Psychiatrists diseases
        psychiatristsList.add("Not sure")
        psychiatristsList.add("Alcohol use disorder")
        psychiatristsList.add("Alzheimer’s disease")
        psychiatristsList.add("Anxiety disorders")
        psychiatristsList.add("Bipolar disorder")
        psychiatristsList.add("Depression")
        psychiatristsList.add("Eating disorders")
        psychiatristsList.add("Mood disorders")
        psychiatristsList.add("Panic disorder")
        psychiatristsList.add("Sleep disorders")

        //List initializing for Radiologists treated diseases
        radiologistList.add("Not sure")
        radiologistList.add("Brain tumor")
        radiologistList.add("Breast cancer")
        radiologistList.add("Kidney stones")
        radiologistList.add("Liver tumors")
        radiologistList.add("Lung cancer")
        radiologistList.add("Neck pain")
        radiologistList.add("Pancreatic cancer")
        radiologistList.add("Pituitary tumors")
        radiologistList.add("Testicular cancer")
        radiologistList.add("Thyroid cancer")

        //List initializing for Pulmonologist treated diseases
        pulmonologistList.add("Not sure")
        pulmonologistList.add("Asthma")
        pulmonologistList.add("Chest pain or tightness")
        pulmonologistList.add("COVID-19")
        pulmonologistList.add("Interstitial lung disease")
        pulmonologistList.add("Pulmonary hypertension")
        pulmonologistList.add("Tuberculosis")

        //List initializing for Pulmonologist treated diseases
        neurologistList.add("Not sure")
        neurologistList.add("Acute Spinal Cord Injury")
        neurologistList.add("Alzheimer's Disease")
        neurologistList.add("Amyotrophic Lateral Sclerosis")
        neurologistList.add("Brain Tumors")
        neurologistList.add("Cerebral Aneurys")

        //List initializing for Urologists treated diseases
        urologistsList.add("Not sure")
        urologistsList.add("Kidney Stones")
        urologistsList.add("Bladder Infection")
        urologistsList.add("Urinary Retention")
        urologistsList.add("Hematuria")
        urologistsList.add("Erectile Dysfunction")
        urologistsList.add("Prostate Enlargement")
        urologistsList.add("Interstitial Cystitis")

        //List initializing for Otolaryngologist treated diseases
        otolaryngologistsList.add("Not sure")
        otolaryngologistsList.add("Hearing loss")
        otolaryngologistsList.add("Ear infections")
        otolaryngologistsList.add("Balance disorders")
        otolaryngologistsList.add("Diseases of the larynx")
        otolaryngologistsList.add("Nerve pain")
        otolaryngologistsList.add("Facial and cranial nerve disorders")

        //List initializing for Rheumatologists treated diseases
        rheumatologistsList.add("Not sure")
        rheumatologistsList.add("Vasculitis")
        rheumatologistsList.add("Rheumatoid arthritis")
        rheumatologistsList.add("Lupus")
        rheumatologistsList.add("Scleroderma")

        //List initializing for Anesthesiologists treated diseases
        anesthesiologistsList.add("Not sure")
        anesthesiologistsList.add("Back pain or muscle pain")
        anesthesiologistsList.add("Chills caused by low body temperature")
        anesthesiologistsList.add("Difficulty urinating")
        anesthesiologistsList.add("Fatigue")
        anesthesiologistsList.add("Headache")
        anesthesiologistsList.add("Itching")
        anesthesiologistsList.add("Infectious arthritis")

        //List initializing for Gastroenterologist treated diseases
        gastroenterologistsList.add("Not sure")
        gastroenterologistsList.add("Diarrhea")
        gastroenterologistsList.add("Food Poisoning")
        gastroenterologistsList.add("Gas")
        gastroenterologistsList.add("Gastroparesis")
        gastroenterologistsList.add("Irritable Bowel Syndrome")
        gastroenterologistsList.add("Liver Disease")
        gastroenterologistsList.add("Pancreatitis")
        gastroenterologistsList.add("Stomach ache")


        //List initializing for  Allergists treated diseases
        allergistsList.add("Not sure")
        allergistsList.add("Asthma")
        allergistsList.add("Skin Allergies")
        allergistsList.add("Vomiting or diarrhea")
        allergistsList.add("Drop in blood pressure")
        allergistsList.add("Redness of the skin and/or hives")
        allergistsList.add("Difficulty breathing")
        allergistsList.add("Swelling of the throat and/or tongue")


        mapOfDiseasesList["Allergists"] = allergistsList
        mapOfDiseasesList["Anesthesiologists"] = anesthesiologistsList
        mapOfDiseasesList["Cardiologist"] = cardiologistList
        mapOfDiseasesList["Dentist"] = dentistList
        mapOfDiseasesList["ENT specialist"] = entList
        mapOfDiseasesList["Gastroenterologists"] = gastroenterologistsList
        mapOfDiseasesList["Psychiatrists"] = psychiatristsList
        mapOfDiseasesList["Radiologist"] = radiologistList
        mapOfDiseasesList["Pulmonologist"] = pulmonologistList
        mapOfDiseasesList["Neurologist"] = neurologistList
        mapOfDiseasesList["Otolaryngologists"] = otolaryngologistsList
        mapOfDiseasesList["Obstetrician/Gynaecologist"] = gynaecologistList
        mapOfDiseasesList["Orthopaedic surgeon"] = orthopaedicList
        mapOfDiseasesList["Urologists"] = urologistsList
        mapOfDiseasesList["Rheumatologists"] = rheumatologistsList


    }

    private fun setConditionValue(conditionValue: HashMap<String, Float>) {
        conditionValue["Severe Pain"] = 1.5f
        conditionValue["Mild Pain"] = 1.2f
        conditionValue["No Pain"] = 0.5f

    }

    private fun setDiseaseValues(diseaseValue: HashMap<String, Float>) {
        diseaseValue["Not sure"] = 6f

        diseaseValue["High blood pressure"] = 6f
        diseaseValue["High cholesterol"] = 5f
        diseaseValue["Angina (chest pain)"] = 4f
        diseaseValue["Heart rhythm disorders"] = 5f
        diseaseValue["Atrial fibrillation"] = 4f

        diseaseValue["Tooth Decay/Cavities"] = 6f
        diseaseValue["Gum Disease"] = 5f
        diseaseValue["Cracked or Broken Teeth"] = 2f
        diseaseValue["Root Infection"] = 4f
        diseaseValue["Tooth Loss"] = 1f

        diseaseValue["Hearing problems"] = 6f
        diseaseValue["Allergies"] = 4f
        diseaseValue["Nasal congestion"] = 5f
        diseaseValue["Tonsil infections"] = 5f
        diseaseValue["Enlarged tonsils"] = 4f

        diseaseValue["Bleeding during pregnancy"] = 6f
        diseaseValue["Female infertility"] = 5f
        diseaseValue["Heart disease in pregnancy"] = 6f
        diseaseValue["Menopause"] = 4f
        diseaseValue["Menstrual cramps"] = 6f
        diseaseValue["Miscarriage"] = 5f
        diseaseValue["Ovarian cysts"] = 3f
        diseaseValue["Vaginal bleeding"] = 5f

        diseaseValue["Bone fractures"] = 6f
        diseaseValue["Muscle strains"] = 5f
        diseaseValue["Joint or back pain"] = 2f
        diseaseValue["Injuries to tendons or ligaments"] = 5f
        diseaseValue["Limb abnormalities"] = 4f
        diseaseValue["Bone cancer"] = 4f

        diseaseValue["Alcohol use disorder"] = 4f
        diseaseValue["Alzheimer’s disease"] = 6f
        diseaseValue["Anxiety disorders"] = 5f
        diseaseValue["Bipolar disorder"] = 5f
        diseaseValue["Depression"] = 6f
        diseaseValue["Eating disorder"] = 5f
        diseaseValue["Mood disorders"] = 4f
        diseaseValue["Panic disorder"] = 3f
        diseaseValue["Sleep disorders"] = 4f

        diseaseValue["Brain tumor"] = 6f
        diseaseValue["Breast cancer"] = 5f
        diseaseValue["Kidney stones"] = 4f
        diseaseValue["Liver tumors"] = 5f
        diseaseValue["Lung cancer"] = 6f
        diseaseValue["Neck pain"] = 2f
        diseaseValue["Pancreatic cancer"] = 3f
        diseaseValue["Pituitary tumors"] = 5f
        diseaseValue["Testicular cancer"] = 6f
        diseaseValue["Thyroid cancer"] = 5f

        diseaseValue["Asthma"] = 6f
        diseaseValue["Chest pain or tightness"] = 2f
        diseaseValue["COVID-19"] = 3f
        diseaseValue["Interstitial lung disease"] = 3f
        diseaseValue["Pulmonary hypertension"] = 4f
        diseaseValue["Tuberculosis"] = 5f

        diseaseValue["Acute Spinal Cord Injury"] = 6f
        diseaseValue["Amyotrophic Lateral Sclerosis"] = 5f
        diseaseValue["Brain Tumors"] = 6f
        diseaseValue["Cerebral Aneurys"] = 5.5f

        diseaseValue["Kidney Stones"] = 5f
        diseaseValue["Bladder Infection"] = 5f
        diseaseValue["Urinary Retention"] = 2f
        diseaseValue["Hematuria"] = 6f
        diseaseValue["Erectile Dysfunction"] = 6f
        diseaseValue["Prostate Enlargement"] = 5f
        diseaseValue["Interstitial Cystitis"] = 4f

        diseaseValue["Hearing loss"] = 6f
        diseaseValue["Ear infections"] = 5f
        diseaseValue["Balance disorders"] = 4f
        diseaseValue["Diseases of the larynx"] = 3f
        diseaseValue["Nerve pain"] = 6f
        diseaseValue["Facial and cranial nerve disorders"] = 6f

        diseaseValue["Vasculitis"] = 6f
        diseaseValue["Lupus"] = 5f
        diseaseValue["Rheumatoid arthritis"] = 6f
        diseaseValue["Scleroderma"] = 4f

        diseaseValue["Back pain or muscle pain"] = 5f
        diseaseValue["Chills caused by low body temperature"] = 4f
        diseaseValue["Difficulty urinating"] = 6f
        diseaseValue["Fatigue"] = 2f
        diseaseValue["Headache"] = 4f
        diseaseValue["Itching"] = 3f
        diseaseValue["Infectious arthritis"] = 4f

        diseaseValue["Diarrhea"] = 4f
        diseaseValue["Food Poisoning"] = 5f
        diseaseValue["Gas"] = 2f
        diseaseValue["Gastroparesis"] = 4f
        diseaseValue["Irritable Bowel Syndrome"] = 4f
        diseaseValue["Liver Disease"] = 5f
        diseaseValue["Pancreatitis"] = 6f
        diseaseValue["Stomach ache"] = 4f

        diseaseValue["Skin Allergies"] = 2f
        diseaseValue["Vomiting or diarrhea"] = 4f
        diseaseValue["Drop in blood pressure"] = 5f
        diseaseValue["Redness of the skin and/or hive"] = 4f
        diseaseValue["Difficulty breathing"] = 6f
        diseaseValue["Swelling of the throat and/or tongue"] = 5f
    }

}

