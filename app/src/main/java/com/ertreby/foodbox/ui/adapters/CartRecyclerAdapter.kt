package com.ertreby.foodbox.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.foodbox.R
import com.ertreby.foodbox.data.Cart
import com.ertreby.foodbox.databinding.CartItemListBinding
import com.squareup.picasso.Picasso

typealias myReflection = (Cart) -> Unit

class CartRecyclerAdapter(val cart: Cart, val onOrderUpdate: myReflection) :
    RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item_list, parent, false)
        return ViewHolder(view, cart, ::notifyDataSetChanged, onOrderUpdate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(position)


    }


    override fun getItemCount(): Int {
        return cart.orders?.size!!
    }


    class ViewHolder(
        itemView: View,
        private val cart: Cart,
        private val onDataChange: () -> Unit,
        val onOrderUpdate: myReflection
    ) : RecyclerView.ViewHolder(itemView) {
        val binding = CartItemListBinding.bind(itemView)


        private var mealQuantity: Int = 0
            set(value) {
                if (value in 1..9) {
                    field = value
                }
            }

        fun bindView(position: Int) {
            val order = cart.orders?.get(position)!!
            Picasso.get().load(order.meal?.imageUrl).into(binding.cartImage)
            mealQuantity = order.quantity!!
            binding.mealPrice.text = order.meal?.price
            binding.mealName.text = order.meal?.name
            binding.mealQuantity.text = order.quantity?.toString()
            binding.minusBtn.setOnClickListener {

                order.quantity = --mealQuantity
                binding.mealQuantity.text = order.quantity?.toString()
                onOrderUpdate(cart)
            }
            binding.plusBtn.setOnClickListener {

                order.quantity = ++mealQuantity
                binding.mealQuantity.text = order.quantity?.toString()
                onOrderUpdate(cart)
            }

            binding.leftImage.setOnClickListener {
                removeItem(position)
                onDataChange()

            }

            binding.rightImage.setOnClickListener {
                removeItem(position)
                onDataChange()
            }


        }

        private fun removeItem(position: Int) {
            binding.motion.progress = 0f
            cart.orders?.removeAt(position)
            onOrderUpdate(cart)


        }


    }

}