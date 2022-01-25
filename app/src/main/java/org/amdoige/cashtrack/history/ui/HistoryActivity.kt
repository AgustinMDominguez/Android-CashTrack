package org.amdoige.cashtrack.history.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.MovementsDatabase
import org.amdoige.cashtrack.databinding.ActivityHistoryBinding
import org.amdoige.cashtrack.history.data.HistoryRepository
import org.amdoige.cashtrack.history.HistoryViewModel
import timber.log.Timber

class HistoryActivity : AppCompatActivity() {
    private var movementsAdapter = HistoryMovementsAdapter()
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        binding.recyclerMovements.adapter = movementsAdapter

        val historyRepository = HistoryRepository(MovementsDatabase.getInstance(this))
        viewModel = ViewModelProvider(
            this,
            HistoryViewModel.Companion.Factory(historyRepository)
        )[HistoryViewModel::class.java]
        setLivedataObservers()
        setListeners()
        setContentView(binding.root)
    }

    private fun setLivedataObservers() {
        val movementPagerObserver = Observer<PagingData<Movement>> { pagingData ->
            movementsAdapter.submitData(lifecycle, pagingData)
        }
        viewModel.movementsPagingData.observe(this, movementPagerObserver)

        val balanceObserver = Observer<String> { binding.balanceAmount.text = it }
        viewModel.balanceString.observe(this, balanceObserver)
    }

    private fun setListeners() {
        binding.addRandomMovementButton.setOnClickListener { addRandomMovement() }

        binding.addOneHundredMovementsButton.setOnClickListener {
            Timber.i("Adding many movements!")
            lifecycleScope.launch {
                for (a in 1..100) {
                    addRandomMovement()
                    delay(1)
                }
            }
            Timber.i("Done Adding Movements")
        }
        binding.deleteMovementsButton.setOnClickListener { viewModel.deleteAllMovements() }
    }

    private fun addRandomMovement() {
        val sign = if ((0..1).random() == 0) 1.0 else -1.0
        val amount = sign * (10..1000).random().toDouble() + (0..99).random().toDouble() / 100.0
        viewModel.newImmediateMovement(amount, "random Movement", "some descr")
    }
}
