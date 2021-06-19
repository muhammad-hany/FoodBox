package com.ertreby.foodbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.foodbox.R
import com.ertreby.foodbox.data.Category
import com.ertreby.foodbox.databinding.CategoryItemListBinding

class CategoryRecyclerAdapter(
    val context: Context,
    val categories: List<Category>,
    val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(categories[position])
        holder.binding.card.setOnClickListener { onItemClick(position) }
    }

    override fun getItemCount(): Int {
        return categories.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CategoryItemListBinding.bind(itemView)

        fun bindView(category:Category){
            binding.card.setCardBackgroundColor(category.color)
            binding.imageLabel.text = category.name
            binding.cardImage.setImageDrawable(category.drawable)
        }
    }
}