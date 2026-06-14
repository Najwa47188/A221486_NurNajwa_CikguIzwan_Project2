package com.example.a221486_cikguizwan_project2

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthDetailScreen(activityName: String?, navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()

    var healthTipData by remember { mutableStateOf<HealthTipResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = activityName) {
        coroutineScope.launch {
            try {
                isLoading = true
                val responseList = RetrofitClient.getHealthTips()
                val foundData = responseList.find { it.activityName.equals(activityName, ignoreCase = true) }
                
                if (foundData != null) {
                    healthTipData = foundData
                } else {
                    healthTipData = getFallbackData(activityName ?: "Activity")
                }
                isLoading = false
            } catch (e: Exception) {
                e.printStackTrace()
                healthTipData = getFallbackData(activityName ?: "Activity")
                isLoading = false
                if (healthTipData == null) isError = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(activityName ?: "Tips", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFBFDFA))
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF43A047))
            } else if (isError || healthTipData == null) {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ErrorOutline, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.LightGray)
                    Spacer(Modifier.height(16.dp))
                    Text("Connection issue. Showing saved tips.", color = Color.Gray)
                }
            } else {
                val data = healthTipData!!
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Surface(
                            color = Color(0xFF43A047),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Text(data.activityName, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                                Spacer(Modifier.height(8.dp))
                                Text(data.description, color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp, lineHeight = 22.sp)
                            }
                        }
                    }

                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatCard("Calories", data.caloriesBurned, Icons.Default.LocalFireDepartment, Color(0xFFFF5252), Modifier.weight(1f))
                            StatCard("Duration", data.duration, Icons.Default.Timer, Color(0xFF448AFF), Modifier.weight(1f))
                        }
                    }

                    item {
                        Text("Pro Health Tips", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray, modifier = Modifier.padding(top = 8.dp))
                    }

                    items(data.tips) { tip ->
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color(0xFFF0F0F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                                Surface(color = Color(0xFFE8F5E9), shape = CircleShape, modifier = Modifier.size(28.dp)) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.padding(6.dp), tint = Color(0xFF43A047))
                                }
                                Spacer(Modifier.width(16.dp))
                                Text(tip, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
                            }
                        }
                    }
                    
                    item { Spacer(Modifier.height(20.dp)) }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(Modifier.height(12.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.DarkGray)
            Text(label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

fun getFallbackData(activity: String): HealthTipResponse? {
    // ... same implementation as before ...
    return when (activity.lowercase()) {
        "food" -> HealthTipResponse("Balanced Diet", "Nutritious food is essential for SDG 3.", "N/A", "Daily", listOf("Eat more vegetables", "Reduce sugar", "Drink water"))
        "water" -> HealthTipResponse("Hydration", "Stay hydrated for better organ function.", "0 kcal", "2L / day", listOf("8 glasses daily", "Use reusable bottle", "Drink before meals"))
        "weight" -> HealthTipResponse("Weight Control", "Healthy weight reduces disease risk.", "Variable", "Ongoing", listOf("Monitor weekly", "Diet & Exercise", "Stay consistent"))
        "heart rate" -> HealthTipResponse("Heart Health", "A strong heart ensures good circulation.", "N/A", "Always", listOf("Cardio exercise", "No smoking", "Quality sleep"))
        "cycling" -> HealthTipResponse("Cycling", "Low-impact cardio exercise.", "500 kcal/hr", "30 mins", listOf("Wear helmet", "Stay safe", "Check bike"))
        "walking" -> HealthTipResponse("Walking", "Simple way to maintain health.", "150 kcal", "30 mins", listOf("Comfy shoes", "Steady pace", "Nature walk"))
        "running" -> HealthTipResponse("Running", "Burns calories fast.", "350 kcal", "20 mins", listOf("Warm up", "Good form", "Cool down"))
        else -> HealthTipResponse(activity, "Health info for $activity.", "Varies", "Daily", listOf("Stay active", "Eat healthy", "Stay positive"))
    }
}