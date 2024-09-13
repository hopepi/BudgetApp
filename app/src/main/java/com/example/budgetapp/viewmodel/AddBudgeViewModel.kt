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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddBudgeViewModel @Inject constructor(application: Application) : BaseViewModel(application) {

    val allCategory = mutableStateOf<List<Category>>(emptyList())

    init {
        getAllCategory()
    }

    fun addNewCategory(categoryName : String){
        viewModelScope.launch (Dispatchers.IO) {
            val database = BudgetDatabase(getApplication())
            database.categoryDao().insertCategory(Category(categoryName))

            getAllCategory()
        }
    }

    private fun getAllCategory(){
        viewModelScope.launch(Dispatchers.IO) {
            val database = BudgetDatabase(getApplication())
            if (database.categoryDao().getAllCategory().isNullOrEmpty()){
                addNewCategory("Other")
                withContext(Dispatchers.Main){
                    allCategory.value = database.categoryDao().getAllCategory()!!
                }
            }else{
                withContext(Dispatchers.Main){
                    allCategory.value = database.categoryDao().getAllCategory()!!
                }
            }

        }
    }

    fun deleteCategory(categoryId: Int){
        viewModelScope.launch (Dispatchers.IO){
            val database = BudgetDatabase(getApplication())
            database.categoryDao().deleteCategoryById(categoryId)
            getAllCategory()
        }
    }

    fun addNewBudget(amount: Double,amountOfDate: String,moneyType : String ,categoryId : Int?){
        viewModelScope.launch (Dispatchers.IO){
            val database = BudgetDatabase(getApplication())
            database.incomeAndExpensesDao().insertIncomeAndExpenses(IncomeAndExpenses(amount,amountOfDate, moneyType = moneyType,categoryId))
        }
    }

    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }
}