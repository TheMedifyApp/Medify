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
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        sharedPreference = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        db = FirebaseDatabase.getInstance().reference

        return binding.root
    }

}