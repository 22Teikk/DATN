package com.teikk.datn.view.dashboard.fragment

import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.setSafeOnClickListener
import com.teikk.datn.data.model.Order
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
            btnCheckout.setSafeOnClickListener {
                val order = Order(total = txtCount.text.toString().toInt(), userId = viewModel.uid, description = edtDescription.text.toString(), isShipment = radShipment.isChecked)
                viewModel.createOrder(order, cartAdapter.currentList)
                initObserver()
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
            if (cartAdapter.currentList.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.layoutData.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutData.visibility = View.VISIBLE
                binding.txtCount.text = cartAdapter.currentList.size.toString()
                binding.txtSubtotal.text = subTotal.toString() + "$"
                binding.txtTotal.text = (subTotal - discount).toString() + "$"
            }
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
                if (it.isEmpty()) {
                    binding.layoutEmpty.visibility = View.VISIBLE
                    binding.layoutData.visibility = View.GONE
                } else {
                    binding.layoutEmpty.visibility = View.GONE
                    binding.layoutData.visibility = View.VISIBLE
                    binding.txtCount.text = it.size.toString()
                    binding.txtSubtotal.text = subTotal.toString() + "$"
                    binding.txtTotal.text = (subTotal - discount).toString() + "$"
                }
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