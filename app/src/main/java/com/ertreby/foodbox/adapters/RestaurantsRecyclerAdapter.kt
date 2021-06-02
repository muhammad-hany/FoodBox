package com.ertreby.foodbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.foodbox.R
import com.ertreby.foodbox.data.Restaurant
import com.ertreby.foodbox.databinding.RestaurantItemListBinding

class RestaurantsRecyclerAdapter(val context: Context,val restaurants:MutableList<Restaurant>) : RecyclerView.Adapter<RestaurantsRecyclerAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.card.setCardBackgroundColor(restaurants[position].color)
        holder.binding.restaurantTitle.text=restaurants[position].title
        holder.binding.restaurantDescription.text=restaurants[position].description
        holder.binding.ratingBar.rating=restaurants[position].rating.toFloat()
        holder.binding.icon.setImageDrawable(restaurants[position].logoImage)

    }

    override fun getItemCount(): Int {
        return restaurants.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding=RestaurantItemListBinding.bind(itemView)
    }

}