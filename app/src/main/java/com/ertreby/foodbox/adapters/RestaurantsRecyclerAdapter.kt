package com.ertreby.foodbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.foodbox.R
import kotlinx.android.synthetic.main.popular_item_list.view.card
import kotlinx.android.synthetic.main.restaurant_item_list.view.*

class RestaurantsRecyclerAdapter(val context: Context) : RecyclerView.Adapter<RestaurantsRecyclerAdapter.ViewHolder>() {


    val colorsIds= listOf(R.color.mcdonalds_color,R.color.starbucks_color,R.color.dominos_color)
    val drwableIds= listOf(R.drawable.ic_mcdonald,R.drawable.ic_starbucks,R.drawable.ic_domino_s,)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.card.setCardBackgroundColor(ResourcesCompat.getColor(context.resources,colorsIds[position],null))
        val drawable=ResourcesCompat.getDrawable(context.resources,drwableIds[position],null)
        drawable?.let { holder.itemView.icon.setImageDrawable(it) }

    }

    override fun getItemCount(): Int {
        return colorsIds.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}