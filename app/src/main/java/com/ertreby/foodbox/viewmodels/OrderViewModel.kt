package com.ertreby.foodbox.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ertreby.foodbox.data.FirebaseService
import com.ertreby.foodbox.data.Order
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {



    private val _order= MutableLiveData<List<Order>>()
    val order: LiveData<List<Order>> =_order


    init {
        viewModelScope.launch {
            _order.value= FirebaseService.getUserActiveOrders()
        }
    }


    fun submitOrder(order: Order, onSubmissionSuccess: (Order) -> Unit , context: Context) {
        FirebaseService.bookOrder(order,onSubmissionSuccess)
       // order.orderId?.let { FirebaseService.sendMessageToRestaurant(it, context) }
    }
}