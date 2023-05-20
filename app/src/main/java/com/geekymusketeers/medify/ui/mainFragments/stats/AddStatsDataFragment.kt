package com.geekymusketeers.medify.ui.mainFragments.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.FragmentAddStatsDataBinding
import com.geekymusketeers.medify.databinding.FragmentStatisticsBinding


class AddStatsDataFragment : Fragment() {

    private var _binding: FragmentAddStatsDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding= FragmentAddStatsDataBinding.inflate(inflater, container, false)

        return binding.root
    }


}