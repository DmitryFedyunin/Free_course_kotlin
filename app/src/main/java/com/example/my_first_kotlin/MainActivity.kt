package com.example.my_first_kotlin


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    val TAG: String = "MainActivity"

    lateinit var requestInput: TextInputEditText

    lateinit var podsAdapter: Adapter

    lateinit var progressBar: ProgressBar

    val pods = mutableListOf<HashMap<String,String>>(
        HashMap<String,String>().apply {
            put("Title", "Title 1")
            put("Content", "Content 1")
        },
        HashMap<String,String>().apply {
            put("Title", "Title 3")
            put("Content", "Content 3")
        },
        HashMap<String,String>().apply {
            put("Title", "Title 4")
            put("Content", "Content 4")
        },
        HashMap<String,String>().apply {
            put("Title", "Title 5")
            put("Content", "Content 5")
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

    }
    fun initView(){
        val toolbar: MaterialToolbar = findViewById(R.id.too_bar)
        setSupportActionBar(toolbar)

        requestInput = findViewById(R.id.text_input_Edit)

        val podsList: ListView = findViewById(R.id.pods_list)
        podsAdapter = SimpleAdapter(
            applicationContext,
            pods,
            R.layout.item_pod,
            arrayOf("Title", "Content"),
            intArrayOf(R.id.title, R.id.content)
        )

        podsList.adapter = podsAdapter as SimpleAdapter

        val voiceInpudButton : FloatingActionButton = findViewById(R.id.voice_input_btn)
        voiceInpudButton.setOnClickListener{
            Log.d(TAG, "FAB")
        }

        progressBar = findViewById(R.id.progress_bar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tool_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_stop ->{
                Log.d(TAG,"action_stop")
                return true
            }
            R.id.action_clear ->{
                Log.d(TAG,"action_clear")
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }
}