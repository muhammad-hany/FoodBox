package com.ertreby.foodbox.data

import android.app.Activity
import com.ertreby.foodbox.data.User.Companion.toUser
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

object FirebaseService {


    val currentUser: FirebaseUser? by lazy {
        Firebase.auth.currentUser
    }


    suspend fun getUserActiveCart(): Cart? {
        val db = Firebase.firestore
        val cartRef =
            db.collection("users").document(currentUser?.uid.toString()).collection("carts")
        return try {
            cartRef.whereEqualTo("isItFulfilled", false).get()
                .await().documents.mapNotNull { it.toObject<Cart>() }[0]
        } catch (e: Exception) {
            Firebase.crashlytics.log("Error getting user active cart")
            Firebase.crashlytics.setCustomKey("user id", currentUser?.uid.toString())
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
            val user = User(firstName, lastName, id, address)
            Firebase.firestore.collection("users").document(id.toString()).set(user)
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
            db.collection("users").document(userId.toString()).get().await().toUser()
        } catch (e: Exception) {
            Firebase.crashlytics.log("Error getting user object")
            Firebase.crashlytics.setCustomKey("user id", currentUser?.uid.toString())
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
        return try {
            Firebase.firestore.collection("meals").get().await().mapNotNull { it.toObject() }
        } catch (e: java.lang.Exception) {
            Firebase.crashlytics.log("Error getting meals")
            Firebase.crashlytics.setCustomKey("user id", currentUser?.uid.toString())
            Firebase.crashlytics.recordException(e)
            emptyList()
        }
    }

    suspend fun queryMeals(field: String, value: String): List<Meal> {
        return Firebase.firestore.collection("meals").whereEqualTo(field, value).get()
            .await().documents.mapNotNull { it.toObject() }
    }


    suspend fun getRestaurants(): List<Restaurant> {
        return try {
            Firebase.firestore.collection("restaurants").get().await().mapNotNull { it.toObject() }
        } catch (e: java.lang.Exception) {
            Firebase.crashlytics.log("Error getting restaurants")
            Firebase.crashlytics.setCustomKey("user id", currentUser?.uid.toString())
            Firebase.crashlytics.recordException(e)
            emptyList()
        }
    }

    private val cartsRef: CollectionReference by lazy {
        Firebase.firestore.collection("users").document(currentUser?.uid.toString())
            .collection("carts")
    }




    fun setCartFulfilled(cart: Cart, onSuccessAction: () -> Unit) {

        cartsRef.document(cart.timestamp.toString()).update("isItFulfilled", true).addOnSuccessListener {
            onSuccessAction()
        }

    }

    fun updateCartOrders(cart: Cart) {
        val isOrdersEmpty=cart.orders?.isNotEmpty() ?: true
        if (isOrdersEmpty){
            cartsRef.document(cart.timestamp.toString()).set(cart)
        }else{
            cartsRef.document(cart.timestamp.toString()).delete()
        }

    }



    fun setOrderToActiveCart(order: Order, onAddingSuccess: (Cart) -> Unit) {
        val db = Firebase.firestore
        val userId = Firebase.auth.currentUser?.uid
        val cartRef = db.collection("users").document(userId.toString()).collection("carts")

        cartRef.whereEqualTo("isItFulfilled", false).get().addOnSuccessListener { snapshot ->
            if (snapshot.size() > 0) {
                val carts = snapshot.toObjects<Cart>()
                carts[0].orders?.add(order)
                val cartDocumentName = carts[0].timestamp.toString()
                cartRef.document(cartDocumentName).update("orders", carts[0].orders)
                    .addOnSuccessListener {
                        onAddingSuccess(carts[0])
                    }
            } else {
                val cart = Cart(mutableListOf(order), false, order.timestamp)
                cartRef.document(order.timestamp.toString()).set(cart).addOnSuccessListener {
                    onAddingSuccess(cart)
                }
            }
        }
    }


}