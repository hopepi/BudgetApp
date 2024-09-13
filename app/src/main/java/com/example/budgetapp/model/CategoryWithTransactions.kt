package com.example.budgetapp.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithTransactions(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val transactions: List<IncomeAndExpenses>
)
