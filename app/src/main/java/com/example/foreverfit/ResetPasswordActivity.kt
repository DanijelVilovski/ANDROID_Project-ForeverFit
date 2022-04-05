package com.example.foreverfit

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_reset_password.*

@SuppressLint
class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        //Auth
        auth = FirebaseAuth.getInstance()

        //Email validation
        val emailStream = RxTextView.textChanges(et_email)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }

        //Reset Password
        btn_reset_pw.setOnClickListener {
            val email = et_email.text.toString().trim()

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this) { reset ->
                    if (reset.isSuccessful) {
                        Intent(this, LoginActivity::class.java). also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            Toast.makeText(this, "Check your email to set your new password", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, reset.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }

        //Click

        tv_back_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun showEmailValidAlert(isNotValid: Boolean) {
        if (isNotValid) {
            et_email.error = "Email is not valid"
            btn_reset_pw.isEnabled = false
            btn_reset_pw.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
        } else {
            et_email.error = null
            btn_reset_pw.isEnabled = true
            btn_reset_pw.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_color)
        }
    }
}