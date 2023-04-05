package com.example.proyectohackathon

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

class advicesActivity : AppCompatActivity() {

    private val contexto = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advices)
    }

    override fun onStart() {
        super.onStart()

        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        //TODO
        //Use coroutine to create chart and update UI
        GlobalScope.launch {
            lateinit var response: Array<String>
            withContext(Dispatchers.Default) {
                val py = Python.getInstance()
                try {
                    val module = py.getModule("chatGPT")

                    //val bytes = module.callAttr("plot")
                    //    .toJava(ByteArray::class.java)

                    response = module.callAttr("get_daily_tip")
                        .toJava(Array<String>::class.java)


                } catch (e: PyException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(contexto, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
            withContext(Dispatchers.Main){
                findViewById<TextView>(R.id.weatherContentTextView).setText(response[0])
                findViewById<TextView>(R.id.advicesContentTextView).setText(response[1])
            }
        }
    }

    private fun getWeather(){

    }
}