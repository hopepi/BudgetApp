package com.example.budgetapp.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.protobuf.Internal.BooleanList
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.budgetapp.model.NavItem




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    showSettingsButton: Boolean = false,
    onShowBackButton : (() -> Unit)? = null,
    onSettingsButton: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
    navController: NavController,
) {
    val items: List<NavItem> = listOf(
        NavItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home,
            contentDesc = "Home"
        ),
        NavItem(
            title = "Add Budget",
            selectedIcon = Icons.Filled.ShoppingCart,
            unSelectedIcon = Icons.Outlined.ShoppingCart,
            contentDesc = "Add Budget"
        ),
        NavItem(
            title = "Income And Expenses List",
            selectedIcon = Icons.AutoMirrored.Filled.List,
            unSelectedIcon = Icons.AutoMirrored.Outlined.List,
            contentDesc = "Income And Expenses List"
        ),
        NavItem(
            title = "Report",
            selectedIcon = Icons.Filled.Info,
            unSelectedIcon = Icons.Outlined.Info,
            contentDesc = "Report"
        )
    )

    var selectedIndex by remember { mutableIntStateOf(0) }
    var topBarTitle by remember { mutableStateOf("HOME") }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val showTopBar = currentRoute != "settings"
    val showBottomBar = currentRoute != "settings"

    Scaffold(
        modifier = modifier,
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(text = topBarTitle) },
                    actions = {
                        if (showSettingsButton) {
                            IconButton(
                                onClick = { onSettingsButton?.invoke() },
                                Modifier.height(30.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "settings"
                                )
                            }
                        }
                    }
                )
            }else{
                TopAppBar(
                    title = { Text(text = "Settings") },
                    navigationIcon = {
                        IconButton(
                            onClick = { onShowBackButton?.invoke() },
                            Modifier.height(15.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        },
        content = content,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    items.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                                when (index) {
                                    0 -> {
                                        navController.navigate("home")
                                        topBarTitle = "HOME"
                                    }
                                    1 -> {
                                        navController.navigate("add_budget")
                                        topBarTitle = "ADD BUDGET"
                                    }
                                    2 -> {
                                        navController.navigate("income_and_expenses")
                                        topBarTitle = "Income And Expenses"
                                    }
                                    3 -> {
                                        navController.navigate("report")
                                        topBarTitle = "Reports"
                                    }
                                }
                            },
                            icon = {
                                if (selectedIndex == index) {
                                    Icon(
                                        imageVector = navItem.selectedIcon,
                                        contentDescription = navItem.contentDesc
                                    )
                                } else {
                                    Icon(
                                        imageVector = navItem.unSelectedIcon,
                                        contentDescription = navItem.contentDesc
                                    )
                                }
                            },
                            label = { Text(text = navItem.title) }
                        )
                    }
                }
            }
        }
    )
}
