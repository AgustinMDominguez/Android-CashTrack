package org.amdoige.cashtrack.billfolds.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import org.amdoige.cashtrack.databinding.FragmentNewWalletBinding
import org.amdoige.cashtrack.mainscreen.SharedViewModel

class NewWalletFragment : Fragment() {
    private lateinit var binding: FragmentNewWalletBinding
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModel.Companion.Factory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLivedataObservers()
    }

    private fun setLivedataObservers() {}
}
