package com.photo.sharing.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userName: String
)