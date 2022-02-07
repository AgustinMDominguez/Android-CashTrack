package org.amdoige.cashtrack.history.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.databinding.FragmentNewMovementBinding
import org.amdoige.cashtrack.mainscreen.SharedViewModel
import timber.log.Timber

class NewMovementFragment : Fragment() {
    private lateinit var binding: FragmentNewMovementBinding
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModel.Companion.Factory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewMovementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.randomMovementButton.setOnClickListener { addRandomMovement() }
    }

    private fun addRandomMovement(forceAmount: Double? = null) {
        val amount = forceAmount ?: run {
            val sign = if ((0..1).random() == 0) 1.0 else -1.0
            val units = (10..1000).random().toDouble()
            val cents = (0..99).random().toDouble() / 100.0
            sign * units + cents
        }
        val newMovement = Movement(
            amount = amount,
            milliseconds = System.currentTimeMillis(),
            title = "Random Movement",
            description = "Some description"
        )
        sharedViewModel.newMovement(newMovement)
        Timber.i("New movement added!")
    }
}
