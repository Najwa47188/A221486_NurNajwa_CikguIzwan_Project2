package com.example.a221486_cikguizwan_project2

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TogetherScreen(viewModel: FitViewModel) { // Menggunakan viewModel sahaja
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Komuniti GoFit (Firebase Cloud)",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4DB6AC)
        )
        Text(
            text = "Kongsi langkah anda untuk menyokong SDG 3 bersama komuniti.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.shareStepsToCloud {
                    Toast.makeText(context, "Berjaya berkongsi kemajuan ke Cloud! ☁️", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A))
        ) {
            Icon(Icons.Default.CloudUpload, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Kongsi Langkah Saya Hari Ini")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Papan Pendahulu Semasa",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.leaderboardList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Tiada data komuniti di awan.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(uiState.leaderboardList) { index, user ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (index < 3) {
                                    Icon(
                                        Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = if (index == 0) Color(0xFFFFD700) else if (index == 1) Color(0xFFC0C0C0) else Color(0xFFCD7F32)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(
                                    text = "${index + 1}. ${user.username}",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "${user.totalSteps} steps",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4DB6AC)
                            )
                        }
                    }
                }
            }
        }
    }
}