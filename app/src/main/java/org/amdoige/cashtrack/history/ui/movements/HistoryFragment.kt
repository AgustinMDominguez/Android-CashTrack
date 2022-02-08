package org.amdoige.cashtrack.history.ui.movements

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import org.amdoige.cashtrack.core.WalletsRepositoryProvider
import org.amdoige.cashtrack.core.database.CashTrackDatabase
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.Wallet
import org.amdoige.cashtrack.databinding.FragmentHistoryBinding
import org.amdoige.cashtrack.history.data.HistoryRepository
import org.amdoige.cashtrack.history.ui.movements.adapters.HistoryMovementsAdapter
import org.amdoige.cashtrack.history.ui.movements.adapters.HistoryWalletsAdapter
import org.amdoige.cashtrack.mainscreen.SharedViewModel
import timber.log.Timber

class HistoryFragment : Fragment() {
    private var movementsAdapter = HistoryMovementsAdapter()
    private var walletsAdapter = HistoryWalletsAdapter()
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: HistoryViewModel

    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModel.Companion.Factory()
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
        binding.recyclerWallets.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        binding.recyclerWallets.adapter = walletsAdapter

        val historyRepository = HistoryRepository(CashTrackDatabase.getInstance(requireContext()))
        val walletRepository = WalletsRepositoryProvider.getRepository(requireContext())
        viewModel = ViewModelProvider(
            this,
            HistoryViewModel.Companion.Factory(historyRepository, walletRepository)
        )[HistoryViewModel::class.java]
        setLivedataObservers()
    }

    private fun setLivedataObservers() {
        val movementPagerObserver = Observer<PagingData<Movement>> { pagingData ->
            movementsAdapter.submitData(lifecycle, pagingData)
        }
        viewModel.movementsPagingData.observe(this, movementPagerObserver)

        val walletsObserver = Observer<List<Wallet>> { walletsAdapter.submitList(it) }
        viewModel.wallets.observe(this, walletsObserver)

        val balanceObserver = Observer<String> { binding.balanceAmount.text = it }
        viewModel.balanceString.observe(this, balanceObserver)

        val addButtonObserver = Observer<Boolean> {
            if (it) {
                binding.newMovementFragment.visibility = View.VISIBLE
                sharedViewModel.releaseAddButton()
            }
        }
        sharedViewModel.addButtonPressed.observe(this, addButtonObserver)

        val newMovementObserver = Observer<Movement?> {
            if (it != null) {
                Timber.i("New movement received!")
                viewModel.newMovement(it)
                binding.newMovementFragment.visibility = View.GONE
                sharedViewModel.ackNewMovement()
            }
        }
        sharedViewModel.movementCreated.observe(this, newMovementObserver)
    }
}
