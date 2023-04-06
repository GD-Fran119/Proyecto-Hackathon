package com.example.proyectohackathon

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
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

        try {
            val placeholder = findViewById<ImageView>(R.id.imageViewPlaceholder)
            findViewById<ConstraintLayout>(R.id.statslayout).removeView(placeholder)
            findViewById<ImageView>(R.id.imageView).setImageBitmap(chart)
            findViewById<TextView>(R.id.textView).text = "Estadísticas globales"
        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}