package com.example.foreverfit.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class UserViewModel(application: Application, private var repository: UserRepository) : AndroidViewModel(application) {

    val readAllData: LiveData<List<User>>
    val readAllSavedFood: LiveData<List<SavedFood>>
    lateinit var user: User
    val myResponse: MutableLiveData<Response<List<Food>>> = MutableLiveData()

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
        readAllSavedFood = repository.readAllSavedFood
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun addSavedFood(food: SavedFood) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSavedFood(food)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    fun deleteSavedFood(food: SavedFood) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSavedFood(food)
        }
    }

    fun deleteAllSavedFood(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllSavedFood(email)
        }
    }
}