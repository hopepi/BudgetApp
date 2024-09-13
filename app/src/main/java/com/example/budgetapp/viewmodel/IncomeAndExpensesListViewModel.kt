package com.example.budgetapp.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.model.Category
import com.example.budgetapp.model.IncomeAndExpenses
import com.example.budgetapp.service.BudgetDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class IncomeAndExpensesListViewModel @Inject constructor(application: Application) : BaseViewModel(application) {
    val incomeAndExpensesAllList = mutableStateOf<Map<IncomeAndExpenses, Category?>>(emptyMap())

    init {
        loadGetAllIncomeAndExpensesDetails()
    }
    private fun loadGetAllIncomeAndExpensesDetails(){
        viewModelScope.launch (Dispatchers.IO) {
            val database = BudgetDatabase(getApplication())
            val incomeAndExpensesDao = database.incomeAndExpensesDao()
            val categoryDao = database.categoryDao()
            val incomeAndExpensesList = incomeAndExpensesDao.getAllIncomeAndExpenses()
            val incomeAndExpensesWithCategories = mutableMapOf<IncomeAndExpenses, Category?>()

            for (incomeAndExpense in incomeAndExpensesList) {
                val category = incomeAndExpense.categoryId?.let {
                    categoryDao.getCategoryById(it)
                }
                incomeAndExpensesWithCategories[incomeAndExpense] = category
            }

            withContext(Dispatchers.Main){
                incomeAndExpensesAllList.value = incomeAndExpensesWithCategories
            }
        }
    }

}