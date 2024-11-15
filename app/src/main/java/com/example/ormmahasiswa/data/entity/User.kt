package com.example.ormmahasiswa.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Long? = null,
    val namamahasiswa: String,
    val nim: String,
    val alamat: String,
    val asalsekolah: String
) : Serializable
