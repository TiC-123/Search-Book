package com.example.tubesppb.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import android.text.Editable

import android.text.TextWatcher
import com.example.tubesppb.R


class SettingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val nama = findViewById<EditText>(R.id.edit3)
        val tombol=findViewById<Switch>(R.id.switch1)
        val jumlah = findViewById<EditText>(R.id.editview2)

        loadData()
        nama.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                // you can call or do what you want with your EditText here
                saveData()
            }
        })

        loadData()
        jumlah.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                // you can call or do what you want with your EditText here
                saveData()
            }
        })

        loadData()
        tombol.setOnCheckedChangeListener{ _, isChecked ->
            when(isChecked){
                true->{
                    saveData()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                false->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        } 
        saveData()
    }

    private fun saveData() {
        val tombol = findViewById<Switch>(R.id.switch1)
        val name = findViewById<EditText>(R.id.edit3)
        val jumlah = findViewById<EditText>(R.id.editview2)
        var size = 5

        if (jumlah.text.toString() != "") {
            size = jumlah.text.toString().toInt()
        }

        if (size < 5) {
            size = 5
        } else if (size > 50) {
            size = 50
        }

        val sharedPreferences = getSharedPreferences("sharedPreferences",Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()

        editor.apply{
            putBoolean("BOOLEAN_KEY",tombol.isChecked)
            putString("NAME_KEY", name.text.toString())
            putInt("SIZE_KEY", size)
        }.apply()

        Toast.makeText(this, "DataSaved", Toast.LENGTH_SHORT).show()
    }

    private fun loadData() {
        val tombol = findViewById<Switch>(R.id.switch1)
        val name = findViewById<EditText>(R.id.edit3)
        val jumlah = findViewById<EditText>(R.id.editview2)

        val sharedPreferences=getSharedPreferences("sharedPreferences",Context.MODE_PRIVATE)
        val savedBoolean = sharedPreferences.getBoolean("BOOLEAN_KEY",false)
        val savedString = sharedPreferences.getString("NAME_KEY","User")
        val savedInt = sharedPreferences.getInt("SIZE_KEY",25)

        tombol.isChecked = savedBoolean
        name.setText(savedString)
        jumlah.setText(savedInt.toString())
    }
}