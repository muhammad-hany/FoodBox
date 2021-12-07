package com.ertreby.controlpanel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Restaurant(
    val name: String? = null,
    val description: String? = null,
    val rating: Int? = null,
    val logoImage: String? = null,
    val color: String? = null,
    val menu: List<String>? = null,
    val restaurantId:String?= null,
    var token:String?=null
):Parcelable {

    override fun toString(): String {
        return name.toString()
    }
}