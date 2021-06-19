package com.ertreby.foodbox.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ertreby.foodbox.R
import com.ertreby.foodbox.databinding.FragmentVerificationReceiveBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class VerificationReceive : Fragment() {

    lateinit var bind: FragmentVerificationReceiveBinding
    lateinit var fullNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullNumber = arguments?.getString(EnterPhoneFragment.PHONE_NUMBER)!!
        phoneVerification()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentVerificationReceiveBinding.inflate(inflater, container, false)
        return bind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.phoneText.text = fullNumber
        bind.verifyButton.setOnClickListener {
            if (bind.otpView.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Enter OTP first !", Toast.LENGTH_SHORT).show()
            }
        }

        bind.resendCodeButton.setOnClickListener {
            phoneVerification()
            Toast.makeText(requireContext(),"Code sent !",Toast.LENGTH_SHORT).show()
        }

    }


    private fun phoneVerification() {
        val options = PhoneAuthOptions.newBuilder(Firebase.auth).apply {

            setPhoneNumber(fullNumber)
            setTimeout(60L, TimeUnit.SECONDS)
            setActivity(requireActivity())
            setCallbacks(calback)

        }.build()

        PhoneAuthProvider.verifyPhoneNumber(options)


    }

    private val calback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            bind.otpView.setText(p0.smsCode)
            signInWithPhoneAuthCredential(p0)

        }

        override fun onVerificationFailed(p0: FirebaseException) {

        }

    }
    val db = Firebase.firestore

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        Firebase.auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    findNavController().navigate(R.id.action_verificationReceive_to_verifiedFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "${result.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }
    }

}