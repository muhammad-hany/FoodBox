package com.ertreby.controlpanel

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
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
        Firebase.messaging.token.addOnSuccessListener { token ->
            restaurant.token = token

            Firebase.firestore.collection(RESTAURANTS_COLLECTION_KEY)
                .document(restaurant.restaurantId.toString())
                .set(restaurant).addOnSuccessListener {

                    onSuccess()
                }.addOnFailureListener {
                    onFailure(it.message.toString())
                }
        }

    }

    fun saveTokenInServer(token: String) {
        val restaurantRef = Firebase.firestore.collection(RESTAURANTS_COLLECTION_KEY)
        val data = hashMapOf("token" to token)
        val restaurantId=Firebase.auth.currentUser?.uid
        restaurantId?.let {
            restaurantRef.document(it).set(data, SetOptions.merge())
        }

    }


    fun addMeal(
        meal: Meal,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        Firebase.firestore.collection(MEALS_COLLECTION_KEY).document(meal.id.toString()).set(meal)
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

    fun updateMeal(
        meal: Meal,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        Firebase.firestore.collection(MEALS_COLLECTION_KEY).document(meal.id.toString()).set(meal)
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener { onFailure(it.message.toString()) }

    }

    fun removeMeal(meal: Meal, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        Firebase.firestore.collection(MEALS_COLLECTION_KEY).document(meal.id.toString()).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.toString()) }
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


    fun getRestaurantMeals(onSuccess: (List<Meal>) -> Unit) {
        val id = Firebase.auth.currentUser?.uid.toString()
        val mealsRef = Firebase.firestore.collection(MEALS_COLLECTION_KEY)
        val query = mealsRef.whereEqualTo(RESTAURANT_ID_FIELD_KEY, id)
        query.get().addOnSuccessListener { querySnapShot ->
            onSuccess(querySnapShot.documents.mapNotNull { it.toObject() })
        }

    }


    fun getRestaurantOrders(onSuccess: (Array<Order>, Array<User>) -> Unit) {
        val restaurantId = Firebase.auth.currentUser?.uid ?: return
        val ordersRef = Firebase.firestore.collection(ORDERS_COLLECTION_KEY)
        val usersRef = Firebase.firestore.collection(USERS_COLLECTION_KEY)
        ordersRef.whereEqualTo(RESTAURANT_ID_FIELD_KEY, restaurantId)
            .whereEqualTo(FULFILLED_FIELD_KEY, false).get().addOnSuccessListener { ordersSnapshot ->
                val orders = ordersSnapshot.toObjects<Order>()
                if (orders.isEmpty()) return@addOnSuccessListener
                val usersIds = orders.map { it.userId }
                usersRef.whereIn(USER_ID_FIELD_KEY, usersIds).get()
                    .addOnSuccessListener { usersSnapshot ->
                        val users = usersSnapshot.toObjects<User>()
                        onSuccess(orders.toTypedArray(), users.toTypedArray())
                    }

            }

    }


    fun getUniqueId(): String {
        val alphabet: List<Char> = ('a'..'z') + ('0'..'9') + ('A'..'Z')
        return List(20) { alphabet.random() }.joinToString(separator = "")
    }


    private const val RESTAURANTS_COLLECTION_KEY = "restaurants"
    private const val MEALS_COLLECTION_KEY = "meals"
    private const val RESTAURANT_ID_FIELD_KEY = "restaurantId"
    private const val ORDERS_COLLECTION_KEY = "orders"
    private const val FULFILLED_FIELD_KEY = "fulfilled"
    private const val USERS_COLLECTION_KEY = "users"
    private const val USER_ID_FIELD_KEY = "id"


}