package com.teikk.datn.view.dashboard.fragment

import androidx.activity.OnBackPressedCallback
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentCartBinding

class CartFragment : BaseFragment<FragmentCartBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_cart
    }

    override fun init() {
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }
}