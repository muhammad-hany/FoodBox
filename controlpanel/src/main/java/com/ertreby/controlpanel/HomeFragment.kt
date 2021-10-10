package com.ertreby.controlpanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ertreby.controlpanel.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {


    private lateinit var bind: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bind = FragmentHomeBinding.inflate(inflater, container, false)
        return bind.root
    }

}