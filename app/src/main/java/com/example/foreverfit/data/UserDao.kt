package com.example.foreverfit.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSavedFood(food: SavedFood)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM saved_food_table")
    fun readAllSavedFood(): LiveData<List<SavedFood>>

    @Query("SELECT * FROM user_table WHERE email LIKE :searchQuery")
    fun getUser(searchQuery: String): User

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteSavedFood(food: SavedFood)

    @Query("DELETE FROM saved_food_table WHERE useremail LIKE :email")
    suspend fun deleteAllSavedFood(email: String)

}