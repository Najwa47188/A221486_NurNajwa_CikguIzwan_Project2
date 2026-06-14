package com.example.a221486_cikguizwan_project2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gofit_profile")
data class ProfileEntity(
    @PrimaryKey val id: Int = 1, // Kita guna 1 ID sahaja sebab telefon ini milik seorang pengguna
    val name: String,
    val weight: String,
    val age: String
)