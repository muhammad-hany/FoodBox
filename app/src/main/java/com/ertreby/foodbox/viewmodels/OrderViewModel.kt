package com.ertreby.foodbox.viewmodels

import androidx.lifecycle.ViewModel
import com.ertreby.foodbox.data.Cart
import com.ertreby.foodbox.data.Order
import com.ertreby.foodbox.data.FirebaseService

class OrderViewModel : ViewModel() {




    fun submitOrder(order: Order, onSubmissionSuccess: (Cart) -> Unit) {
        FirebaseService.setOrderToActiveCart(order,onSubmissionSuccess)
    }
}