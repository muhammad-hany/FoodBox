package com.ertreby.controlpanel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ertreby.controlpanel.databinding.OrdersItemListBinding

class OrdersListAdapter(val orders: Array<Order> , val users:Array<User>) :
    RecyclerView.Adapter<OrdersListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.orders_item_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(orders[position],users[position])
    }

    override fun getItemCount(): Int {
        return orders.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val bind = OrdersItemListBinding.bind(view)
        fun onBind(order: Order , user: User){
            bind.mealName.text=order.meal?.name
            bind.username.text=user.firstName
        }
    }
}