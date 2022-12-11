package com.example.simplechatapp.Ui.signupActivity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.simplechatapp.Ui.usersActivity.MainActivity
import com.example.simplechatapp.Models.Users
import com.example.simplechatapp.R
import com.example.simplechatapp.Ui.loginActivity.LogonPeresenter
import com.example.simplechatapp.databinding.ActivityLoginBinding
import com.example.simplechatapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() ,SignupInterface{
    private lateinit var rootdata: ActivitySignupBinding
    private  var present: SignupPeresenter = SignupPeresenter(this,this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootdata = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        supportActionBar!!.hide()
        rootdata.signupbtn.setOnClickListener {
            val name= rootdata.nameuser.text.toString()
            val email = rootdata.email.text.toString()
            val password = rootdata.password.text.toString()
            if (name.isEmpty()) Toast.makeText(this,"please enter Name",Toast.LENGTH_LONG).show()
            if (email.isEmpty()) Toast.makeText(this,"please enter Email",Toast.LENGTH_LONG).show()
            if (password.isEmpty()) Toast.makeText(this,"please enter Password",Toast.LENGTH_LONG).show()
            else
            {
                present.signUp(name, email, password)
                val pdialog = ProgressDialog(this)
                pdialog.setTitle("warming")
                pdialog.setMessage("This user is added successfully")
                pdialog.show()
                val progressRunnable = Runnable { pdialog.cancel() }

                val pdCanceller = Handler()
                pdCanceller.postDelayed(progressRunnable, 2000)

            }

        }
    }

    override fun onSignup(name: String, email: String, password: String) {
        rootdata.nameuser.setText(name)
        rootdata.email.setText(email)
        rootdata.password.setText(password)    }
}