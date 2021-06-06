package com.ertreby.foodbox.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.foodbox.R
import com.ertreby.foodbox.adapters.CategoryRecyclerAdapter
import com.ertreby.foodbox.adapters.PopularRecyclerAdapter
import com.ertreby.foodbox.adapters.RestaurantsRecyclerAdapter
import com.ertreby.foodbox.data.Category
import com.ertreby.foodbox.data.PopularMeal
import com.ertreby.foodbox.data.Restaurant
import com.ertreby.foodbox.databinding.FragmentHomeBinding
import kotlin.random.Random

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createCategoryList()
        createPopularList()
        createRestaurantsList()


    }

    private fun createRestaurantsList() {
        val titles = resources.getStringArray(R.array.restaurants_title)
        val descriptions = resources.getStringArray(R.array.restaurants_description)
        val colorsIds =
            listOf(R.color.mcdonalds_color, R.color.starbucks_color, R.color.dominos_color)
        val drawableIds =
            listOf(R.drawable.ic_mcdonald, R.drawable.ic_starbucks, R.drawable.ic_domino_s)
        val restaurants = mutableListOf<Restaurant>()
        val random = Random(0)
        titles.forEachIndexed { index, text ->
            val drawable = ResourcesCompat.getDrawable(resources, drawableIds[index], null)
            val color = ResourcesCompat.getColor(resources, colorsIds[index], null)
            drawable?.let {
                restaurants.add(
                    Restaurant(
                        text,
                        descriptions[index],
                        random.nextInt(3, 5),
                        it,
                        color
                    )
                )
            }
        }

        val restaurantsRecyclerView = binding.restaurantsRecyclerView
        restaurantsRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        context?.let {
            val adapter = RestaurantsRecyclerAdapter(it, restaurants)
            restaurantsRecyclerView.adapter = adapter
        }
    }

    private fun createPopularList() {
        val titles = resources.getStringArray(R.array.popular_titles)
        val descriptions = resources.getStringArray(R.array.popular_descriptions)
        val populars = mutableListOf<PopularMeal>()
        val drawableIds = listOf<Int>(
            R.drawable.stake,
            R.drawable.mashed_potato_casserole,
            R.drawable.grilled_potato_wedge_fries
        )
        titles.forEachIndexed { index, text ->
            val random = Random(index)
            val drawable = ResourcesCompat.getDrawable(resources, drawableIds[index], null)
            drawable?.let {
                populars.add(
                    PopularMeal(
                        text,
                        descriptions[index],
                        random.nextInt(10, 30).toString() + " $",
                        random.nextInt(3, 5).toString(),
                        it
                    )
                )
            }
        }
        val recyclerView: RecyclerView = binding.popularRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        context?.let {
            val adapter = PopularRecyclerAdapter(it, populars)
            recyclerView.adapter = adapter
        }
    }

    private fun createCategoryList() {
        val foodCategories = listOf("Burger", "Sushi", "Pizza", "Chicken", "Pasta")
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

        val categoryRecyclerView = binding.categoryRecyclerView as RecyclerView
        categoryRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        context?.let {
            val adapter = CategoryRecyclerAdapter(it, foodList)
            categoryRecyclerView.adapter = adapter
        }
    }
}