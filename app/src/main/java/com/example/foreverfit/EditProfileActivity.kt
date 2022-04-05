package com.example.foreverfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.foreverfit.data.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_addprofile.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.etFirstName
import kotlinx.android.synthetic.main.activity_edit_profile.etLastName
import kotlinx.android.synthetic.main.activity_register.*




class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userViewModel: UserViewModel
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        //Auth
        auth = FirebaseAuth.getInstance()

        val userDao = com.example.foreverfit.data.UserDatabase.getDatabase(application).userDao()
        val repository = UserRepository(userDao)
        val viewModelFactory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        user = intent.getParcelableExtra<User>("user")!!


        tv_fullName.setText(user.firstName + " " + user.lastName)
        etFirstName.setText(user.firstName)
        etLastName.setText(user.lastName)
        et_Email.setText(user.email)
        etAge.setText(user.age)
        etHeight.setText(user.height)
        etWeight.setText(user.weight)

        var userGoal: String? = user.goal
        var spinner = spGoal //id spinnera
        val adapter = ArrayAdapter.createFromResource(this, com.example.foreverfit.R.array.goals_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.setAdapter(adapter);
        setGoalSpinner(userGoal)

        var userActivityLevel: String? = user.activityLevel
        var spinner2 = spActivityLevel //id spinnera
        val adapter2 = ArrayAdapter.createFromResource(this, com.example.foreverfit.R.array.activityLevel_array, android.R.layout.simple_spinner_item)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner2.setAdapter(adapter2);
        setActivitySpinner(userActivityLevel)

        var userGender: String? = user.gender
        var spinner3 = spGender//id spinnera
        val adapter3 = ArrayAdapter.createFromResource(this, com.example.foreverfit.R.array.gender_array, android.R.layout.simple_spinner_item)
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner3.setAdapter(adapter3);
        setGenderSpinner(userGender)

        btnApply.setOnClickListener {
            updateUser()
        }
    }

    private fun updateUser() {
        val firstName = etFirstName.text.toString()
        val lastName = etLastName.text.toString()
        val email = et_Email.text.toString()
        val gender = spGender.getSelectedItem().toString()
        val age = etAge.text.toString()
        val height = etHeight.text.toString()
        val weight = etWeight.text.toString()
        val goal = spGoal.getSelectedItem().toString();
        val activityLevel = spActivityLevel.getSelectedItem().toString();

        if(inputCheck(firstName, lastName, email, gender, age, height, weight, goal, activityLevel)) {
            val updatedUser = User(user.id, firstName, lastName, email, gender, age, height, weight, goal, activityLevel)
            userViewModel.updateUser(updatedUser)

            Toast.makeText(this, "Successfully updated!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@EditProfileActivity, ProfileActivity::class.java))
        } else {
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_LONG).show()
        }
    }
    private fun inputCheck(firstName: String, lastName: String, email: String, gender: String, age: String, height: String,
                           weight: String, goal: String, activityLevel: String): Boolean {
        return !(TextUtils.isEmpty(firstName) ||
                TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(email))
    }

    //Metode spinnera

    private fun setGoalSpinner(userGoal: String?) {
        if(userGoal == "Lose weight")
            spGoal.setSelection(0)
        else if (userGoal == "Maintain weight") {
            spGoal.setSelection(1)
            Log.d("user", "Uso")
        }else if (userGoal == "Gain weight")
            spGoal.setSelection(2)
        else
            spGoal.setSelection(0)
    }

    private fun setActivitySpinner(userActivityLevel: String?) {
        if(userActivityLevel == "Sedentary (little or no exercise)")
            spActivityLevel.setSelection(0)
        else if (userActivityLevel == "Lightly active (exercise 1–3 days/week)")
            spActivityLevel.setSelection(1)
        else if (userActivityLevel == "Moderately active (exercise 3–5 days/week)")
            spActivityLevel.setSelection(2)
        else if (userActivityLevel == "Active (exercise 6–7 days/week)")
            spActivityLevel.setSelection(3)
        else if (userActivityLevel == "Very active (hard exercise 6–7 days/week)")
            spActivityLevel.setSelection(4)
        else
            spActivityLevel.setSelection(0)
    }

    private fun setGenderSpinner(userActivityLevel: String?) {
        if(userActivityLevel == "Male")
            spGender.setSelection(0)
        else if (userActivityLevel == "Female")
            spGender.setSelection(1)
        else
            spGender.setSelection(0)
    }


}