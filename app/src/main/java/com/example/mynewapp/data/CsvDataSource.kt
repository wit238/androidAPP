package com.example.mynewapp.data

import android.content.Context
import com.example.mynewapp.data.MiBandActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class CsvDataSource(private val context: Context) {

    fun getActivities(fileName: String): List<MiBandActivity> {
        val activities = mutableListOf<MiBandActivity>()
        try {
            context.assets.open(fileName).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    // Skip header
                    reader.readLine()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val tokens = line!!.split(',')
                        if (tokens.size >= 7) {
                            val activity = MiBandActivity(
                                timestamp = tokens[0].toLong(),
                                deviceId = tokens[1].toInt(),
                                userId = tokens[2].toInt(),
                                rawIntensity = tokens[3].toInt(),
                                steps = tokens[4].toInt(),
                                rawKind = tokens[5].toInt(),
                                heartRate = tokens[6].toInt()
                            )
                            activities.add(activity)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return activities
    }
}
