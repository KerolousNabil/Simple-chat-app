package com.example.simplechatapp.Ui.chatActivity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplechatapp.Models.Message
import com.example.simplechatapp.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private lateinit var imageURI : Uri
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var chatRecycler:RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sendButton:ImageView
    private lateinit var uploadButton:ImageView
    private lateinit var messageList:ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var myDB : DatabaseReference

    var senderRoom:String? =null
    var receverRoom:String? =null
    val senderUid = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        getSupportActionBar()!!.setBackgroundDrawable( ColorDrawable(getResources().getColor(R.color.background)))

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        myDB = FirebaseDatabase.getInstance().getReference()
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        senderRoom = receiverUid + senderUid
        receverRoom = senderUid + receiverUid
        supportActionBar!!.title = "\t\t$name"



        chatRecycler = findViewById(R.id.chatrecycler)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sent)
        uploadButton = findViewById(R.id.upload)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)
        chatRecycler.layoutManager = LinearLayoutManager(this)
        chatRecycler.adapter = messageAdapter

        myDB.child("chats").child(senderRoom!!).child("message")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (i in snapshot.children)
                    {
                        val message = i.getValue(Message::class.java)
                        messageList.add(message!!)

                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        sendButton.setOnClickListener {

                val message = messageBox.text.toString()
                val messageObject = Message(message, senderUid,img = "null")
                myDB.child("chats").child(senderRoom!!).child("message").push()
                    .setValue(messageObject).addOnSuccessListener {
                        myDB.child("chats").child(receverRoom!!).child("message").push()
                            .setValue(messageObject)
                    }
                messageBox.setText("")


        }

        uploadButton.setOnClickListener {
            GlobalScope.launch {
              launchGallery()
              uploadImage()

            }

        }


    }
    private suspend fun launchGallery() {
        delay(500)
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            imageURI = data.data!!
            try {

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addUploadRecordToDb(uri: String){
        val messageObject = Message(message = "null", senderUid , img = uri)

        myDB.child("chats").child(senderRoom!!).child("message").push()
            .setValue(messageObject).addOnSuccessListener {
                myDB.child("chats").child(receverRoom!!).child("message").push()
                    .setValue(messageObject)
            }

    }

    private suspend fun uploadImage(){
        delay(3000)
        if(imageURI != null){
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(imageURI!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadRecordToDb(downloadUri.toString())
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }

    }




}