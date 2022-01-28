package org.amdoige.cashtrack.history.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.amdoige.cashtrack.databinding.FragmentHistoryBinding
import timber.log.Timber

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding

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
//            val action = HistoryFragmentDirections.actionNavigationHomeToNavigationBillfolds()
//            findNavController().navigate(action)
        }
    }
}
