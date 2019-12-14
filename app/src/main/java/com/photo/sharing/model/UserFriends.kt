package com.photo.sharing.model

data class UserFriends(
    val id: Int,
    val userName: String,

    val friendUserId: Int,
    val userId: Int,
    val friendId: Int,
    val friendSince: Long
)