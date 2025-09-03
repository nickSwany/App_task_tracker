@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasktracker.ui.screen

import android.content.Context
import android.graphics.drawable.Icon
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tasktracker.R
import com.example.tasktracker.ui.ToolBar
import com.example.tasktracker.ui.theme.Background
import com.example.tasktracker.ui.theme.Black
import com.example.tasktracker.ui.theme.BlueText
import com.example.tasktracker.ui.theme.GraySwitch
import com.example.tasktracker.ui.theme.Red
import com.example.tasktracker.ui.theme.White
import com.example.tasktracker.ui.theme.darkGray
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

    val scrollState = rememberScrollState()
    val canSave by remember(viewModel.taskTitle) { derivedStateOf { viewModel.taskTitle.isNotBlank() } }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background)
    ) {
        ToolBar(navController, onDoneClick = {
            viewModel.saveTask()
            navController.popBackStack()
        }, canSave, "Создание", 1)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {


//            Spacer(modifier = Modifier.height(24.dp))

//            Text(text = "Категория")
//            IconButton(
//                onClick = {
//                    showBottomSheet = true
//                },
//                modifier = Modifier
//                    .background(
//                        Black,
//                        shape = RoundedCornerShape(16.dp)
//                    )
//                    .size(48.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_down),
//                    contentDescription = stringResource(R.string.add),
//                    modifier = Modifier.size(24.dp)
//                )
//            } // Исправить на правельную категорию как в дизайне

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false // Закрываем BottomSheet
                    },
                    sheetState = sheetState,
                    scrimColor = Black.copy(alpha = 0.6f),
                    containerColor = Background
                ) {
                    SheetContent()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = viewModel.taskTitle,
                onValueChange = { viewModel.onTitleChange(it) },
                textStyle = TextStyle(color = Black),
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
                textStyle = TextStyle(color = Black),
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

            ChooseDate(onDateSelectedStart = { viewModel.onSaveDate(it) })

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseDate(onDateSelectedStart: (LocalDate) -> Unit) {

    var switchState by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var isExpanded by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dateFormatter = remember { DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru")) }


    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
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
                    contentDescription = null,
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
                    selectedDate.format(dateFormatter),
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
                    uncheckedBorderColor = Color.Transparent,
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
                        if (switchState) onDateSelectedStart(selectedDate)
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
                        onSurface = White
                    )
                ) {
                    DatePicker(state = datePickerState)

                }
            }
        }

        LaunchedEffect(switchState, selectedDate) {
            if (switchState) onDateSelectedStart(selectedDate) else onDateSelectedStart(LocalDate.now())
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
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm", Locale("ru")) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    ) {

        TimeSelectionRow(
            icon = R.drawable.ic_time,
            label = stringResource(R.string.start),
            time = selectedTimeStart.format(timeFormatter),
            switchState = switchStateStart,
            onSwitchChange = { switchStateStart = it },
            onClick = { isExpandedTimeStart = !isExpandedTimeStart })

        if (isExpandedTimeStart) {
            Spacer(modifier = Modifier.height(16.dp))
            CustomTimePicker(
                initialHour = selectedTimeStart.hour,
                initialMinute = selectedTimeStart.minute
            ) { hour, minute ->
                selectedTimeStart = LocalTime.of(hour, minute)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TimeSelectionRow(
            icon = R.drawable.ic_time,
            label = stringResource(R.string.end),
            time = selectedTimeEnd.format(timeFormatter),
            switchState = switchStateEnd,
            onSwitchChange = { switchStateEnd = it },
            onClick = { isExpandedTimeEnd = !isExpandedTimeEnd })

        if (isExpandedTimeEnd) {
            Spacer(modifier = Modifier.height(16.dp))
            CustomTimePicker(
                initialHour = selectedTimeEnd.hour,
                initialMinute = selectedTimeEnd.minute
            ) { hour, minute ->
                selectedTimeEnd = LocalTime.of(hour, minute)
            }
        }
    }
    LaunchedEffect(switchStateStart, selectedTimeStart) {
        if (switchStateStart) onTimeSelectedStart(selectedTimeStart)
    }

    LaunchedEffect(switchStateEnd, selectedTimeStart, selectedTimeEnd) {
        if (switchStateEnd) {
            var durationMinutes = ChronoUnit.MINUTES.between(selectedTimeStart, selectedTimeEnd)
            if (durationMinutes < 0) durationMinutes = 0
            val hours = durationMinutes / 60
            val minutes = durationMinutes % 60
            val durationText = when {
                hours > 0 && minutes > 0 -> "$hours ч $minutes мин"
                hours > 0 -> "$hours ч"
                else -> "$minutes мин"
            }
            onDurationCalculated(durationText)
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
                contentDescription = null,
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

@Composable
internal fun TimeColumnPicker(
    initialValue: Int,
    onValueChange: (Int) -> Unit,
    range: IntProgression,
    modifier: Modifier = Modifier,
    isHoursColumn: Boolean
) {
    val context = LocalContext.current
    val pad = countOfVisibleItemsInPicker / 2
    val values by remember(range) { mutableStateOf(range.map { it.getTimeDefaultStr() }) }
    val repeatBlocks = 200
    val list by remember(values) {
        mutableStateOf(mutableListOf<String>().apply {
            repeat(pad) { add("") }
            repeat(repeatBlocks) { values.forEach { add(it) } }
            repeat(pad) { add("") }
        })
    }
    val indexInValues = remember(initialValue, isHoursColumn) {
        if (isHoursColumn) initialValue else initialValue / 5
    }
    val centerBlock = repeatBlocks / 2
    val initialFirstVisible =
        remember(indexInValues) { pad + centerBlock * values.size + indexInValues - pad }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialFirstVisible)

    var selectedValue by remember { mutableIntStateOf(initialValue) }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && listState.firstVisibleItemScrollOffset.pixelsToDp(
                context
            ) % itemHeight != 0f
        ) {
            listState.animateScrollToItem(listState.itemForScrollTo(context))
        }
        val minIndex = pad + values.size
        val maxIndex = pad + (repeatBlocks - 1) * values.size
        if (!listState.isScrollInProgress) {
            val current = listState.firstVisibleItemIndex
            if (current < minIndex || current > maxIndex) {
                val normalized = ((current - pad) % values.size + values.size) % values.size
                val target = pad + centerBlock * values.size + normalized
                listState.scrollToItem(target)
            }
        }
    }

    LaunchedEffect(listState.firstVisibleItemScrollOffset) {
        val centerIndex = listState.itemForScrollTo(context) + pad
        val raw = list[centerIndex]
        val newValue = raw.toIntOrNull()
        if (newValue != null && newValue != selectedValue) {
            onValueChange(newValue)
            selectedValue = newValue
        }
    }

    Box(
        modifier = modifier
            .height(listHeight.dp)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Border(itemHeight = itemHeight.dp, color = Black)

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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight)
            .background(darkGray, shape = RoundedCornerShape(10.dp))
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
                .background(color = Color.Transparent, shape = RoundedCornerShape(10.dp))
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

@Composable
fun SheetContent() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Background)) {
        Text(text = "SheetContent")
    }
    // доделать bottomSheet

}