package com.example.tasktracker.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.tasktracker.ui.theme.TaskTrackerTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge() Убираеть верхнию и нижнею панель
        setContent {
            TaskTrackerTheme {


//                WindowCompat.setDecorFitsSystemWindows(window, false)
//                window.navigationBarColor = Color.White.toArgb()
                MainScreen()
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "task_list") {
        composable("task_list") { TaskScreen(navController) }
        composable("create_task") { CreateTaskScreen(navController) }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    AppNavHost(navController)
}