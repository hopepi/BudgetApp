package com.example.budgetapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.model.Category
import com.example.budgetapp.model.IncomeAndExpenses
import com.example.budgetapp.service.BudgetDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(application: Application) : BaseViewModel(application) {


    val incomeAndExpenses10List = mutableStateOf<Map<IncomeAndExpenses,Category?>>(emptyMap())
    val lineDetailsList = mutableStateOf<List<Pair<Int, Double>>>(emptyList())
    val expense = mutableDoubleStateOf(value = 0.0)
    val income = mutableDoubleStateOf(value = 0.0)
    var isLoading by mutableStateOf(true)

    init {
        loadLast10Budget()
        loadLineDetails7Days()
        loadGetAllIncomeAndExpenses()
    }

    private fun loadGetAllIncomeAndExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            val database = BudgetDatabase(getApplication())
            val incomeAndExpensesDao = database.incomeAndExpensesDao()

            val totalExpenses = incomeAndExpensesDao.getNegativeAmounts() ?: 0.0
            val totalIncome = incomeAndExpensesDao.getPositiveAmounts() ?: 0.0

            withContext(Dispatchers.Main) {
                expense.doubleValue = totalExpenses
                income.doubleValue = totalIncome
            }
        }
    }

    private fun loadLast10Budget() {
        viewModelScope.launch(Dispatchers.IO) {
            val database = BudgetDatabase(getApplication())
            val incomeAndExpensesDao = database.incomeAndExpensesDao()
            val categoryDao = database.categoryDao()
            val incomeAndExpensesList = incomeAndExpensesDao.getAllTransactionsSortedByDateDesc()

            val incomeAndExpensesWithCategories = mutableMapOf<IncomeAndExpenses, Category?>()

            for (incomeAndExpense in incomeAndExpensesList) {
                val category = incomeAndExpense.categoryId?.let {
                    categoryDao.getCategoryById(it)
                }
                incomeAndExpensesWithCategories[incomeAndExpense] = category
            }

            withContext(Dispatchers.Main){
                incomeAndExpenses10List.value = incomeAndExpensesWithCategories
            }
        }
    }


    private fun loadLineDetails7Days() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        val dailyTotals = mutableListOf<Pair<Int, Double>>()

        viewModelScope.launch(Dispatchers.IO) {
            val database = BudgetDatabase(getApplication())
            val incomeAndExpensesDao = database.incomeAndExpensesDao()

            for (i in 0 until 7) {
                val currentDate = dateFormat.format(calendar.time)

                val totalAmount = incomeAndExpensesDao.getTotalAmountByDate(currentDate) ?: 0.0

                dailyTotals.add(Pair(i+1, totalAmount))

                calendar.add(Calendar.DAY_OF_YEAR, -1)
            }

            withContext(Dispatchers.Main) {
                lineDetailsList.value = dailyTotals
            }
            isLoading = false
        }
    }

}