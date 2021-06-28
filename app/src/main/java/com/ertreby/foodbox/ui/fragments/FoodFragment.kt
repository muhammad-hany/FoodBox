package com.ertreby.foodbox.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ertreby.foodbox.R
import com.ertreby.foodbox.data.Meal
import com.ertreby.foodbox.databinding.FragmentFoodBinding
import com.ertreby.foodbox.ui.adapters.PopularRecyclerAdapter
import com.ertreby.foodbox.viewmodels.FoodViewModel


class FoodFragment : Fragment() {


    lateinit var bind: FragmentFoodBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        bind = FragmentFoodBinding.inflate(inflater, container, false)
        return bind.root
    }

    private val meals = mutableListOf<Meal>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel:FoodViewModel by viewModels()
        val field=requireArguments().getString("field")
        if (field=="restaurantId"){
            bind.typeText.text=requireArguments().getString("name")

        }else{
            bind.typeText.text=requireArguments().getString("value")
        }



        val gridLayoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        bind.recyclerView.layoutManager = gridLayoutManager
        val adapter = PopularRecyclerAdapter(requireContext(), meals, ::recyclerOnItemClick)
        bind.recyclerView.adapter = adapter
        viewModel.meals.observe(viewLifecycleOwner){
            meals.clear()
            meals.addAll(it)
            adapter.notifyDataSetChanged()
        }

        bind.backButton.setOnClickListener { findNavController().navigateUp() }


    }


    private fun recyclerOnItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putParcelable("meal", meals[position])
        findNavController().navigate(R.id.action_foodFragment_to_orderFragment, bundle)
    }







}