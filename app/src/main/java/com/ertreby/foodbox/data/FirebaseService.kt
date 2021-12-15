package com.ertreby.foodbox.data

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

object FirebaseService {


    suspend fun getUserActiveOrders(): List<Order>? {
        val db = Firebase.firestore
        val userOrdersRef =
            db.collection(ORDERS_COLLECTION_KEY)
        val userId = Firebase.auth.currentUser?.uid

        return try {
            userOrdersRef.whereEqualTo("fulfilled", false).whereEqualTo("userId", userId).get()
                .await().documents.mapNotNull {
                    it.toObject<Order>()
                }
        } catch (e: Exception) {
            Firebase.crashlytics.log("Error getting user active cart")
            Firebase.crashlytics.setCustomKey("user id", userId.toString())
            Firebase.crashlytics.recordException(e)
            null
        }

    }

    fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        address: String,
        onSuccess: () -> Unit,
        onFailed: (e: java.lang.Exception) -> Unit
    ) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val id = it.user?.uid
            val user = User(firstName, lastName, id, address, listOf())
            Firebase.firestore.collection(USERS_COLLECTION_KEY).document(id.toString()).set(user)
                .addOnSuccessListener {
                    onSuccess()
                }

        }.addOnFailureListener {
            onFailed(it)
        }
    }


    suspend fun getUserProfile(): User? {
        val db = Firebase.firestore
        val userId = Firebase.auth.currentUser?.uid
        return try {
            db.collection(USERS_COLLECTION_KEY).document(userId.toString()).get().await().toObject<User>()
        } catch (e: Exception) {
            Firebase.crashlytics.log("Error getting user object")
            Firebase.crashlytics.setCustomKey("user id", userId.toString())
            Firebase.crashlytics.recordException(e)
            null
        }

    }

    fun verifyPhoneNumber(
        fullNumber: String,
        activity: Activity,
        onSuccess: () -> Unit,
        onFail: (java.lang.Exception?) -> Unit
    ) {
        val options = PhoneAuthOptions.newBuilder(Firebase.auth).apply {

            setPhoneNumber(fullNumber)
            setTimeout(60L, TimeUnit.SECONDS)
            setActivity(activity)
            setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(p0, onSuccess, onFail)
                }

                override fun onVerificationFailed(p0: FirebaseException) {

                }

            })

        }.build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        onSuccess: () -> Unit,
        onFail: (java.lang.Exception?) -> Unit
    ) {

        Firebase.auth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                onFail(it.exception)
            }

        }
    }

    suspend fun getMeals(): List<Meal> {
        val userId = Firebase.auth.currentUser?.uid
        return try {
            Firebase.firestore.collection(MEALS_COLLECTION_KEY).get().await().mapNotNull { it.toObject() }
        } catch (e: java.lang.Exception) {
            Firebase.crashlytics.log("Error getting meals")
            Firebase.crashlytics.setCustomKey("user id", userId.toString())
            Firebase.crashlytics.recordException(e)
            emptyList()
        }
    }

    suspend fun queryMeals(field: String, value: String): List<Meal> {
        return Firebase.firestore.collection(MEALS_COLLECTION_KEY).whereEqualTo(field, value).get()
            .await().documents.mapNotNull { it.toObject() }
    }


    suspend fun getRestaurants(): List<Restaurant> {
        val userId = Firebase.auth.currentUser?.uid
        return try {
            Firebase.firestore.collection(RESTAURANTS_COLLECTION_KEY).get().await().mapNotNull { it.toObject() }
        } catch (e: java.lang.Exception) {
            Firebase.crashlytics.log("Error getting restaurants")
            Firebase.crashlytics.setCustomKey("user id", userId.toString())
            Firebase.crashlytics.recordException(e)
            emptyList()
        }
    }

    private val ordersRef: CollectionReference by lazy {
        Firebase.firestore.collection(ORDERS_COLLECTION_KEY)
    }


    fun setOrdersFulfilled(orders: MutableList<Order>, onSuccessAction: () -> Unit) {

        val order = orders.last()
        ordersRef.document(order.orderId.toString()).update("fulfilled", true)
            .addOnSuccessListener {
                if (orders.isNotEmpty()) {
                    orders.removeLast()
                    setOrdersFulfilled(orders, onSuccessAction)
                } else {
                    onSuccessAction()
                }

            }

    }

    fun updateCartOrders(order: Order) {
        ordersRef.document(order.orderId.toString()).set(order)


    }


    fun removeOrder(order: Order) {
        ordersRef.document(order.orderId.toString()).delete()
    }


    fun bookOrder(order: Order, onBookingSuccess: (Order) -> Unit) {
        val orderRef = ordersRef.document(order.orderId.toString())

        orderRef.set(order).addOnSuccessListener {
            addOrderIdInDb(order, onBookingSuccess)

        }


    }

    private fun addOrderIdInDb(order: Order, onBookingSuccess: (Order) -> Unit) {
        val db = Firebase.firestore
        val userId = Firebase.auth.currentUser?.uid
        val data = hashMapOf("fulfilled" to false, "orderId" to order.orderId)
        val restaurantId = order.restaurantId
        if (userId != null && restaurantId != null) {
            db.collection("users").document(userId).collection("ordersIds")
                .document(order.orderId.toString())
                .set(data).addOnSuccessListener {
                    db.collection(RESTAURANTS_COLLECTION_KEY).document(restaurantId).collection("ordersIds")
                        .document(order.orderId.toString()).set(data).addOnSuccessListener {

                          //  order.orderId?.let { sendMessageToRestaurant(restaurantId, it) }
                            onBookingSuccess(order)
                        }
                }
        }
    }


     fun sendMessageToRestaurant(orderId: String, context: Context) {

        val  url ="https://us-central1-food-box-e9997.cloudfunctions.net/sendRestarantMessage?orderId=$orderId"
        val queue=Volley.newRequestQueue(context)
        var responseText:String
        val stringRequest = StringRequest(Request.Method.GET, url,

            { response ->

                responseText = "Response is: ${response.substring(0, 500)}"
                Log.v("TAG",responseText)
            },
            {
                responseText = "That didn't work!"
                Log.v("TAG",responseText)
            })

        queue.add(stringRequest)
    }


    fun getUniqueId(): String {
        val alphabet: List<Char> = ('a'..'z') + ('0'..'9') + ('A'..'Z')
        return List(20) { alphabet.random() }.joinToString(separator = "")
    }

    private const val RESTAURANTS_COLLECTION_KEY = "restaurants"
    private const val RESTAURANT_TOKEN_FIELD_KEY = "token"
    private const val MEALS_COLLECTION_KEY = "meals"
    private const val RESTAURANT_ID_FIELD_KEY = "restaurantId"
    private const val ORDERS_COLLECTION_KEY = "orders"
    private const val FULFILLED_FIELD_KEY = "fulfilled"
    private const val USERS_COLLECTION_KEY = "users"
    private const val USER_ID_FIELD_KEY = "id"


}