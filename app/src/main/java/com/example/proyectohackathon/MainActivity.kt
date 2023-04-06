package com.example.proyectohackathon

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.showAdvices)
            .setOnClickListener {
                //TODO
                //Read text from file
                val intent = Intent(this, advicesActivity::class.java)
                startActivity(intent)

            }

        findViewById<Button>(R.id.showStats)
            .setOnClickListener {

                val intent = Intent(this, Stats::class.java)
                startActivity(intent)

            }

        findViewById<ImageView>(R.id.statsImage).setOnClickListener{
            val intent = Intent(this, Stats::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.getData)
            .setOnClickListener {
                //TODO
                val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
                if(adapter == null) Toast.makeText(this, "Dispositivo sin Bluetooth", Toast.LENGTH_SHORT).show()
                else if(!adapter.isEnabled) Toast.makeText(this, "Habilite Bluetooth", Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(this, "Datos recibidos", Toast.LENGTH_SHORT).show()
                    //This block of code should retrieve data from arduino
                }
            }
    }

}
