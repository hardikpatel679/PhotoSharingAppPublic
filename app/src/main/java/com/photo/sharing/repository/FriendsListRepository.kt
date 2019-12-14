package com.photo.sharing.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.photo.sharing.model.*
import com.photo.sharing.room.RoomDb
import com.photo.sharing.utils.ProgressDialogUtils
import java.io.File

class FriendsListRepository(application: Application) {

    private val userDao = RoomDb.getInstance(application).roomDao()

    fun getFriends(userId: Int = 1): LiveData<List<UserFriends>> {
        return userDao.getUserFriends(userId)
    }

    suspend fun insertPhoto(sharedPhoto: List<SharedPhoto>) {
        userDao.insertSharedPhoto(sharedPhoto)
    }

    fun getUserSharedPhoto(userId: Int, friendId: Int): LiveData<List<UserSharedPhoto>> {
        return userDao.getUserSharedPhotos(userId, friendId)
    }

    suspend fun deleteSharedPhoto(sharedId: Int) {
        userDao.deletedSharedPhoto(sharedId)
    }

    suspend fun getUsersList(): List<User> {
        return userDao.getUsers()
    }

    fun uploadFile(
        uploadObserver: TransferObserver,
        fileObj: File,
        imageUploadStatus: MutableLiveData<ResourceState>
    ) {

        imageUploadStatus.postValue(ResourceState.LOADING)
        uploadObserver.setTransferListener(object : TransferListener {

            override fun onStateChanged(id: Int, state: TransferState) {
                if (TransferState.COMPLETED == state) {
                    imageUploadStatus.postValue(ResourceState.SUCCESS)
                    imageUploadStatus.postValue(ResourceState.DONE)
                } else if (TransferState.FAILED == state)
                    imageUploadStatus.postValue(ResourceState.ERROR)

                fileObj.delete()
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                val percentDone = (bytesCurrent.toFloat() / bytesTotal.toFloat() * 100).toInt()
                if (percentDone == 100)
                    imageUploadStatus.postValue(ResourceState.SUCCESS)
            }

            override fun onError(id: Int, ex: Exception) {
                ex.printStackTrace()
                ProgressDialogUtils.dismissDialog()
                imageUploadStatus.postValue(ResourceState.ERROR)
            }

        })
    }
}