package com.ertreby.controlpanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.menuListButton.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_menuFragment) }
        bind.ordersButton.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_ordersFragment) }
    }

}