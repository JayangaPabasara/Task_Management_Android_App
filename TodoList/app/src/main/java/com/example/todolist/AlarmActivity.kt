package com.example.todolist

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AlarmActivity : AppCompatActivity() {

    private lateinit var alarmManager: AlarmManager
    private lateinit var timePicker: TimePicker
    private lateinit var messageEditText: EditText
    private lateinit var setAlarmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        timePicker = findViewById(R.id.timePicker)
        messageEditText = findViewById(R.id.messageEditText)
        setAlarmButton = findViewById(R.id.setAlarmButton)

        // Log to trace any activity redirection
        Log.d("AlarmActivity", "onCreate: AlarmActivity started")

        setAlarmButton.setOnClickListener {
            val message = messageEditText.text.toString()

            // Get current time and selected time from TimePicker
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)
            calendar.set(Calendar.SECOND, 0)

            // Set the alarm
            setAlarm(calendar.timeInMillis, message)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(alarmTimeInMillis: Long, message: String) {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("message", message)

        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (alarmTimeInMillis > System.currentTimeMillis()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pendingIntent)

            // Show toast BEFORE any redirection or finish to ensure visibility
            val timeFormatted = String.format(
                "%02d:%02d",
                Calendar.getInstance().apply { timeInMillis = alarmTimeInMillis }.get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().apply { timeInMillis = alarmTimeInMillis }.get(Calendar.MINUTE)
            )
            Toast.makeText(this, "Alarm set for $timeFormatted", Toast.LENGTH_LONG).show()

            // Explicitly ensure the Toast shows by logging it
            Log.d("AlarmActivity", "Alarm set for: $timeFormatted")

            // We no longer call finish() or startActivity() to prevent redirection
            // If you need redirection, ensure it's explicitly called at the right place.
            // finish() // Uncomment if you still want the activity to finish.
        } else {
            Toast.makeText(this, "Please set a time in the future!", Toast.LENGTH_LONG).show()
            Log.d("AlarmActivity", "Invalid time: time is in the past.")
        }
    }
}
