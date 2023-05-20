package com.geekymusketeers.medify.ui.mainFragments.settings.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.base.ViewModelFactory
import com.geekymusketeers.medify.databinding.FragmentProfileBinding
import com.geekymusketeers.medify.model.User
import com.geekymusketeers.medify.utils.Constants
import com.geekymusketeers.medify.utils.DateTimeExtension
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.Date


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel> { ViewModelFactory() }
    private lateinit var sharedPreferences: SharedPreferences


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        sharedPreferences =
            requireActivity().getSharedPreferences(Constants.UserData, Context.MODE_PRIVATE)

        initObservers()
        initViews()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        binding.run {
            nameEditText.setUserInputListener {
                profileViewModel.setUserName(it)
            }
            ageEditText.setUserInputListener {
                showCalendarDialog()
            }
            phoneEditText.setUserInputListener {
                profileViewModel.setUserPhone(it)
            }
            addressEditText.setUserInputListener {
                profileViewModel.setUserAddress(it)
            }
            updateAccountButton.setOnClickListener {
                profileViewModel.updateUserProfile(sharedPreferences)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    private fun showCalendarDialog() {
        val datePicker = MaterialDatePicker.Builder.datePicker().apply {

            val constraintsBuilder = CalendarConstraints.Builder()
            constraintsBuilder.setValidator(DateValidatorPointBackward.now())
            setCalendarConstraints(constraintsBuilder.build())

            val calendar = Calendar.getInstance()
            setSelection(calendar.timeInMillis)

        }.build()
        datePicker.show(requireActivity().supportFragmentManager, "DatePicker")

        datePicker.addOnPositiveButtonClickListener {
            val age =
                DateTimeExtension.calculateAge(DateTimeExtension.convertDateToLocalDate(Date(it)))
            profileViewModel.setUserAge(age)
            binding.ageEditText.setEditTextBox(age.toString())
        }
    }

    private fun initObservers() {
        profileViewModel.run {
            getDataFromSharedPreferences(sharedPreferences)
            userLiveData.observe(viewLifecycleOwner) {
                updateEditFieldsHints(it)
            }
            isValidName.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), getString(R.string.invalid_name), Toast.LENGTH_SHORT).show()
            }
            isValidAge.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), getString(R.string.invalid_age), Toast.LENGTH_SHORT).show()
            }
            isValidPhoneNumber.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), getString(R.string.invalid_phone_number), Toast.LENGTH_SHORT).show()
            }
            isValidAddress.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), getString(R.string.invalid_address), Toast.LENGTH_SHORT).show()
            }
            enableNextButtonLiveData.observe(viewLifecycleOwner) {
                binding.updateAccountButton.isEnabled = it
                binding.updateAccountButton.setButtonEnabled(it)
            }
            isProfileUpdated.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(requireContext(), getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack(R.id.settings, false)
                } else {
                    Toast.makeText(requireContext(), getString(R.string.failed_to_update_profile), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateEditFieldsHints(user: User?) {
        binding.apply {
            nameEditText.setHint(user?.Name)
            ageEditText.setHint(user?.Age.toString())
            phoneEditText.setHint(user?.Phone)
            addressEditText.setHint(user?.Address)
        }
    }
}