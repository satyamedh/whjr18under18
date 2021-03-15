package com.satyamedh.schoolmanagementprototype

import android.annotation.SuppressLint
import android.app.ProgressDialog
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
    private lateinit var progressBar: ProgressDialog

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

        progressBar = ProgressDialog(this)
        progressBar.setCancelable(false)

        progressBar.setMessage("Logging in")
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressBar.setProgressPercentFormat(null)
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER)

        loginButton.setOnClickListener { login(emailBox, passwordBox) }

    }

    private fun login(emailBox: EditText, passwordBox: EditText) {
        val email = emailBox.text
        val password = passwordBox.text
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill in all the details", Toast.LENGTH_LONG).show()
            return
        }else {
            progressBar.show()
            auth.signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener(this) { task ->
                        progressBar.hide()
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


}