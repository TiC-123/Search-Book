package com.example.tubesppb.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.tubesppb.R
import com.example.tubesppb.presenter.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        val start = findViewById<Button>(R.id.buttonMulai)
        val name = sharedPreferences.getString("NAME_KEY","User")

        Toast.makeText(this, "Selamat Datang, " + name, Toast.LENGTH_LONG).show()

        start.setOnClickListener {
            doAsync {
                if (!Request().checkConnection(this@FirstActivity)) {
                    uiThread {
                        Toast.makeText(this@FirstActivity, "No Internet Connection!!!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val intent = Intent(this@FirstActivity, MainActivity::class.java)

                    uiThread {
                        startActivity(intent)
                    }
                }
            }
        }
    }
}