package com.example.ormmahasiswa.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ormmahasiswa.data.dao.UserDao
import com.example.ormmahasiswa.data.entity.User

@Database(entities = [User::class], version = 4) // Increment version jika ada perubahan schema
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao

    companion object{
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app-database")
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }
    }
}
