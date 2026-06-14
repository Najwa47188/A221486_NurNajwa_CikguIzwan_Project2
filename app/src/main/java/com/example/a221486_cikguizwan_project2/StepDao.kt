package com.example.a221486_cikguizwan_project2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    // ---- Fungsi Langkah Kaki ----
    @Query("SELECT * FROM gofit_steps ORDER BY date DESC")
    fun getAllSteps(): Flow<List<StepEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStep(step: StepEntity)

    // ---- Fungsi Profil Pengguna (Guna UserProfile) ----
    @Query("SELECT * FROM gofit_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    // ---- Fungsi Matlamat (Goals) ----
    @Query("SELECT * FROM gofit_goals ORDER BY id DESC")
    fun getAllGoals(): Flow<List<Goal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal)
}