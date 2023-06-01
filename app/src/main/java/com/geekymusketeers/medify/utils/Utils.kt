package com.geekymusketeers.medify.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.model.Doctor
import com.geekymusketeers.medify.model.Gender
import com.geekymusketeers.medify.model.Specialist


object Utils {

    @SuppressLint("QueryPermissionsNeeded", "IntentReset")
    fun sendEmailToGmail(activity: Activity, subject: String?, body: String?, email: String?) {
        val emailIntent = Intent().apply {
            action = Intent.ACTION_SEND
            data = Uri.parse("mailto:")
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        if (emailIntent.resolveActivity(activity.packageManager) != null) {
            emailIntent.setPackage("com.google.android.gm")
            startActivity(activity, emailIntent, null)
        } else {
            Toast.makeText(
                activity,
                "No app available to send email!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun containsLetters(phone: String, searchedData: String): Boolean {
        return phone.trim().lowercase().toStringWithoutSpaces().contains(
            searchedData.lowercase().trim().toStringWithoutSpaces()
        )
    }

    fun View.setNonDuplicateClickListener(listener: View.OnClickListener?) {
        setOnClickListener {
            var lastClickTime: Long = 0
            if (getTag(R.id.TAG_CLICK_TIME) != null) {
                lastClickTime = getTag(R.id.TAG_CLICK_TIME) as Long
            }
            val curTime = System.currentTimeMillis()
            if (curTime - lastClickTime > context.resources.getInteger(R.integer.duplicate_click_delay)) {
                listener?.onClick(this)
                setTag(R.id.TAG_CLICK_TIME, curTime)
            }
        }
    }

    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.GONE
    }

    fun View.invisible() {
        visibility = View.INVISIBLE
    }

    fun String.toStringWithoutSpaces() : String {
        val stringBuilder = StringBuilder()
        for (char in this.toCharArray())
            if (char.isDigit() or char.isLetter())
                stringBuilder.append(char)
        return stringBuilder.toString()
    }

    fun getListOfSpecialization() : List<String> {
        return listOf(
            Specialist.ALLERGISTS.toItemString(),
            Specialist.ENT_SPECIALIST.toItemString(),
            Specialist.CARDIOLOGIST.toItemString(),
            Specialist.DENTIST.toItemString(),
            Specialist.ENT_SPECIALIST.toItemString(),
            Specialist.OBSTETRICIAN_GYNAECOLOGIST.toItemString(),
            Specialist.ORTHOPAEDIC_SURGEON.toItemString(),
            Specialist.PSYCHIATRIST.toItemString(),
            Specialist.RADIOLOGIST.toItemString(),
            Specialist.PULMONOLOGIST.toItemString(),
            Specialist.NEUROLOGIST.toItemString(),
            Specialist.ALLERGISTS.toItemString(),
            Specialist.GASTROENTEROLOGISTS.toItemString(),
        )
    }

    fun getListOfIsDoctor() : List<String> {
        return listOf(
            Doctor.IS_DOCTOR.toDisplayString(),
            Doctor.IS_NOT_DOCTOR.toDisplayString()
        )
    }

    fun getListOfGenders() : List<String> {
        return listOf(
            Gender.MALE.toDisplayString(),
            Gender.FEMALE.toDisplayString(),
            Gender.OTHER.toDisplayString()
        )
    }

    fun setDiseaseValues(context: Context): HashMap<String, Float> {
        val diseaseValue: HashMap<String, Float> = HashMap()

        // Diseases and their corresponding values are added to the diseaseValue map
        diseaseValue[context.getString(R.string.disease_not_sure)] = 6f

        // Cardiovascular diseases
        diseaseValue[context.getString(R.string.disease_high_blood_pressure)] = 6f
        diseaseValue[context.getString(R.string.disease_high_cholesterol)] = 5f
        diseaseValue[context.getString(R.string.disease_angina)] = 4f
        diseaseValue[context.getString(R.string.disease_heart_rhythm_disorders)] = 5f
        diseaseValue[context.getString(R.string.disease_atrial_fibrillation)] = 4f

        // Dental diseases
        diseaseValue[context.getString(R.string.disease_tooth_decay)] = 6f
        diseaseValue[context.getString(R.string.disease_gum_disease)] = 5f
        diseaseValue[context.getString(R.string.disease_cracked_teeth)] = 3f
        diseaseValue[context.getString(R.string.disease_root_infection)] = 4f
        diseaseValue[context.getString(R.string.disease_tooth_loss)] = 2f

        // Ear, nose, and throat diseases
        diseaseValue[context.getString(R.string.disease_hearing_problems)] = 6f
        diseaseValue[context.getString(R.string.disease_allergies)] = 4f
        diseaseValue[context.getString(R.string.disease_nasal_congestion)] = 5f
        diseaseValue[context.getString(R.string.disease_tonsil_infections)] = 5f
        diseaseValue[context.getString(R.string.disease_enlarged_tonsils)] = 4f

        // Women's health-related diseases
        diseaseValue[context.getString(R.string.disease_pregnancy_bleeding)] = 6f
        diseaseValue[context.getString(R.string.disease_female_infertility)] = 5f
        diseaseValue[context.getString(R.string.disease_heart_disease_pregnancy)] = 6f
        diseaseValue[context.getString(R.string.disease_menopause)] = 4f
        diseaseValue[context.getString(R.string.disease_menstrual_cramps)] = 6f
        diseaseValue[context.getString(R.string.disease_miscarriage)] = 5f
        diseaseValue[context.getString(R.string.disease_ovarian_cysts)] = 3f
        diseaseValue[context.getString(R.string.disease_vaginal_bleeding)] = 5f

        // Musculoskeletal diseases
        diseaseValue[context.getString(R.string.disease_bone_fractures)] = 6f
        diseaseValue[context.getString(R.string.disease_muscle_strains)] = 5f
        diseaseValue[context.getString(R.string.disease_joint_back_pain)] = 3f
        diseaseValue[context.getString(R.string.disease_injuries_tendons_ligaments)] = 5f
        diseaseValue[context.getString(R.string.disease_limb_abnormalities)] = 4f
        diseaseValue[context.getString(R.string.disease_bone_cancer)] = 4f

        // Mental health-related diseases
        diseaseValue[context.getString(R.string.disease_alcohol_use_disorder)] = 4f
        diseaseValue[context.getString(R.string.disease_alzheimers)] = 6f
        diseaseValue[context.getString(R.string.disease_anxiety_disorders)] = 5f
        diseaseValue[context.getString(R.string.disease_bipolar_disorder)] = 5f
        diseaseValue[context.getString(R.string.disease_depression)] = 6f
        diseaseValue[context.getString(R.string.disease_eating_disorder)] = 5f
        diseaseValue[context.getString(R.string.disease_mood_disorders)] = 4f
        diseaseValue[context.getString(R.string.disease_panic_disorder)] = 4f
        diseaseValue[context.getString(R.string.disease_sleep_disorders)] = 4f

        // Cancer-related diseases
        diseaseValue[context.getString(R.string.disease_brain_tumor)] = 6f
        diseaseValue[context.getString(R.string.disease_breast_cancer)] = 5f
        diseaseValue[context.getString(R.string.disease_kidney_stones)] = 4f
        diseaseValue[context.getString(R.string.disease_liver_tumors)] = 5f

        return diseaseValue
    }

    fun setConditionValue(context: Context): HashMap<String, Float> {
        val conditionValue: HashMap<String, Float> = HashMap()
        conditionValue[context.getString(R.string.condition_severe_pain)] = 2.0f
        conditionValue[context.getString(R.string.condition_mild_pain)] = 1.5f
        conditionValue[context.getString(R.string.condition_no_pain)] = 1.0f
        return conditionValue
    }

    fun initializeSpecializationWithDiseasesLists(): HashMap<String, ArrayList<String>> {
        val mapOfDiseasesList: HashMap<String, ArrayList<String>> = HashMap()

        val specialistsAndDiseases = listOf(
            "Allergists" to arrayOf(
                "Not sure",
                "Asthma",
                "Skin Allergies",
                "Vomiting or diarrhea",
                "Drop in blood pressure",
                "Redness of the skin and/or hives",
                "Difficulty breathing",
                "Swelling of the throat and/or tongue"
            ),
            "Anesthesiologists" to arrayOf(
                "Not sure",
                "Back pain or muscle pain",
                "Chills caused by low body temperature",
                "Difficulty urinating",
                "Fatigue",
                "Headache",
                "Itching",
                "Infectious arthritis"
            ),
            "Cardiologist" to arrayOf(
                "Not sure",
                "High blood pressure",
                "High cholesterol",
                "Angina (chest pain)",
                "Heart rhythm disorders",
                "Atrial fibrillation"
            ),
            "Dentist" to arrayOf(
                "Not sure",
                "Tooth Decay/Cavities",
                "Gum Disease",
                "Cracked or Broken Teeth",
                "Root Infection",
                "Tooth Loss"
            ),
            "ENT specialist" to arrayOf(
                "Not sure",
                "Hearing problems",
                "Allergies",
                "Nasal congestion",
                "Tonsil infections",
                "Enlarged tonsils"
            ),
            "Gastroenterologists" to arrayOf(
                "Not sure",
                "Diarrhea",
                "Food Poisoning",
                "Gas",
                "Gastroparesis",
                "Irritable Bowel Syndrome",
                "Liver Disease",
                "Pancreatitis",
                "Stomach ache"
            ),
            "Psychiatrists" to arrayOf(
                "Not sure",
                "Alcohol use disorder",
                "Alzheimer’s disease",
                "Anxiety disorders",
                "Bipolar disorder",
                "Depression",
                "Eating disorders",
                "Mood disorders",
                "Panic disorder",
                "Sleep disorders"
            ),
            "Radiologist" to arrayOf(
                "Not sure",
                "Brain tumor",
                "Breast cancer",
                "Kidney stones",
                "Liver tumors",
                "Lung cancer",
                "Neck pain",
                "Pancreatic cancer",
                "Pituitary tumors",
                "Testicular cancer",
                "Thyroid cancer"
            ),
            "Pulmonologist" to arrayOf(
                "Not sure",
                "Asthma",
                "Chest pain or tightness",
                "COVID-19",
                "Interstitial lung disease",
                "Pulmonary hypertension",
                "Tuberculosis"
            ),
            "Neurologist" to arrayOf(
                "Not sure",
                "Acute Spinal Cord Injury",
                "Alzheimer's Disease",
                "Amyotrophic Lateral Sclerosis",
                "Brain Tumors",
                "Cerebral Aneurys"
            ),
            "Otolaryngologists" to arrayOf(
                "Not sure",
                "Hearing loss",
                "Ear infections",
                "Balance disorders",
                "Diseases of the larynx",
                "Nerve pain",
                "Facial and cranial nerve disorders"
            ),
            "Obstetrician/Gynaecologist" to arrayOf(
                "Not sure",
                "Bleeding during pregnancy",
                "Female infertility",
                "Heart disease in pregnancy",
                "Menopause",
                "Menstrual cramps",
                "Miscarriage",
                "Ovarian cysts",
                "Vaginal bleeding"
            ),
            "Orthopaedic surgeon" to arrayOf(
                "Not sure",
                "Bone fractures",
                "Muscle strains",
                "Joint or back pain",
                "Injuries to tendons or ligaments",
                "Limb abnormalities",
                "Bone cancer"
            ),
            "Urologists" to arrayOf(
                "Not sure",
                "Kidney Stones",
                "Bladder Infection",
                "Urinary Retention",
                "Hematuria",
                "Erectile Dysfunction",
                "Prostate Enlargement",
                "Interstitial Cystitis"
            ),
            "Rheumatologists" to arrayOf(
                "Not sure",
                "Vasculitis",
                "Rheumatoid arthritis",
                "Lupus",
                "Scleroderma"
            )
        )

        for ((specialization, diseases) in specialistsAndDiseases) {
            mapOfDiseasesList[specialization] = diseases.toList() as java.util.ArrayList<String>
        }

        return mapOfDiseasesList
    }

    fun makePhoneCall(activity: Activity, phone: String?) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        activity.startActivity(intent)
    }
}