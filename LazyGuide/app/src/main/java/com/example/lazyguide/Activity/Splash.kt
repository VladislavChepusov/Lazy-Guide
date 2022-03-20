package com.example.lazyguide.Activity
/*
Для загрузочного активити Задержка чтобы все подгрузилось и чтобы посмотреть на заставку*/
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.lazyguide.R
class Splash : AppCompatActivity(){
     // This is the loading time of the splash screen
     private val SPLASH_TIME_OUT:Long = 3000 // 1 sec
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_splash)
         Handler().postDelayed({
             startActivity(Intent(this, MainActivity::class.java))

             finish()
         }, SPLASH_TIME_OUT)
     }
}