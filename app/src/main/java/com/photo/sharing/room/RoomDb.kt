package com.photo.sharing.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.photo.sharing.model.Friends
import com.photo.sharing.model.SharedPhoto
import com.photo.sharing.model.User

@Database(
    entities = [User::class,
        Friends::class,
        SharedPhoto::class],
    version = 1,
    exportSchema = false
)
abstract class RoomDb : RoomDatabase() {
    abstract fun roomDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDb? = null

        fun getInstance(context: Context): RoomDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RoomDb::class.java, "photoshare.db"
            )
                .build()
    }
}