package com.example.budgetapp.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.budgetapp.model.IncomeAndExpenses

@Dao
interface IncomeAndExpensesDao {
    @Insert
    suspend fun insertIncomeAndExpenses(incomeAndExpenses: IncomeAndExpenses) : Long

    @Update
    suspend fun updateIncomeAndExpenses(incomeAndExpenses: IncomeAndExpenses)

    @Delete
    suspend fun deleteIncomeAndExpenses(incomeAndExpenses: IncomeAndExpenses)

    @Query("DELETE FROM incomeAndExpenses WHERE id = :id")
    suspend fun deleteIncomeAndExpensesById(id : Int)

    @Query("SELECT * FROM incomeAndExpenses ORDER BY amountOfDate DESC,id DESC")
    suspend fun getAllIncomeAndExpenses() : List<IncomeAndExpenses>

    @Query("SELECT * FROM incomeAndExpenses WHERE id = :id")
    suspend fun getIncomeAndExpenses(id: Int) : IncomeAndExpenses

    @Query("SELECT * FROM incomeAndExpenses ORDER BY amountOfDate DESC, id ASC LIMIT 10")
    suspend fun getAllTransactionsSortedByDateAsc(): List<IncomeAndExpenses>

    @Query("SELECT * FROM incomeAndExpenses ORDER BY amountOfDate DESC, id DESC LIMIT 10")
    suspend fun getAllTransactionsSortedByDateDesc(): List<IncomeAndExpenses>

    @Query("SELECT SUM(amount) FROM incomeAndExpenses")
    suspend fun getTotalAmount(): Double?

    @Query("SELECT SUM(amount) FROM incomeAndExpenses WHERE amountOfDate BETWEEN :startDate AND :endDate")
    suspend fun getTotalAmountInDateRange(startDate: String, endDate: String): Double?

    @Query("SELECT SUM(amount) FROM incomeAndExpenses WHERE amountOfDate = :date")
    suspend fun getTotalAmountByDate(date: String): Double?

    @Query("SELECT SUM(amount) FROM incomeAndExpenses WHERE amount < 0")
    suspend fun getNegativeAmounts() : Double?

    @Query(" SELECT SUM(amount) FROM incomeAndExpenses WHERE amount < 0 AND amountOfDate BETWEEN :startDate AND :endDate")
    suspend fun getNegativeAmountsInDateRange(startDate: String, endDate: String): Double?

    @Query(" SELECT SUM(amount) FROM incomeAndExpenses WHERE amount > 0 AND amountOfDate BETWEEN :startDate AND :endDate")
    suspend fun getPositiveAmountsInDateRange(startDate: String, endDate: String): Double?

    @Query("SELECT SUM(amount) FROM incomeAndExpenses WHERE amount > 0")
    suspend fun getPositiveAmounts() : Double?
}