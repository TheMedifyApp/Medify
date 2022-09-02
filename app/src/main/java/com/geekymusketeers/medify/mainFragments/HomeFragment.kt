package com.geekymusketeers.medify.mainFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.geekymusketeers.medify.databinding.FragmentHomeBinding
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ncorti.slidetoact.SlideToActView


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: DatabaseReference

    //Current User's data
    private lateinit var userName : String
    private lateinit var userEmail : String
    private lateinit var userPhone : String

    //Searched doctor's data
    private lateinit var searchedName : String
    private lateinit var searchedEmail : String
    private lateinit var searchedPhone : String
    private lateinit var searchedData : String
    private lateinit var searchedUID : String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        db = FirebaseDatabase.getInstance().reference

        db.child("Users").child(user?.uid!!).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userName = snapshot.child("name").value.toString().trim()
                userEmail = snapshot.child("email").value.toString().trim()
                userPhone = snapshot.child("phone").value.toString().trim()
                binding.namePreview.text = userName

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.searchButton.setOnClickListener {
            searchedData = binding.doctorData.text.toString().trim()
            if (searchedData.isNotEmpty()) {
                if (searchedData == userPhone || searchedData == userEmail) {
                    Toast.makeText(requireActivity(), "Stop searching yourself", Toast.LENGTH_SHORT).show()
                    binding.cardView.isVisible = false
                    binding.slider.isVisible = false
                    return@setOnClickListener
                }
                doctorIsPresent()
            } else {
                Toast.makeText(requireActivity(), "Enter doctor's email / phone", Toast.LENGTH_SHORT).show()
            }

        }

        binding.slider.animDuration = 150
        binding.slider.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                val intent =  Intent(requireActivity(), AppointmentBooking::class.java)
                intent.putExtra("uid", searchedUID)
                intent.putExtra("name", searchedName)
                intent.putExtra("email", searchedEmail)
                intent.putExtra("phone", searchedPhone)
                startActivity(intent)
                binding.slider.resetSlider()
            }
        }


        return binding.root
    }

    private fun doctorIsPresent() {

        db.child("Doctor").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    val map = child.value as HashMap<*, *>
                    val name = map["name"].toString().trim()
                    val email = map["email"].toString().trim()
                    val phone = map["phone"].toString().trim()
                    if (searchedData == email || searchedData == phone) {
                        searchedName = name
                        searchedEmail = email
                        searchedPhone = phone
                        searchedUID = child.value.toString().trim()
                        binding.textView3.isVisible = false
                        binding.cardView.isVisible = true
                        binding.slider.isVisible = true
                        binding.doctorName.text = name
                        binding.doctorEmail.text = email
                        binding.doctorPhone.text = phone
                        return
                    } else
                        binding.textView3.isVisible = true
                }
            }

            override fun onCancelled(error: DatabaseError) {}
            fun onCancelled(firebaseError: FirebaseError?) {}
        })
    }

}