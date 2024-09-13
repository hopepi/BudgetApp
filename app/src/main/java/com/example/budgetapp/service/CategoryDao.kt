package com.example.budgetapp.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.budgetapp.model.Category
import com.example.budgetapp.model.CategoryTotal
import com.example.budgetapp.model.CategoryWithTransactions

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: Category) : Long

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("DELETE FROM category WHERE categoryId = :categoryId")
    suspend fun deleteCategoryById(categoryId: Int)

    @Query("SELECT * FROM category")
    suspend fun getAllCategory() : List<Category>?

    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    suspend fun getCategoryById(categoryId: Int) : Category

    @Transaction
    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    suspend fun getCategoryIncomeAndExpenses(categoryId: Int) : CategoryWithTransactions



    @Query("""
    SELECT c.categoryName, SUM(i.amount) as totalAmount
    FROM IncomeAndExpenses i
    LEFT JOIN category c ON i.categoryId = c.categoryId
    GROUP BY c.categoryName
    """)
    suspend fun getCategorySums(): List<CategoryTotal>
}