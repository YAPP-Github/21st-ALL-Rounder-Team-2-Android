package com.yapp.gallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.yapp.gallery.home.screen.HomeActivity
import com.yapp.gallery.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startLogin()
    }

    @TestOnly
    private fun startLogin(){
        Log.e("current User", auth.currentUser?.uid.toString())
        auth.currentUser?.let {
            startActivity(Intent(this, HomeActivity::class.java))
        } ?: run {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        //startActivity(Intent(this, LoginActivity::class.java))

    }

}
