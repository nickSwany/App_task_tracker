package com.example.tasktracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tasktracker.R
import com.example.tasktracker.domain.model.Task
import com.example.tasktracker.ui.ToolBar
import com.example.tasktracker.ui.isValidField
import com.example.tasktracker.ui.theme.Background
import com.example.tasktracker.ui.theme.Red
import com.example.tasktracker.ui.theme.lightGray
import com.example.tasktracker.ui.viewModel.EditTaskViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun EditTaskScreen(
    navController: NavHostController,
    taskId: Long,
    viewModel: EditTaskViewModel = koinViewModel()
) {

    val task by viewModel.task.collectAsState()
    val data = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        ToolBar(
            navController, onDoneClick = {
                navController.popBackStack()
            }, true, task?.date.toString(), 0
        )

        Spacer(modifier = Modifier.height(32.dp))

        EditTaskInfo(task = task)

    }
}

@Composable
fun EditTaskInfo(task: Task?) {

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // Текста ниже объеденить в одлну функцию
            Text(
                text = task?.categoryName ?: "Без категории",
                fontSize = 12.sp,
                modifier = Modifier
                    .background(color = Red, shape = RoundedCornerShape(12.dp))
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    )
                    .alpha(if (task?.categoryName == null) 0f else 1f)
            )

            Spacer(modifier = Modifier.width(4.dp))

            if (isValidField(task?.startTime)) {
                val startTime = try {
                    LocalTime.parse(task?.startTime).format(DateTimeFormatter.ofPattern("HH:mm"))
                } catch (e: Exception) {
                    task?.startTime ?: "00:00"
                }
                Text(
                    text = startTime,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(color = lightGray, shape = RoundedCornerShape(12.dp))
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            if (isValidField(task?.duration)) {
                Text(
                    text = task?.duration.toString(),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(color = lightGray, shape = RoundedCornerShape(12.dp))
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                )
            }

        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = task?.title.toString(),
            fontSize = 32.sp,
            minLines = 1,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = task?.description.toString(),
            fontSize = 16.sp,
            minLines = 1,
            modifier = Modifier.fillMaxWidth()
        )

    }
}