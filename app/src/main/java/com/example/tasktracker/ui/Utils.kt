package com.example.tasktracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tasktracker.R
import com.example.tasktracker.ui.theme.White
import com.example.tasktracker.ui.theme.expandedGradient
import com.example.tasktracker.ui.theme.whiteButton

@Composable
fun ToolBar(
    navController: NavHostController,
    onDoneClick: () -> Unit,
    nameTask: Boolean = false,
    textToolBar: String,
    icon: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                expandedGradient, shape = RoundedCornerShape(
                    topStart = 0.dp, topEnd = 0.dp, bottomStart = 32.dp, bottomEnd = 32.dp
                )
            )
            .padding(top = 38.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
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
            textToolBar,
            color = White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        IconButton(
            onClick = { if (nameTask) onDoneClick() }, modifier = Modifier
                .size(48.dp)
                .background(
                    color = whiteButton, shape = RoundedCornerShape(16.dp)
                )
        ) {
            Image(
                painter = painterResource(
                    when (icon) {
                        1 -> R.drawable.ic_done
                        else -> R.drawable.ic_edit
                    }
                ),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

fun isValidField(value: String?): Boolean {
    return value != null && value != "null" && value.isNotBlank()
}