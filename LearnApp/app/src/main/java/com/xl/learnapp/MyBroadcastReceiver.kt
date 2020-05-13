package com.xl.learnapp

import android.widget.Toast
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context


class MyBroadcastReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show()
//    }

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "检测到意图。", Toast.LENGTH_LONG).show()
    }

}