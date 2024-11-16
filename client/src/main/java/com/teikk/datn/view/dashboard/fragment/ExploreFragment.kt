package com.teikk.datn.view.dashboard.fragment

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.setSafeOnClickListener
import com.teikk.datn.databinding.FragmentExploreBinding
import com.teikk.datn.view.dashboard.DashBoardActivity
import com.teikk.datn.view.dashboard.DashBoardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : BaseFragment<FragmentExploreBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
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

    override fun onResume() {
        super.onResume()
        (requireActivity() as DashBoardActivity).showBottomNav()
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }
}