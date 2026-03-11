package com.example.taskmanager.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.model.Task
import com.example.taskmanager.ui.components.AddEditTaskDialog
import com.example.taskmanager.ui.components.TaskCard
import com.example.taskmanager.ui.theme.*
import com.example.taskmanager.viewmodel.TaskViewModel

@Composable
fun TaskListScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.allTasks.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(DeepNavy)) {
        if (tasks.isEmpty()) {
            EmptyTasksPlaceholder()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
            ) {
                itemsIndexed(
                    items = tasks,
                    key = { _, task -> task.taskId }
                ) { index, task ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(300, delayMillis = index * 50)) +
                                slideInVertically(tween(300, delayMillis = index * 50)) { it / 4 }
                    ) {
                        TaskCard(
                            task = task,
                            onEdit = { taskToEdit = it },
                            onDelete = { viewModel.deleteTask(it) }
                        )
                    }
                }
            }
        }
    }

    if (taskToEdit != null) {
        AddEditTaskDialog(
            task = taskToEdit,
            onDismiss = { taskToEdit = null },
            onSave = { updatedTask ->
                viewModel.updateTask(updatedTask)
                taskToEdit = null
            }
        )
    }
}

@Composable
private fun EmptyTasksPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PurpleGlow.copy(alpha = 0.3f),
                            PurpleGlow.copy(alpha = 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.PlaylistAdd,
                contentDescription = null,
                tint = PurpleGlow,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "No Tasks Yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the + button to add your first task",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}
