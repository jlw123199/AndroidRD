package com.xl.learnapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.view.View


class MainActivity : AppCompatActivity() {

    var msg = "----------Logs-------------: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        Log.d(msg, "The onCreate() event");
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }

    }

    // Method to start the service
    fun startService(view: View) {
        startService(Intent(baseContext, MyService::class.java))
    }

    // Method to stop the service
    fun stopService(view: View) {
        stopService(Intent(baseContext, MyService::class.java))
    }

    /** 当活动即将可见时调用  */
    override fun onStart() {
        super.onStart()
        Log.d(msg, "The onStart() event")
    }

    /** 当活动可见时调用  */
    override fun onResume() {
        super.onResume()
        Log.d(msg, "The onResume() event")
    }

    /** 当其他活动获得焦点时调用  */
    override fun onPause() {
        super.onPause()
        Log.d(msg, "The onPause() event")
    }

    /** 当活动不再可见时调用  */
    override fun onStop() {
        super.onStop()
        Log.d(msg, "The onStop() event")
    }

    /** 当活动将被销毁时调用  */
    public override fun onDestroy() {
        super.onDestroy()
        Log.d(msg, "The onDestroy() event")
    }
}
