package com.ertreby.foodbox.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val meal: Meal? = null,
    val orderedExtras: List<String>? = null,
    var quantity: Int? = null,
    val orderId: String? = null,
    val restaurantId :String?=meal?.restaurantId,
    val fulfilled:Boolean?=null,
    val userId:String?=null
):Parcelable
