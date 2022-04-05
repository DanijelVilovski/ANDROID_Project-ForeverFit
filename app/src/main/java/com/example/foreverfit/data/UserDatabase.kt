package com.example.foreverfit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foreverfit.converters.FoodConverter
import com.example.foreverfit.converters.NutrientConverter
import com.example.foreverfit.converters.NutritionConverter
import com.example.foreverfit.converters.ServingConverter

@Database(entities = [User::class, Food::class, SavedFood::class], version = 1, exportSchema = false)
@TypeConverters(NutritionConverter::class, ServingConverter::class, NutrientConverter::class)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}