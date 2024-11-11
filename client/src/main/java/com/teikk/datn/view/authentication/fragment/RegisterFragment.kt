package com.teikk.datn.view.authentication.fragment

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentRegisterBinding
import com.teikk.datn.view.authentication.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private val viewModel: AuthenticationViewModel by activityViewModels()
    override fun getLayoutResId(): Int {
        return R.layout.fragment_register
    }

    override fun init() {

    }

    override fun initEvent() {
        with(binding) {
            btnRegister.setOnClickListener {
                val userName = "test13@gmail.com"
                val password = "123456"
                viewModel.register(email = userName, password = password) {
                    if (it) {
                        val direction = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(userName, password)
                        findNavController().navigate(direction)
                    } else {
                        Toast.makeText(requireContext(), "Register failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }
}