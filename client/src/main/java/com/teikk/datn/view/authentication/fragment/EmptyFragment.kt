package com.teikk.datn.view.authentication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.teikk.datn.R
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