package com.teikk.datn.view.dashboard.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teikk.datn.view.dashboard.fragment.order.DeliveryFragment
import com.teikk.datn.view.dashboard.fragment.order.HistoryFragment
import com.teikk.datn.view.dashboard.fragment.order.UpcomingFragment

class OrderViewPagerAdapter(
    context: AppCompatActivity
) : FragmentStateAdapter(context) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingFragment()
            1 -> DeliveryFragment()
            2 -> HistoryFragment()
            else -> UpcomingFragment()
        }
    }
}

