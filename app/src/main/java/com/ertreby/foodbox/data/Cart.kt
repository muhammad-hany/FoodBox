package com.ertreby.foodbox.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val orders: MutableList<Order>?=null,
    @field:JvmField
    val isItFulfilled:Boolean?=null,
    val timestamp: String?=null
    ): Parcelable