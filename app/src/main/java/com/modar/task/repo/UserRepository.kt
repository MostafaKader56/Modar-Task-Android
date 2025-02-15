package com.modar.task.repo

import com.modar.task.db.UserDao
import com.modar.task.model.main.User
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun getAllUsers() = try {
        userDao.getAllUsers()
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }

    suspend fun insert(user: User) = try {
        userDao.insert(user)
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }

    suspend fun delete(user: User) = try {
        userDao.delete(user)
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }

    suspend fun deleteAllUsers() = try {
        userDao.deleteAllUsers()
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}
