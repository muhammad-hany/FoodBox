package com.ertreby.foodbox.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ertreby.foodbox.data.FirebaseService
import com.ertreby.foodbox.data.Order
import kotlinx.coroutines.launch

class CartViewModel  () : ViewModel() {


    private val _orders=MutableLiveData<List<Order>>()
    val orders:LiveData<List<Order>> =_orders


    init {
        viewModelScope.launch {
            _orders.value= FirebaseService.getUserActiveOrders()
        }
    }




    fun setOrderFulfilled(onSuccessAction:()->Unit){
        viewModelScope.launch {
            orders.value?.let { FirebaseService.setOrdersFulfilled(it.toMutableList(),onSuccessAction) }
        }

    }

    fun updateOrder(order: Order){
        FirebaseService.updateCartOrders(order)
    }

    fun removeOrder(order:Order){
        FirebaseService.removeOrder(order)
    }


    
}