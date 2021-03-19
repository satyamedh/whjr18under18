package com.satyamedh.schoolmanagementprototype

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*


class UploadPublishment : AppCompatActivity() {

    private lateinit var fileUri: Uri
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_publishment)

        init()
        context = this

    }

    fun init(){
        val button = findViewById<Button>(R.id.button)
        val editText = findViewById<EditText>(R.id.description)
        val chooseafile = findViewById<Button>(R.id.chooseafile)

        chooseafile.setOnClickListener{
            getPDF()
        }

        button.setOnClickListener{
            if (editText.text.isEmpty()){
                Toast.makeText(this, "Please enter description", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (!(this::fileUri.isInitialized)){
                Toast.makeText(this, "Please choose PDF file", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val storage = Firebase.storage
            val ref = storage.reference.child(intent?.extras?.get("type") as String).child(editText.text.toString())
            ref.putFile(fileUri).addOnSuccessListener {
                val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

                val db = Firebase.database
                val ref2 = db.reference.child(intent?.extras?.get("type") as String)
                val key = ref2.push()
                var hashMap : HashMap<String, String>
                        = HashMap<String, String> ()
                hashMap.put("date", currentDate)
                hashMap.put("publishment", editText.text.toString())
                var downloadLink: Uri?
                ref.downloadUrl.addOnSuccessListener {
                    downloadLink = it
                    hashMap.put("attachmentLink", downloadLink.toString())

                    val auth = Firebase.auth
                    val postListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // Get Post object and use the values to update the UI
                            val post = dataSnapshot.value
                            val lol = post as String
                            key.updateChildren(hashMap as Map<String, Any>)
                            Toast.makeText(context, "Successfully Published!", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, MasterActivity::class.java)
                            intent.putExtra("userType", lol)
                            startActivity(intent)
                            finish()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                        // ...
                    }
                    db.reference.child("userType").child(auth.uid.toString()).addValueEventListener(postListener)






                }


            }


            //val ref3 = ref2.child(key.toString())


        }


    }

    private fun getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:$packageName"))
            startActivity(intent)
            return
        }

        //creating an intent for file chooser
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), 769)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 769 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                this.fileUri = data.data!!
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }

        }


}