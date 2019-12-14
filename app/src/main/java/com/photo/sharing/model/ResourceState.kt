package com.photo.sharing.model

sealed class ResourceState {
    object LOADING : ResourceState()
    object SUCCESS : ResourceState()
    object ERROR : ResourceState()
    object DONE : ResourceState()
}