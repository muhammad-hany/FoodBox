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
import com.ertreby.foodbox.repositories.FirebaseService

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
            Toast.makeText(requireContext(), "Code sent !", Toast.LENGTH_SHORT).show()
        }

    }


    private fun phoneVerification() {
        FirebaseService.verifyPhoneNumber(fullNumber,requireActivity(),::onVerificationSuccess,::onVerificationFailed)

    }


    private fun onVerificationSuccess() {
        findNavController().navigate(R.id.action_verificationReceive_to_verifiedFragment)
    }


    private fun onVerificationFailed(exception: Exception?) {
        Toast.makeText(
            requireContext(),
            "$exception",
            Toast.LENGTH_LONG
        ).show()
    }

}