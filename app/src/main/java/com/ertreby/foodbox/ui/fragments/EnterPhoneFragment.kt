package com.ertreby.foodbox.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ertreby.foodbox.R
import com.ertreby.foodbox.databinding.FragmentEnterphoneBinding

class EnterPhoneFragment : Fragment() {
    lateinit var bind: FragmentEnterphoneBinding
    companion object{
        const val PHONE_NUMBER="phone_number"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentEnterphoneBinding.inflate(inflater, container, false)
        return bind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCountryPickerToLocal()
        bind.nextButton.setOnClickListener {
            if (bind.editTextPhone.text.isNotEmpty()){
                val bundle=Bundle()
                val countryCode=bind.countryCodePicker.selectedCountryCode
                val actualNumber=bind.editTextPhone.text.toString()
                val fullNumber= "+$countryCode$actualNumber"
                bundle.putString(PHONE_NUMBER,fullNumber)
                findNavController().navigate(R.id.action_enterPhoneFragment_to_verificationReceive,bundle)
            }else{
                bind.editTextPhone.error="Type your phone number !"
            }
        }


    }





    private fun setCountryPickerToLocal() {
        val country: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localList=requireContext().applicationContext.resources.configuration.locales
            country = localList[localList.size()-1].country
        } else {

            country = requireContext().applicationContext.resources.configuration.locale.country
        }

        bind.countryCodePicker.setCountryForNameCode(country)
    }


}