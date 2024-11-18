package com.teikk.datn.view.dashboard.fragment

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentPaymentMethodBinding
import com.teikk.datn.view.dashboard.DashBoardViewModel
import com.teikk.datn.view.dashboard.adapter.PaymentMethodAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentMethodFragment : BaseFragment<FragmentPaymentMethodBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private lateinit var adapter: PaymentMethodAdapter
    override fun getLayoutResId(): Int {
        return R.layout.fragment_payment_method
    }

    override fun init() {
        adapter = PaymentMethodAdapter()
        with(binding) {
            rcvPaymentMethod.setHasFixedSize(true)
            rcvPaymentMethod.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            rcvPaymentMethod.addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(
                    requireContext(),
                    androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
                )
            )
            rcvPaymentMethod.adapter = adapter
        }
    }

    override fun initObserver() {
        viewModel.paymentMethod.observe(viewLifecycleOwner) {
            Log.d("klajsflkdsjfklasdf", it.toString())
            adapter.submitList(it)
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
}