package org.amdoige.cashtrack.history.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.amdoige.cashtrack.core.database.CashTrackDatabase
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.databinding.FragmentHistoryBinding
import org.amdoige.cashtrack.history.HistoryViewModel
import org.amdoige.cashtrack.history.data.HistoryRepository
import org.amdoige.cashtrack.mainscreen.UIStateViewModel
import timber.log.Timber

class HistoryFragment : Fragment() {
    private var movementsAdapter = HistoryMovementsAdapter()
    private lateinit var viewModel: HistoryViewModel
    private lateinit var binding: FragmentHistoryBinding
    private val uiStateViewModel: UIStateViewModel by activityViewModels {
        UIStateViewModel.Companion.Factory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerMovements.adapter = movementsAdapter
        val historyRepository = HistoryRepository(CashTrackDatabase.getInstance(requireContext()))
        viewModel = ViewModelProvider(
            this,
            HistoryViewModel.Companion.Factory(historyRepository)
        )[HistoryViewModel::class.java]
        setLivedataObservers()
    }

    private fun setLivedataObservers() {
        val movementPagerObserver = Observer<PagingData<Movement>> { pagingData ->
            movementsAdapter.submitData(lifecycle, pagingData)
        }
        viewModel.movementsPagingData.observe(this, movementPagerObserver)

        val balanceObserver = Observer<String> { binding.balanceAmount.text = it }
        viewModel.balanceString.observe(this, balanceObserver)

        val addButtonObserver = Observer<Boolean> {
            if (it) {
                addRandomMovement()
                uiStateViewModel.unpressAddButton()
            }
        }
        uiStateViewModel.addButtonPressed.observe(this, addButtonObserver)
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
