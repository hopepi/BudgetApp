package com.example.budgetapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("category")
data class Category(
    @ColumnInfo("categoryName")
    val categoryName : String
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("categoryId")
    var categoryId : Int = 0
}
