package com.geekymusketeers.medify.mainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.FragmentAppointmentBinding
import com.geekymusketeers.medify.databinding.FragmentSettingsBinding


class AppointmentFragment : Fragment() {

    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)

        // CODE



        return binding.root
    }

}