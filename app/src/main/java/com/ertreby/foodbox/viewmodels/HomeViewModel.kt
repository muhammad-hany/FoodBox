package com.ertreby.foodbox.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ertreby.foodbox.data.FirebaseService
import com.ertreby.foodbox.data.Meal
import com.ertreby.foodbox.data.Restaurant
import com.ertreby.foodbox.data.User
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _restaurants: MutableLiveData<List<Restaurant>> =
        MutableLiveData()
    val restaurants: LiveData<List<Restaurant>> = _restaurants

    private val _meals: MutableLiveData<List<Meal>> = MutableLiveData()
    val meals: LiveData<List<Meal>> = _meals

    private var _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    init {
        setUpValues()

    }


    /*fun getActiveCart(): Cart? {
        var cart: Cart? = null
        user.value?.carts?.forEach { _cart ->
            _cart.isItFulfilled?.let { if (!it) cart = _cart }

        }
        return cart
    }*/



    fun getSearchList():List<String>{
        val result= mutableListOf<String>()
        meals.value?.forEach { result.add(it.name.toString()) }
        restaurants.value?.forEach { result.add(it.toString()) }
        return result
    }

    fun setUpValues(){
        viewModelScope.launch {
            _restaurants.value = FirebaseService.getRestaurants()
            _meals.value = FirebaseService.getMeals()
            _user.value = FirebaseService.getUserProfile()

        }
    }
}