package com.satyamedh.schoolmanagementprototype.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.satyamedh.schoolmanagementprototype.R
import com.satyamedh.schoolmanagementprototype.UploadPublishment
import com.satyamedh.schoolmanagementprototype.publishmentAdaptor
import com.satyamedh.schoolmanagementprototype.publishmentHolder


class GalleryFragment : Fragment(){

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var assignView: RecyclerView
    private lateinit var adapter: publishmentAdaptor
    private lateinit var db: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        val db = Firebase.database
        val auth = Firebase.auth
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                val post = dataSnapshot.value
                val lol = post as String
                if (lol == "student"){
                    hidemybutton()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            // ...
        }
        auth.uid?.let { db.reference.child("userType").child(it).addValueEventListener(
            postListener
        ) }

        init(root)
        return root
    }

    fun hidemybutton(){
        view?.findViewById<FloatingActionButton>(R.id.Assign)?.hide()
    }

    fun init(root: View){
        db = Firebase.database.reference.child("assignments")
        //val v = root.findViewById<FloatingActionButton>(R.id.Assign)
        assignView = root.findViewById(R.id.assignView)!!
        assignView.setLayoutManager(LinearLayoutManager(activity));
        root.findViewById<FloatingActionButton>(R.id.Assign)?.setOnClickListener{
            val intent = Intent(context, UploadPublishment::class.java)
            intent.putExtra("type", "assignments")
            startActivity(intent)
        }

        val options: FirebaseRecyclerOptions<publishmentHolder> = FirebaseRecyclerOptions.Builder<publishmentHolder>()
            .setQuery(db, publishmentHolder::class.java)
            .build()

        adapter = publishmentAdaptor(options)
        assignView.adapter = adapter


    }

    override fun onStart() {
        super.onStart()

        adapter.startListening()
    }


    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}