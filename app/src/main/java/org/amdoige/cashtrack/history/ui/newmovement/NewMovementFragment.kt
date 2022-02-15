package org.amdoige.cashtrack.history.ui.newmovement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import org.amdoige.cashtrack.core.classes.WalletsRepositoryProvider
import org.amdoige.cashtrack.databinding.FragmentNewMovementBinding
import org.amdoige.cashtrack.mainscreen.SharedViewModel

class NewMovementFragment : Fragment() {
    private lateinit var binding: FragmentNewMovementBinding
    private lateinit var viewModel: NewMovementViewModel
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
        val walletsRepository = WalletsRepositoryProvider.getRepository(requireContext())
        viewModel = ViewModelProvider(
            this,
            NewMovementViewModel.Companion.Factory(walletsRepository)
        )[NewMovementViewModel::class.java]

        setListeners()
    }

    private fun setListeners() {
        binding.cancelButton.setOnClickListener { sharedViewModel.closeNewElementFragment.emit() }
    }
}
