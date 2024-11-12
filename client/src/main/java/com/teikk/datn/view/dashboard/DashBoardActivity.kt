package com.teikk.datn.view.dashboard

import android.os.Bundle
import android.view.Gravity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.teikk.datn.R
import com.teikk.datn.base.BaseActivity
import com.teikk.datn.databinding.ActivityDashBoardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardActivity : BaseActivity<ActivityDashBoardBinding>() {
    private lateinit var navController: NavController
    private lateinit var badgeCart: BadgeDrawable
    private lateinit var badgeNotification: BadgeDrawable
    override fun getLayoutResId(): Int {
        return R.layout.activity_dash_board
    }

    override fun init() {
        navController = findNavController(R.id.dashboardNavController)
        with(binding) {
            bottomNavigation.setupWithNavController(navController)
            badgeCart = bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                badgeGravity = BadgeDrawable.TOP_END
                backgroundColor = getColor(R.color.yellow)
            }
            badgeNotification = bottomNavigation.getOrCreateBadge(R.id.notificationFragment).apply {
                badgeGravity = BadgeDrawable.TOP_END
            }
        }

    }

    override fun initObserve() {
        badgeCart.number = 20
        badgeNotification.isVisible = true
    }

    fun openDrawer() {
        binding.main.openDrawer(Gravity.START)
    }

    companion object {
        const val TAG = "DashBoardActivityTag"
    }
}