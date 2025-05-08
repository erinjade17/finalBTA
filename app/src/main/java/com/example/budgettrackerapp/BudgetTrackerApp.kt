package com.example.budgettrackerapp

import android.app.Application
import com.example.budgettrackerapp.data.AppDatabase

class BudgetTrackerApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) } // Changed to getInstance
}