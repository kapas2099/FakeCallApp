package com.example.fakecall

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameInput = findViewById<EditText>(R.id.callerNameInput)
        val delayInput = findViewById<EditText>(R.id.delayInput)
        val startButton = findViewById<Button>(R.id.startButton)

        startButton.setOnClickListener {
            val callerName = nameInput.text.toString().ifBlank { "Unknown" }
            val delaySeconds = delayInput.text.toString().toLongOrNull() ?: 5L

            scheduleFakeCall(callerName, delaySeconds)
            Toast.makeText(
                this,
                "$callerName ko fake call $delaySeconds second ma aaucha",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun scheduleFakeCall(callerName: String, delaySeconds: Long) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM_PERMISSION))
            Toast.makeText(this, "Exact alarm permission dinu ani feri try garnus", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(this, CallReceiver::class.java).apply {
            putExtra("caller_name", callerName)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAt = System.currentTimeMillis() + delaySeconds * 1000

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        }
    }
}
