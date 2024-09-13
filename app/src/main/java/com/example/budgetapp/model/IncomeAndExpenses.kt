package com.example.budgetapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "incomeAndExpenses",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["categoryId"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.SET_NULL // Kategori silindiğinde category_id alanını null yapar
    )]
)
data class IncomeAndExpenses(
    @ColumnInfo("amount")
    val amount : Double,
    @ColumnInfo("amountOfDate")
    val amountOfDate : String,
    @ColumnInfo("moneyType")
    val moneyType : String? = "$",
    @ColumnInfo("categoryId")
    val categoryId : Int?
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id : Int = 0
}
