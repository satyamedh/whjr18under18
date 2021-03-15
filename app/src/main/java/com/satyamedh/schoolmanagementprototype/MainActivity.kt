package com.satyamedh.schoolmanagementprototype

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        val WelcomeText = findViewById<TextView>(R.id.LoginWelcomeText)
        WelcomeText.text = "Welcome to " + getString(R.string.app_name) + "\nLogin please!"
        WelcomeText.textSize = 20.0f
        auth = Firebase.auth
        val loginButton = findViewById<Button>(R.id.loginButton)
        val emailBox = findViewById<EditText>(R.id.LoginEditTextUname)
        val passwordBox = findViewById<EditText>(R.id.editTextTextPassword)
        val email = emailBox.text
        val password = passwordBox.text
        loginButton.setOnClickListener { login(email.toString(), password.toString()) }

    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user == null) {
                            Toast.makeText(this, "Login failed. try again", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "LOGIN SUCCESS", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }


    }


}