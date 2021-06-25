package com.ertreby.foodbox.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ertreby.foodbox.databinding.FragmentReviewsBinding

class ReviewsFragment :Fragment() {
    lateinit var binding:FragmentReviewsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentReviewsBinding.inflate(inflater,container,false)
        return binding.root
    }

}