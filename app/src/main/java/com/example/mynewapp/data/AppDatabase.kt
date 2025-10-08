package com.example.mynewapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

@Database(entities = [User::class, Device::class, MiBandActivity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun deviceDao(): DeviceDao
    abstract fun miBandActivityDao(): MiBandActivityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val dbFile = context.getDatabasePath("Gadgetbridge.db")
                
                // Copy the database from assets if it doesn't exist
                if (!dbFile.exists()) {
                    createFromAsset(context, "Gadgetbridge.db")
                }

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Gadgetbridge.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                
                INSTANCE = instance
                instance
            }
        }

        private fun createFromAsset(context: Context, databaseName: String) {
            val dbPath = context.getDatabasePath(databaseName)
            
            // Ensure the parent directory exists
            if (!dbPath.parentFile.exists()) {
                dbPath.parentFile.mkdirs()
            }

            // Copy the file from assets
            try {
                val inputStream = context.assets.open(databaseName)
                val outputStream = dbPath.outputStream()
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
                println("Database copied successfully from assets.")
            } catch (e: Exception) {
                println("Error copying database from assets: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
