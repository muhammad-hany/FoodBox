package com.ertreby.foodbox.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ertreby.foodbox.ui.adapters.CartRecyclerAdapter
import com.ertreby.foodbox.data.Cart
import com.ertreby.foodbox.databinding.FragmentCartBinding
import com.ertreby.foodbox.viewmodels.CartViewModel


class CartFragment : Fragment() {

    lateinit var binding: FragmentCartBinding


    val viewModel: CartViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        setUpRecyclerView()




        binding.checkoutButton.setOnClickListener {
            viewModel.setCartFulfilled(::onCartFulfilled)
        }


    }

    fun onCartFulfilled() {
        findNavController().popBackStack()
    }


    private fun setUpRecyclerView() {
        var cart: Cart? = requireArguments().getParcelable("cart")
        cart?.let {
            setVisibilityOfEmptyItemsText(false)
            val adapter = CartRecyclerAdapter(it)
            binding.recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.recyclerView.adapter = adapter
            viewModel.cart.observe(viewLifecycleOwner) { _cart ->
                cart = _cart
                adapter.notifyDataSetChanged()
            }
        }

        if (cart == null) {
            setVisibilityOfEmptyItemsText(true)
        }


    }

    private fun setVisibilityOfEmptyItemsText(isCartEmpty: Boolean) {
        if (isCartEmpty) {
            binding.noItemsText.visibility = View.VISIBLE
            binding.checkoutButton.visibility = View.INVISIBLE
        } else {
            binding.noItemsText.visibility = View.INVISIBLE
            binding.checkoutButton.visibility = View.VISIBLE
        }

    }

}