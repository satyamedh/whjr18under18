package com.satyamedh.schoolmanagementprototype

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressDialog
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private lateinit var usertype: String

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

        database = FirebaseDatabase.getInstance()
        myRef = database!!.getReference()

        progressBar = ProgressDialog(this)
        progressBar.setCancelable(false)

        progressBar.setMessage("Logging in")
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressBar.setProgressPercentFormat(null)
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER)

        loginButton.setOnClickListener { login(emailBox, passwordBox) }


        val context = this

        if (auth.currentUser != null) {
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.value
                    val lol = post as String

                    val intent = Intent(context, MasterActivity::class.java)
                    intent.putExtra("userType", lol)
                    startActivity(intent)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
                // ...
            }

            val user = auth.currentUser
            myRef.database.reference.child("userType").child(user!!.uid)
                .addValueEventListener(postListener)
        }



    }

    private fun login(emailBox: EditText, passwordBox: EditText) {
        val context = this
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
                                val postListener = object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        // Get Post object and use the values to update the UI
                                        val post = dataSnapshot.value
                                        usertype = post as String

                                        val intent = Intent(context, MasterActivity::class.java)
                                        intent.putExtra("userType", usertype)
                                        startActivity(intent)
                                        // ...
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Getting Post failed, log a message
                                    }
                                }
                                myRef?.child("userType")?.child(user.uid)?.addValueEventListener(postListener)

                            }
                        } else {
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

        }
    }

    override fun onStart() {
        super.onStart()
        val context = this
        if (auth.currentUser != null) {
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.value
                    val lol = post as String

                    val intent = Intent(context, MasterActivity::class.java)
                    intent.putExtra("userType", lol)
                    startActivity(intent)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
                // ...
            }

            val user = auth.currentUser
            myRef?.database?.reference?.child("userType").child(user.uid).addValueEventListener(postListener)
        }
    }


}