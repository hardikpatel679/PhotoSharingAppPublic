package com.photo.sharing.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SharedPhoto(
    @PrimaryKey(autoGenerate = true)
    val sharedId: Int = 0,
    @ColumnInfo(index = true)
    val userId: Int,
    val friendId: Int,
    val imageName: String
)