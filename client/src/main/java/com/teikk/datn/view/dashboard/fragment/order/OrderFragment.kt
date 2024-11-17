package com.teikk.datn.view.dashboard.fragment.order

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentOrderBinding
import com.teikk.datn.view.dashboard.adapter.OrderViewPagerAdapter

class OrderFragment : BaseFragment<FragmentOrderBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_order
    }

    override fun init() {
        setUpTabLayoutViewPager()
    }

    private fun setUpTabLayoutViewPager() {
        val adapter = OrderViewPagerAdapter(requireActivity() as AppCompatActivity)
        with (binding) {
            viewPager2.adapter = adapter
            TabLayoutMediator(tablayout, viewPager2) { tab, position ->
                when (position) {
                    0 -> tab.text = "Upcoming"
                    1 -> tab.text = "History"
                }
            }.attach()
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
}