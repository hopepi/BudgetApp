package com.example.budgetapp.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.model.CategoryTotal
import com.example.budgetapp.service.BudgetDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//Test ViewModel buglar bulunuyor
@HiltViewModel
class ReportViewModel @Inject constructor(application: Application) :BaseViewModel(application){

    val categorySumsTotalList = mutableStateOf<List<CategoryTotal>>(emptyList())

    val totalAmount = mutableStateOf(0.0)
    val totalExpense = mutableStateOf(0.0)
    val totalIncome = mutableStateOf(0.0)


    init {
        loadTotalSumsCategory()
        loadSumAmount(null,null)
    }

    private fun loadTotalSumsCategory (){
        viewModelScope.launch (Dispatchers.IO) {
            val database = BudgetDatabase(getApplication())
            val categorySums = database.categoryDao().getCategorySums()

            withContext(Dispatchers.Main){
                categorySumsTotalList.value = categorySums
            }
        }
    }

    fun loadSumAmount(startDay : String?,endDay : String?){
        viewModelScope.launch (Dispatchers.IO) {
            val database = BudgetDatabase(getApplication())
            var totalAmountNet = 0.0
            var totalAmountExpense = 0.0
            var totalAmountIncome = 0.0
            if (startDay.isNullOrBlank() || endDay.isNullOrBlank()){
                totalAmountNet = database.incomeAndExpensesDao().getTotalAmount() ?: 0.0
                totalAmountExpense = database.incomeAndExpensesDao().getNegativeAmounts() ?: 0.0
                totalAmountIncome = database.incomeAndExpensesDao().getPositiveAmounts() ?: 0.0
            } else{
                totalAmountNet = database.incomeAndExpensesDao().getTotalAmountInDateRange(startDay,endDay) ?: 0.0
                totalAmountExpense = database.incomeAndExpensesDao().getNegativeAmountsInDateRange(startDay,endDay) ?: 0.0
                totalAmountIncome = database.incomeAndExpensesDao().getPositiveAmountsInDateRange(startDay,endDay) ?: 0.0
            }
            totalAmountExpense *= -1
            withContext(Dispatchers.Main) {
                totalAmount.value = totalAmountNet
                totalExpense.value = totalAmountExpense
                totalIncome.value = totalAmountIncome
            }
        }
    }
    fun getHighestSpendingCategory(categories: List<CategoryTotal>): CategoryTotal? {
        return categories.maxByOrNull { it.totalAmount }
    }

    fun getLowestSpendingCategory(categories: List<CategoryTotal>): CategoryTotal? {
        return categories.minByOrNull { it.totalAmount }
    }

    fun getHighestIncomeCategory(categories: List<CategoryTotal>): CategoryTotal? {
        return categories.maxByOrNull { it.totalAmount }
    }

    fun getLowestIncomeCategory(categories: List<CategoryTotal>): CategoryTotal? {
        return categories.minByOrNull { it.totalAmount }
    }
}