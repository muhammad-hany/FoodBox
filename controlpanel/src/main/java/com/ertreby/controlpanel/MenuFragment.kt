package com.ertreby.controlpanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ertreby.controlpanel.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private lateinit var bind:FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind= FragmentMenuBinding.inflate(inflater,container,false)
        return bind.root
    }
}