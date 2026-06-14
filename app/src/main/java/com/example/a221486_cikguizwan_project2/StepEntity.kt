package com.example.a221486_cikguizwan_project2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gofit_steps")
data class StepEntity(
    @PrimaryKey
    val date: String, // Contoh format: "2026-06-11"
    val stepCount: Int
)