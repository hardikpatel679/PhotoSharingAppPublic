package com.photo.sharing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.photo.sharing.base.PhotoSharingApplication
import com.photo.sharing.model.Friends
import com.photo.sharing.model.User
import com.photo.sharing.repository.SplashRepository
import com.photo.sharing.utils.CurrentEnvironment
import com.photo.sharing.enumconstants.EnvironmentKey
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel constructor(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var splashRepository: SplashRepository

    private var isDbCreated = MutableLiveData<Boolean>()

    init {
        (application as PhotoSharingApplication).component.inject(this)

        if (CurrentEnvironment.getBoolean(EnvironmentKey.isDataBaseCreated)) {
            isDbCreated.postValue(true)
        } else {
            createUsersWithFriends()
        }
    }

    private fun insertUsers(users: List<User>) {
        viewModelScope.launch {
            splashRepository.insertUsers(users)
        }
    }

    private fun insertFriends(friends: List<Friends>) {
        viewModelScope.launch {
            splashRepository.insertFriends(friends)
        }
    }

    private fun createUsersWithFriends() {
        val usersList = mutableListOf<User>()
        usersList.add(User(userName = "Santosh"))
        usersList.add(User(userName = "Kapil"))
        usersList.add(User(userName = "Sandesh"))
        usersList.add(User(userName = "Jayram"))
        usersList.add(User(userName = "Sagar"))
        usersList.add(User(userName = "Shivaji"))
        usersList.add(User(userName = "Ram"))
        usersList.add(User(userName = "Laxman"))
        usersList.add(User(userName = "Deepak"))
        insertUsers(usersList)

        val friendsList = mutableListOf<Friends>()
        friendsList.add(Friends(userId = 1, friendId = 2, friendSince = 1559369051))
        friendsList.add(Friends(userId = 1, friendId = 3, friendSince = 1514786651))
        friendsList.add(Friends(userId = 1, friendId = 4, friendSince = 1554098651))
        friendsList.add(Friends(userId = 1, friendId = 5, friendSince = 1559369051))
        friendsList.add(Friends(userId = 1, friendId = 7, friendSince = 1547186651))

        friendsList.add(Friends(userId = 2, friendId = 1, friendSince = 1559369051))
        friendsList.add(Friends(userId = 2, friendId = 6, friendSince = 1514786651))
        friendsList.add(Friends(userId = 2, friendId = 7, friendSince = 1547186651))

        friendsList.add(Friends(userId = 3, friendId = 1, friendSince = 1559369051))
        friendsList.add(Friends(userId = 4, friendId = 1, friendSince = 1559369051))
        friendsList.add(Friends(userId = 5, friendId = 1, friendSince = 1559369051))
        friendsList.add(Friends(userId = 7, friendId = 1, friendSince = 1559369051))
        insertFriends(friendsList)

        CurrentEnvironment.setBoolean(EnvironmentKey.isDataBaseCreated, true)
        CurrentEnvironment.setInt(EnvironmentKey.currentUserId, 1)
        isDbCreated.postValue(true)
    }

    fun isDBCreated(): LiveData<Boolean> {
        return isDbCreated
    }
}