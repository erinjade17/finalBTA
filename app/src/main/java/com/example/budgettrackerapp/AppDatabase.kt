package com.example.budgettrackerapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgettrackerapp.data.dao.*
import com.example.budgettrackerapp.data.entities.*
import com.example.budgettrackerapp.data.model.DateConverter
import com.example.budgettrackerapp.data.model.Transaction
import com.example.budgettrackerapp.data.model.TransactionTypeConverter

@Database(
    entities = [User::class, Category::class, Expense::class, BudgetGoal::class, Transaction::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class, TransactionTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetGoalDao(): BudgetGoalDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase { // Changed to getInstance
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_tracker_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}