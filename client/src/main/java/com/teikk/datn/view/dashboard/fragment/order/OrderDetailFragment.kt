package com.teikk.datn.view.dashboard.fragment.order

import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.data.model.Feedback
import com.teikk.datn.data.model.advanced.ProductOrderItem
import com.teikk.datn.databinding.FragmentOrderDetailBinding
import com.teikk.datn.view.dashboard.DashBoardActivity
import com.teikk.datn.view.dashboard.DashBoardViewModel
import com.teikk.datn.view.dashboard.adapter.OrderItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderDetailFragment(
) : BaseFragment<FragmentOrderDetailBinding>() {
    private val args by navArgs<OrderDetailFragmentArgs>()
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private val orderItemAdapter by lazy {
        OrderItemAdapter(args.isFeedback)
    }
    override fun getLayoutResId(): Int {
        return R.layout.fragment_order_detail
    }

    override fun init() {
        (requireActivity() as DashBoardActivity).closeDrawerAndHideBottomNav()
        if (!args.isFeedback) {
            binding.btnSubmit.visibility = View.GONE
        }else {
            binding.btnSubmit.visibility = View.GONE
        }
    }

    override fun initEvent() {
        with(binding) {
            rcvCart.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                adapter = orderItemAdapter
            }
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            btnSubmit.setOnClickListener {
                val ratingMap = orderItemAdapter.ratingMap
                val titleMap = orderItemAdapter.textMap

                orderItemAdapter.currentList.forEachIndexed { index, productOrderItem ->
                    val feedback = Feedback(
                        id = "",
                        userId = viewModel.uid,
                        productId = productOrderItem.product.id,
                        title = titleMap[index] ?: "",
                        star = ratingMap[index] ?: 5,
                    )
                    viewModel.sendFeedback(feedback)
                }
                findNavController().navigateUp()
            }
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            combine(viewModel.products, viewModel.orderItem) { products, orderItem ->
                products.filter { product ->
                    orderItem.any {it.productId == product.id}
                }.map { product ->
                    val orderItem = orderItem.find { it.productId == product.id }
                    ProductOrderItem(
                        product = product,
                        orderItem = orderItem!!
                    )
                }
            }.collectLatest {
                orderItemAdapter.submitList(it.toList())
                val subTotal = it.sumOf {
                    it.orderItem.price
                }
                binding.txtTotal.text = (subTotal).toString() + "$"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as DashBoardActivity).showBottomNav()
    }

    override fun onDestroy() {
        (requireActivity() as DashBoardActivity).showBottomNav()
        super.onDestroy()
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
}