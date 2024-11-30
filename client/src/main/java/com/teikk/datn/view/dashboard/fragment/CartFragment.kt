package com.teikk.datn.view.dashboard.fragment

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.data.model.advanced.ProductCart
import com.teikk.datn.databinding.FragmentCartBinding
import com.teikk.datn.view.dashboard.DashBoardActivity
import com.teikk.datn.view.dashboard.DashBoardViewModel
import com.teikk.datn.view.dashboard.adapter.CartAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private var discount = 0.0
    private var subTotal = 0.0
    private val cartAdapter by lazy {
        CartAdapter()
    }
    override fun getLayoutResId(): Int {
        return R.layout.fragment_cart
    }

    override fun init() {
        with(binding) {
        }
    }

    override fun initEvent() {
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                viewModel.fetchCartData()
                swipeRefresh.isRefreshing = false
            }
            rcvCart.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                adapter = cartAdapter
            }
        }
        cartAdapter.listener = {item, position ->
            cartAdapter.notifyItemChanged(position)
            subTotal = cartAdapter.currentList.sumOf {
                it.product.price * it.cart.quantity
            }
            binding.txtSubtotal.text = "$subTotal$"
            binding.txtTotal.text = (subTotal - discount).toString() + "$"
        }
        cartAdapter.deleteListener = {item, position ->
            viewModel.deleteCart(item.cart)
            cartAdapter.notifyItemRemoved(position)
            subTotal = cartAdapter.currentList.sumOf {
                it.product.price * it.cart.quantity
            }
            binding.txtSubtotal.text = "$subTotal$"
            binding.txtTotal.text = (subTotal - discount).toString() + "$"
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            combine(viewModel.products, viewModel.carts) { products, carts ->
                products.filter { product ->
                    carts.any {it.productId == product.id}
                }.map { product ->
                    val cart = carts.find { it.productId == product.id }
                    ProductCart(
                        product = product,
                        cart = cart!!
                    )
                }
            }.collectLatest {
                cartAdapter.submitList(it)
                subTotal = it.sumOf {
                    it.product.price * it.cart.quantity
                }
                binding.txtCount.text = it.size.toString() + " items"
                binding.txtSubtotal.text = subTotal.toString() + "$"
                binding.txtTotal.text = (subTotal - discount).toString() + "$"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as DashBoardActivity).showBottomNav()
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }
}