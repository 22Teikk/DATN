package com.teikk.datn.view.authentication.fragment

import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.showShortToast
import com.teikk.datn.data.model.Singleton
import com.teikk.datn.databinding.FragmentLoginBinding
import com.teikk.datn.utils.Resource
import com.teikk.datn.view.authentication.AuthenticationViewModel
import com.teikk.datn.view.dashboard.DashBoardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val args : LoginFragmentArgs by navArgs()
    private val viewModel by activityViewModels<AuthenticationViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.fragment_login
    }

    override fun init() {
    }

    override fun initView() {
        with(binding) {
            edtPassword.setText(args.password)
            edtEmail.setText(args.username)
        }
    }

    override fun initEvent() {
        with(binding) {
            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            btnLogin.setOnClickListener {
                viewModel.login(edtEmail.text.toString(), edtPassword.text.toString())
            }
        }
    }

    override fun initObserver() {
        viewModel.user.observe(this) {
            when (it) {
                is Resource.Success -> {
                   if (it.data != null && it.data.address.isNotBlank()) {
                        Log.d("sdljkfhasdfjkl", it.data.address)
                        Log.d("sdljkfhasdfjkl", it.data.address.isNotBlank().toString())
                        Log.d("sdljkfhasdfjkl", it.data.address.isNotEmpty().toString())
                        Singleton.user = it.data
                        openActivity(DashBoardActivity::class.java, isClearBackStack = true)
                    } else {
                        findNavController().navigate(R.id.action_loginFragment_to_firstLoginFragment)
                    }
                }
                is Resource.Error -> {
                    showShortToast(it.message.toString())
                }
                is Resource.Loading -> {

                }
            }
        }
    }

    override fun onResume() {
        if (args.username != "" && args.password != "") {
            binding.edtEmail.setText(args.username)
            binding.edtPassword.setText(args.password)
        }
        super.onResume()
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }
}