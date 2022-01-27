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
import org.amdoige.cashtrack.core.database.CashTrackDatabase
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

        val historyRepository = HistoryRepository(CashTrackDatabase.getInstance(this))
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
                    addRandomMovement(forceAmount = (1000 - a).toDouble())
                    delay(1)
                }
            }
            Timber.i("Done Adding Movements")
        }
        binding.deleteMovementsButton.setOnClickListener { viewModel.deleteAllMovements() }
    }

    private fun addRandomMovement(forceAmount: Double? = null) {
        val amount = forceAmount ?: run {
            val sign = if ((0..1).random() == 0) 1.0 else -1.0
            val units = (10..1000).random().toDouble()
            val cents = (0..99).random().toDouble() / 100.0
            sign * units + cents
        }
        viewModel.newImmediateMovement(amount, "random Movement", "some description")
    }
}
