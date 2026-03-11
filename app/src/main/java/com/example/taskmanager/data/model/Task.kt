package com.example.taskmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val title: String,
    val description: String,
    val dueDate: String
)
