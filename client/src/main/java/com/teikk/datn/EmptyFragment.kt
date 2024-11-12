package com.teikk.datn

import androidx.activity.OnBackPressedCallback
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentLoginBinding

class EmptyFragment : BaseFragment<FragmentLoginBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_login
    }

    override fun init() {
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }
}