package com.teikk.datn.view.dashboard.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teikk.datn.view.dashboard.fragment.order.HistoryFragment
import com.teikk.datn.view.dashboard.fragment.order.UpcomingFragment

class OrderViewPagerAdapter(
    context: AppCompatActivity
) : FragmentStateAdapter(context) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingFragment()
            1 -> HistoryFragment()
            else -> UpcomingFragment()
        }
    }
}

