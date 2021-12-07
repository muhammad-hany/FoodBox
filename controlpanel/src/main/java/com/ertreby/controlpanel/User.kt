package com.ertreby.controlpanel

data class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val id: String? = null,
    val address:String?=null,
    val ordersIds: List<String>? = null){


    /*companion object{

        suspend fun DocumentSnapshot.toUser(): User{
            val firstName=getString("firstName")
            val lastName=getString("lastName")
            val id=getString("id")
            val address=getString("address")
            val orderIds=this.reference.collection("ordersIds").get().await().documents.mapNotNull { it. }
            val user=User(firstName,lastName,id,address,carts.toMutableList())
            return user
        }
    }*/
}