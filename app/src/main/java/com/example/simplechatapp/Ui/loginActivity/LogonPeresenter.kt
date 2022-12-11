package com.example.simplechatapp.Ui.loginActivity

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.simplechatapp.Ui.usersActivity.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LogonPeresenter {

    lateinit var view:LoginInterface
    lateinit var mycxt:Context
    constructor(view:LoginInterface , mycnt:Context )
    {
        this.view = view
        this.mycxt = mycnt

    }

     private var mAuth:FirebaseAuth =  FirebaseAuth.getInstance()


    fun login(email: String,password: String)
{
    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
        task->
        if (task.isSuccessful) {
            //   finish()
            val intent = Intent(mycxt,MainActivity::class.java)
            mycxt.startActivity(intent)
        } else {
            Toast.makeText(mycxt,"ERROR", Toast.LENGTH_LONG).show()

        }
    }

    view.onLogin(email, password)
}
}