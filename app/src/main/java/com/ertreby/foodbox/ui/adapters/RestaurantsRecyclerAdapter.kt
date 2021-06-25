package com.ertreby.foodbox.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.foodbox.R
import com.ertreby.foodbox.data.Restaurant
import com.ertreby.foodbox.databinding.RestaurantItemListBinding
import com.squareup.picasso.Picasso

class RestaurantsRecyclerAdapter(val context: Context, val restaurants: MutableList<Restaurant>,val onItemClick:(Int)->Unit) :
    RecyclerView.Adapter<RestaurantsRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.restaurant_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.bindView(restaurants[position])
        holder.binding.whiteCard.setOnClickListener { onItemClick(position) }

    }

    override fun getItemCount(): Int {
        return restaurants.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RestaurantItemListBinding.bind(itemView)


        fun bindView(restaurant: Restaurant){
            val color = Color.parseColor(restaurant.color)
            binding.card.setCardBackgroundColor(color)
            binding.restaurantTitle.text = restaurant.name
            binding.restaurantDescription.text = restaurant.description
            binding.ratingBar.rating = restaurant.rating?.toFloat()!!
            val url = restaurant.logoImage
            Picasso.get().load(url).placeholder(R.drawable.app_icon).into(binding.icon)
        }
    }

}