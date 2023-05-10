package com.erya.filemanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FileEntity::class], version = 1, exportSchema = false)
abstract class FileRoomDatabase : RoomDatabase() {

    abstract fun fileDao(): FileDao

    companion object {
        @Volatile
        private var INSTANCE: FileRoomDatabase? = null

        fun getDatabase(
            context: Context
        ): FileRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FileRoomDatabase::class.java,
                    "saved_files"
                )
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
