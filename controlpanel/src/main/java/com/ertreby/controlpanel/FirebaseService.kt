package com.ertreby.controlpanel

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object FirebaseService {

    val currentUser: FirebaseUser? by lazy {
        Firebase.auth.currentUser
    }

    fun signUp(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit

    ) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                result.user?.let { onSuccess(it.uid) }
            }.addOnFailureListener {
                onFailure(it.message.toString())
            }
    }


    fun signIn(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { result ->
            result.user?.let { onSuccess(it.uid) }
        }.addOnFailureListener {
            onFailure(it.message.toString())
        }
    }


    fun addRestaurant(
        restaurant: Restaurant,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        Firebase.firestore.collection("restaurants").document(restaurant.restaurantId.toString())
            .set(restaurant).addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onFailure(it.message.toString())
            }
    }


    fun addMeal(
        meal: Meal,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        Firebase.firestore.collection("meals").document(getUniqueId()).set(meal)
            .addOnSuccessListener {
                val restaurantRef = Firebase.firestore.collection("restaurants")
                    .document(meal.restaurantId.toString())
                Firebase.firestore.runTransaction {
                    val snapShot = it.get(restaurantRef)
                    val menus = (snapShot.get("menu") as List<*>).toMutableList()
                    menus.add(meal.id)
                    it.update(restaurantRef, "menu", menus)
                }.addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener { onFailure(it.message.toString()) }


            }.addOnFailureListener { onFailure(it.message.toString()) }
    }


    fun uploadLogoImage(
        imageUri: Uri,
        id: String,
        onSuccess: (String, String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val storageRef = Firebase.storage.reference.child("logos/$id")
        storageRef.putFile(imageUri).addOnSuccessListener {

            storageRef.downloadUrl.addOnSuccessListener { onSuccess(it.toString(), id) }

        }.addOnFailureListener { onFailure(it.message.toString()) }

    }


    fun uploadMealImage(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        val imageId = getUniqueId()
        val storageRef = Firebase.storage.reference.child("images/$imageId")
        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { onSuccess(it.toString()) }
                .addOnFailureListener { onFailure(it.message.toString()) }
        }.addOnFailureListener { onFailure(it.message.toString()) }
    }


    fun getUniqueId(): String {
        val alphabet: List<Char> = ('a'..'z') + ('0'..'9') + ('A'..'Z')
        return List(20) { alphabet.random() }.joinToString(separator = "")
    }
}