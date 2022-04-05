package com.example.foreverfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.foreverfit.data.*
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_edit_profile.etLastName as etLastName1
import kotlinx.android.synthetic.main.activity_register.etFirstName as etFirstName1

class RegisterActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_register)

        val userDao = com.example.foreverfit.data.UserDatabase.getDatabase(application).userDao()
        val repository = UserRepository(userDao)
        val viewModelFactory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        //Auth
        auth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            registerUser(email, password)
            insertDataToDatabase()
        }

        tv_have_account.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        //Firstname validation
        val firstNameStream = RxTextView.textChanges(etFirstName)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        firstNameStream.subscribe {
            showFirstNameExistAlert(it)
        }

        //Lastname validation
        val lastNameStream = RxTextView.textChanges(etLastName)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        lastNameStream.subscribe {
            showLastNameExistAlert(it)
        }

        //Email validation
        val emailStream = RxTextView.textChanges(etEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }

        //Password validation
        val passwordStream = RxTextView.textChanges(etPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 6
            }
        passwordStream.subscribe {
            showTextMinimalAlert(it)
        }

        //Confirm password validation
        val passwordConfirmStream = Observable.merge(
            RxTextView.textChanges(etPassword)
                .skipInitialValue()
                .map { password ->
                    password.toString() != etConfirmPassword.text.toString()
                },
            RxTextView.textChanges(etConfirmPassword)
                .skipInitialValue()
                .map { confirmPassword ->
                    confirmPassword.toString() != etPassword.text.toString()
                })

        passwordConfirmStream.subscribe {
            showPasswordConfirmAlert(it)
        }

        //Button enable true of false
        val invalidFieldsStream = Observable.combineLatest (
            firstNameStream,
            lastNameStream,
            emailStream,
            passwordStream,
            passwordConfirmStream,
            { firstNameInvalid: Boolean, lastNameInvalid: Boolean, emailInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmInvalid: Boolean ->
                !firstNameInvalid && !lastNameInvalid && !emailInvalid && !passwordInvalid && !passwordConfirmInvalid
            })
        invalidFieldsStream.subscribe { isValid ->
            if (isValid) {
                btn_register.isEnabled = true
                btn_register.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_color)
            } else {
                btn_register.isEnabled = false
                btn_register.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            }
        }
    }

    private fun showFirstNameExistAlert(isNotValid: Boolean) {
        etFirstName.error = if (isNotValid) "You need to enter first name" else null
    }

    private fun showLastNameExistAlert(isNotValid: Boolean) {
        etLastName.error = if (isNotValid) "You need to enter last name" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean) {
        etPassword.error = if (isNotValid) "Password must have at least 8 digits" else null
    }

    private fun showEmailValidAlert(isNotValid: Boolean) {
        etEmail.error = if (isNotValid) "Email is not valid" else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean) {
        etConfirmPassword.error = if (isNotValid) "Passwords must be same" else null
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    Toast.makeText(this, "Register successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun insertDataToDatabase() {
        val firstName = etFirstName.text.toString()
        val lastName = etLastName.text.toString()
        val email = etEmail.text.toString()
        val gender = ""
        val age = ""
        val height = ""
        val weight = ""
        val goal = ""
        val activityLevel = ""

        if(inputCheck(firstName, lastName, email, gender, age, height, weight, goal, activityLevel)) {
            val user = User(0, firstName, lastName, email, gender, age,
                height, weight, goal, activityLevel)
            userViewModel.addUser(user)
            Toast.makeText(this, "Successfully added!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        } else {
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, email: String, gender: String, age: String, height: String, weight: String,
                           goal: String, activityLevel: String): Boolean {
        return !(TextUtils.isEmpty(firstName) ||
                TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(email))
    }
}