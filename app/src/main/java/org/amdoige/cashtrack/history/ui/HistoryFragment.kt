package org.amdoige.cashtrack.history.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import org.amdoige.cashtrack.databinding.FragmentHistoryBinding
import org.amdoige.cashtrack.mainscreen.MainFragments
import org.amdoige.cashtrack.mainscreen.UIStateViewModel
import timber.log.Timber

class HistoryFragment : Fragment() {
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
        setListeners()
    }

    private fun setListeners() {
        binding.toBillfoldsButton.setOnClickListener {
            Timber.i("TBR: Navigating to Billfolds!")
            uiStateViewModel.switchScreens()
        }
    }
}
