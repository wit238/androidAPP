package com.example.mynewapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MiBandActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(activities: List<MiBandActivity>)

    @Query("SELECT * FROM MI_BAND_ACTIVITY_SAMPLE ORDER BY timestamp ASC")
    fun getAll(): Flow<List<MiBandActivity>>
}
