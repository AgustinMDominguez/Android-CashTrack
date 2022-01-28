package org.amdoige.cashtrack.mainscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.amdoige.cashtrack.R
import org.amdoige.cashtrack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
    }
}