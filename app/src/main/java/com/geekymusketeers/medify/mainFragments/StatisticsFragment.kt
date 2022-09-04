package com.geekymusketeers.medify.mainFragments

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.number.IntegerWidth
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.FragmentSettingsBinding
import com.geekymusketeers.medify.databinding.FragmentStatisticsBinding
import java.util.Collections.max
import java.util.Collections.min


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        setBloodPressure()
        setSugarFasting()
        setSugarPP()
        setCholesterol()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setCholesterol() {
        val cholesterolList: ArrayList<Int> = arrayListOf(110,117,118,115, 124, 122, 124, 114, 110, 125)
        val cholesterolMin = min(cholesterolList)
        val cholesterolMax = max(cholesterolList)
        binding.cholesterolRange.text = "Min: $cholesterolMin, Max: $cholesterolMax"
        binding.cholesterol.setData(cholesterolList)

    }

    @SuppressLint("SetTextI18n")
    private fun setSugarPP() {
        val sugarPPList: ArrayList<Int> = arrayListOf(110,117,118,115, 124, 122, 124, 114, 110, 125)
        val sugarPPMin = min(sugarPPList)
        val sugarPPMax = max(sugarPPList)
        binding.sugarPPRange.text = "Min: $sugarPPMin, Max: $sugarPPMax"
        binding.sugarPP.setData(sugarPPList)
    }

    @SuppressLint("SetTextI18n")
    private fun setSugarFasting() {
        val sugarFastingList: ArrayList<Int> = arrayListOf(110,117,118,115, 124, 122, 124, 114, 110, 125)
        val sugarFastingMin = min(sugarFastingList)
        val sugarFastingMax = max(sugarFastingList)
        binding.sugarFastingRange.text = "Min: $sugarFastingMin, Max: $sugarFastingMax"
        binding.sugarFasting.setData(sugarFastingList)
    }

    @SuppressLint("SetTextI18n")
    private fun setBloodPressure() {
        val bloodPressureList: ArrayList<Int> = arrayListOf(110,117,118,115, 124, 122, 124, 114, 110, 125)
        val bloodPressureMin = min(bloodPressureList)
        val bloodPressureMax = max(bloodPressureList)
        binding.bloodPressureRange.text = "Min: $bloodPressureMin, Max: $bloodPressureMax"
        binding.bloodPressure.setData(bloodPressureList)
    }

}