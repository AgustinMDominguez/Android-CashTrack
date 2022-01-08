package org.amdoige.cashtrack.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.amdoige.cashtrack.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        binding.balanceAmount.text = "Current Balance is $99.99"
        setContentView(binding.root)
    }
}