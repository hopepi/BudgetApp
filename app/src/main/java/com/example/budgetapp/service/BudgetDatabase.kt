package com.example.budgetapp.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.budgetapp.model.Category
import com.example.budgetapp.model.IncomeAndExpenses

@Database(entities = [Category::class,IncomeAndExpenses::class], version = 2, exportSchema = false)
abstract class BudgetDatabase : RoomDatabase(){

    abstract fun categoryDao() : CategoryDao
    abstract fun incomeAndExpensesDao() : IncomeAndExpensesDao

    companion object{
        @Volatile
        private var INSTANCE : BudgetDatabase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(lock){
            INSTANCE ?: createDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            BudgetDatabase::class.java,
            "BudgetDatabase"
        ).addMigrations(MIGRATION_1_2)
            .build()
    }
}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Yeni sütunu eklemek için SQL komutu
        database.execSQL("ALTER TABLE incomeAndExpenses ADD COLUMN moneyType TEXT DEFAULT '$'")
        database.execSQL("UPDATE incomeAndExpenses SET moneyType = '$' WHERE moneyType IS NULL")
    }
}
