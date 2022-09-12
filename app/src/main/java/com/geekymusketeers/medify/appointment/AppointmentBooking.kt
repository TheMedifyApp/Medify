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

        val diseaseValue = HashMap<String, Int>()
        setDiseaseValues(diseaseValue)

        val conditionValue = HashMap<String, Int>()
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

                totalPoint += diseaseValue[disease]!!
                totalPoint += conditionValue[situation]!!
                totalPoint = (totalPoint * firstComeFirstServe).toInt()

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
        cardiologistList.add("Not Sure")
        cardiologistList.add("High blood pressure")
        cardiologistList.add("High cholesterol")
        cardiologistList.add("Angina (chest pain)")
        cardiologistList.add("Heart rhythm disorders")
        cardiologistList.add("Atrial fibrillation")

        //List initializing for Dental diseases
        dentistList.add("Not Sure")
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

    private fun setConditionValue(conditionValue: HashMap<String, Int>) {
        conditionValue["Severe Pain"] = 15
        conditionValue["Mild Pain"] = 8
        conditionValue["No Pain"] = 0

    }

    private fun setDiseaseValues(diseaseValue: HashMap<String, Int>) {
        diseaseValue["Not sure"] = 10

        diseaseValue["High blood pressure"] = 8
        diseaseValue["High cholesterol"] = 7
        diseaseValue["Angina (chest pain)"] = 5
        diseaseValue["Heart rhythm disorders"] = 7
        diseaseValue["Atrial fibrillation"] = 7

        diseaseValue["Tooth Decay/Cavities"] = 9
        diseaseValue["Gum Disease"] = 8
        diseaseValue["Cracked or Broken Teeth"] = 6
        diseaseValue["Root Infection"] = 7
        diseaseValue["Tooth Loss"] = 4

        diseaseValue["Hearing problems"] = 8
        diseaseValue["Allergies"] = 6
        diseaseValue["Nasal congestion"] = 6
        diseaseValue["Tonsil infections"] = 7
        diseaseValue["Enlarged tonsils"] = 6

        diseaseValue["Bleeding during pregnancy"] = 9
        diseaseValue["Female infertility"] = 6
        diseaseValue["Heart disease in pregnancy"] = 9
        diseaseValue["Menopause"] = 8
        diseaseValue["Menstrual cramps"] = 9
        diseaseValue["Miscarriage"] = 7
        diseaseValue["Ovarian cysts"] = 8
        diseaseValue["Vaginal bleeding"] = 8

        diseaseValue["Bone fractures"] = 9
        diseaseValue["Muscle strains"] = 7
        diseaseValue["Joint or back pain"] = 6
        diseaseValue["Injuries to tendons or ligaments"] = 8
        diseaseValue["Limb abnormalities"] = 9
        diseaseValue["Bone cancer"] = 8

        diseaseValue["Alcohol use disorder"] = 7
        diseaseValue["Alzheimer’s disease"] = 9
        diseaseValue["Anxiety disorders"] = 8
        diseaseValue["Bipolar disorder"] = 8
        diseaseValue["Depression"] = 10
        diseaseValue["Eating disorder"] = 8
        diseaseValue["Mood disorders"] = 7
        diseaseValue["Panic disorder"] = 8
        diseaseValue["Sleep disorders"] = 6

        diseaseValue["Brain tumor"] = 10
        diseaseValue["Breast cancer"] = 8
        diseaseValue["Kidney stones"] = 7
        diseaseValue["Liver tumors"] = 8
        diseaseValue["Lung cancer"] = 9
        diseaseValue["Neck pain"] = 4
        diseaseValue["Pancreatic cancer"] = 6
        diseaseValue["Pituitary tumors"] = 8
        diseaseValue["Testicular cancer"] = 9
        diseaseValue["Thyroid cancer"] = 8

        diseaseValue["Asthma"] = 10
        diseaseValue["Chest pain or tightness"] = 6
        diseaseValue["COVID-19"] = 4
        diseaseValue["Interstitial lung disease"] = 7
        diseaseValue["Pulmonary hypertension"] = 8
        diseaseValue["Tuberculosis"] = 8

        diseaseValue["Acute Spinal Cord Injury"] = 9
        diseaseValue["Amyotrophic Lateral Sclerosis"] = 8
        diseaseValue["Brain Tumors"] = 10
        diseaseValue["Cerebral Aneurys"] = 9

        diseaseValue["Kidney Stones"] = 8
        diseaseValue["Bladder Infection"] = 8
        diseaseValue["Urinary Retention"] = 6
        diseaseValue["Hematuria"] = 9
        diseaseValue["Erectile Dysfunction"] = 9
        diseaseValue["Prostate Enlargement"] = 8
        diseaseValue["Interstitial Cystitis"] = 8

        diseaseValue["Hearing loss"] = 10
        diseaseValue["Ear infections"] = 8
        diseaseValue["Balance disorders"] = 7
        diseaseValue["Diseases of the larynx"] = 6
        diseaseValue["Nerve pain"] = 9
        diseaseValue["Facial and cranial nerve disorders"] = 9

        diseaseValue["Vasculitis"] = 9
        diseaseValue["Lupus"] = 8
        diseaseValue["Rheumatoid arthritis"] = 9
        diseaseValue["Scleroderma"] = 7

        diseaseValue["Back pain or muscle pain"] = 8
        diseaseValue["Chills caused by low body temperature"] = 7
        diseaseValue["Difficulty urinating"] = 9
        diseaseValue["Fatigue"] = 6
        diseaseValue["Headache"] = 8
        diseaseValue["Itching"] = 5
        diseaseValue["Infectious arthritis"] = 8

        diseaseValue["Diarrhea"] = 7
        diseaseValue["Food Poisoning"] = 8
        diseaseValue["Gas"] = 5
        diseaseValue["Gastroparesis"] = 7
        diseaseValue["Irritable Bowel Syndrome"] = 7
        diseaseValue["Liver Disease"] = 8
        diseaseValue["Pancreatitis"] = 9
        diseaseValue["Stomach ache"] = 7

        diseaseValue["Skin Allergies"] = 6
        diseaseValue["Vomiting or diarrhea"] = 7
        diseaseValue["Drop in blood pressure"] = 8
        diseaseValue["Redness of the skin and/or hive"] = 7
        diseaseValue["Difficulty breathing"] = 9
        diseaseValue["Swelling of the throat and/or tongue"] = 8
    }

}

