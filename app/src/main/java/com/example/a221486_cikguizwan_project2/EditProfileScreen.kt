package com.example.a221486_cikguizwan_project2

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(viewModel: FitViewModel, navController: NavHostController) {
    // 1. Get existing data from ViewModel as initial values
    val uiState by viewModel.uiState.collectAsState()

    // 2. State variables to store text typed by the user
    var inputName by remember { mutableStateOf(uiState.name) }
    var inputWeight by remember { mutableStateOf(uiState.weight) }
    var inputAge by remember { mutableStateOf(uiState.age) }
    var inputGender by remember { mutableStateOf(uiState.gender) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
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
                color = Color(0xFF8BC34A).copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Keep your profile updated to help us track your SDG 3 progress more accurately.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 13.sp,
                    color = Color(0xFF2E7D32),
                    lineHeight = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            // ---- NAME INPUT ----
            OutlinedTextField(
                value = inputName,
                onValueChange = { inputName = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // ---- WEIGHT INPUT ----
            OutlinedTextField(
                value = inputWeight,
                onValueChange = { inputWeight = it },
                label = { Text("Weight (e.g. 65 kg)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.MonitorWeight, contentDescription = null) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // ---- AGE INPUT ----
            OutlinedTextField(
                value = inputAge,
                onValueChange = { inputAge = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // ---- GENDER INPUT ----
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Gender", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = inputGender == "Male", onClick = { inputGender = "Male" })
                        Text("Male", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(24.dp))
                        RadioButton(selected = inputGender == "Female", onClick = { inputGender = "Female" })
                        Text("Female", fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ---- SAVE BUTTON ----
            Button(
                onClick = {
                    viewModel.updateProfile(inputName, inputWeight, inputAge, inputGender)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
            ) {
                Text("SAVE CHANGES", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}