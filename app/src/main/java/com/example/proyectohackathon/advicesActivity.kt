package com.example.proyectohackathon

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

class advicesActivity : AppCompatActivity() {

    private val contexto = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advices)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onStart() {
        super.onStart()

        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }


        GlobalScope.launch {

            val preferences = contexto.getSharedPreferences("ChatGPTAdvices", MODE_PRIVATE)
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val currentDateString = formatter.format(Date()).toString()

            if(preferences.contains("date") and preferences.getString("date", "").equals(currentDateString) ){
                withContext(Dispatchers.Main) {
                    findViewById<TextView>(R.id.weatherContentTextView).text =
                        preferences.getString("weather", "Error")
                    findViewById<TextView>(R.id.advicesContentTextView).text =
                        preferences.getString("advice", "Error")
                }
            }
            else{
                lateinit var response: Array<String>

                withContext(Dispatchers.Default) {
                    val py = Python.getInstance()
                    try {
                        val module = py.getModule("chatGPT")

                        //val bytes = module.callAttr("plot")
                        //    .toJava(ByteArray::class.java)

                        response = module.callAttr("get_daily_tip")
                            .toJava(Array<String>::class.java)

                        with (preferences.edit()) {
                            putString("date", currentDateString)
                            putString("weather", response[0])
                            putString("advice", response[1])
                            apply()
                        }

                        withContext(Dispatchers.Main){
                            findViewById<TextView>(R.id.weatherContentTextView).text = response[0]
                            findViewById<TextView>(R.id.advicesContentTextView).text = response[1]
                        }

                    } catch (e: PyException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(contexto, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

    }
}