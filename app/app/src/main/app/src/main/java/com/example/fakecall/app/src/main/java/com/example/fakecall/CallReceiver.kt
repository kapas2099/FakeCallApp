package com.example.fakecall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val callerName = intent.getStringExtra("caller_name") ?: "Unknown"

        val callIntent = Intent(context, IncomingCallActivity::class.java).apply {
            putExtra("caller_name", callerName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(callIntent)
    }
}
