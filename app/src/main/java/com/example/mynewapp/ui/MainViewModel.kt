package com.example.mynewapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mynewapp.data.AppDatabase
import com.example.mynewapp.data.Device
import com.example.mynewapp.data.DeviceDao
import com.example.mynewapp.data.MiBandActivity
import com.example.mynewapp.data.MiBandActivityDao
import com.example.mynewapp.data.UserDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    userDao: UserDao,
    deviceDao: DeviceDao,
    miBandActivityDao: MiBandActivityDao
) : ViewModel() {
    val activities: StateFlow<List<MiBandActivity>> =
        miBandActivityDao.getAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val devices: StateFlow<List<Device>> =
        deviceDao.getAllDevices()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val database = AppDatabase.getDatabase(context)
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                userDao = database.userDao(),
                deviceDao = database.deviceDao(),
                miBandActivityDao = database.miBandActivityDao()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}