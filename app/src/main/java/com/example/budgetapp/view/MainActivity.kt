package com.example.budgetapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.budgetapp.model.IncomeAndExpenses
import com.example.budgetapp.view.charts.DonutPie
import com.example.budgetapp.view.charts.LineChart
import com.example.budgetapp.view.ui.theme.BudgetAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetAppTheme {
                val navController = rememberNavController()
                AppScaffold(
                    showSettingsButton = true,
                    onShowBackButton = {navController.navigateUp()},
                    onSettingsButton = {navController.navigate("settings")},
                    content = { paddingValues ->
                        NavHost(
                            navController = navController, startDestination = "home",
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable("home") {
                                HomeScreen()
                            }
                            composable("add_budget") {
                                AddBudgetScreen()
                            }
                            composable("income_and_expenses") {
                                IncomeAndExpensesListScreen()
                            }
                            composable("report") {
                                ReportScreen()
                            }
                            composable("settings") {
                                //İsteğe bağlı yapılacak
                            }
                        }
                    },
                    navController = navController
                )
            }
        }
    }
}

