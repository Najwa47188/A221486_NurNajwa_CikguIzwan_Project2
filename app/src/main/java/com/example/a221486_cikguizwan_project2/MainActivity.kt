package com.example.a221486_cikguizwan_project2

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlin.math.sqrt

class MainActivity : ComponentActivity(), SensorEventListener {

    // 1. Sediakan pembolehubah untuk Sensor Android
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    // Nilai ambang (threshold) untuk kesan goncangan/langkah kaki
    private val movementThreshold = 12f

    // Ambil ViewModel menggunakan delegasi 'viewModels()' supaya boleh dikongsi dengan sensor
    private val fitViewModel: FitViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 2. Inisialisasi SensorManager dan Accelerometer
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            MaterialTheme {
                // Pasangkan fitViewModel yang sama ke dalam MainApp
                MainApp(viewModel = fitViewModel)
            }
        }
    }

    // 3. Hidupkan sensor apabila aplikasi dibuka (Foreground)
    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    // 4. Matikan sensor apabila aplikasi ditutup/latar belakang (Save bateri)
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // 5. Logik mengira langkah apabila sensor mengesan pergerakan fizikal peranti
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Formula Magnitude untuk kira kekuatan pergerakan 3D (Paksi X, Y, Z)
            val gForce = sqrt(x * x + y * y + z * z)

            // Jika peranti digoncang melebihi had ambang, tambah langkah dalam ViewModel!
            if (gForce > movementThreshold) {
                fitViewModel.incrementSteps()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Tidak perlu diusik untuk fungsi asas
    }
}

@Composable
fun MainApp(viewModel: FitViewModel) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "me",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("me") { MeScreen(viewModel, navController, snackbarHostState) }
            composable("together") { TogetherScreen(viewModel, navController) }
            composable("summary") { SummaryScreen(viewModel, navController) }
            composable("profile_display") { ProfileDisplayScreen(viewModel, navController) }
            composable("edit_profile") { EditProfileScreen(viewModel, navController) }
            composable("more") { MoreScreen(navController) }
            composable("sdg_info") { SDGInfoScreen(navController) }
            composable("activities") { ActivitiesScreen(viewModel, navController) }
            composable("community_map") { CommunityMapScreen(navController) }
            composable("details/{activityName}") { backStackEntry ->
                val activityName = backStackEntry.arguments?.getString("activityName")
                HealthDetailScreen(activityName, navController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem("Me", "me", Icons.Default.Home),
        NavigationItem("Together", "together", Icons.Default.People),
        NavigationItem("Summary", "summary", Icons.AutoMirrored.Filled.List),
        NavigationItem("More", "more", Icons.Default.MoreVert)
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class NavigationItem(
    val title: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)