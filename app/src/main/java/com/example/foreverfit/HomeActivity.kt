package com.example.foreverfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.foreverfit.api.RetrofitInstance
import com.example.foreverfit.data.*
import com.example.foreverfit.utils.Constants.Companion.API_KEY
import com.example.recipeapp.adapter.FoodAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_addprofile.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.bottom_nav
import kotlinx.android.synthetic.main.rv_search.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var userViewModel: UserViewModel
    private lateinit var user: User
    private var totalCalories: Int = 0
    private var leftCalories: Int = 0
    private var totalProteins: Double = 0.0
    private var totalCarbs: Double = 0.0
    private var totalFats: Double = 0.0
    private lateinit var pie: Pie
    var nutrients = ArrayList<String>()
    var values = ArrayList<Int>()
    var array = ArrayList<Int>()
    var dataEntries = ArrayList<DataEntry>()
    private var x: Int = 0
    private lateinit var anyChartView: AnyChartView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //FoodAdapter
        val recyclerView: RecyclerView = findViewById(R.id.rv_recyclerView)
        foodAdapter = FoodAdapter()
        foodAdapter.setClickListener(onClickedDelete)
        recyclerView.adapter = foodAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //SearchAdapter
        val searchRecyclerView: RecyclerView = findViewById(R.id.rv_searchRecyclerView)
        searchAdapter = SearchAdapter()
        searchAdapter.setClickListener(onClickedFood)
        searchRecyclerView.adapter = searchAdapter
        searchRecyclerView.layoutManager = LinearLayoutManager(this)

        //Auth
        auth = FirebaseAuth.getInstance()

        val userDao = com.example.foreverfit.data.UserDatabase.getDatabase(application).userDao()
        val repository = UserRepository(userDao)
        val viewModelFactory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        calculateCalories()

        bottom_nav.setOnNavigationItemSelectedListener {item ->
            when(item.itemId) {
                R.id.ic_profile -> {
                    startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
                }
                R.id.ic_logout -> {
                    auth.signOut()
                    Intent(this, LoginActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            false
        }

        //PieChart
        anyChartView = findViewById<AnyChartView>(R.id.pieChart)

        //SearchView
        svSearchHome.isSubmitButtonEnabled = true
        svSearchHome.setOnQueryTextListener(this)

        btnDeleteAll.setOnClickListener {
            deleteAllFood()
        }
    }

    private fun searchFood(query: String) {
        val call = RetrofitInstance.api.getFoodByName(query, true, 1,   API_KEY)
        call.enqueue(object: Callback<Result> {

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(this@HomeActivity, t.message, Toast.LENGTH_SHORT)
                    .show()
                Log.d("eror", t.message.toString())
            }

            override fun onResponse(
                call: Call<Result>,
                response: Response<Result>
            ) {
                if (response.body() != null)
                    searchAdapter.setData(response.body()!!.products)
            }
        })
    }

    private val onClickedFood = object: SearchAdapter.OnItemClickListener {
        override fun onClicked(food: Food, quantity: Double, kcal: Double, pr: Double, ch: Double, f: Double) {
            customizeInfo(food, quantity, kcal, pr, ch, f)
            rv_searchRecyclerView.isInvisible = true
            searchAdapter.clear()
        }
    }

    private val onClickedDelete = object: FoodAdapter.OnItemClickListener {
        override fun onClicked(food: SavedFood) {
            userViewModel.deleteSavedFood(food)
        }
    }

    private fun deleteAllFood() {
        userViewModel.deleteAllSavedFood(user.email)
        foodAdapter.clear()

        totalProteins = 0.0
        totalCarbs = 0.0
        totalFats = 0.0
        leftCalories = totalCalories

        tvCaloriesLeft.text = "Left: " + totalCalories + "kcal"
        tvProteins.text = "Proteins: " + totalProteins + "g"
        tvCarbs.text = "Carbs: " + totalCarbs + "g"
        tvFats.text = "Fats: " + totalFats + "g"

    }

    private fun customizeInfo(food: Food, quantity: Double, kcal: Double, pr: Double, ch: Double, f: Double) {
        val multiplier: Double = quantity / food.servings.size.toString().toDouble()
        val newKcal = String.format("%.1f", kcal * multiplier).toDouble()
        val newP = String.format("%.1f", pr * multiplier).toDouble()
        val newCH = String.format("%.1f", ch * multiplier).toDouble()
        val newF = String.format("%.1f", f * multiplier).toDouble()

        val newFood = SavedFood(
            0, food.title, food.image, Nutrition(
                listOf(
                    Nutrient("calories", newKcal),
                    Nutrient("proteins", newP),
                    Nutrient("carbohydrates", newCH),
                    Nutrient("fats", newF)
                )
            ), Serving(quantity, "g"), user.email
        )

        foodAdapter.addData(newFood)
        userViewModel.addSavedFood(newFood)
        //Log.d("array1", foodAdapter.arrFood.toString())

        leftCalories -= newKcal.toInt()
        totalProteins += newP
        totalCarbs += newCH
        totalFats += newF

        Log.d("n1", totalProteins.toString())
        Log.d("n1", totalCarbs.toString())
        Log.d("n1", totalFats.toString())

        //setupPieChart()

        tvCaloriesLeft.text = "Left: " + leftCalories + "kcal"
        tvProteins.text = "Proteins: " + totalProteins + "g"
        tvCarbs.text = "Carbs: " + totalCarbs + "g"
        tvFats.text = "Fats: " + totalFats + "g"

        updatePieChart()
    }

    private fun updatePieChart() {

        values.clear()

        values.add(totalCarbs.toInt())
        values.add(totalProteins.toInt())
        values.add(totalFats.toInt())

        dataEntries.clear()

        Log.d("pie", dataEntries.size.toString())
        for(i in array) {
            dataEntries.add(ValueDataEntry(nutrients[i], values[i]))
        }

       // pie.data()

        pie.data(dataEntries)
        //anyChartView.setChart(pie)
    }

    private fun calculateCalories() {
        //var totalCalories: Int = 0
        userViewModel.readAllData.observe(this, Observer { users ->

            user = users.find { it.email == auth.currentUser?.email!!}!!
            if (user != null) {
                var BMR: Double = 0.0 //Basal metabolic rate

                if(user.gender == "Male") {
                    BMR = 66.47 + (13.75 * Integer.parseInt(user.weight)) + (5.003 * Integer.parseInt(user.height)) - (6.755 * Integer.parseInt(user.age))
                } else if (user.gender == "Female") {
                    BMR = 655.1 + (9.563 * Integer.parseInt(user.weight)) + (1.850 * Integer.parseInt(user.height)) - (4.676 * Integer.parseInt(user.age))
                }
                var AMR: Double = calculateAMR(BMR)

                if(user.goal == "Lose weight") {
                    totalCalories = AMR.toInt() - 300
                } else if(user.goal == "Maintain weight") {
                    totalCalories = AMR.toInt()
                } else if(user.goal == "Gain weight") {
                    totalCalories = AMR.toInt() + 300
                }
                leftCalories = totalCalories


                userViewModel.readAllSavedFood.observe(this, Observer { savedFood ->
                    var foodForUser: List<SavedFood> = savedFood.filter { it.useremail == auth.currentUser?.email!! }
                    Log.d("array1", foodForUser.toString())
                    foodAdapter.clear()
                    for(i in foodForUser) {
                        foodAdapter.addData(i)
                        leftCalories -= i.nutrition.nutrients[0].amount.toInt()
                        totalProteins += i.nutrition.nutrients[1].amount
                        totalCarbs += i.nutrition.nutrients[2].amount
                        totalFats += i.nutrition.nutrients[3].amount
                    }
                    Log.d("array", foodAdapter.arrFood.toString())
                    tvCalories.text = "Total: " + totalCalories.toString() + "kcal"
                    tvCaloriesLeft.text = "Left: " + leftCalories.toString() + "kcal"
                    tvProteins.text = "Proteins: " + totalProteins
                    tvCarbs.text = "Carbs: " + totalCarbs
                    tvFats.text = "Fats: " + totalFats
                    if(totalCalories == 0)
                        Toast.makeText(this, "Please fill out your information on profil page!", Toast.LENGTH_LONG).show()



                    setupPieChart()
                })



            }
        })

    }

    private fun calculateAMR(BMR: Double) :Double {

        var AMR: Double = 0.0

        if(user.activityLevel == "Sedentary (little or no exercise)") {
            AMR = BMR * 1.2
        } else if(user.activityLevel == "Lightly active (exercise 1–3 days/week)") {
            AMR = BMR * 1.375
        } else if(user.activityLevel == "Moderately active (exercise 3–5 days/week)") {
            AMR = BMR * 1.55
        } else if(user.activityLevel == "Active (exercise 6–7 days/week)") {
            AMR = BMR * 1.725
        } else if(user.activityLevel == "Very active (hard exercise 6–7 days/week)") {
            AMR = BMR * 1.9
        }
        return AMR
    }

    private fun setupPieChart() {
        if(x == 0) {

            nutrients.add("Carbs")
            nutrients.add("Proteins")
            nutrients.add("Fats")

            values.add(totalCarbs.toInt())
            values.add(totalProteins.toInt())
            values.add(totalFats.toInt())
            array.add(0)
            array.add(1)
            array.add(2)

            Log.d("blabla", totalProteins.toString())
            Log.d("n2", totalCarbs.toString())
            Log.d("n2", totalFats.toString())

            pie = AnyChart.pie()

            for(i in array) {
                dataEntries.add(ValueDataEntry(nutrients[i], values[i]))
            }

            pie.data(dataEntries)
            anyChartView.setChart(pie)
        }
        x++



    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            rv_searchRecyclerView.isInvisible = false
            searchFood(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}