package com.example.tasktracker.ui.screen


import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.tasktracker.ui.state.TaskState
import com.example.tasktracker.ui.theme.Green
import com.example.tasktracker.ui.theme.Red
import com.example.tasktracker.ui.theme.White
import com.example.tasktracker.ui.theme.expandedGradient
import com.example.tasktracker.ui.theme.gray
import com.example.tasktracker.ui.theme.lightGray
import com.example.tasktracker.ui.theme.primaryBackground
import com.example.tasktracker.ui.theme.whiteButton
import com.example.tasktracker.ui.viewModel.MainViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.abs
import kotlin.math.min
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.draw.alpha
import com.example.tasktracker.ui.isValidField
import java.time.LocalTime
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.with
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

@Preview
@Composable
fun TaskListScreenPreview() {
    TaskScreen(navController = rememberNavController())
}

@OptIn(androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun TaskScreen(navController: NavHostController) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    expandedGradient,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 32.dp,
                        bottomEnd = 32.dp
                    )
                )
//                .padding(WindowInsets.statusBars.asPaddingValues())
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 38.dp,
                    bottom = 20.dp,
                    start = 20.dp,
                    end = 20.dp
                )
            ) {
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

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            navController.navigate("create_task")
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
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                val currentMonth = LocalDate.now().month.getDisplayName(
                    TextStyle.FULL_STANDALONE,
                    Locale("ru")
                ).replaceFirstChar { it.uppercase() }

                if (!isExpanded) {
                    Text(
                        text = currentMonth,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 0.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedContent(
                    targetState = isExpanded,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) + slideInVertically(
                            animationSpec = tween(300),
                            initialOffsetY = { if (targetState) -it else it }
                        ) with fadeOut(animationSpec = tween(300)) + slideOutVertically(
                            animationSpec = tween(300),
                            targetOffsetY = { if (targetState) it else -it }
                        )
                    }
                ) { expanded ->
                    if (expanded) {
                        CalendarExpanded(
                            selectedDate = selectedDate,
                            onDateSelected = { selectedDate = it }
                        )
                    } else {
                        CalendarCollapsed(
                            selectedDate = selectedDate,
                            onDateSelected = { selectedDate = it }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        TaskListScreen(navController = navController, selectedDate = selectedDate)
    }
}

@Composable
fun CalendarCollapsed(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val startOfWeek = selectedDate.with(DayOfWeek.MONDAY)
    val dates = (0..6).map { startOfWeek.plusDays(it.toLong()) }
    val today = LocalDate.now()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        dates.forEachIndexed { index, date ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru")),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            when {
                                date == selectedDate -> Color.White // Полностью белый фон для выбранной даты
                                date == today -> Color.Transparent // Прозрачный фон для сегодняшней даты
                                else -> Color.Transparent
                            },
                            shape = RoundedCornerShape(16.dp),
                        )
                        .then(
                            if (date == today) {
                                Modifier.border(
                                    width = 1.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(16.dp)
                                )
                            } else {
                                Modifier
                            }
                        )
                        .clickable { onDateSelected(date) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = when {
                            date == selectedDate -> Color.DarkGray // Темно-серый цвет для выбранной даты
                            date == today -> Color.White // Белый цвет для сегодняшней даты
                            else -> Color.White
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Добавляем Spacer между элементами для равномерного распределения
            if (index < dates.size - 1) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CalendarExpanded(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var currentMonth by remember { mutableStateOf(selectedDate) }
    val today = LocalDate.now()

    // Синхронизируем currentMonth с selectedDate
    LaunchedEffect(selectedDate) {
        if (selectedDate.month != currentMonth.month || selectedDate.year != currentMonth.year) {
            currentMonth = selectedDate
        }
    }

    val firstDayOfMonth = currentMonth.withDayOfMonth(1)
    val daysInMonth = firstDayOfMonth.lengthOfMonth()

    // Создаем список дней с правильным началом недели (понедельник = 1, воскресенье = 7)
    val days = mutableListOf<LocalDate?>()

    // Находим первый день недели для первого дня месяца
    // Если первый день месяца - понедельник, то firstDayOfWeek = 1
    // Если первый день месяца - воскресенье, то firstDayOfWeek = 7
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value

    // Вычисляем, сколько пустых дней нужно добавить в начале
    // Если первый день месяца - понедельник (1), то добавляем 0 пустых дней
    // Если первый день месяца - вторник (2), то добавляем 1 пустой день
    // Если первый день месяца - воскресенье (7), то добавляем 6 пустых дней
    val emptyDaysAtStart = if (firstDayOfWeek == 1) 0 else firstDayOfWeek - 1

    // Добавляем пустые дни в начале месяца
    repeat(emptyDaysAtStart) {
        days.add(null)
    }

    // Добавляем все дни месяца
    (1..daysInMonth).forEach { day ->
        days.add(firstDayOfMonth.withDayOfMonth(day))
    }

    // Дополняем последнюю неделю пустыми днями, если нужно
    while (days.size % 7 != 0) {
        days.add(null)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with month navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp), // Добавляем отступы по бокам
            horizontalArrangement = Arrangement.SpaceBetween, // Оставляем SpaceBetween для максимального расстояния
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    currentMonth = currentMonth.minusMonths(1)
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Предыдущий месяц",
                    tint = Color.White
                )
            }

            Text(
                text = "${
                    currentMonth.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
                        .replaceFirstChar { it.uppercase() }
                } ${currentMonth.year}",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    currentMonth = currentMonth.plusMonths(1)
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Следующий месяц",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Day of week headers - используем тот же шрифт, что и в collapsed календаре
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
                Text(
                    text = day,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center
                )
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
                    if (date != null) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    when {
                                        date == selectedDate -> Color.White // Полностью белый фон для выбранной даты
                                        date == today -> Color.Transparent // Прозрачный фон для сегодняшней даты
                                        else -> Color.Transparent
                                    }
                                )
                                .then(
                                    if (date == today) {
                                        Modifier.border(
                                            width = 1.dp,
                                            color = Color.White,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                    } else {
                                        Modifier
                                    }
                                )
                                .clickable {
                                    onDateSelected(date)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                color = when {
                                    date == selectedDate -> Color.DarkGray // Темно-серый цвет для выбранной даты
                                    date == today -> Color.White // Белый цвет для сегодняшней даты
                                    else -> Color.White
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TaskListScreen(
    viewModel: MainViewModel = koinViewModel(),
    navController: NavHostController,
    selectedDate: LocalDate = LocalDate.now()
) {
    val state by viewModel.tasks.collectAsState()
    var isPlaned by remember { mutableStateOf(true) }

    val filteredTasks = when (state) {
        is TaskState.Success -> {
            val tasks = (state as TaskState.Success).tasks
            tasks.filter {
                it.isCompleted == !isPlaned &&
                        it.date == selectedDate.toString()
            }
        }

        else -> emptyList()
    }

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
                onClick = { isPlaned = true },
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

        when (state) {
            is TaskState.Loading -> LoadingView()
            is TaskState.Error -> LoadingView() /*ErrorView(message = (state as TaskState.Error).message)*/
            is TaskState.Success -> {
                if (filteredTasks.isEmpty()) {
                    EmptyListView(isPlaned = isPlaned)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = filteredTasks,
                            key = { it.id }
                        ) { task ->
                            SwipeItemTask(
                                task = task,
                                onDone = {
                                    val isCompleted = task.isCompleted
                                    val completed = !isCompleted
                                    viewModel.approve(task.id, completed)
                                    Log.e(TAG, "Подтверждение")
                                },
                                onDelete = {
                                    viewModel.delete(task = task)
                                    Log.e(TAG, "УДАЛЕНИЕ")
//                                    viewModel.processIntent(TaskIntent.DeleteTask(task))
                                },
                                modifier = Modifier.animateItem(),
                                navController,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = Color.Blue,
            strokeWidth = 6.dp
        )
    }
}

@Composable
fun EmptyListView(isPlaned: Boolean /* Дата на которую выставлена задача */) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isPlaned) "Нет запланированных задач"
            else "Нет выполненных задач",
            color = Color.Gray,
        )
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp, color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = task.categoryName ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Blue, // Добавить цвет заднего фона у категории
                    modifier = Modifier

                        .background(Color.Blue, RoundedCornerShape(14.dp))
                        .alpha(if (task.categoryName == null) 0f else 1f)
                        .padding(top = 11.dp, bottom = 13.dp, start = 16.dp, end = 16.dp)

                )

                Row(horizontalArrangement = Arrangement.Center) {

                    if (isValidField(task.startTime)) {
                        val time = try {
                            LocalTime.parse(task.startTime)
                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                        } catch (e: Exception) {
                            task.startTime ?: "00:00"
                        }
                        Text(
                            text = time,
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


                    if (isValidField(task.duration)) {
                        Text(
                            text = task.duration.toString(),
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
    modifier: Modifier,
    navController: NavHostController
) {
    val swipeState = rememberSwipeableState(initialValue = 0)
    val density = LocalDensity.current
    val revealDistancePx = with(density) { 96.dp.toPx() }
    val confirmDistancePx = with(density) { 240.dp.toPx() }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    // Anchors: 0 (closed), +/-1 (reveal), +/-2 (confirm)
    val anchors = mapOf(
        0f to 0,
        -revealDistancePx to -1,
        -confirmDistancePx to -2,
        revealDistancePx to 1,
        confirmDistancePx to 2
    )

    val swipeProgress by animateFloatAsState(
        targetValue = min(1f, abs(swipeState.offset.value) / confirmDistancePx),
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )


    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Left side (Check) when swiping right
        if (swipeState.offset.value > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .pointerInput(Unit) { }
                    .fillMaxHeight()
                    .padding(start = 32.dp)
                    .width(80.dp * swipeProgress)
                    .background(
                        color = Green.copy(alpha = swipeProgress),
                        shape = RoundedCornerShape(
                            16.dp * swipeProgress
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        onDone()
                        scope.launch { swipeState.snapTo(0) }
                    },
                ) {
                    Image(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .size(24.dp * swipeProgress)
                    )
                }
            }
        }

        // Right side (Delete) when swiping left
        if (swipeState.offset.value < 0) {
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
                IconButton(
                    onClick = {
                        onDelete()
                        scope.launch { swipeState.snapTo(0) }
                    },
                    modifier = Modifier.size(48.dp * swipeProgress)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_delete_task),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .size(24.dp * swipeProgress)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .swipeable(
                    state = swipeState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal,
                )
                .offset { IntOffset(swipeState.offset.value.toInt(), 0) }
                .zIndex(1f)
        ) {
            TaskItem(task = task, onClick = {
                navController.navigate("edit_task/${task.id}")
            })
        }

        LaunchedEffect(swipeState.currentValue) {
            when (swipeState.currentValue) {
                2 -> {
                    onDone()
                    swipeState.animateTo(0, tween(250))
                }

                -2 -> {
                    onDelete()
                    swipeState.animateTo(0, tween(250))
                }

                1, -1 -> {
                    // Give user time to tap the icon, then auto-close
                    kotlinx.coroutines.delay(1500)
                    if (swipeState.currentValue == 1 || swipeState.currentValue == -1) {
                        swipeState.animateTo(0, tween(250))
                    }
                }

                else -> {}
            }
        }

    }
}


//@Composable
//fun SwipeItemTask(
//    task: Task,
//    onDone: () -> Unit,
//    onDelete: () -> Unit,
//    modifier: Modifier,
//    navController: NavHostController
//) {
//    val swipeState = rememberSwipeableState(initialValue = 0)
//    val density = LocalDensity.current
//    val scope = androidx.compose.runtime.rememberCoroutineScope()
//
//    androidx.compose.foundation.layout.BoxWithConstraints(
//        modifier = modifier
//            .fillMaxWidth()
//    ) {
//        val widthPx = with(density) { maxWidth.toPx() }
//        val revealDistancePx = widthPx * 0.5f
//        val confirmDistancePx = widthPx * 0.9f
//
//        // Anchors: 0 (closed), +/-1 (reveal at mid), +/-2 (confirm near far edge)
//        val anchors = mapOf(
//            0f to 0,
//            -revealDistancePx to -1,
//            -confirmDistancePx to -2,
//            revealDistancePx to 1,
//            confirmDistancePx to 2
//        )
//
//        // Track when we are programmatically returning to 0 to avoid icon flash
//        val thresholdPx = with(density) { 16.dp.toPx() }
//        var hideIconsOverride by remember { mutableStateOf(false) }
//
//        // Progress for icon reveal should cap at mid (revealDistancePx) and follow finger
//        val revealProgress by animateFloatAsState(
//            targetValue = min(1f, abs(swipeState.offset.value) / revealDistancePx),
//            animationSpec = spring(stiffness = Spring.StiffnessLow)
//        )
//
//        val iconsVisible = remember(swipeState.offset.value, hideIconsOverride) {
//            abs(swipeState.offset.value) > thresholdPx && !hideIconsOverride
//        }
//
//        // Left side (Check) when swiping right
//        if (iconsVisible && swipeState.offset.value > 0) {
//            Box(
//                modifier = Modifier
//                    .align(Alignment.CenterStart)
//                    .fillMaxHeight()
//                    .padding(start = 32.dp)
//                    .width(maxOf(80.dp * revealProgress, 48.dp))
//                    .background(
//                        color = Green.copy(alpha = revealProgress),
//                        shape = RoundedCornerShape(
//                            16.dp * revealProgress
//                        )
//                    )
//                    .zIndex(0f),
//                contentAlignment = Alignment.Center
//            ) {
//                IconButton(
//                    onClick = {
//                        Log.e(TAG, "Check icon clicked!")
//                        onDone()
//                        scope.launch { swipeState.snapTo(0) }
//                    },
//                    modifier = Modifier.size(48.dp)
//                ) {
//                    Image(
//                        imageVector = Icons.Default.Check,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(24.dp)
//                    )
//                }
//            }
//        }
//
//        // Right side (Delete) when swiping left
//        if (iconsVisible && swipeState.offset.value < 0) {
//            Box(
//                modifier = Modifier
//                    .align(Alignment.CenterEnd)
//                    .fillMaxHeight()
//                    .padding(end = 32.dp)
//                    .width(maxOf(80.dp * revealProgress, 48.dp))
//                    .background(
//                        color = Red.copy(alpha = revealProgress),
//                        shape = RoundedCornerShape(
//                            16.dp * revealProgress
//                        )
//                    )
//                    .zIndex(0f),
//                contentAlignment = Alignment.Center
//            ) {
//                IconButton(
//                    onClick = {
//                        Log.e(TAG, "Delete icon clicked!")
//                        onDelete()
//                        scope.launch { swipeState.snapTo(0) }
//                    },
//                    modifier = Modifier.size(48.dp)
//                ) {
//                    Image(
//                        painter = painterResource(R.drawable.ic_delete_task),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(24.dp)
//                    )
//                }
//            }
//        }
//
//        Box(
//            modifier = Modifier
//                .swipeable(
//                    state = swipeState,
//                    anchors = anchors,
//                    thresholds = { from, to ->
//                        when {
//                            // easy snap between closed and reveal
//                            (from == 0 && (to == 1 || to == -1)) || ((from == 1 || from == -1) && to == 0) -> FractionalThreshold(0.3f)
//                            // require deeper drag to confirm from reveal
//                            (from == 1 && to == 2) || (from == -1 && to == -2) -> FractionalThreshold(0.7f)
//                            else -> FractionalThreshold(0.5f)
//                        }
//                    },
//                    orientation = Orientation.Horizontal,
//                )
//                .offset { IntOffset(swipeState.offset.value.toInt(), 0) }
//                .zIndex(1f)
//        ) {
//            TaskItem(task = task, onClick = { navController.navigate("edit_task") })
//        }
//
//        // Behavior handling for reveal and confirm states
//        LaunchedEffect(swipeState.currentValue) {
//            when (swipeState.currentValue) {
//                2 -> {
//                    onDone()
//                    hideIconsOverride = true
//                    try {
//                        swipeState.animateTo(0, tween(250))
//                    } finally {
//                        hideIconsOverride = false
//                    }
//                }
//                -2 -> {
//                    onDelete()
//                    hideIconsOverride = true
//                    try {
//                        swipeState.animateTo(0, tween(250))
//                    } finally {
//                        hideIconsOverride = false
//                    }
//                }
//                1, -1 -> {
//                    // Give user time to tap the icon, then auto-close
//                    kotlinx.coroutines.delay(1500)
//                    if (swipeState.currentValue == 1 || swipeState.currentValue == -1) {
//                        hideIconsOverride = true
//                        try {
//                            swipeState.animateTo(0, tween(250))
//                        } finally {
//                            hideIconsOverride = false
//                        }
//                    }
//                }
//                else -> {}
//            }
//        }
//    }
//}