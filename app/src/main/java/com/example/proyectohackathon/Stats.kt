package com.example.proyectohackathon

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.WorkerThread
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.coroutines.*

class Stats : AppCompatActivity() {

    private val contexto = this
    lateinit var chart: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

    }

    override fun onStart() {
        super.onStart()

        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        //TODO
        //Use coroutine to create chart and update UI
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                createPlot()
            }
            withContext(Dispatchers.Main){
                showChart(chart)
            }
        }
    }


    private fun createPlot() {
        val py = Python.getInstance()
        try {
            val module = py.getModule("plot")

            //val bytes = module.callAttr("plot")
            //    .toJava(ByteArray::class.java)

            val bytes = module.callAttr("show_interpolation_graph")
                .toJava(ByteArray::class.java)
            chart = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        } catch (e: PyException) {
            Toast.makeText(contexto, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun showChart(chart:Bitmap){
        findViewById<ImageView>(R.id.imageView).setImageBitmap(chart)
    }
}