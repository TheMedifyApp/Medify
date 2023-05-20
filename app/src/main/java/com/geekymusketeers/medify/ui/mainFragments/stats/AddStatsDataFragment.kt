package com.geekymusketeers.medify.ui.mainFragments.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.geekymusketeers.medify.base.ViewModelFactory
import com.geekymusketeers.medify.databinding.FragmentAddStatsDataBinding


class AddStatsDataFragment : Fragment() {

    private var _binding: FragmentAddStatsDataBinding? = null
    private val args by navArgs<AddStatsDataFragmentArgs>()
    private val addStatsDataViewModel by viewModels<AddStatsDataViewModel> { ViewModelFactory() }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddStatsDataBinding.inflate(inflater, container, false)

        initObserver()
        initView()

        return binding.root
    }

    private fun initObserver() {
        addStatsDataViewModel.run {
            if (args.stats.healthId != null) {
                setHealthData(args.stats)
            } else {
                setHealthId()
            }
            testName.observe(viewLifecycleOwner) {
                binding.testNameEditText.setEditTextBox(it)
            }
            statsTreeMap.observe(viewLifecycleOwner) {
                Toast.makeText(context, "Added new data", Toast.LENGTH_SHORT).show() //Temporary
            }
            enableButton.observe(viewLifecycleOwner) {
                binding.saveButton.isEnabled = it
                binding.saveButton.setButtonEnabled(it)
            }
        }
    }

    private fun initView() {
        binding.run {
            testNameEditText.setUserInputListener {
                addStatsDataViewModel.setTestName(it)
            }
            testResultEditText.setUserInputListener {
                addStatsDataViewModel.setTestResult(it)
            }
            saveButton.setOnClickListener {
                addStatsDataViewModel.saveDataInFirebase()
            }
        }
    }


}