package com.example.a221486_cikguizwan_project2

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeScreen(viewModel: FitViewModel, navController: NavHostController, snackbarHostState: SnackbarHostState) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("GoFit", fontWeight = FontWeight.ExtraBold, color = Color(0xFF2E7D32))
                        Spacer(Modifier.width(8.dp))
                        Surface(
                            color = Color(0xFF8BC34A).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("SDG 3", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier.padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(uiState.name, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.AccountCircle, 
                            contentDescription = null, 
                            tint = Color(0xFF8BC34A), 
                            modifier = Modifier.size(32.dp).clickable { navController.navigate("profile_display") }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFBFDFA))
                .verticalScroll(scrollState)
        ) {
            // 1. Welcome Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .background(Brush.horizontalGradient(listOf(Color(0xFF66BB6A), Color(0xFF43A047))))
                        .padding(24.dp)
                ) {
                    Column {
                        Text("Welcome back,", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
                        Text(uiState.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Ready to achieve your health goals today?",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // 2. Main Step Tracker Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Steps Counter", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = Color(0xFF8BC34A))
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { (uiState.steps / 6000f).coerceIn(0f, 1f) },
                            modifier = Modifier.size(160.dp),
                            color = Color(0xFF43A047),
                            strokeWidth = 12.dp,
                            trackColor = Color(0xFFE8F5E9),
                            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${uiState.steps}",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF2E7D32)
                            )
                            Text(
                                text = "of 6,000",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = {
                            viewModel.shareStepsToCloud {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Data synced to Firebase Cloud!")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9)),
                        elevation = null
                    ) {
                        Icon(Icons.Default.CloudSync, contentDescription = null, tint = Color(0xFF2E7D32))
                        Spacer(Modifier.width(8.dp))
                        Text("Sync to Leaderboard", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // 3. Navigation Shortcuts
            Text(
                "Quick Access",
                modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 12.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.DarkGray
            )

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // Button to Activities
                ShortcutCard(
                    title = "Health Activities & Tips",
                    subtitle = "Get expert advice from API",
                    icon = Icons.Default.HealthAndSafety,
                    color = Color(0xFF43A047),
                    onClick = { navController.navigate("activities") }
                )
                
                Spacer(Modifier.height(12.dp))
                
                // Button to Map
                ShortcutCard(
                    title = "Community Locations",
                    subtitle = "View full map of activities",
                    icon = Icons.Default.Map,
                    color = Color(0xFF2196F3),
                    onClick = { navController.navigate("community_map") }
                )
                
                Spacer(Modifier.height(12.dp))
                
                // Button to Together (Goals)
                ShortcutCard(
                    title = "Set Community Goals",
                    subtitle = "Contribute to SDG 3",
                    icon = Icons.Default.Flag,
                    color = Color(0xFFFF9800),
                    onClick = { navController.navigate("together") }
                )
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ShortcutCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = color.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.padding(12.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}
