package com.nauka.timepad

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.nauka.timepad.databinding.RegistrationFragmentBinding
import java.util.concurrent.TimeUnit


class Registration : Fragment() {

    //view binding
    private lateinit var binding: RegistrationFragmentBinding

    // if code send failed< will used to resend
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

    private var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mVerificationId: String? = null
    private lateinit var firebaseAuth: FirebaseAuth

    private val TAG = "MAIN_TAG"

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.registration_fragment,
            container,
            false
        )

        binding.phoneLl.visibility = View.VISIBLE
        binding.codeLl.visibility = View.GONE

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()
                Toast.makeText(activity, "${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent: ${verificationId}")
                mVerificationId = verificationId
                forceResendingToken = token
                progressDialog.dismiss()

                //hide phone layout, show code layout
                binding.phoneLl.visibility = View.GONE
                binding.codeLl.visibility = View.VISIBLE
                Toast.makeText(activity, "Verification code sent...", Toast.LENGTH_SHORT)
                    .show()
                binding.codeSentDescriptionTv.text =
                    "Please type the verification code we sent to ${
                        binding.phonEt.text.toString().trim()
                    }"
            }
        }

        //phoneContinueBtn click: input phone number> validate, start phone authentication/login
        binding.phoneContinueBtn.setOnClickListener {
            //input phone number
            val phone = binding.phonEt.text.toString().trim()
            //validate phone number
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(activity, "Please enter phone number", Toast.LENGTH_SHORT)
                    .show()
            } else {
                startPhoneNumberVerification(phone)
            }
        }
        //resendCodeTv click: (if code didn't receive) resend verification code / OTP
        binding.resendCodeTv.setOnClickListener {
            //input phone number
            val phone = binding.phonEt.text.toString().trim()
            //validate phone number
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(activity, "Please enter phone number", Toast.LENGTH_SHORT)
                    .show()
            } else {
                resendVerificationCode(phone, forceResendingToken)
            }
        }
        //codeSubmitBtn click: input verification code, validate, verify phone number with vecification code
        binding.codeSubmitBtn.setOnClickListener {
            //input verification code
            val code = binding.codeEt.text.toString().trim()
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(
                    activity,
                    "Please enter verification code",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                verifyPhoneNumberWithCode(mVerificationId, code)
            }
        }

        return binding.root
    }

    private fun startPhoneNumberVerification(phone: String) {
        progressDialog.setMessage("Verifying Phone Number...")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(mCallback)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendVerificationCode(
        phone: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        progressDialog.setMessage("Resending Code...")
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(mCallback)
            .setForceResendingToken(token)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        progressDialog.setMessage("Verifying Code...")
        progressDialog.show()

        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        progressDialog.setMessage("Logging In")

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
                val phone = firebaseAuth.currentUser.phoneNumber
                Toast.makeText(activity, "Logged In as $phone", Toast.LENGTH_SHORT).show()

                //start profile activity
                findNavController().navigate(R.id.action_global_profileFragment)
            }
            .addOnFailureListener { e ->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(activity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            //logget out
            //startActivity(Intent(activity, MainActivity::class.java))

        }
        else{
            //startActivity(Intent(activity, ProfileActivity2::class.java))
            findNavController().navigate(R.id.action_global_home2)
        }
    }

}