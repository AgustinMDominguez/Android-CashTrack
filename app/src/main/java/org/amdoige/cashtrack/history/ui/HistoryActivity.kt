package org.amdoige.cashtrack.history.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import org.amdoige.cashtrack.databinding.ActivityHistoryBinding
import org.amdoige.cashtrack.history.HistoryViewModel

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        binding.balanceAmount.text = "Current Balance is $99.99"
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        setContentView(binding.root)
    }
}
