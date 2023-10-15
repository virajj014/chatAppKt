package com.example.chatapplive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplive.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private var receiverId: String? = null
    private var senderId: String? = null
    private lateinit var databaseReferenceSender: DatabaseReference
    private lateinit var databaseReferenceReciever: DatabaseReference
    var messageAdapter : MessageAdapter?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        receiverId = intent.getStringExtra("id")
        senderId = FirebaseAuth.getInstance().uid


        val senderRoom = "$senderId$receiverId"
        val receiverRoom = "$receiverId$senderId"

//        println("RECIEVER ROOM FROM CHATACTIVITY  ${recieverRoom}")
//        println("SENDER ROOM FROM CHATACTIVITY  ${senderRoom}")


        messageAdapter = MessageAdapter(this)

        binding?.recycler?.adapter = messageAdapter
        binding?.recycler?.layoutManager = LinearLayoutManager(this)

        databaseReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom)
        databaseReferenceReciever = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);


        databaseReferenceSender.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageAdapter!!.clear()
                for (dataSnapshot in snapshot.children) {
                    val messageModel = dataSnapshot.getValue(MessageModel::class.java)
                    if (messageModel != null) {
                        messageAdapter!!.add(messageModel)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.sendMessage.setOnClickListener {
            val message = binding.messageEd.text.toString().trim()
            if (message.length > 0) {
                sendMessage(message)
            }
        }
    }

    private fun sendMessage(message: String) {
        val messageId = UUID.randomUUID().toString()
        val messageModel = MessageModel(messageId, FirebaseAuth.getInstance().uid, message)
        messageAdapter?.add(messageModel)
        databaseReferenceSender.child(messageId).setValue(messageModel)
        databaseReferenceReciever.child(messageId).setValue(messageModel)

    }
}