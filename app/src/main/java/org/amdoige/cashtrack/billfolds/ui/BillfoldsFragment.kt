package org.amdoige.cashtrack.billfolds.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.amdoige.cashtrack.billfolds.BillfoldsViewModel
import org.amdoige.cashtrack.core.WalletsRepositoryProvider
import org.amdoige.cashtrack.databinding.FragmentBillfoldsBinding
import org.amdoige.cashtrack.mainscreen.SharedViewModel

class BillfoldsFragment : Fragment() {
    private lateinit var binding: FragmentBillfoldsBinding
    private lateinit var viewModel: BillfoldsViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModel.Companion.Factory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillfoldsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val walletsRepository = WalletsRepositoryProvider.getRepository(requireContext())
        viewModel = ViewModelProvider(
            this,
            BillfoldsViewModel.Companion.Factory(walletsRepository)
        )[BillfoldsViewModel::class.java]
        setLivedataObservers()
    }

    private fun setLivedataObservers() {
        val addButtonObserver = Observer<Boolean> {
            if (it) {
                binding.newWalletFragment.visibility = View.VISIBLE
                sharedViewModel.releaseAddButton()
            }
        }
        sharedViewModel.addButtonPressed.observe(viewLifecycleOwner, addButtonObserver)
    }
}