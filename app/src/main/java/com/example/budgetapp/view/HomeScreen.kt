package com.example.budgetapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetapp.view.charts.LineChart
import com.example.budgetapp.view.charts.PieChart
import com.example.budgetapp.view.charts.PieChartInput
import com.example.budgetapp.viewmodel.HomeViewModel
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
){
    Surface (
        modifier = Modifier
            .fillMaxSize()
    ){
        Column {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(375.dp)
                            .size(450.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(16.dp)
                    ) {
                        if (viewModel.isLoading) {

                            LoadingAnimation()
                        } else {

                            LineChart(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(325.dp),
                                data = if (viewModel.lineDetailsList.value.isEmpty())  List(7) { Pair(0, 0.0) }  else  viewModel.lineDetailsList.value

                            )
                        }
                    }
                }

                item {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(375.dp)
                            .size(450.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(16.dp)
                    ) {
                        val income = viewModel.income.doubleValue.toInt()
                        val expenses = viewModel.expense.doubleValue.toInt()
                        if (income == 0 && expenses == 0){
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "You don't have any data yet",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                        else{
                            PieChart(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(16.dp),
                                input = listOf(
                                    PieChartInput(Color.Green,viewModel.income.doubleValue.toInt(),"INCOME",isTapped = true),
                                    PieChartInput(Color.Red,viewModel.expense.doubleValue.toInt() * -1,"EXPENSES", isTapped = true)
                                ),
                                centerText = "Income/Expense"
                            )
                        }

                    }
                }
            }

            //Son 10 Harcama
            HorizontalDivider(modifier = Modifier.height(10.dp))
            LazyColumn {
                item {
                    BeautifulTitle()
                }
                val last10History by viewModel.incomeAndExpenses10List
                if (last10History.isEmpty()) {
                    item {
                        CustomMessage() }
                } else {
                    items(last10History.toList()) { (incomeExpensesDetails, category) ->
                        TransactionCard(
                            category = category?.categoryName ?: "UNCATEGORY",
                            isIncome = incomeExpensesDetails.amount >= 0,
                            amount = "${incomeExpensesDetails.moneyType} ${incomeExpensesDetails.amount}",
                            date = incomeExpensesDetails.amountOfDate
                        )
                    }
                }

            }

        }
    }
}

@Composable
private fun TransactionCard(
    category: String,
    isIncome: Boolean,
    amount: String,
    date: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                    text = if (isIncome) "Income" else "Expense",
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

@Composable
fun BeautifulTitle() {
    Text(
        text = "✨ Latest Events ✨",
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        fontStyle = FontStyle.Italic,
        color = Color(0xFF6200EA),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun CustomMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "You don't have any data yet",
            modifier = Modifier
                .fillMaxWidth(),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF455A64),
                textAlign = TextAlign.Center,
                shadow = Shadow(
                    color = Color.Black,
                    blurRadius = 4f
                ),
                letterSpacing = 1.2.sp
            )
        )
    }
}
@Composable
fun LoadingAnimation() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
    }
}