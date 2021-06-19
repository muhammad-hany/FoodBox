package com.ertreby.foodbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.foodbox.R
import com.ertreby.foodbox.data.Meal
import com.ertreby.foodbox.databinding.PopularItemListBinding
import com.squareup.picasso.Picasso

class PopularRecyclerAdapter(val context: Context, private val meals: List<Meal>, private val callback:(Int)->Unit) :
    RecyclerView.Adapter<PopularRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.popular_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(meals[position])
        holder.binding.card.setOnClickListener { callback(position) }
    }

    override fun getItemCount(): Int {
        return meals.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PopularItemListBinding.bind(itemView)

        fun bindViews(meal: Meal) {
            binding.titleText.text = meal.name
            binding.descriptionText.text = meal.description
            val url = meal.imageUrl
            Picasso.get().load(url).placeholder(R.drawable.ic_avatar).into(binding.backgroundImage)
            binding.ratingText.text = meal.rating.toString()
            binding.priceText.text = meal.price
        }


    }
}