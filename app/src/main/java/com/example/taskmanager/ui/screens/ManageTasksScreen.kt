package com.example.taskmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ManageAccounts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskmanager.ui.theme.*
import com.example.taskmanager.viewmodel.TaskViewModel

@Composable
fun ManageTasksScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.allTasks.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavy)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(PurpleSoft, PurpleGlow.copy(alpha = 0.6f))
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ManageAccounts,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                    Column {
                        Text(
                            text = "Manage Tasks Here",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${tasks.size} task${if (tasks.size != 1) "s" else ""} in total",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary.copy(alpha = 0.75f)
                        )
                    }
                }
            }

            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nothing to manage yet.\nAdd tasks from the Tasks tab!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(items = tasks, key = { it.taskId }) { task ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 5.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    if (task.dueDate.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Due: ${task.dueDate}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                var showConfirm by remember { mutableStateOf(false) }

                                if (showConfirm) {
                                    TextButton(
                                        onClick = {
                                            viewModel.deleteTask(task)
                                            showConfirm = false
                                        },
                                        colors = ButtonDefaults.textButtonColors(contentColor = PinkAccent)
                                    ) {
                                        Text("Confirm", fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    IconButton(onClick = { showConfirm = true }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Delete,
                                            contentDescription = "Delete",
                                            tint = PinkAccent
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
