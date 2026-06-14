package com.example.a221486_cikguizwan_project2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gofit_goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String
)