package com.mosambee.kotlinapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {

    lateinit var textView: TextView
    var value =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        textView = findViewById(R.id.textView) as TextView
        value = intent.getStringExtra("test").toString()
        //
        textView.setText(value)

        val textViewValue = textView.text
        Log.d("Data:::::", textViewValue.toString())
    }
}