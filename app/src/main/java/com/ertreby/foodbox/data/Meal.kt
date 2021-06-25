package com.ertreby.foodbox.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meal(
    val name: String?=null,
    val restaurantId: String?=null,
    val price: String?=null,
    val imageUrl: String?=null,
    val extras: List<String>?=null,
    val description: String?=null,
    val rating: Int?=null,
    val type:String?=null
):Parcelable {
    override fun toString(): String {
        return name.toString()
    }


}