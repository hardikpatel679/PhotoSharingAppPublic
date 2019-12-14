package com.photo.sharing.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.photo.sharing.base.PhotoSharingApplication
import com.photo.sharing.enumconstants.EnvironmentKey
import com.photo.sharing.model.*
import com.photo.sharing.repository.FriendsListRepository
import com.photo.sharing.utils.CurrentEnvironment
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class FriendsListViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var friendsListRepository: FriendsListRepository

    private val filterUserFriendsLiveData = MutableLiveData<Pair<Int, Int>>()

    private val userFriendSharedLiveData: LiveData<List<UserSharedPhoto>>

    private val imageUploadStatus = MutableLiveData<ResourceState>()

    var selectedUsers = HashSet<Int>()

    private val filterUserIdLiveData = MutableLiveData<Int>()
    private val userFriendsLiveData: LiveData<List<UserFriends>>

    init {
        (application as PhotoSharingApplication).component.inject(this)

        userFriendSharedLiveData =
            Transformations.switchMap<Pair<Int, Int>, List<UserSharedPhoto>>(
                filterUserFriendsLiveData
            ) {
                it?.let {
                    friendsListRepository.getUserSharedPhoto(it.first, it.second)
                }
            }

        userFriendsLiveData =
            Transformations.switchMap<Int, List<UserFriends>>(
                filterUserIdLiveData
            ) {
                it?.let {
                    friendsListRepository.getFriends(it)
                }
            }
    }

    fun getUsersFriendsList(): LiveData<List<UserFriends>> {
        return userFriendsLiveData
    }

    fun setFilterUserId(userId: Int) {
        filterUserIdLiveData.value = userId
    }

    private fun insertSharedPhoto(
        sharedPhoto: List<SharedPhoto>
    ) {
        viewModelScope.launch {
            friendsListRepository.insertPhoto(sharedPhoto)
        }
    }

    fun getUserSharedImages(): LiveData<List<UserSharedPhoto>> {
        return userFriendSharedLiveData
    }

    fun deleteSharedPhoto(sharedId: Int) {
        viewModelScope.launch {
            friendsListRepository.deleteSharedPhoto(sharedId)
        }
    }

    fun setFilter(userId: Int, friendId: Int) {
        filterUserFriendsLiveData.postValue(Pair(userId, friendId))
    }

    fun uploadSelectedImageToAWS(
        fileName: String,
        fileObj: File,
        transferUtility: TransferUtility
    ) {

        val sharePhotoFriends = mutableListOf<SharedPhoto>()

        selectedUsers.forEach { friendId ->
            sharePhotoFriends.add(
                SharedPhoto(
                    userId = CurrentEnvironment.getInt(EnvironmentKey.currentUserId),
                    friendId = friendId,
                    imageName = fileName
                )
            )
        }

        insertSharedPhoto(sharePhotoFriends)

        val uploadObserver =
            transferUtility.upload(
                "images/$fileName", fileObj, CannedAccessControlList.PublicRead
            )

        friendsListRepository.uploadFile(uploadObserver, fileObj, imageUploadStatus)

    }

    fun getImageUploadStatusLiveData(): LiveData<ResourceState> {
        return imageUploadStatus
    }

    suspend fun getUsers(): List<User> {
        return friendsListRepository.getUsersList()
    }
}