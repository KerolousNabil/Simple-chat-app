package com.example.simplechatapp.Ui.loginActivity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.simplechatapp.Ui.usersActivity.MainActivity
import com.example.simplechatapp.R
import com.example.simplechatapp.Ui.signupActivity.Signup
import com.example.simplechatapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() , LoginInterface {
    private lateinit var rootdata:ActivityLoginBinding
    private  var present: LogonPeresenter = LogonPeresenter(this , this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide()
        rootdata = DataBindingUtil.setContentView(this, R.layout.activity_login)
        rootdata.signbtn.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)

        }
        rootdata.loginbtn.setOnClickListener {

            val email = rootdata.email.text.toString()
            val password = rootdata.password.text.toString()
            if (email.isEmpty()) { Toast.makeText(this,"please enter Email",Toast.LENGTH_LONG).show() }
            if (password.isEmpty()) { Toast.makeText(this,"please enter Password",Toast.LENGTH_LONG).show() }
            else
            {
                val pdialog = ProgressDialog(this)
                pdialog.setTitle("warming")
                pdialog.setMessage("Login is done successfully")
                pdialog.show()
                val progressRunnable = Runnable { pdialog.cancel() }

                val pdCanceller = Handler()
                pdCanceller.postDelayed(progressRunnable, 2000)
                present.login(email, password)
            }
        }


    }
    override fun onLogin(email: String, password: String) {

        rootdata.email.setText(email)
        rootdata.password.setText(password)

    }
}