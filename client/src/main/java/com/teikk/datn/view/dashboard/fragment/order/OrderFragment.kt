package com.teikk.datn.view.dashboard.fragment.order

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentOrderBinding
import com.teikk.datn.view.dashboard.DashBoardViewModel
import com.teikk.datn.view.dashboard.adapter.OrderViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
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
                    1 -> tab.text = "Delivered"
                    2 -> tab.text = "History"
                }
            }.attach()
        }
    }

    override fun initEvent() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun initObserver() {
        with (binding) {
            viewModel.user.observe(viewLifecycleOwner) {
                Glide.with(root).load(it.data?.imageUrl).into(imgAvatar)
            }
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
}