package com.ertreby.foodbox.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ertreby.foodbox.data.FirebaseService
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _firebaseUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    init {
        viewModelScope.launch {
            _firebaseUser.value = FirebaseService.currentUser
        }
    }

}