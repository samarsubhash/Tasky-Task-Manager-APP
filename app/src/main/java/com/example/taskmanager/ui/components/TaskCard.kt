package com.example.taskmanager.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.model.Task
import com.example.taskmanager.ui.theme.*

@Composable
fun TaskCard(
    task: Task,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val cardColor by animateColorAsState(
        targetValue = if (isPressed) CardSurfaceElevated else CardSurface,
        animationSpec = tween(150),
        label = "cardColor"
    )

    // Accent color cycling based on task ID for visual variety
    val accentColors = listOf(
        PurpleGlow, CyanAccent, PinkAccent, GreenSuccess, OrangeWarning
    )
    val accentColor = accentColors[task.taskId % accentColors.size]

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Gradient left accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(IntrinsicSize.Max)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(accentColor, accentColor.copy(alpha = 0.4f))
                        ),
                        shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                    )
                    .fillMaxHeight()
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, top = 16.dp, bottom = 12.dp, end = 8.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Due date chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarToday,
                        contentDescription = "Due Date",
                        modifier = Modifier.size(12.dp),
                        tint = accentColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = task.dueDate.ifBlank { "No due date" },
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Action buttons
            Column(
                modifier = Modifier
                    .padding(end = 8.dp, top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                IconButton(
                    onClick = { onEdit(task) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "Edit Task",
                        tint = PurpleGlow,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = { onDelete(task) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete Task",
                        tint = PinkAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
