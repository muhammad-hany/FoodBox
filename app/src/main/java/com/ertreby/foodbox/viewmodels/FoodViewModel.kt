package com.ertreby.foodbox.viewmodels

import androidx.lifecycle.*
import com.ertreby.foodbox.data.Meal
import com.ertreby.foodbox.data.FirebaseService
import kotlinx.coroutines.launch

class FoodViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {


    private val _meals: MutableLiveData<List<Meal>> = MutableLiveData()
    val meals: LiveData<List<Meal>> = _meals

    init {
        val field: String = savedStateHandle.get<String>("field").toString()
        val value: String = savedStateHandle.get<String>("value").toString()

        viewModelScope.launch {
            _meals.value = FirebaseService.queryMeals(field, value)
        }
    }


}