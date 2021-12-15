package com.ertreby.controlpanel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.controlpanel.databinding.OrdersItemListBinding
import com.squareup.picasso.Picasso

class OrdersListAdapter(val orders: Array<Order> , val users:Array<User>) :
    RecyclerView.Adapter<OrdersListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.orders_item_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order=orders[position]
        val user=users.find { it.id == order.userId }
        user?.let { holder.onBind(order, it) }
    }

    override fun getItemCount(): Int {
        return orders.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val bind = OrdersItemListBinding.bind(view)
        fun onBind(order: Order , user: User){
            bind.orderId.text=order.orderId
            bind.mealQuantity.text=order.quantity.toString()
            val thumbnailUrl=order.meal?.imageUrl
            thumbnailUrl?.let { Picasso.get().load(it).into(bind.orderImage) }
            bind.orderedExtras.text=order.orderedExtras.toString().filterNot { it == '{' || it =='}' }

            bind.mealName.text=order.meal?.name
            bind.username.text=user.firstName
            bind.userAddress.text=user.address
            val mealPrice=order.meal?.price?.toIntOrNull()
            val quantity = order.quantity
            if (mealPrice!=null && quantity!=null) {
                 bind.orderPrice.text = "${quantity * mealPrice} USD"
            }
        }
    }
}