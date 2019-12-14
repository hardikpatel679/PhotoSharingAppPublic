package com.photo.sharing.activities

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.photo.sharing.R
import com.photo.sharing.enumconstants.EnvironmentKey
import com.photo.sharing.extensions.displaySingleChoiceDialog
import com.photo.sharing.fragments.FriendsListFragment
import com.photo.sharing.utils.AWSConfiguration
import com.photo.sharing.utils.CurrentEnvironment
import com.photo.sharing.viewmodel.FriendsListViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.alert


class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: FriendsListViewModel
    private val currentFragment: Fragment?
        get() = navHost.childFragmentManager.findFragmentById(R.id.navHost)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.navHost)

        setupActionBarWithNavController(navController)
        AWSConfiguration.configureAws(this)
        setUpViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController(R.id.navHost).navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (currentFragment !is FriendsListFragment) {
            findNavController(R.id.navHost).navigateUp()
        } else {
            showAppExitDialog()
        }
    }

    private fun setUpViewModel() {
        viewModel =
            ViewModelProvider(this).get(FriendsListViewModel::class.java)
    }

    private fun showAppExitDialog() {
        alert(getString(R.string.exit_app_msg)) {
            title(getString(R.string.alert))
            positiveButton(getString(R.string.yes)) {
                finish()
            }
            negativeButton(getString(R.string.No)) {
                this.dismiss()
            }
        }.show()
    }
}
