package com.photo.sharing.repository

import android.app.Application
import com.photo.sharing.model.Friends
import com.photo.sharing.model.User
import com.photo.sharing.room.RoomDb

class SplashRepository(application: Application) {

    private val userDao = RoomDb.getInstance(application).roomDao()


    suspend fun insertUsers(users: List<User>) {
        userDao.insertUsers(users)
    }

    suspend fun insertFriends(friends: List<Friends>) {
        userDao.insertFriends(friends)
    }
}