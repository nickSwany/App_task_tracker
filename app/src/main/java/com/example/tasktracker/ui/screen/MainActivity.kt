package com.example.tasktracker.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.example.tasktracker.ui.theme.TaskTrackerTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Делаем статус-бар прозрачным, как в Google Погоде
        window.statusBarColor = Color.Transparent.toArgb()

        // Дополнительные флаги для полной прозрачности
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {
            TaskTrackerTheme {
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
        composable("edit_task/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toLongOrNull() ?: 0L
            EditTaskScreen(navController, taskId = taskId)
        } // Передача taskId для запроса
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    AppNavHost(navController)
}