package com.ertreby.foodbox.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

data class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val id: String? = null,
    val address:String?=null,
    val carts: MutableList<Cart>? = null){


    companion object{

        suspend fun DocumentSnapshot.toUser(): User{
            val firstName=getString("firstName")
            val lastName=getString("lastName")
            val id=getString("id")
            val address=getString("address")
            val carts=reference.collection("carts").get().await().documents.mapNotNull { it.toObject<Cart>() }
            val user=User(firstName,lastName,id,address,carts.toMutableList())
            return user
        }
    }
}