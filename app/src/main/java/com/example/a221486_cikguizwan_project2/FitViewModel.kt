package com.example.a221486_cikguizwan_project2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class FitUiState(
    val name: String = "User",
    val weight: String = "65 kg",
    val age: String = "22",
    val gender: String = "Male",
    val steps: Int = 0,
    val leaderboardList: List<CommunityUser> = emptyList()
)

class FitViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val stepDao = database.stepDao()

    // Initialize Firebase Firestore directly
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(FitUiState())
    val uiState: StateFlow<FitUiState> = _uiState.asStateFlow()

    val allHistorySteps = stepDao.getAllSteps()

    // Goals list state from Room
    val goalsList = stepDao.getAllGoals()

    private val todayDate: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    init {
        // Fetch profile data from Room
        viewModelScope.launch {
            stepDao.getUserProfile().collect { profile ->
                if (profile != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            name = profile.name,
                            weight = profile.weight,
                            age = profile.age,
                            gender = profile.gender
                        )
                    }
                } else {
                    // If profile is empty (first time opening), insert default data
                    // This ensures the database appears in App Inspection
                    stepDao.insertOrUpdateProfile(UserProfile(id = 1, name = "GoFit User"))
                }
            }
        }

        // Fetch today's step data from Room
        viewModelScope.launch {
            allHistorySteps.collect { history ->
                val todayRecord = history.find { it.date == todayDate }
                if (todayRecord != null) {
                    _uiState.update { it.copy(steps = todayRecord.stepCount) }
                }
            }
        }

        fetchLeaderboard()
    }

    fun incrementSteps() {
        _uiState.update { currentState ->
            val newSteps = currentState.steps + 1
            viewModelScope.launch {
                stepDao.insertOrUpdateStep(StepEntity(date = todayDate, stepCount = newSteps))
            }
            currentState.copy(steps = newSteps)
        }
    }

    // Function to add a new goal to Room
    fun addGoal(title: String, description: String) {
        viewModelScope.launch {
            stepDao.insertGoal(Goal(title = title, description = description))
        }
    }

    fun updateProfile(newName: String, newWeight: String, newAge: String, newGender: String = "Male") {
        viewModelScope.launch {
            stepDao.insertOrUpdateProfile(UserProfile(name = newName, weight = newWeight, age = newAge, gender = newGender))
            _uiState.update { currentState ->
                currentState.copy(name = newName, weight = newWeight, age = newAge, gender = newGender)
            }
        }
    }

    // ---- SHARE DATA TO FIREBASE CLOUD WITH LOGS ----
    fun shareStepsToCloud(onSuccess: () -> Unit) {
        val currentState = _uiState.value

        // Diagnostic logs for Logcat
        println("GoFitLog: Attempting to send data [${currentState.name}] to Firestore...")

        val userCloudData = hashMapOf(
            "username" to currentState.name,
            "age" to currentState.age,
            "weight" to currentState.weight,
            "gender" to currentState.gender,
            "totalSteps" to currentState.steps,
            "lastUpdated" to todayDate
        )

        firestore.collection("leaderboard")
            .document(currentState.name)
            .set(userCloudData)
            .addOnSuccessListener {
                println("GoFitLog: UPLOAD SUCCESSFUL TO CLOUD! 🎉")
                onSuccess()
                fetchLeaderboard()
            }
            .addOnFailureListener { exception ->
                println("GoFitLog: UPLOAD FAILED. Reason: ${exception.message}")
            }
    }

    // ---- FETCH DATA FROM FIREBASE ----
    fun fetchLeaderboard() {
        firestore.collection("leaderboard")
            .orderBy("totalSteps", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null || snapshot == null) {
                    println("GoFitLog: Failed to read leaderboard: ${error?.message}")
                    return@addSnapshotListener
                }

                val users = snapshot.documents.mapNotNull { doc ->
                    val username = doc.getString("username") ?: ""
                    val totalSteps = doc.getLong("totalSteps")?.toInt() ?: 0
                    CommunityUser(username, totalSteps)
                }

                _uiState.update { currentState ->
                    currentState.copy(leaderboardList = users)
                }
                println("GoFitLog: Leaderboard updated. Number of participants: ${users.size}")
            }
    }
}