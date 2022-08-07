package com.example.languageflashcard.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.languageflashcard.R
import com.example.languageflashcard.databinding.ActivityNavigationDrawerBinding
import com.example.languageflashcard.databinding.NavHeaderNavigationDrawerBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationDrawerActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationDrawerBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar)

        binding.appBarNavigationDrawer.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setupNavigationDrawerHeaderUI()
    }

    private fun setupNavigationDrawerHeaderUI() {
        val navHeaderView = binding.navView.getHeaderView(0)
        val navHeaderBinding = NavHeaderNavigationDrawerBinding.bind(navHeaderView)

        fetchAndBindUserDataForNavHeader(navHeaderBinding)
    }

    private fun fetchAndBindUserDataForNavHeader(binding: NavHeaderNavigationDrawerBinding) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        binding.userNameTv.text = currentUser?.displayName
        binding.userEmailTv.text = currentUser?.email
        Glide.with(this)
            .load(currentUser?.photoUrl)
            .circleCrop()
            .into(binding.userProfilePicIv)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_drawer, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}