package com.example.taskmanager.ui.navigation

sealed class Screen(val route: String, val label: String, val icon: String) {
    object Tasks : Screen("tasks", "Tasks", "task")
    object Manage : Screen("manage", "Manage", "settings")
}
