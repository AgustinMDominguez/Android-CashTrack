package org.amdoige.cashtrack.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.amdoige.cashtrack.billfolds.data.WalletsRepository
import org.amdoige.cashtrack.core.classes.WalletsRepositoryProvider
import org.amdoige.cashtrack.core.database.CashTrackDatabase
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.databinding.FragmentDevOptionsBinding
import org.amdoige.cashtrack.history.data.HistoryRepository
import org.amdoige.cashtrack.history.data.PagingDatabaseIntermediary

class DevOptionsFragment : Fragment() {
    private lateinit var binding: FragmentDevOptionsBinding
    private lateinit var walletRepository: WalletsRepository
    private lateinit var historyRepository: HistoryRepository
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModel.Companion.Factory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDevOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRepositories()
        setListeners()
    }

    private fun setRepositories() {
        walletRepository = WalletsRepositoryProvider.getRepository(requireContext())
        val cashTrackDatabase = CashTrackDatabase.getInstance(requireContext())
        val pagingDatabaseIntermediary = PagingDatabaseIntermediary(
            cashTrackDatabase, walletRepository::addWalletInfoToMovements
        )
        historyRepository = HistoryRepository(cashTrackDatabase, pagingDatabaseIntermediary)
    }

    private fun setListeners() {
        binding.devExitButton.setOnClickListener { sharedViewModel.closeDevOptions.emit() }
        binding.devAddOneMovementButton.setOnClickListener {
            lifecycleScope.launch { addRandomMovementBlocking() }
        }
        binding.devAddOneHundredMovementsButton.setOnClickListener {
            lifecycleScope.launch { repeat((1..100).count()) { addRandomMovementBlocking() } }
        }
        binding.devDeleteMovementsButton.setOnClickListener {
            lifecycleScope.launch { historyRepository.deleteAllMovements() }
        }
    }

    private suspend fun addRandomMovementBlocking(forceAmount: Double? = null) {
        val amount = forceAmount ?: run {
            val sign = if ((0..1).random() == 0) 1.0 else -1.0
            val units = (10..1000).random().toDouble()
            val cents = (0..99).random().toDouble() / 100.0
            sign * units + cents
        }
        val newMovement = Movement(
            walletId = walletRepository.getDefaultWallet().id,
            amount = amount,
            title = "Random Movement",
            description = "Random Description"
        )
        sharedViewModel.movementCreationEvent.emit(newMovement)
        while (!sharedViewModel.movementCreationEvent.isHandled()) {
            delay(10L)
        }
    }
}