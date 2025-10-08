package com.example.mynewapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE id = :userId")
    fun getUserById(userId: Int): Flow<User>
}
