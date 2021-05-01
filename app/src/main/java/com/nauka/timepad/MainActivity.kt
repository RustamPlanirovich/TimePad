package com.nauka.timepad

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.nauka.timepad.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityMainBinding
    lateinit var mSetting: SharedPreferences
    var readState: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        mSetting = getSharedPreferences("InternetMode", Context.MODE_PRIVATE)
        readOnLineDataBase()
    }


    fun readOnLineDataBase() {
        readState = mSetting.getBoolean("InternetMode", false)
        when(readState){
            true -> Home().db.enableNetwork()
            false -> Home().db.disableNetwork()
        }
    }
}