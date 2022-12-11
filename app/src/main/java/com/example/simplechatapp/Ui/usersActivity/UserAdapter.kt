package com.example.simplechatapp.Ui.usersActivity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simplechatapp.Models.Users
import com.example.simplechatapp.R
import com.example.simplechatapp.Ui.chatActivity.ChatActivity

class UserAdapter (var mycontext : Context, private val userList : ArrayList<Users>) : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
          val username: TextView = itemView.findViewById(R.id.nameofEmail)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.users_layout,parent , false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user : Users = userList[position]
        holder.username.text = user.name
        holder.itemView.setOnClickListener {
            val intent = Intent(mycontext, ChatActivity::class.java)
            intent.putExtra("name",user.name)
            intent.putExtra("uid",user.uid)
            mycontext.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }
}