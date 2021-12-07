package com.ertreby.controlpanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ertreby.controlpanel.databinding.FragmentOrderBinding

class OrdersFragment : Fragment() {
    lateinit var bind: FragmentOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentOrderBinding.inflate(inflater, container, false)
        return bind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        FirebaseService.getRestaurantOrders { orders, users ->
            val adapter = OrdersListAdapter(orders, users)
            bind.recyclerView.adapter = adapter
        }
    }
}