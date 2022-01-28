package org.amdoige.cashtrack.billfolds.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.amdoige.cashtrack.databinding.FragmentBillfoldsBinding

class BillfoldsFragment : Fragment() {
    private lateinit var binding: FragmentBillfoldsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillfoldsBinding.inflate(inflater, container, false)
        return binding.root
    }
}