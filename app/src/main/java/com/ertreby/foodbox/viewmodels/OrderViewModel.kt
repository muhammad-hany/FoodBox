package com.ertreby.foodbox.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ertreby.foodbox.data.Cart
import com.ertreby.foodbox.data.FirebaseService
import com.ertreby.foodbox.data.Order
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {



    private val _cart= MutableLiveData<Cart>()
    val cart: LiveData<Cart> =_cart


    init {
        viewModelScope.launch {
            _cart.value= FirebaseService.getUserActiveCart()
        }
    }


    fun submitOrder(order: Order, onSubmissionSuccess: (Cart) -> Unit) {
        FirebaseService.setOrderToActiveCart(order,onSubmissionSuccess)
    }
}