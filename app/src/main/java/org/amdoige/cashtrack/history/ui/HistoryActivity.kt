package org.amdoige.cashtrack.history.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.MovementsDatabase
import org.amdoige.cashtrack.databinding.ActivityHistoryBinding
import org.amdoige.cashtrack.history.HistoryRepository
import org.amdoige.cashtrack.history.HistoryViewModel
import timber.log.Timber

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        binding.balanceAmount.text = "Current Balance is $99.99"
        binding.recyclerMovements.adapter = HistoryMovementsAdapter()

        val historyRepository = HistoryRepository(MovementsDatabase.getInstance(this))
        viewModel = ViewModelProvider(
            this,
            HistoryViewModel.Companion.Factory(historyRepository)
        )[HistoryViewModel::class.java]
        setupObservers()
        setupListeners()
        setContentView(binding.root)
    }

    private fun setupObservers() {
        val movementsObserver = Observer<List<Movement>> { newList ->
            (binding.recyclerMovements.adapter as HistoryMovementsAdapter).movements = newList
            Timber.i("Observed a change in livedata!: $newList")
        }
        viewModel.movements.observe(this, movementsObserver)
    }

    private fun setupListeners() {
        binding.addRandomMovementButton.setOnClickListener {
            val amount = (10..1000).random().toDouble() + (0..99).random().toDouble() / 10
            viewModel.newImmediateMovement(amount, "random Movement", "some descr")
        }
    }
}
