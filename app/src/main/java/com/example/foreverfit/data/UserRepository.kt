package com.example.foreverfit.data

import androidx.lifecycle.LiveData
import com.example.foreverfit.ProfileActivity
import com.example.foreverfit.api.RetrofitInstance
import com.example.foreverfit.utils.Constants.Companion.API_KEY
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllUsers()
    val readAllSavedFood: LiveData<List<SavedFood>> = userDao.readAllSavedFood()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun addSavedFood(food: SavedFood) {
        userDao.addSavedFood(food)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteSavedFood(food: SavedFood) {
        userDao.deleteSavedFood(food)
    }

    suspend fun deleteAllSavedFood(email: String) {
        userDao.deleteAllSavedFood(email)
    }
}