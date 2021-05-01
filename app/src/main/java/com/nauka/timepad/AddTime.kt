package com.nauka.timepad

import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nauka.timepad.databinding.FragmentAddTimeBinding
import java.time.Duration
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit


class AddTime : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddTimeBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var db: FirebaseFirestore

    companion object {
        const val TAG = "AddTime"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_time,
            container,
            false
        )

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val firebaseUser = firebaseAuth.currentUser.uid



        binding.button.setOnClickListener {

            val timeHour = TimeUnit.HOURS.toMillis(binding.taskHourET.text.toString().toLong())
            val timeMinute =
                TimeUnit.MINUTES.toMillis(binding.taskMinitsET.text.toString().toLong())
            val time = timeHour + timeMinute

            val user = hashMapOf(
                "name" to binding.taskNameET.text.toString(),
                "time" to time.toString(),
                "tag" to binding.editTextTextPersonName3.text.toString()
            )
            Toast.makeText(context, "${binding.taskMinitsET.hint}", Toast.LENGTH_SHORT).show()

            db.collection(firebaseUser).document(binding.taskNameET.text.toString())
                .set(user)
                .addOnSuccessListener {
                    Toast.makeText(binding.taskNameET.context, "Complete", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(binding.taskNameET.context, "$it", Toast.LENGTH_SHORT).show()
                }
        }



        return binding.root
    }

}