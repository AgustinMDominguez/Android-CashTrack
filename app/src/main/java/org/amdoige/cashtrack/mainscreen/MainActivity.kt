package org.amdoige.cashtrack.mainscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.amdoige.cashtrack.R
import org.amdoige.cashtrack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var uiStateViewModel: UIStateViewModel
    private val bottomNavScreenMapping: Map<MainFragments, Int> get() = getScreenMapping()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        uiStateViewModel = ViewModelProvider(
            this,
            UIStateViewModel.Companion.Factory()
        )[UIStateViewModel::class.java]
        setContentView(binding.root)
        setUpBottomNav()
        setLivedataObservers()
        setListeners()
    }

    private fun setLivedataObservers() {
        val screenObserver = Observer<MainFragments?> {
            bottomNavScreenMapping[it]?.let { menuItemId ->
                binding.bottomNavigation.selectedItemId = menuItemId
                uiStateViewModel.ackScreenSwitch()
            }
        }
        uiStateViewModel.currentScreen.observe(this, screenObserver)
    }

    private fun setListeners() {
        binding.addButton.setOnClickListener { uiStateViewModel.pressAddButton() }
    }

    private fun setUpBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun getScreenMapping(): Map<MainFragments, Int> {
        val mutMap: MutableMap<MainFragments, Int> = mutableMapOf()
        for (i in (0 until binding.bottomNavigation.menu.size())) {
            val menuItem = binding.bottomNavigation.menu.getItem(i)
            if (menuItem.isEnabled) {
                val mainFragment: MainFragments? = when (menuItem.title) {
                    getString(R.string.home_ic_title) -> MainFragments.MOVEMENTS
                    getString(R.string.wallets_ic_title) -> MainFragments.BILLFOLDS
                    else -> null
                }
                mainFragment?.let { mutMap.putIfAbsent(it, menuItem.itemId) }
            }
        }
        return mutMap.toMap()
    }
}
