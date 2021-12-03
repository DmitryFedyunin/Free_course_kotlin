package com.example.my_first_kotlin
/// 3LTQL6-32HR856PYP  API WolframAlpha

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.wolfram.alpha.WAEngine
import com.wolfram.alpha.WAPlainText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder
class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    lateinit var requestInput: TextInputEditText

    lateinit var podsAdapter: SimpleAdapter

    val pods = mutableListOf<HashMap<String, String>>()

    lateinit var progressBar: ProgressBar

    lateinit var waEngine: WAEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initWolframEngine()
    }

    private fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.too_bar)
        setSupportActionBar(toolbar)

        requestInput = findViewById<TextInputEditText>(R.id.text_input_Edit)
        requestInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                pods.clear()
                podsAdapter.notifyDataSetChanged()

                val question = requestInput.text.toString()
                askWolfram(question)
            }
            return@setOnEditorActionListener false
        }

        val podsList = findViewById<ListView>(R.id.pods_list)
        podsAdapter = SimpleAdapter(
            applicationContext,
            pods,
            R.layout.item_pod,
            arrayOf("Title", "Content"),
            intArrayOf(R.id.title, R.id.content)
        )
        podsList.adapter = podsAdapter

        progressBar = findViewById<ProgressBar>(R.id.progress_bar)
    }

    fun initWolframEngine() {
        waEngine = WAEngine()
        waEngine.appID = "DEMO"
        waEngine.addFormat("plaintext")
    }

    fun showErrorDialog(error: String) {
        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_INDEFINITE).apply {
            setAction(android.R.string.ok) {
                dismiss()
            }
            show()
        }
    }

    fun askWolfram(request: String) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val query = waEngine.createQuery().apply { input = request }
            runCatching {
                waEngine.performQuery(query)
            }.onSuccess { queryResult ->
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (queryResult.isError) {
                        showErrorDialog(queryResult.errorMessage)
                        return@withContext
                    }

                    if (!queryResult.isSuccess) {
                        requestInput.error = getString(R.string.error_do_not_understand)
                        return@withContext
                    }

                    for (pod in queryResult.pods) {
                        if (!pod.isError) {
                            val content = StringBuilder()
                            for (subpod in pod.subpods) {
                                for (element in subpod.contents) {
                                    if (element is WAPlainText) {
                                        content.append(element.text)
                                    }
                                }
                            }
                            pods.add(0, HashMap<String, String>().apply {
                                put("Title", pod.title)
                                put("Content", content.toString())
                            })
                        }
                    }
                    podsAdapter.notifyDataSetChanged()
                }
            }.onFailure { t ->
                Log.e(TAG, "Performing Wolfram query was failed", t)
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    showErrorDialog(t.message ?: getString(R.string.error_something_went_wrong))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tool_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_stop -> {
                Log.d(TAG, "action_stop")
                return true
            }
            R.id.action_clear -> {
                requestInput.text?.clear()
                pods.clear()
                podsAdapter.notifyDataSetChanged()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}}