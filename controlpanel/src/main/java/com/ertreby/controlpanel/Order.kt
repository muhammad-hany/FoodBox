package com.ertreby.controlpanel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val meal: Meal? = null,
    val orderedExtras: List<String>? = null,
    var quantity: Int? = null,
    val timestamp: String? = null
):Parcelable
