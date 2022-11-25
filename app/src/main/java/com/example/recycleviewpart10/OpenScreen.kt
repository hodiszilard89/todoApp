package com.example.recycleviewpart10


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.TextView
import androidx.core.graphics.rotationMatrix
import kotlinx.coroutines.delay
import java.util.*
import kotlin.concurrent.timerTask

class OpenScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.open_screen)
        supportActionBar?.hide()

//        Handler().postDelayed({
//            val intent = Intent(this@OpenScreen, MainActivity::class.java)
//            startActivity(intent)
//        },2800)

        Timer().schedule(timerTask {
            val intent = Intent(this@OpenScreen, MainActivity::class.java)
            startActivity(intent)
        }, 3000)

        val tv1 = findViewById<TextView>(R.id.tv_1)
        val tv2 = findViewById<TextView>(R.id.tv_2)
        val tv3 = findViewById<TextView>(R.id.tv_3)
        val tv4 = findViewById<TextView>(R.id.tv_4)

        val anim1 = loadAnimation(this, R.anim.fade_in)

        tv1.startAnimation(anim1)
        tv2.startAnimation(anim1)
        tv3.startAnimation(anim1)
        tv4.startAnimation(anim1)
    }
}