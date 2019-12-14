package com.photo.sharing.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.photo.sharing.model.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriends(users: List<Friends>)

    @Query("SELECT User.*,fr.id as friendUserId, fr.friendId, fr.userId, fr.friendSince FROM `User` LEFT JOIN `Friends` as fr ON User.id = fr.friendId WHERE fr.userId = :userId")
    fun getUserFriends(userId: Int): LiveData<List<UserFriends>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSharedPhoto(photo: List<SharedPhoto>)

    @Query("SELECT User.*,sp.friendId as friendId, sp.userId, sp.sharedId, sp.imageName FROM `User` INNER JOIN `SharedPhoto` as sp ON User.id = sp.friendId WHERE sp.userId = :userId AND sp.friendId = :friendId")
    fun getUserSharedPhotos(userId: Int, friendId: Int): LiveData<List<UserSharedPhoto>>

    @Query("DELETE FROM `SharedPhoto` where sharedId is :sharedId")
    suspend fun deletedSharedPhoto(sharedId: Int)

    @Query("SELECT * FROM User")
    suspend fun getUsers(): List<User>

    @Query("SELECT * FROM SharedPhoto WHERE userId =:userId AND friendId =:friendId")
    fun test(userId: Int, friendId: Int): LiveData<List<SharedPhoto>>
}