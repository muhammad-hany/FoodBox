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
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlin.random.Random

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, null, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createCategoryList(view)
        createPopularList(view)
        createRestaurantsList(view)


    }

    private fun createRestaurantsList(view: View) {
        val restaurantsRecyclerView=view.restaurantsRecyclerView as RecyclerView
        restaurantsRecyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        context?.let {
            val adapter= RestaurantsRecyclerAdapter(it)
            restaurantsRecyclerView.adapter=adapter
        }
    }

    private fun createPopularList(view: View) {
        val titles = resources.getStringArray(R.array.titles)
        val descriptions = resources.getStringArray(R.array.descriptions)
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
                        random.nextInt(10, 30).toString()+" $",
                        random.nextInt(3, 5).toString(),
                        it
                    )
                )
            }
        }
        val recyclerView: RecyclerView = view.popularRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        context?.let {
            val adapter = PopularRecyclerAdapter(it, populars)
            recyclerView.adapter = adapter
        }
    }

    private fun createCategoryList(view: View) {
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

        val categoryRecyclerView = view.categoryRecyclerView as RecyclerView
        categoryRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        context?.let {
            val adapter = CategoryRecyclerAdapter(it, foodList)
            categoryRecyclerView.adapter = adapter
        }
    }
}