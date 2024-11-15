package com.example.ormmahasiswa.data.dao

import androidx.room.*
import com.example.ormmahasiswa.data.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertAll(vararg user: User)

    @Delete
    suspend fun delete(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid = :id LIMIT 1")
    suspend fun getById(id: Long): User
}
