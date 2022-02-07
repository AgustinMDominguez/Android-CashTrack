package org.amdoige.cashtrack.history.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import org.amdoige.cashtrack.databinding.FragmentNewMovementBinding
import org.amdoige.cashtrack.mainscreen.SharedViewModel

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
        setLivedataObservers()
    }

    private fun setLivedataObservers() {}
}
