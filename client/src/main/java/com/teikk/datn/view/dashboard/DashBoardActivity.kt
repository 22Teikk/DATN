package com.teikk.datn.view.dashboard

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.badge.BadgeDrawable
import com.teikk.datn.R
import com.teikk.datn.base.BaseActivity
import com.teikk.datn.databinding.ActivityDashBoardBinding
import com.teikk.datn.view.authentication.AuthenticationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashBoardActivity : BaseActivity<ActivityDashBoardBinding>() {
    private lateinit var navController: NavController
    private lateinit var badgeCart: BadgeDrawable
    private lateinit var badgeNotification: BadgeDrawable
    private val viewModel by viewModels<DashBoardViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.activity_dash_board
    }

    override fun init() {
        navController = findNavController(R.id.dashboardNavController)
        with(binding) {
            bottomNavigation.setupWithNavController(navController)
            main.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            badgeCart = bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                badgeGravity = BadgeDrawable.TOP_END
                backgroundColor = getColor(R.color.yellow)
            }
            badgeNotification = bottomNavigation.getOrCreateBadge(R.id.notificationFragment).apply {
                badgeGravity = BadgeDrawable.TOP_END
            }
            binding.navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.orderFragment -> {
                        navController.navigate(R.id.action_exploreFragment_to_orderFragment)
                        closeDrawerAndHideBottomNav()
                        true
                    }
                    R.id.profileFragment -> {
                        navController.navigate(R.id.action_exploreFragment_to_profileFragment2)
                        closeDrawerAndHideBottomNav()
                        true
                    }
                    R.id.addressFragment -> {
                        navController.navigate(R.id.action_exploreFragment_to_addressFragment2)
                        closeDrawerAndHideBottomNav()
                        true
                    }
                    R.id.paymentMethodFragment -> {
                        navController.navigate(R.id.action_exploreFragment_to_paymentMethodFragment2)
                        closeDrawerAndHideBottomNav()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    override fun initEvent() {
        with(binding) {
            btnLogout.setOnClickListener {
                viewModel.logout {
                    startActivity(Intent(this@DashBoardActivity, AuthenticationActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }
            }
        }
    }

    override fun initObserve() {
        with (binding) {
            viewModel.user.observe(this@DashBoardActivity) {
                navigationView.findViewById<TextView>(R.id.txtName).setText(it.data?.name)
                navigationView.findViewById<TextView>(R.id.txtEmail).setText(it.data?.email)
                Glide.with(root).load(it.data?.imageUrl).into(navigationView.findViewById(R.id.nav_header_imageView))

            }
            lifecycleScope.launch {
                viewModel.carts.collectLatest {
                    badgeCart.number = it.size
                }
            }
        }
        badgeNotification.isVisible = true
    }

    fun openDrawer() {
        binding.main.openDrawer(GravityCompat.START)
    }

    fun closeDrawerAndHideBottomNav() {
        with(binding) {
            main.closeDrawer(GravityCompat.START)
            bottomNavigation.visibility = View.GONE
        }
    }

    fun showBottomNav() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    companion object {
        const val TAG = "DashBoardActivityTag"
    }
}