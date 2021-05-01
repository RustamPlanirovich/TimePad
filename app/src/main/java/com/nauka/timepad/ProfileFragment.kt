package com.nauka.timepad

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nauka.timepad.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var firebaseAuth: FirebaseAuth

    var state: Boolean = false
    lateinit var mSetting: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )
        mSetting = context!!.getSharedPreferences("InternetMode", Context.MODE_PRIVATE)
        readOnLineDataBase()

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //loggoutBtn click, Logout the user
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }


        binding.onOffSunc.setOnCheckedChangeListener { buttonView, isChecked ->
            // do something, the isChecked will be
            // true if the switch is in the On position

            Toast.makeText(context, state.toString(), Toast.LENGTH_SHORT).show()

            when (isChecked) {
                true -> {
                    Home().db.enableNetwork()
                    state = isChecked
                }
                false -> {
                    Home().db.disableNetwork()
                    state = isChecked
                }
            }
        }

        return binding.root
    }

    private fun checkUser() {
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            //logget out
            findNavController().navigate(R.id.action_global_registration)

        } else {
            //logget in, get phone number of user
            val phone = firebaseUser.phoneNumber
            //set phone number
            binding.phoneTv.text = phone
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveOnlineDataBase()
    }

    fun saveOnlineDataBase() {
        val editor: SharedPreferences.Editor = mSetting.edit()
        editor.putBoolean("InternetMode", state)
        editor.apply()
    }

    fun readOnLineDataBase() {
        var readState = mSetting.getBoolean("InternetMode", false)
        when (readState) {
            true -> {
                binding.onOffSunc.setChecked(true)
                state = readState
            }
            false -> {
                binding.onOffSunc.setChecked(false)
                state = readState
            }
        }
    }

}