package com.ertreby.foodbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.foodbox.R
import com.ertreby.foodbox.data.PopularMeal
import com.ertreby.foodbox.databinding.PopularItemListBinding

class PopularRecyclerAdapter(val context: Context, val populars:List<PopularMeal>) : RecyclerView.Adapter<PopularRecyclerAdapter.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.popular_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.titleText.text = populars[position].title
        holder.binding.descriptionText.text=populars[position].description
        holder.binding.backgroundImage.setImageDrawable(populars[position].drawable)
        holder.binding.rattingText.text = populars[position].ratting
        holder.binding.priceText.text= populars[position].price.toString()
    }

    override fun getItemCount(): Int {
        return populars.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding=PopularItemListBinding.bind(itemView)
    }
}