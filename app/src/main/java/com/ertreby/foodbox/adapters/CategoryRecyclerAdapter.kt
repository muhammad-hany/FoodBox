package com.ertreby.foodbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.foodbox.R
import com.ertreby.foodbox.data.Category
import com.ertreby.foodbox.databinding.CategoryItemListBinding

class CategoryRecyclerAdapter(val context: Context, val categories:List<Category>) : RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.card.setCardBackgroundColor(categories[position].color)
        holder.binding.imageLabel.text = categories[position].name
        holder.binding.cardImage.setImageDrawable(categories[position].drawable)
    }

    override fun getItemCount(): Int {
        return categories.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding=CategoryItemListBinding.bind(itemView)
    }
}