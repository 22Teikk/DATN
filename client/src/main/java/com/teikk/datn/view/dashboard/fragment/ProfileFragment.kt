package com.teikk.datn.view.dashboard.fragment

import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_profile
    }

    override fun init() {
    }

    override fun initEvent() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
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