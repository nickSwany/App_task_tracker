@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasktracker.ui.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tasktracker.R
import com.example.tasktracker.domain.api.repository.CreateTaskRepository
import com.example.tasktracker.domain.model.Task
import com.example.tasktracker.ui.theme.Background
import com.example.tasktracker.ui.theme.Black
import com.example.tasktracker.ui.theme.BlueText
import com.example.tasktracker.ui.theme.GraySwitch
import com.example.tasktracker.ui.theme.White
import com.example.tasktracker.ui.theme.darkGray
import com.example.tasktracker.ui.theme.expandedGradient
import com.example.tasktracker.ui.theme.lightGray
import com.example.tasktracker.ui.theme.whiteButton
import com.example.tasktracker.ui.viewModel.CreateTaskViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.abs

internal const val countOfVisibleItemsInPicker = 5
internal const val itemHeight = 35f
internal const val listHeight = countOfVisibleItemsInPicker * itemHeight

@Composable
fun CreateTaskScreen(
    navController: NavHostController, viewModel: CreateTaskViewModel = koinViewModel()
) {

//    var canCreateTask by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
//    var taskTitle by remember { mutableStateOf("") }
//    var taskDiscription by remember { mutableStateOf("") }
    val brush = remember {
        Brush.linearGradient(
            colors = listOf(Color(0xFF6A88F7), Color(0xFF9C27B0))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)

    ) {
        ToolBar(navController, onDoneClick = {
            viewModel.saveTask()
            navController.popBackStack()
        })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = viewModel.taskTitle,
                onValueChange = { viewModel.onTitleChange(it) },
                textStyle = TextStyle(brush = brush),
                label = { Text("ЗАГОЛОВОК") },
                maxLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                )
            )


            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = viewModel.taskDescription,
                onValueChange = { viewModel.onDescriptionChange(it) },
                textStyle = TextStyle(brush = brush),
                label = { Text("Описание") },
                maxLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            ChooseDate()

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(lightGray)
            ) { }

            Spacer(modifier = Modifier.height(16.dp))

            ChooseTime(onTimeSelectedStart = {
                viewModel.onStartTimeChange(it)
            }, onDurationCalculated = {
                viewModel.onDurationChange(it)
            })

            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}

@Composable
fun ToolBar(navController: NavHostController, onDoneClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                expandedGradient, shape = RoundedCornerShape(
                    topStart = 0.dp, topEnd = 0.dp, bottomStart = 32.dp, bottomEnd = 32.dp
                )
            )
            .padding(top = 10.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { navController.popBackStack() }, modifier = Modifier
                .size(48.dp)
                .background(
                    color = whiteButton, shape = RoundedCornerShape(16.dp)
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            "Создание",
            color = White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        IconButton(
            onClick = { onDoneClick() }, modifier = Modifier
                .size(48.dp)
                .background(
                    color = whiteButton, shape = RoundedCornerShape(16.dp)
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_done),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ChooseDate() { // возможно нужно сделать возврат данных

    var switchState by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var isExpanded by remember { mutableStateOf(false) }
//    var isExpandedMonth by remember { mutableStateOf(false) }
//    var expanded by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()


    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isExpanded = !isExpanded
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(lightGray, shape = RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = "Calendar",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
            ) {

                Text(
                    "Дата", color = Black, fontSize = 16.sp, fontWeight = FontWeight.Light
                )

                Text(
                    selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))),
                    color = BlueText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )

            }

            Switch(
                checked = switchState,
                onCheckedChange = { switchState = !switchState },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = BlueText,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = GraySwitch,
                    uncheckedBorderColor = Color.Transparent, // Убираем контур в `false`
                )
            )
        }

        if (isExpanded) {
            DatePickerDialog(onDismissRequest = { isExpanded = false }, confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        selectedDate = Instant.ofEpochMilli(selectedMillis)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    isExpanded = false
                }) { Text("OK") }
            }, dismissButton = {
                TextButton(onClick = { isExpanded = false }) { Text("Отмена") }
            }) {
                MaterialTheme(
                    colorScheme = darkColorScheme(
                        primary = Color.White,
                        onPrimary = Color.Transparent,
                        background = lightGray,
                        surface = lightGray,
                        onSurface = White // Проверить при выборе даты цвет
                    )
                ) {
                    DatePicker(state = datePickerState)

                }
            }
        }
    }
}

@Composable
fun ChooseTime(
    onTimeSelectedStart: (LocalTime) -> Unit, onDurationCalculated: (String) -> Unit
) {
    var switchStateStart by remember { mutableStateOf(false) }
    var switchStateEnd by remember { mutableStateOf(false) }
    var isExpandedTimeStart by remember { mutableStateOf(false) }
    var isExpandedTimeEnd by remember { mutableStateOf(false) }
    var selectedTimeStart by remember { mutableStateOf(LocalTime.now()) }
    var selectedTimeEnd by remember { mutableStateOf(LocalTime.now().plusHours(1)) }

//    var timeStart = selectedTimeStart.format(DateTimeFormatter.ofPattern("HH:mm", Locale("ru")))

    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    ) {

        TimeSelectionRow(icon = R.drawable.ic_time,
            label = stringResource(R.string.start),
            time = selectedTimeStart.format(DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))),
            switchState = switchStateStart,
            onSwitchChange = {
                switchStateStart = it
                if (switchStateStart) onTimeSelectedStart(selectedTimeStart)
            },
            onClick = {
                isExpandedTimeStart = !isExpandedTimeStart
            })

        if (isExpandedTimeStart) {
            Spacer(modifier = Modifier.height(16.dp))
            CustomTimePicker { hour, minute ->
                selectedTimeStart = LocalTime.of(hour, minute)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TimeSelectionRow(icon = R.drawable.ic_time,
            label = stringResource(R.string.end),
            time = selectedTimeEnd.format(DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))),
            switchState = switchStateEnd,
            onSwitchChange = {
                switchStateEnd = it
                if (switchStateEnd) {
                    val durationMinutes =
                        ChronoUnit.MINUTES.between(selectedTimeStart, selectedTimeEnd)
                    val hours = durationMinutes / 60
                    val minutes = durationMinutes % 60

                    val durationText = when {
                        hours > 0 && minutes > 0 -> "$hours ч $minutes мин"
                        hours > 0 -> "$hours ч"
                        else -> "$minutes мин"
                    }

                    onDurationCalculated(durationText)
                }
            },
            onClick = {
                isExpandedTimeEnd = !isExpandedTimeEnd
            })

        if (isExpandedTimeEnd) {
            Spacer(modifier = Modifier.height(16.dp))
            CustomTimePicker { hour, minute ->
                selectedTimeEnd = LocalTime.of(hour, minute)
            }
        }
    }
}


@Composable
fun TimeSelectionRow(
    icon: Int,
    label: String,
    time: String,
    switchState: Boolean,
    onSwitchChange: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(lightGray, shape = RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
        ) {
            Text(
                label, color = Black, fontSize = 16.sp, fontWeight = FontWeight.Light
            )

            Text(
                time, color = BlueText, fontSize = 16.sp, fontWeight = FontWeight.Light
            )
        }

        Switch(
            checked = switchState, onCheckedChange = onSwitchChange, colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BlueText,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = GraySwitch,
                uncheckedBorderColor = Color.Transparent,
            )
        )
    }
}

@Preview
@Composable
fun CreateTaskScreenPreview() {
    val fakeViewModel = object : CreateTaskViewModel(object : CreateTaskRepository {
        override suspend fun createTask(task: Task) {
            // Пустая заглушка, чтобы не падало
        }
    }) {}

    CreateTaskScreen(navController = rememberNavController(), viewModel = fakeViewModel)
}


@Composable
internal fun TimeColumnPicker(
    initialValue: Int,
    onValueChange: (Int) -> Unit,
    range: IntProgression,
    modifier: Modifier = Modifier,
    isHoursColumn: Boolean
) {
    val context = LocalContext.current
    val listState =
        rememberLazyListState(initialFirstVisibleItemIndex = initialValue / (if (isHoursColumn) 1 else 5))

    val list by remember {
        mutableStateOf(mutableListOf<String>().apply {
            (1..(countOfVisibleItemsInPicker / 2)).forEach { _ -> add("") }
            for (i in range) add(i.getTimeDefaultStr())
            (1..(countOfVisibleItemsInPicker / 2)).forEach { _ -> add("") }
        })
    }

    var selectedValue by remember { mutableIntStateOf(initialValue) }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && listState.firstVisibleItemScrollOffset.pixelsToDp(
                context
            ) % itemHeight != 0f
        ) {
            listState.animateScrollToItem(listState.itemForScrollTo(context))
        }
    }

    LaunchedEffect(listState.firstVisibleItemScrollOffset) {
        val newValue =
            list[listState.itemForScrollTo(context) + countOfVisibleItemsInPicker / 2].toIntOrNull()
        if (newValue != null && newValue != selectedValue) {
            onValueChange(newValue)
            selectedValue =
                newValue // тут возможно будет ошибка, если выставлять одно и тоже время на часы, но другие минуты
        }
    }

    Box(
        modifier = modifier
            .height(listHeight.dp)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Border(itemHeight = itemHeight.dp, color = Black) //можно кастомить

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = if (isHoursColumn) Alignment.End else Alignment.Start
        ) {
            itemsIndexed(
                items = list
            ) { index, it ->
                Box(
                    modifier = Modifier
                        .fillParentMaxHeight(1f / countOfVisibleItemsInPicker)
                        .graphicsLayer {
                            scaleX = calculateScaleX(listState, index)
                            scaleY = calculateScaleY(listState, index)
                            alpha = calculateAlpha(index, listState)
                        }, contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it,
                        fontSize = 19.sp,
                        modifier = Modifier.wrapContentSize(),
                        textAlign = if (isHoursColumn) TextAlign.End else TextAlign.Start
                    )
                }
            }
        }

    }
}

private fun Int.getTimeDefaultStr(): String {
    return "${if (this <= 9) "0" else ""}$this"
}

private fun Int.pixelsToDp(context: Context): Float =
    this / (context.resources.displayMetrics.densityDpi / 160f)

internal fun LazyListState.itemForScrollTo(context: Context): Int {
    val offset = firstVisibleItemScrollOffset.pixelsToDp(context)
    return when {
        offset == 0f -> firstVisibleItemIndex
        offset % itemHeight >= itemHeight / 2 -> firstVisibleItemIndex + 1
        else -> firstVisibleItemIndex
    }
}

private fun calculateScaleX(listState: LazyListState, index: Int): Float {
    val layoutInfo = listState.layoutInfo
    val visibleItem = layoutInfo.visibleItemsInfo.map { it.index }
    if (!visibleItem.contains(index)) return 1f

    val itemInfo = layoutInfo.visibleItemsInfo.first { it.index == index }
    val center = (layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset) / 2f
    val distance = abs((itemInfo.offset + itemInfo.size / 2) - center)
    val maxDistance = layoutInfo.viewportEndOffset / 2f

    return 1f - (distance / maxDistance) * 0.5f
}

private fun calculateScaleY(listState: LazyListState, index: Int): Float {
    val layoutInfo = listState.layoutInfo
    val visibleItem = layoutInfo.visibleItemsInfo.map { it.index }
    if (!visibleItem.contains(index)) return 1f

    val itemInfo = layoutInfo.visibleItemsInfo.first { it.index == index }
    val center = (layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset) / 2f
    val distance = abs((itemInfo.offset + itemInfo.size / 2) - center)
    val maxDistance = layoutInfo.viewportEndOffset / 2f

    return 1f - (distance / maxDistance)
}

private fun calculateAlpha(index: Int, listState: LazyListState): Float {
    val layoutInfo = listState.layoutInfo
    val visibleItem = layoutInfo.visibleItemsInfo.map { it.index }
    if (visibleItem.isEmpty()) return 1f

    val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index } ?: return 1f
    val center = (layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset) / 2f
    val distance = abs((itemInfo.offset + itemInfo.size / 2) - center)
    val maxDistance = layoutInfo.viewportEndOffset / 2f

    return 1f - (distance / maxDistance) * 0.7f
}


@Composable
internal fun Border(itemHeight: Dp, color: Color) {
    val width = 2.dp
    val strokeWidthPx = with(LocalDensity.current) { width.toPx() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight)
            .background(darkGray, shape = RoundedCornerShape(10.dp))
//            .drawBehind {
//                drawLine(
//                    color = color,
//                    strokeWidth = strokeWidthPx,
//                    start = Offset(0f, 0f),
//                    end = Offset(
//                        size.width, 0f
//                    )
//                )
//
//                drawLine(
//                    color = color,
//                    strokeWidth = strokeWidthPx,
//                    start = Offset(0f, size.height),
//                    end = Offset(
//                        size.width, size.height
//                    )
//                )
//            }
    ) { }
}

@Composable
fun CustomTimePicker(
    initialHour: Int = LocalTime.now().hour,
    initialMinute: Int = LocalTime.now().minute,
    onTimeSelected: (Int, Int) -> Unit
) {
    var selectedHour by remember { mutableIntStateOf(initialHour) }
    var selectedMinute by remember { mutableIntStateOf(initialMinute) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = lightGray, shape = RoundedCornerShape(24.dp)),
        horizontalArrangement = Arrangement.Center
    ) {
        TimeColumnPicker(
            initialValue = selectedHour,
            onValueChange = { selectedHour = it },
            range = 0..23,
            modifier = Modifier.weight(1f),
            true
        )

        Text(
            ":",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .background(darkGray, shape = RoundedCornerShape(10.dp))
        )

        TimeColumnPicker(
            initialValue = selectedMinute,
            onValueChange = { selectedMinute = it },
            range = (0..55 step 5),
            modifier = Modifier.weight(1f),
            false
        )
    }

    LaunchedEffect(selectedHour, selectedMinute) {
        onTimeSelected(selectedHour, selectedMinute)
    }
}