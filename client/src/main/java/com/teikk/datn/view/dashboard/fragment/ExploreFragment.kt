package com.teikk.datn.view.dashboard.fragment

import androidx.activity.OnBackPressedCallback
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.setSafeOnClickListener
import com.teikk.datn.databinding.FragmentExploreBinding
import com.teikk.datn.view.dashboard.DashBoardActivity

class ExploreFragment : BaseFragment<FragmentExploreBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_explore
    }

    override fun init() {
    }

    override fun initEvent() {
        with (binding) {
            btnMenu.setSafeOnClickListener {
                (requireActivity() as DashBoardActivity).openDrawer()
            }
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }
}