package com.example.foreverfit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foreverfit.data.User
import com.example.foreverfit.data.UserRepository
import com.example.foreverfit.data.UserViewModel
import com.example.foreverfit.data.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_addprofile.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class ProfileActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userViewModel: UserViewModel
    private lateinit var user: User
    private lateinit var e_mail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addprofile)

        //Auth
        auth = FirebaseAuth.getInstance()
        e_mail= auth.currentUser?.email!!

        //User
        val userDao = com.example.foreverfit.data.UserDatabase.getDatabase(application).userDao()
        val repository = UserRepository(userDao)
        val viewModelFactory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        val bottom_nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottom_nav.setOnNavigationItemSelectedListener {item ->
            when(item.itemId) {
                R.id.ic_home -> {
                    startActivity(Intent(this@ProfileActivity, HomeActivity::class.java))
                }
                R.id.ic_logout -> {
                    startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                }
            }
            false
        }

        getUserInfo()

        tv_edit_profile.setOnClickListener{
            var intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    fun getUserInfo() {
        userViewModel.readAllData.observe(this, Observer { users ->
            user = users.find { it.email == e_mail}!!
            //Log.d("user", users.toString())
            if (user != null) {
                val firstName = user.firstName
                val lastName = user.lastName
                val email = user.email
                val gender = user.gender
                val age = user.age
                val height = user.height
                val weight = user.weight
                val goal = user.goal
                val activityLevel = user.activityLevel

                tv_fullname.setText(user.firstName + " " + user.lastName)
                tv_firstname.text = firstName
                tv_lastname.text = lastName
                tv_email.text = email
                tv_gender.text = gender
                tv_age.text = age.toString()
                tv_height.text = height.toString()
                tv_weight.text = weight.toString()
                tv_goal.text = goal
                tv_activityLevel.text = activityLevel
            }
        })
    }


}