package com.ertreby.foodbox.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.foodbox.R
import com.ertreby.foodbox.data.Category
import com.ertreby.foodbox.data.Meal
import com.ertreby.foodbox.data.Restaurant
import com.ertreby.foodbox.databinding.FragmentHomeBinding
import com.ertreby.foodbox.ui.adapters.CategoryRecyclerAdapter
import com.ertreby.foodbox.ui.adapters.PopularRecyclerAdapter
import com.ertreby.foodbox.ui.adapters.RestaurantsRecyclerAdapter
import com.ertreby.foodbox.viewmodels.HomeViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    lateinit var bind: FragmentHomeBinding
    val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentHomeBinding.inflate(inflater, container, false)
        return bind.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startLoadingAnimation()
        displayUsername()

        val isSearchActive:Boolean? = arguments?.getBoolean("search_state")
        isSearchActive.let { if (it == true) bind.editTextSearch.requestFocus() }





        bind.cartButton.setOnClickListener {
            /*val bundle = Bundle()
            val cart: Cart? = viewModel.getActiveCart()
            bundle.putParcelable("cart", cart)*/
            findNavController().navigate(
                R.id.action_homeFragment_to_cartFragment
            )

        }

        bind.profileButton.setOnClickListener {
            signOut()

        }




        createCategoryList()
        createPopularList()
        createRestaurantsList()


    }

    override fun onResume() {
        super.onResume()
        viewModel.setUpValues()
    }

    private fun setUpSearchBar() {
        val searchList = viewModel.getSearchList()
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, searchList)
        bind.editTextSearch.setAdapter(adapter)
        bind.editTextSearch.setOnItemClickListener { parent, _, position, _ ->
            val choice:String=parent.getItemAtPosition(position).toString()
            val index=searchList.indexOf(choice)
            if (index in 0 until meals.size){
                //selection is a meal
                popularOnItemClick(index)
            }else if (index in meals.size-1 until searchList.size){
             //selection is restaurant
                restaurantOnItemClick(index-(meals.size))
            }
        }

    }


    private fun startLoadingAnimation() {
        bind.scroll.visibility = View.INVISIBLE
        val loadingAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink)
        bind.loadingImage.startAnimation(loadingAnimation)
    }


    private fun endLoadingAnimation() {
        bind.scroll.visibility = View.VISIBLE
        bind.loadingImage.visibility = View.GONE
        bind.loadingImage.clearAnimation()
    }

    private fun signOut() {
        Firebase.auth.signOut()
        findNavController().navigateUp()
    }


    private fun displayUsername() {
        viewModel.user.observe(viewLifecycleOwner) {

            bind.usernameText.text = it.firstName
        }

    }

    private val restaurants = mutableListOf<Restaurant>()
    private fun createRestaurantsList() {
        val restaurantsRecyclerView = bind.restaurantsRecyclerView
        restaurantsRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter =
            RestaurantsRecyclerAdapter(requireContext(), restaurants, ::restaurantOnItemClick)
        restaurantsRecyclerView.adapter = adapter
        viewModel.restaurants.observe(viewLifecycleOwner) {
            restaurants.clear()
            restaurants.addAll(it)
            adapter.notifyDataSetChanged()
            setUpSearchBar()
        }


    }

    private val meals = mutableListOf<Meal>()

    private fun createPopularList() {


        val recyclerView: RecyclerView = bind.popularRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = PopularRecyclerAdapter(requireContext(), meals, ::popularOnItemClick)
        recyclerView.adapter = adapter
        viewModel.meals.observe(viewLifecycleOwner) {
            meals.clear()
            meals.addAll(it)
            endLoadingAnimation()
            adapter.notifyDataSetChanged()
            setUpSearchBar()
        }


    }

    val foodCategories = listOf("Burger", "Sushi", "Pizza", "Chicken", "Pasta")

    private fun createCategoryList() {

        val colors = listOf(
            R.color.card1_color,
            R.color.card2_color,
            R.color.card3_color,
            R.color.card4_color,
            R.color.card5_color
        )
        val images = listOf(
            R.drawable.ic_burger,
            R.drawable.ic_sushi,
            R.drawable.ic_pizza,
            R.drawable.ic_chicken,
            R.drawable.ic_pasta
        )
        val foodList = mutableListOf<Category>()
        foodCategories.forEachIndexed { index, s ->
            val drawable = ResourcesCompat.getDrawable(resources, images[index], null)
            val color = ResourcesCompat.getColor(resources, colors[index], null)

            drawable?.let { foodList.add(Category(s, color, it)) }
        }

        val categoryRecyclerView = bind.categoryRecyclerView
        categoryRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CategoryRecyclerAdapter(requireContext(), foodList, ::categoryOnItemClick)
        categoryRecyclerView.adapter = adapter
    }


    private fun popularOnItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putParcelable("meal", meals[position])
        findNavController().navigate(R.id.action_home_to_order, bundle)
    }


    private fun restaurantOnItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putString("field", "restaurantId")
        bundle.putString("value", restaurants[position].restaurantId)
        bundle.putString("name",restaurants[position].name)
        findNavController().navigate(R.id.action_home_to_food, bundle)
    }

    private fun categoryOnItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putString("field", "type")
        bundle.putString("value", foodCategories[position])
        findNavController().navigate(R.id.action_home_to_food, bundle)
    }


}