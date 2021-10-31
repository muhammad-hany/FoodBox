package com.ertreby.controlpanel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.controlpanel.databinding.MenuItemListBinding
import com.squareup.picasso.Picasso

class MenuListAdapter(
    val mealsList: MutableList<Meal>,
    val onEditMeal: (Meal) -> Unit,
    val onRemoveMeal: (Meal,Int) -> Unit
) :
    RecyclerView.Adapter<MenuListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.menu_item_list, parent, false)
        return ViewHolder(view, onEditMeal,onRemoveMeal)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mealsList[position],position)
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    fun removeItem(position: Int) {
        mealsList.removeAt(position)
        notifyItemRemoved(position)
    }


    class ViewHolder(
        itemView: View,
        val onEditMeal: (Meal) -> Unit,
        val onRemoveMeal : (Meal,Int)-> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val binding = MenuItemListBinding.bind(itemView)

        fun bind(meal: Meal, position: Int) {
            binding.mealName.text = meal.name
            binding.mealPrice.text = "${meal.price} USD"
            Picasso.get().load(meal.imageUrl).placeholder(R.drawable.ic_avatar)
                .into(binding.menuImage)

            binding.rightImage.setOnClickListener { onRemoveMeal(meal,position) }
            binding.leftImage.setOnClickListener { onEditMeal(meal) }
        }


    }
}