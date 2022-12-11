package com.example.simplechatapp.Ui.usersActivity

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplechatapp.Models.Users
import com.example.simplechatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var userList:ArrayList<Users> = ArrayList()
    private var userAdapter: UserAdapter = UserAdapter(this,userList)
    private lateinit var mAuth: FirebaseAuth
    private var mydataref: DatabaseReference =  FirebaseDatabase.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()!!.setBackgroundDrawable( ColorDrawable(getResources().getColor(R.color.background)))
        recyclerView = findViewById(R.id.recycler)
        mAuth = FirebaseAuth.getInstance()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter
        mydataref.child("user").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (i in snapshot.children)
                {
                    val currentuser= i.getValue(Users::class.java)
                    if (mAuth.currentUser!!.uid != currentuser!!.uid)
                    {
                         userList.add(currentuser)

                    }
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== R.id.lougout)
        {
            mAuth.signOut()
            finish()
            return  true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Warming")
        builder.setMessage("Are you sure you want to Logout?")
        builder.setPositiveButton("Yes") { dialog, id ->
            mAuth.signOut()
            finish()
        }
        builder.setNegativeButton("No") { dialog, id ->
            dialog.dismiss()

        }
        val alert: AlertDialog = builder.create()
        alert.setCancelable(false)
        alert.show()
    }
}