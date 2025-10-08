package com.example.mynewapp

import android.app.Application
import com.example.mynewapp.data.AppDatabase

class MyApplication : Application() {
    // Using by lazy so the database is only created when needed
    // rather than when the application starts
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
