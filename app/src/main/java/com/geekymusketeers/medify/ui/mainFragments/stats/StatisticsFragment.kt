package com.geekymusketeers.medify.ui.mainFragments.stats

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.base.ViewModelFactory
import com.geekymusketeers.medify.databinding.FragmentStatisticsBinding
import com.geekymusketeers.medify.model.HealthData
import com.geekymusketeers.medify.ui.adapter.StatsListAdapter
import com.geekymusketeers.medify.utils.Constants
import com.geekymusketeers.medify.utils.Logger
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
    private val statisticsViewModel by viewModels<StatisticsViewModel> { ViewModelFactory() }
    private lateinit var statsListAdapter: StatsListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        sharedPreference =
            requireActivity().getSharedPreferences(Constants.UserData, Context.MODE_PRIVATE)

        initObserver()
        initView()

        return binding.root
    }

    private fun initView() {
        binding.run {
            fabCircle.setOnClickListener {
                val action = StatisticsFragmentDirections.actionStatsToAddStatsDataFragment(
                    HealthData(null)
                )
                findNavController().navigate(action)
            }
            statsListAdapter = StatsListAdapter {
                val action = StatisticsFragmentDirections.actionStatsToAddStatsDataFragment(it)
                findNavController().navigate(action)
            }
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = statsListAdapter
            }
        }
    }

    private fun initObserver() {
        statisticsViewModel.run {
            getDataFromSharedPreference(sharedPreference)
            userLiveData.observe(viewLifecycleOwner) {
                setStatsList()
            }
            statsList.observe(viewLifecycleOwner) {
                Logger.debugLog("StatsList from statsFrag: $it")
                if (it != null)
                    statsListAdapter.addItems(it)
            }
        }
    }

}