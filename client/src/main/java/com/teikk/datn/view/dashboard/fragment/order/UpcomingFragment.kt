package com.teikk.datn.view.dashboard.fragment.order

import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentUpcomingBinding

class UpcomingFragment : BaseFragment<FragmentUpcomingBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_upcoming
    }

    override fun init() {
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
}