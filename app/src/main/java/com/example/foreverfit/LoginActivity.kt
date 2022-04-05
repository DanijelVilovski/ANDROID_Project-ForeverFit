package com.example.foreverfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.foreverfit.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_login)

        //Auth
        auth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            loginUser(email, password)
        }

        tvHaventAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        tv_forgot_pw.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        //Email validation
        val emailStream = RxTextView.textChanges(et_email)
            .skipInitialValue()
            .map { email ->
                email.isEmpty()
            }
        emailStream.subscribe {
            showTextMinimalAlert(it, "Email")
        }

        //Password validation
        val passwordStream = RxTextView.textChanges(et_password)
            .skipInitialValue()
            .map { password ->
                password.isEmpty()
            }
        passwordStream.subscribe {
            showTextMinimalAlert(it, "Password")
        }

        //Button enable true of false
        val invalidFieldsStream = Observable.combineLatest(
            emailStream,
            passwordStream,
            { emailInvalid: Boolean, passwordInvalid: Boolean ->
                !emailInvalid && !passwordInvalid
            })
        invalidFieldsStream.subscribe { isValid ->
            if (isValid) {
                btn_login.isEnabled = true
                btn_login.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_color)
            } else {
                btn_login.isEnabled = false
                btn_login.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            }
        }
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text == "Email")
            et_email.error = if (isNotValid) "You need to enter email" else null
        else if (text == "Password")
            et_password.error = if (isNotValid) "You need to enter password" else null
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { login ->
                if (login.isSuccessful) {
                    Intent(this, HomeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, login.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}