package com.example.tasktracker.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.tasktracker.R
import com.example.tasktracker.domain.model.Task
import com.example.tasktracker.ui.theme.Green
import com.example.tasktracker.ui.theme.Red
import com.example.tasktracker.ui.theme.White
import com.example.tasktracker.ui.theme.expandedGradient
import com.example.tasktracker.ui.theme.gray
import com.example.tasktracker.ui.theme.lightGray
import com.example.tasktracker.ui.theme.primaryBackground
import com.example.tasktracker.ui.theme.whiteButton
import com.example.tasktracker.ui.viewModel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.abs
import kotlin.math.min


@Composable
fun TaskScreen(navController: NavHostController) {
    var isExpanded by remember { mutableStateOf(false) }
    var showDropdown by remember { mutableStateOf(false) }
    val months = listOf(
        "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    )
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedMonth by remember { mutableStateOf("Февраль") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackground)
//            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    expandedGradient, shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 32.dp,
                        bottomEnd = 32.dp
                    )
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = whiteButton,
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Image(
                            painter = if (isExpanded) {
                                painterResource(id = R.drawable.ic_open_calendar)
                            } else painterResource(id = R.drawable.ic_calendar_main),
                            contentDescription = stringResource(R.string.calendar),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    IconButton(
                        onClick = {
                            navController.navigate("create_task")
                            /*тут логика нажатия*/
                        },
                        modifier = Modifier
                            .background(
                                whiteButton,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .size(48.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_plus),
                            contentDescription = stringResource(R.string.add),
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showDropdown = true }
                ) {
                    Text(
                        text = selectedMonth,
                        color = White,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Развернуть/Свернуть",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isExpanded) {
                    CalendarExpanded(selectedDate = selectedDate)
                } else {
                    CalendarCollapsed(
                        selectedDate = selectedDate,
                        onDateSelected = { /* прописать что будет происходить*/ })
                }
                Spacer(modifier = Modifier.height(8.dp))


            }
        }

        TaskListScreen()

    }
}

@Composable
fun CalendarCollapsed(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {

    val startOfWeek = selectedDate.with(DayOfWeek.MONDAY)
    val dates = (0..6).map { startOfWeek.plusDays(it.toLong()) }


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dates.forEach { date ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru")),
                    color = Color.White,
                    fontSize = 12.sp
                    //добавить шрифт
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            if (date == selectedDate) Color.White else Color.Transparent,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .clickable { onDateSelected(date) },
                    contentAlignment = Alignment.Center,

                    ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = if (date == selectedDate) Color.Black else Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

}


@Composable
fun CalendarExpanded(selectedDate: LocalDate) {
    val firstDayOfMonth = selectedDate.withDayOfMonth(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val daysInMonth = firstDayOfMonth.lengthOfMonth()

    val days = (1..daysInMonth).map { firstDayOfMonth.withDayOfMonth(it) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach {
                Text(it, color = Color.White, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val weeks = days.chunked(7)
        weeks.forEach { week ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                week.forEach { date ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (date == selectedDate) Color.White else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            date.dayOfMonth.toString(),
                            color = if (date == selectedDate) Color.Black else Color.White
                        )
                    }
                }

            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskListScreen(viewModel: MainViewModel = koinViewModel()) {
    val tasks by viewModel.tasks.collectAsState()
    var isPlaned by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 20.dp)
        ) {
            Button(
                onClick = {
                    isPlaned = true
                    /* TODO */
                },
                colors = if (isPlaned) ButtonDefaults.buttonColors(gray)
                else ButtonDefaults.buttonColors(
                    Color.Transparent
                )
            ) {
                Text("Запланировано", color = Color.White)
            }

            Spacer(modifier = Modifier.width(2.dp))

            Button(
                onClick = {
                    isPlaned = false
                    /* TODO */
                },
                colors = if (!isPlaned) ButtonDefaults.buttonColors(gray)
                else ButtonDefaults.buttonColors(
                    Color.Transparent
                )
            ) {
                Text("Выполнено", color = Color.White)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = tasks,
                key = { it.id }
                /*tasks.filter { it.isCompleted == !isPlaned }*/) { task ->
//                TaskItem(task)
                SwipeItemTask(
                    task = task,
                    onDone = {},
                    onDelete = {},
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
//        elevation = CardDefaults.cardElevation(4.dp) Возможно не нужны тени
    ) {
        Column(
            modifier = Modifier
                .background(White)
                .padding(horizontal = 24.dp, vertical = 20.dp)

        ) {
            task.title?.let {
                Text(
                    it,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp, color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            task.description?.let {
                Text(
                    it,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp, color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (task.categoryName != null) {
                    Text(
                        task.categoryName, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                        color = Color.Blue, // Добавить цвет заднего фона у категории
                        modifier = Modifier
                            .background(Color.Blue, RoundedCornerShape(14.dp))
                            .padding(top = 11.dp, bottom = 13.dp, start = 16.dp, end = 16.dp)
                    )
                } else {
                }
                Row(horizontalArrangement = Arrangement.Center) {
                    task.startTime?.let {
                        Text(
                            it,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .background(
                                    lightGray, shape = RoundedCornerShape(14.dp)
                                )
                                .padding(
                                    top = 11.dp,
                                    bottom = 13.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )

                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    if (task.duration != null) {
                        Text(
                            task.duration,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .background(lightGray, RoundedCornerShape(16.dp))
                                .padding(
                                    top = 11.dp,
                                    bottom = 13.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )

                        )
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeItemTask(
    task: Task,
    onDone: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier
) {
    val swipeState = rememberSwipeableState(initialValue = 0)
    val iconOffset = with(LocalDensity.current) { 32.dp.toPx() }
    val swipeThreshold = with(LocalDensity.current) { 120.dp.toPx() }
    val coroutineScope = rememberCoroutineScope()

    val anchors = mapOf(
        0f to 0,
        -iconOffset to -2,
        -(iconOffset + swipeThreshold) to -1,
        iconOffset to 2,
        (iconOffset + swipeThreshold) to 1
    )

    val swipeProgress by animateFloatAsState(
        targetValue = min(1f, abs(swipeState.offset.value) / (iconOffset + swipeThreshold)),
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
//                .padding(start = 32.dp)
                .width(80.dp /* * swipeProgress*/)
                .background(
                    color = Green.copy(alpha = swipeProgress),
                    shape = RoundedCornerShape(
                        16.dp * swipeProgress
                    )
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = White.copy(alpha = if (swipeState.offset.value > iconOffset) 1f else 0f),
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(24.dp /* * swipeProgress*/)
                    .clickable {
                        if (swipeState.offset.value > iconOffset) {
                            coroutineScope.launch {
                                onDelete()
                                swipeState.animateTo(0)
                            }
                        }
                    }
            )
        }

        // Только правая часть (Delete) при свайпе влево
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(end = 32.dp)
                .width(80.dp * swipeProgress)
                .background(
                    color = Red.copy(alpha = swipeProgress),
                    shape = RoundedCornerShape(
                        16.dp * swipeProgress
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_delete_task),
                contentDescription = null,
                tint = White.copy(alpha = if (swipeState.offset.value < -iconOffset) 1f else 0f),
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(24.dp /* * swipeProgress*/)
            )
        }
    }

    Box(
        modifier = Modifier
            .swipeable(
                state = swipeState,
                anchors = anchors,
                thresholds = { from, to ->
                    if (to == 0) FractionalThreshold(0.5f)
                    else FractionalThreshold(iconOffset)
                },
                orientation = Orientation.Horizontal
            )
            .offset { IntOffset(swipeState.offset.value.toInt(), 0) }
            .zIndex(1f)
    ) {
        TaskItem(task = task)
    }

    LaunchedEffect(swipeState.currentValue) {
        when (swipeState.currentValue) {
            -1 -> {
                if (abs(swipeState.offset.value) >= swipeThreshold) {
                    onDelete()
                } else {
                    return@LaunchedEffect
                }
                delay(300)
            }

            1 -> {
                if (swipeState.offset.value >= swipeThreshold) {
                    onDone()
                } else {
                    return@LaunchedEffect
                }
                delay(300)
            }
        }

        swipeState.animateTo(0, tween(300))

    }

    LaunchedEffect(swipeState.offset.value) {
        if (swipeState.isAnimationRunning) return@LaunchedEffect
        when {
            swipeState.offset.value > iconOffset && !swipeState.isAnimationRunning -> {
                swipeState.animateTo(iconOffset.toInt(), tween(300))
            }

            swipeState.offset.value < -iconOffset && !swipeState.isAnimationRunning -> {
                swipeState.animateTo(-iconOffset.toInt(), tween(300))
            }
        }
    }

}


@Preview
@Composable
fun TaskListScreenPreview() {
    TaskScreen(navController = rememberNavController())
}


//if (swipeState.offset.value > 0) {
//    Box(
//        modifier = Modifier
//            .align(Alignment.CenterStart)
//            .fillMaxHeight()
//            .padding(start = 32.dp)
//            .width(80.dp * swipeProgress)
//            .background(
//                color = Green.copy(alpha = swipeProgress),
//                shape = RoundedCornerShape(
//                    16.dp * swipeProgress
//                )
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = Icons.Default.Check,
//            contentDescription = null,
//            tint = White,
//            modifier = Modifier
//                .padding(vertical = 12.dp)
//                .size(24.dp * swipeProgress)
//        )
//    }
//}
//
//// Только правая часть (Delete) при свайпе влево
//if (swipeState.offset.value < 0) {
//    Box(
//        modifier = Modifier
//            .align(Alignment.CenterEnd)
//            .fillMaxHeight()
//            .padding(end = 32.dp)
//            .width(80.dp * swipeProgress)
//            .background(
//                color = Red.copy(alpha = swipeProgress),
//                shape = RoundedCornerShape(
//                    16.dp * swipeProgress
//                )
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            painter = painterResource(R.drawable.ic_delete_task),
//            contentDescription = null,
//            tint = White,
//            modifier = Modifier
//                .padding(12.dp)
//                .size(24.dp * swipeProgress)
//        )
//    }
//}
//
//Box(
//modifier = Modifier
//.swipeable(
//state = swipeState,
//anchors = anchors,
//thresholds = { _, _ -> FractionalThreshold(0.5f) },
//orientation = Orientation.Horizontal
//)
//.offset { IntOffset(swipeState.offset.value.toInt(), 0) }
//.zIndex(1f)
//) {
//    TaskItem(task = task)
//}
//
//LaunchedEffect(swipeState.currentValue) {
//    when (swipeState.currentValue) {
//        -1 -> {
//            onDone()
//            delay(300)
//        }
//
//        1 -> {
//            onDelete()
//            delay(300)
//        }
//    }
//
//    swipeState.animateTo(0, tween(300))
//
//}
//
//}