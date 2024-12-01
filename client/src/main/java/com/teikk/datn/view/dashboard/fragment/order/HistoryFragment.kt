package com.teikk.datn.view.dashboard.fragment.order

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentHistoryBinding
import com.teikk.datn.view.dashboard.DashBoardViewModel
import com.teikk.datn.view.dashboard.adapter.OrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint

class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private val orderAdapter by lazy {
        OrderAdapter()
    }
    override fun getLayoutResId(): Int {
        return R.layout.fragment_history
    }

    override fun init() {
        with(binding) {
            rcvHistory.setHasFixedSize(true)
            rcvHistory.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            rcvHistory.addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(
                    requireContext(),
                    androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
                )
            )
            rcvHistory.adapter = orderAdapter
        }
    }

    override fun initEvent() {
        orderAdapter.listener = { item, position ->
            viewModel.fetchOrderItemData(item.id)
            val action = OrderFragmentDirections.actionOrderFragmentToOrderDetailFragment()
            findNavController().navigate(action)
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orders.map {
                it.filter { it.status == "Success" }
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