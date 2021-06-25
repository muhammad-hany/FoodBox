package com.ertreby.foodbox.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ertreby.foodbox.data.Cart
import com.ertreby.foodbox.data.FirebaseService
import kotlinx.coroutines.launch

class CartViewModel  () : ViewModel() {


    private val _cart=MutableLiveData<Cart>()
    val cart:LiveData<Cart> =_cart


    init {
        viewModelScope.launch {
            _cart.value= FirebaseService.getUserActiveCart()
        }
    }




    fun setCartFulfilled(onSuccessAction:()->Unit){
        viewModelScope.launch {
            cart.value?.let { FirebaseService.setCartFulfilled(it,onSuccessAction) }
        }

    }


    
}