package com.geekymusketeers.medify.mainFragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.FragmentStatisticsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.util.Collections.max
import java.util.Collections.min
import kotlin.collections.ArrayList


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreference: SharedPreferences

    //Current User's data
    private lateinit var userID: String
    private lateinit var stats: String
    private lateinit var db: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        sharedPreference = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        db = FirebaseDatabase.getInstance().reference
        getDataFromSharedPreference()
        val splitParts = stats.split("?")

        setBloodPressure(splitParts[0], splitParts)
        setSugarFasting(splitParts[1], splitParts)
        setSugarPP(splitParts[2], splitParts)
        setCholesterol(splitParts[3], splitParts)

        return binding.root
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    private fun setBloodPressure(Concat: String, splitParts: List<String>) {
        val splitBloodPressure = Concat.split(":")
        var bloodPressureList: ArrayList<Int> = ArrayList()
        for (i in 0..4) {
            bloodPressureList.add(Integer.parseInt(splitBloodPressure[i]))
        }
        val bloodPressureMin = min(bloodPressureList)
        val bloodPressureMax = max(bloodPressureList)
        if (bloodPressureMax == bloodPressureMax) {
            binding.bloodPressureRange.text = "Value is constant!"
            binding.bloodPressureRange.setTextColor(ContextCompat.getColor(requireActivity(), R.color.light_blue))
        }
        binding.bloodPressureRange.text = "Min: $bloodPressureMin, Max: $bloodPressureMax"
        binding.bloodPressure.setData(bloodPressureList)

        binding.addBloodPressureData.setOnClickListener {
            addNewData(0, bloodPressureList, splitParts)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setSugarFasting(Concat: String, splitParts: List<String>) {
        val splitSugarFasting = Concat.split(":")
        val sugarFastingList: ArrayList<Int> = ArrayList()
        for (i in 0..4) {
            sugarFastingList.add(Integer.parseInt(splitSugarFasting[i]))
        }
        val sugarFastingMin = min(sugarFastingList)
        val sugarFastingMax = max(sugarFastingList)
        binding.sugarFastingRange.text = "Min: $sugarFastingMin, Max: $sugarFastingMax"
        binding.sugarFasting.setData(sugarFastingList)

        binding.addSugarFastingData.setOnClickListener {
            addNewData(1, sugarFastingList, splitParts)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setSugarPP(Concat: String, splitParts: List<String>) {
        val splitSugarPP = Concat.split(":")
        val sugarPPList: ArrayList<Int> = ArrayList()
        for (i in 0..4) {
            sugarPPList.add(Integer.parseInt(splitSugarPP[i]))
        }
        val sugarPPMin = min(sugarPPList)
        val sugarPPMax = max(sugarPPList)
        binding.sugarPPRange.text = "Min: $sugarPPMin, Max: $sugarPPMax"
        binding.sugarPP.setData(sugarPPList)

        binding.addSugarPPData.setOnClickListener {
            addNewData(2, sugarPPList, splitParts)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCholesterol(Concat: String, splitParts: List<String>) {
        val splitCholesterol = Concat.split(":")
        val cholesterolList: ArrayList<Int> = ArrayList()
        for (i in 0..4) {
            cholesterolList.add(Integer.parseInt(splitCholesterol[i]))
        }
        val cholesterolMin = min(cholesterolList)
        val cholesterolMax = max(cholesterolList)
        binding.cholesterolRange.text = "Min: $cholesterolMin, Max: $cholesterolMax"
        binding.cholesterol.setData(cholesterolList)

        binding.addCholesterolData.setOnClickListener {
            addNewData(3, cholesterolList, splitParts)
        }
    }

    private fun addNewData(ind: Int, List: ArrayList<Int>, splitParts: List<String>) {
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(requireActivity())
        builder.setTitle("Title")

        // Set up the input
        val input = EditText(requireActivity())
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.hint = "Enter new data"
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
        // Set up the buttons
        builder.setPositiveButton("Set", DialogInterface.OnClickListener { _, _ ->
            // Here you get get input text from the Edittext
            var newDataValue = input.text.toString().trim()

            val queue: LinkedList<Int> = LinkedList(List)
            queue.poll()
            queue.offer(Integer.parseInt(newDataValue))
            List.clear()
            for (i in 0..4) {
                List.add(queue.poll())
            }
            val editor = sharedPreference.edit()
            val merged = mergedData(ind, List, splitParts)
            db.child("Users").child(userID).child("stats").setValue(merged)
            editor.putString("stats", merged)
            editor.apply()
            val fragmentId = findNavController().currentDestination?.id
            findNavController().popBackStack(fragmentId!!, true)
            findNavController().navigate(fragmentId)
        })
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()

    }

    private fun mergedData(i: Int, List: ArrayList<Int>, splitParts: List<String>): String? {
        val tempString: StringBuilder = StringBuilder()
        for (j in 0..4) {
            tempString.append(List[j]).append(":")
        }
        tempString.setLength(tempString.length - 1)
        val finalStats: StringBuilder = StringBuilder()
        for (j in 0..3) {
            if (j == i) {
                finalStats.append(tempString).append("?")
            } else {
                finalStats.append(splitParts.get(j)).append("?")
            }
        }
        finalStats.setLength(finalStats.length - 1)
        return finalStats.toString()
    }


    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            getDataFromSharedPreference()
        }, 1000)
    }

    @SuppressLint("SetTextI18n")
    private fun getDataFromSharedPreference() {
        userID = sharedPreference.getString("uid", "Not found").toString()
        stats = sharedPreference.getString("stats", "0:0:6:0:0?0:0:0:0:0?0:0:0:0:0?0:0:0:0:0")
            .toString()
    }

}