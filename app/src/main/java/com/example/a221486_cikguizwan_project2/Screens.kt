package com.example.a221486_cikguizwan_project2

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

// --- 1. TOGETHER SCREEN (Input Form) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TogetherScreen(viewModel: FitViewModel, navController: NavHostController) {
    var goalTitle by remember { mutableStateOf("") }
    var goalDesc by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Set Community Goals", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Surface(
                color = Color(0xFF8BC34A).copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF2E7D32))
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "SDG 3: Good Health and Well-being. Share your targets with the community!",
                        fontSize = 12.sp,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = goalTitle,
                onValueChange = { goalTitle = it },
                label = { Text("What is your goal?") },
                placeholder = { Text("e.g. 5km Morning Run") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Flag, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = goalDesc,
                onValueChange = { goalDesc = it },
                label = { Text("Additional Notes") },
                placeholder = { Text("Keep it simple and motivating!") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 4,
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (goalTitle.isNotBlank()) {
                        viewModel.addGoal(goalTitle, goalDesc)
                        navController.navigate("summary")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
            ) {
                Text("SAVE GOAL", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))
            
            Text("Community Location", fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(Modifier.height(12.dp))
            Card(
                modifier = Modifier.clickable { navController.navigate("community_map") },
                shape = RoundedCornerShape(20.dp), 
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painterResource(R.drawable.together_map), 
                        null, 
                        modifier = Modifier.fillMaxWidth().height(160.dp),
                        alpha = 0.6f
                    )
                    Surface(color = Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp)) {
                        Text("View Full Map", color = Color.White, modifier = Modifier.padding(8.dp), fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- COMMUNITY MAP SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityMapScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Community Map", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black)
        ) {
            Image(
                painter = painterResource(R.drawable.together_map),
                contentDescription = "Full Screen Map",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// --- ACTIVITIES SCREEN (API GRID) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesScreen(viewModel: FitViewModel, navController: NavHostController) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Activities", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
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
            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            ) {
                Text(
                    "Explore health tips and expert advice for various activities to improve your well-being.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    color = Color(0xFF2E7D32)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                ModernFeatureCell("Running", "Expert Tips", R.drawable.running, Modifier.weight(1f), Color(0xFFFFEBEE)) {
                    navController.navigate("details/Running")
                }
                Spacer(Modifier.width(12.dp))
                ModernFeatureCell("Food", "Nutritional Advice", R.drawable.food, Modifier.weight(1f), Color(0xFFFFF3E0)) {
                    navController.navigate("details/Food")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ModernFeatureCell("Weight", "Management", R.drawable.weight, Modifier.weight(1f), Color(0xFFE3F2FD)) {
                    navController.navigate("details/Weight")
                }
                Spacer(Modifier.width(12.dp))
                ModernFeatureCell("Water", "Hydration", R.drawable.water, Modifier.weight(1f), Color(0xFFE1F5FE)) {
                    navController.navigate("details/Water")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ModernFeatureCell("Heart", "Monitoring", R.drawable.heart, Modifier.weight(1f), Color(0xFFF3E5F5)) {
                    navController.navigate("details/Heart Rate")
                }
                Spacer(Modifier.width(12.dp))
                ModernFeatureCell("Steps", "Daily Walk", R.drawable.walking, Modifier.weight(1f), Color(0xFFF1F8E9)) {
                    navController.navigate("details/Walking")
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- PROFILE DISPLAY SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDisplayScreen(viewModel: FitViewModel, navController: NavHostController) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = Color(0xFFE8F5E9)
            ) {
                Icon(
                    Icons.Default.Person, 
                    contentDescription = null, 
                    modifier = Modifier.padding(24.dp).fillMaxSize(),
                    tint = Color(0xFF43A047)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(uiState.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Text("Member since June 2026", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(40.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileDetailRow("Age", uiState.age, Icons.Default.CalendarToday)
                ProfileDetailRow("Weight", uiState.weight, Icons.Default.MonitorWeight)
                ProfileDetailRow("Gender", uiState.gender, Icons.Default.Face)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate("edit_profile") }, 
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
            ) {
                Text("EDIT PROFILE", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- MORE SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(navController: NavHostController) {
    val items = listOf(
        Pair("Profile", Icons.Default.Person),
        Pair("SDG Info", Icons.Default.Public),
        Pair("Settings", Icons.Default.Settings),
        Pair("Help & Support", Icons.Default.Help)
    )
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("More Options", fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFBFDFA))
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))
            items.forEach { item ->
                ListItem(
                    headlineContent = { Text(item.first, fontWeight = FontWeight.Medium) },
                    leadingContent = { 
                        Surface(color = Color.White, shape = CircleShape, modifier = Modifier.size(40.dp), border = BorderStroke(1.dp, Color(0xFFEEEEEE))) {
                            Icon(item.second, contentDescription = null, modifier = Modifier.padding(10.dp), tint = Color(0xFF43A047)) 
                        }
                    },
                    trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.LightGray) },
                    modifier = Modifier.clickable { 
                        when (item.first) {
                            "Profile" -> navController.navigate("profile_display")
                            "SDG Info" -> navController.navigate("sdg_info")
                        }
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- SDG INFO SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SDGInfoScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About SDG 3", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.verticalGradient(listOf(Color(0xFF4DB6AC), Color(0xFF2E7D32))))
                    .padding(24.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Icon(Icons.Default.Public, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Good Health and\nWell-being",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "Goal 3 aims to ensure healthy lives and promote well-being for all at all ages.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "The world is facing a global health crisis. GoFit contributes by helping individuals stay active, which is a key factor in reducing non-communicable diseases (NCDs).",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SDGFeatureItem("Step Tracking", "Encourages daily movement to maintain cardiovascular health.")
            SDGFeatureItem("Expert Health Tips", "Provides professional advice via REST API for better nutrition and habits.")
            SDGFeatureItem("Community Goals", "Fosters a supportive environment for long-term health commitment.")
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Button(
                onClick = { navController.popBackStack() }, 
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("I UNDERSTAND")
            }
            
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun SDGFeatureItem(title: String, desc: String) {
    Row(modifier = Modifier.padding(vertical = 12.dp)) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF43A047), modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.DarkGray)
            Text(desc, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF8BC34A), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
        }
    }
    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
}

@Composable
fun ModernFeatureCell(title: String, value: String, img: Int, modifier: Modifier, bgColor: Color, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = bgColor,
                    shape = CircleShape,
                    modifier = Modifier.size(32.dp)
                ) {
                    Image(
                        painter = painterResource(img),
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            }
            
            Column {
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.DarkGray
                )
                Text("Tap to view tips", fontSize = 10.sp, color = Color.LightGray)
            }
        }
    }
}
