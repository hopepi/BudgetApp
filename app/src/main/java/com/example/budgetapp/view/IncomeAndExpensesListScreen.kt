package com.example.budgetapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgetapp.viewmodel.IncomeAndExpensesListViewModel

@Composable
fun IncomeAndExpensesListScreen(
    viewModel: IncomeAndExpensesListViewModel = hiltViewModel()
) {

    val incomeAndExpensesList by viewModel.incomeAndExpensesAllList

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            // LazyColumn with TransactionCard items
            LazyColumn(
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(incomeAndExpensesList.toList()){(incomeAndExpenses, category) ->
                    TransactionCardList(
                        category = category?.categoryName ?: "UNCATEGORY",
                        isIncome = incomeAndExpenses.amount >= 0,
                        amount = "${incomeAndExpenses.moneyType} ${incomeAndExpenses.amount}",
                        date = incomeAndExpenses.amountOfDate
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionCardList(
    category: String,
    isIncome: Boolean,
    amount: String,
    date: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isIncome) "Gelir" else "Gider",
                    fontWeight = FontWeight.Bold,
                    color = if (isIncome) Color.Green else Color.Red,
                    fontSize = 16.sp
                )

                Text(
                    text = category,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Text(
                    text = date,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = amount,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = Color.Black
                )
            }
        }
    }
}

data class TransactionData(
    val category: String,
    val isIncome: Boolean,
    val amount: String,
    val date: String
)