package com.example.mynewapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MI_BAND_ACTIVITY_SAMPLE")
data class MiBandActivity(
    @PrimaryKey val timestamp: Long,
    val deviceId: Int,
    val userId: Int,
    val rawIntensity: Int,
    val steps: Int,
    val rawKind: Int,
    val heartRate: Int
)
