package com.example.simplechatapp.Ui.chatActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplechatapp.Models.Message
import com.example.simplechatapp.R
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(var mycontext : Context, private val messageList : ArrayList<Message>) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECIEVE=1
    val ITEM_SENT=2

    class SentViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
    {
        val sentMessage: TextView = itemView.findViewById(R.id.txt_sent_message)
        val leanerphoto :LinearLayout = itemView.findViewById(R.id.leanerphoto)
        val image:ImageView = itemView.findViewById(R.id.imgchat)

    }
    class RecieveViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
    {
        val recieveMessage: TextView = itemView.findViewById(R.id.txt_recieve_message)
        val leanerphoto :LinearLayout = itemView.findViewById(R.id.leanerphoto)
        val image:ImageView = itemView.findViewById(R.id.imgchat)


    }


    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser!!.uid == currentMessage.senderid) {
            return  ITEM_SENT
        } else {
            return ITEM_RECIEVE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==1)
        {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recieve_layout,parent , false)
            return RecieveViewHolder(itemView)
        }
        else
        {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sent_layout,parent , false)
            return SentViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java)
        {

            val viewHolder = holder as SentViewHolder

            if (currentMessage.img=="null"&&currentMessage.message!="null")
            {
                holder.leanerphoto.visibility = View.GONE
                holder.sentMessage.text = currentMessage.message



            }
            else if (currentMessage.img!="null" && currentMessage.message=="null")
            {
                holder.leanerphoto.visibility = View.VISIBLE
                Glide.with(mycontext).load(currentMessage.img).into(holder.image)
                holder.sentMessage.text = ""

            }
            else if (currentMessage.img!="null" && currentMessage.message!="null")
            {
                holder.leanerphoto.visibility = View.VISIBLE
                Glide.with(mycontext).load(currentMessage.img).into(holder.image)
                holder.sentMessage.text = currentMessage.message


            }
        }
        else
        {
            val viewHolder = holder as RecieveViewHolder
            holder.recieveMessage.text = currentMessage.message
            if (currentMessage.img=="null"&&currentMessage.message!="null")
            {
                holder.leanerphoto.visibility = View.GONE
                holder.recieveMessage.text = currentMessage.message



            }
            else if (currentMessage.img!="null" && currentMessage.message=="null")
            {
                holder.leanerphoto.visibility = View.VISIBLE
                Glide.with(mycontext).load(currentMessage.img).into(holder.image)
                holder.recieveMessage.text = ""

            }
            else if (currentMessage.img!="null" && currentMessage.message!="null")
            {
                holder.leanerphoto.visibility = View.VISIBLE
                Glide.with(mycontext).load(currentMessage.img).into(holder.image)
                holder.recieveMessage.text = currentMessage.message


            }

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}