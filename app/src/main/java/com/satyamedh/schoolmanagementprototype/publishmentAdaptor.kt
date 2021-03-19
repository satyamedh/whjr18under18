package com.satyamedh.schoolmanagementprototype

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.satyamedh.schoolmanagementprototype.publishmentAdaptor.publishmentViewholder
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel
import java.util.*


public class publishmentAdaptor(
    options: FirebaseRecyclerOptions<publishmentHolder>
) : FirebaseRecyclerAdapter<publishmentHolder, publishmentViewholder>(options) {
    override fun onBindViewHolder(
        holder: publishmentViewholder,
        position: Int, model: publishmentHolder
    ) {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        holder.date.setText(model.date)
        holder.publishment.setText(model.publishment)
        holder.attachment.setOnClickListener{
            val website = URL(model.attachmentLink)
            val rbc: ReadableByteChannel = Channels.newChannel(website.openStream())
            val fos = FileOutputStream(
                Environment.getExternalStorageDirectory()
                    .getPath() + "/Download/" + model.publishment + ".pdf"
            )
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE)
            Snackbar.make(holder.itemView, "Saved PDF to your device's downloads folder", Snackbar.LENGTH_LONG).show()
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): publishmentViewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.announceandassignment, parent, false)
        return publishmentViewholder(view)
    }
    inner class publishmentViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView
        var publishment: TextView
        var attachment: ImageButton

        init {
            date = itemView.findViewById(R.id.date)
            publishment = itemView.findViewById(R.id.mainText)
            attachment = itemView.findViewById(R.id.download_attachment)
        }
    }


}