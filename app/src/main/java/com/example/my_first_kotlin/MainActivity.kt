package com.example.my_first_kotlin


import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var tvText:TextView? = null
    private var name: String = "Dimitrii"
    private var surname: String = "Fedyunin"
    private var age: Int = 24
    private val hieght: Double = 188.5
    val Tag: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(Tag, "start onCreate Func")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvText = findViewById(R.id.tvText)
        tvText?.setText("Имя: " + name + " Фамилия: " + surname + " Возраст: " + age + " Рост: " + hieght)

        Log.d(Tag, "end of onCreate Func")
    }
}