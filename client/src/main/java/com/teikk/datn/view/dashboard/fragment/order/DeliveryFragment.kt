package com.teikk.datn.view.dashboard.fragment.order

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentDeliveryBinding
import com.teikk.datn.databinding.FragmentUpcomingBinding
import com.teikk.datn.utils.ShareConstant.PAYMENT_METHOD_ID
import com.teikk.datn.view.dashboard.DashBoardViewModel
import com.teikk.datn.view.dashboard.adapter.OrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeliveryFragment : BaseFragment<FragmentDeliveryBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private val orderAdapter by lazy {
        OrderAdapter()
    }
    override fun getLayoutResId(): Int {
        return R.layout.fragment_delivery
    }

    override fun init() {
        with(binding) {
            rcvDelivery.setHasFixedSize(true)
            rcvDelivery.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            rcvDelivery.addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(
                    requireContext(),
                    androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
                )
            )
            rcvDelivery.adapter = orderAdapter
        }
    }

    override fun initEvent() {
        orderAdapter.listener = { item, position ->
            viewModel.fetchOrderItemData(item.id)
            val action = OrderFragmentDirections.actionOrderFragmentToOrderDetailFragment(isFeedback = true)
            findNavController().navigate(action)
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orders.map {
                it.filter { it.status == "Delivered" }
            }.collectLatest {
                orderAdapter.submitList(it)
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