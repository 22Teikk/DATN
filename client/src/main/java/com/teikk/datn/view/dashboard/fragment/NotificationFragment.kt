package com.teikk.datn.view.dashboard.fragment

import androidx.activity.OnBackPressedCallback
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentLoginBinding
import com.teikk.datn.databinding.FragmentNotificationBinding
import com.teikk.datn.view.dashboard.DashBoardActivity

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_notification
    }

    override fun init() {
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