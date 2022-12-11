package com.example.simplechatapp.Ui.signupActivity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.simplechatapp.Models.Users
import com.example.simplechatapp.Ui.usersActivity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.logging.Handler

class SignupPeresenter {
    private lateinit var mydataref: DatabaseReference
    private var mAuth:FirebaseAuth =  FirebaseAuth.getInstance()
    lateinit var view: SignupInterface
    lateinit var mycxt: Context
    constructor(view: SignupInterface, mycnt: Context)
    {
        this.view = view
        this.mycxt = mycnt

    }

    private fun addUsertoDatabase(name: String, email: String, uid: String?) {
        mydataref = FirebaseDatabase.getInstance().reference
        mydataref.child("user").child(uid!!).setValue(Users(name,email,uid))


    }
     fun signUp(name:String , email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if(name.isEmpty()|| email.isEmpty() || password.isEmpty())
                    {
                        Toast.makeText(mycxt,"please complete data", Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        addUsertoDatabase(name, email, mAuth.currentUser!!.uid)


                    }
                    var intent = Intent(mycxt, MainActivity::class.java)
                    mycxt.startActivity(intent)
                } else {
                    Toast.makeText(mycxt,"Some error occurred", Toast.LENGTH_LONG).show()
                }
            }
        view.onSignup(name, email, password)
    }


}