package com.example.taskmanager.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.ui.components.AddEditTaskDialog
import com.example.taskmanager.ui.navigation.Screen
import com.example.taskmanager.ui.screens.ManageTasksScreen
import com.example.taskmanager.ui.screens.TaskListScreen
import com.example.taskmanager.ui.theme.*
import com.example.taskmanager.viewmodel.TaskViewModel

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagerApp(viewModel: TaskViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var showAddDialog by remember { mutableStateOf(false) }

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Tasks, Icons.Rounded.CheckCircle, Icons.Rounded.CheckCircle),
        BottomNavItem(Screen.Manage, Icons.Rounded.Settings, Icons.Rounded.Settings)
    )

    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomNavBar(
                items = bottomNavItems,
                currentDestination = currentDestination,
                onItemClick = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        floatingActionButton = {
            val currentRoute = currentDestination?.route

            if (currentRoute == Screen.Tasks.route || currentRoute == null) {
                AnimatedFAB(onClick = { showAddDialog = true })
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Tasks.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable(
                route = Screen.Tasks.route,
                enterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { -it / 8 } },
                exitTransition = { fadeOut(tween(300)) }
            ) {
                TaskListScreen(viewModel = viewModel)
            }
            composable(
                route = Screen.Manage.route,
                enterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 8 } },
                exitTransition = { fadeOut(tween(300)) }
            ) {
                ManageTasksScreen(viewModel = viewModel)
            }
        }
    }

    if (showAddDialog) {
        AddEditTaskDialog(
            onDismiss = { showAddDialog = false },
            onSave = { task ->
                viewModel.addTask(task)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        DeepNavy,
                        PurpleGlow.copy(alpha = 0.15f),
                        DeepNavy
                    )
                )
            )
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(PurpleGlow, CyanAccent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.TaskAlt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Task Manager",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            },
            actions = {
                IconButton(
                    onClick = { /* Settings/menu action */ },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSurface)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Tune,
                        contentDescription = "Settings",
                        tint = PurpleGlow,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun BottomNavBar(
    items: List<BottomNavItem>,
    currentDestination: androidx.navigation.NavDestination?,
    onItemClick: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceDark)
    ) {
        // Top gradient border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            PurpleGlow.copy(alpha = 0.5f),
                            CyanAccent.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )

        NavigationBar(
            containerColor = Color.Transparent,
            contentColor = TextSecondary,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->
                val selected = currentDestination?.hierarchy?.any {
                    it.route == item.screen.route
                } == true

                NavigationBarItem(
                    selected = selected,
                    onClick = { onItemClick(item.screen) },
                    icon = {
                        val scale by animateFloatAsState(
                            targetValue = if (selected) 1.15f else 1.0f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                            label = "iconScale"
                        )
                        Box(contentAlignment = Alignment.Center) {
                            if (selected) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(PurpleGlow.copy(alpha = 0.15f))
                                )
                            }
                            Icon(
                                imageVector = if (selected) item.selectedIcon else item.icon,
                                contentDescription = item.screen.label,
                                modifier = Modifier.size((24 * scale).dp),
                                tint = if (selected) PurpleGlow else TextMuted
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.screen.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (selected) PurpleGlow else TextMuted
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
private fun AnimatedFAB(onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "fabScale"
    )

    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        containerColor = Color.Transparent,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
        modifier = Modifier
            .size((56 * scale).dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = PurpleGlow.copy(alpha = 0.6f),
                spotColor = PurpleGlow.copy(alpha = 0.8f)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(PurpleGlow, CyanAccent.copy(alpha = 0.8f))
                ),
                shape = RoundedCornerShape(18.dp)
            )
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add Task",
            tint = Color.White,
            modifier = Modifier.size(26.dp)
        )
    }
}
