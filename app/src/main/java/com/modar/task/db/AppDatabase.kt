package com.modar.task.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.modar.task.model.main.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}