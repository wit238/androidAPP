package com.example.mynewapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("SELECT * FROM Device")
    fun getAllDevices(): Flow<List<Device>>

    @Query("SELECT * FROM Device WHERE id = :deviceId")
    fun getDeviceById(deviceId: Int): Flow<Device>
}
