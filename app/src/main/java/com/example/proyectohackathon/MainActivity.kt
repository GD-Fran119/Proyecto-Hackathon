package com.example.proyectohackathon

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    private val contexto = this
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

    override fun onStart() {
        super.onStart()
        sendNotification()
    }

    private fun sendNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = "Notificaciones"
            val descriptionText = "Canal para notificaciones"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("Notifications", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

            val builder = NotificationCompat.Builder(this, "Notifications")
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("¿Se ha puesto las gotas?")
                .setContentText("Hace mucho tiempo desde la última vez que se echó las gotas")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("Hace mucho tiempo desde la última vez que se echó las gotas"))
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                if (ActivityCompat.checkSelfPermission(
                        contexto,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                notify(0, builder.build())
            }


        }

    }

}
