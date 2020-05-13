package com.xl.learnapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_content_provider.*
import android.widget.Toast
import android.widget.EditText

import android.content.ContentValues
import android.net.Uri
import android.view.Menu
import android.view.View


class ContentProviderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_provider)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun onClickAddName(view: View) {
        // Add a new student record
        val values = ContentValues()

        values.put(StudentsProvider.NAME,
                (findViewById(R.id.editText2) as EditText).text.toString())

        values.put(StudentsProvider.GRADE,
                (findViewById(R.id.editText3) as EditText).text.toString())

        val uri = contentResolver.insert(
                StudentsProvider.CONTENT_URI, values)

        Toast.makeText(baseContext,
                uri!!.toString(), Toast.LENGTH_LONG).show()
    }

    fun onClickRetrieveStudents(view: View) {

        // Retrieve student records
        val URL = "content://com.example.provider.College/students"

        val students = Uri.parse(URL)
        val c = managedQuery(students, null, null, null, "name")

        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(StudentsProvider._ID)) +
                                ", " + c.getString(c.getColumnIndex(StudentsProvider.NAME)) +
                                ", " + c.getString(c.getColumnIndex(StudentsProvider.GRADE)),
                        Toast.LENGTH_SHORT).show()
            } while (c.moveToNext())
        }
    }

}
