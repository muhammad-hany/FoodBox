package com.ertreby.foodbox.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.foodbox.R
import com.ertreby.foodbox.data.Order
import com.ertreby.foodbox.databinding.CartItemListBinding
import com.squareup.picasso.Picasso


typealias orderFunctionType = (Order) -> Unit

class CartRecyclerAdapter(val orders: List<Order>, val onOrderUpdate: orderFunctionType, val onOrderRemove:orderFunctionType) :
    RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(orders[position], position, ::notifyItemRemoved, onOrderUpdate,onOrderRemove)


    }


    override fun getItemCount(): Int {
        return orders.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CartItemListBinding.bind(itemView)


        private var mealQuantity: Int = 0
            set(value) {
                if (value in 1..9) {
                    field = value
                }
            }

        fun bindView(
            order: Order,
            position: Int,
            onDataRemoved: (Int) -> Unit,
            onOrderUpdate: orderFunctionType,
            onOrderRemove: orderFunctionType
        ) {
            Picasso.get().load(order.meal?.imageUrl).into(binding.cartImage)
            mealQuantity = order.quantity!!
            binding.mealPrice.text = order.meal?.price
            binding.mealName.text = order.meal?.name
            binding.mealQuantity.text = order.quantity?.toString()
            binding.minusBtn.setOnClickListener {

                order.quantity = --mealQuantity
                binding.mealQuantity.text = order.quantity?.toString()
                onOrderUpdate(order)
            }
            binding.plusBtn.setOnClickListener {

                order.quantity = ++mealQuantity
                binding.mealQuantity.text = order.quantity?.toString()
                onOrderUpdate(order)
            }

            binding.leftImage.setOnClickListener {
                binding.motion.progress = 0f
                onOrderRemove(order)
                onDataRemoved(position)

            }

            binding.rightImage.setOnClickListener {
                binding.motion.progress = 0f
                onOrderRemove(order)
                onDataRemoved(position)
            }


        }




    }

}