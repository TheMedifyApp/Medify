package com.geekymusketeers.medify.ui.mainFragments.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.FragmentBookingSummaryBinding


class BookingSummaryFragment : Fragment() {

    private lateinit var _binding: FragmentBookingSummaryBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingSummaryBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView() {

    }

}