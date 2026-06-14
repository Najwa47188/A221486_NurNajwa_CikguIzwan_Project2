package com.example.a221486_cikguizwan_project2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gofit_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Tetapkan 1 ID sahaja untuk profil peranti ini
    val name: String = "User",
    val age: String = "20",
    val weight: String = "60 kg",
    val gender: String = "Male"
)