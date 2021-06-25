package com.ertreby.foodbox.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

data class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val id: String? = null,
    val carts: MutableList<Cart>? = null){


    companion object{

        suspend fun DocumentSnapshot.toUser(): User{
            val firstName=getString("firstName")
            val lastName=getString("lastName")
            val id=getString("id")
            val carts=reference.collection("carts").get().await().documents.mapNotNull { it.toObject<Cart>() }
            return User(firstName,lastName,id,carts.toMutableList())
        }
    }
}