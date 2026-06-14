package com.example.a221486_cikguizwan_project2

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(viewModel: FitViewModel, navController: NavHostController) {
    val historyList by viewModel.allHistorySteps.collectAsState(initial = emptyList())
    val goalsList by viewModel.goalsList.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Activity Insights", fontWeight = FontWeight.ExtraBold, color = Color(0xFF2E7D32)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFBFDFA))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // --- SECTION 1: STEPS HISTORY ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = Color(0xFFE8F5E9), shape = CircleShape, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.History, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.padding(8.dp))
                }
                Spacer(Modifier.width(12.dp))
                Text("Step History", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.DarkGray)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            if (historyList.isEmpty()) {
                EmptyStateCard("No history records yet. Start walking!")
            } else {
                historyList.forEach { item ->
                    HistoryCard(item.date, "${item.stepCount} Steps")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- SECTION 2: SAVED GOALS ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = Color(0xFFE8F5E9), shape = CircleShape, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.TrackChanges, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.padding(8.dp))
                }
                Spacer(Modifier.width(12.dp))
                Text("Your Health Goals", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.DarkGray)
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            if (goalsList.isEmpty()) {
                EmptyStateCard("No goals added yet. Add one in the 'Together' screen!")
            } else {
                goalsList.forEach { goal ->
                    GoalCard(goal.title, goal.description)
                }
            }
            
            // Extra spacer at the bottom
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun HistoryCard(date: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF8BC34A), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = date, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            }
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF43A047),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun GoalCard(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9).copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, Color(0xFFE8F5E9))
    ) {
        ListItem(
            headlineContent = { Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32)) },
            supportingContent = { Text(description, color = Color.Gray) },
            leadingContent = { Icon(Icons.Default.CheckCircle, tint = Color(0xFF43A047), contentDescription = null) },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}

@Composable
fun EmptyStateCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
            Text(text = message, color = Color.Gray, textAlign = TextAlign.Center, fontSize = 14.sp)
        }
    }
}
