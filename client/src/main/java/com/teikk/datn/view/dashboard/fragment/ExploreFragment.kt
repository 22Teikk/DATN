package com.teikk.datn.view.dashboard.fragment

import android.content.Intent
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.GridSpacingItemDecoration
import com.teikk.datn.base.setSafeOnClickListener
import com.teikk.datn.data.model.Wishlist
import com.teikk.datn.data.model.advanced.ProductWishlist
import com.teikk.datn.databinding.FragmentExploreBinding
import com.teikk.datn.view.dashboard.DashBoardActivity
import com.teikk.datn.view.dashboard.DashBoardViewModel
import com.teikk.datn.view.dashboard.adapter.CategoryAdapter
import com.teikk.datn.view.dashboard.adapter.ProductWishlistAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreFragment : BaseFragment<FragmentExploreBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private val categoryAdapter by lazy {
        CategoryAdapter()
    }
    private val productWishlistAdapter by lazy {
        ProductWishlistAdapter()
    }
    override fun getLayoutResId(): Int {
        return R.layout.fragment_explore
    }

    override fun init() {
        with(binding) {
            rcvCategory.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                adapter = categoryAdapter
            }
            rcvProduct.apply {
                setHasFixedSize(true)
                layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2)
                addItemDecoration(GridSpacingItemDecoration(2, 15, includeEdge = true))
                adapter = productWishlistAdapter
            }
        }
    }

    override fun initEvent() {
        with (binding) {
            btnMenu.setSafeOnClickListener {
                (requireActivity() as DashBoardActivity).openDrawer()
            }
            swipeRefresh.setOnRefreshListener {
                viewModel.fetchProductData()
                swipeRefresh.isRefreshing = false
            }
            svProduct.setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    findNavController().navigate(R.id.searchFragment)
                }
            }
        }
        categoryAdapter.listener = { item, position ->
            categoryAdapter.selectedPosition = position
            categoryAdapter.notifyDataSetChanged()
            viewLifecycleOwner.lifecycleScope.launch {
                combine(viewModel.products, viewModel.wishlist) { products, wishlists ->
                    products.filter {
                        it.categoryId == categoryAdapter.currentList[categoryAdapter.selectedPosition].id
                    }.map { product ->
                        ProductWishlist(
                            product = product,
                            wishlist = wishlists.find { it.productId == product.id },
                        )
                    }
                }.collectLatest {
                    productWishlistAdapter.submitList(it)
                    Log.d("ProductWishlist", it.size.toString())
                }
            }
        }
        productWishlistAdapter.favoriteListener = { item, position ->
            val product = item.product
            if (item.wishlist != null) {
                viewModel.deleteWishlist(Wishlist(userId = viewModel.uid, productId = product.id, id = item.wishlist!!.id))
            } else {
                viewModel.insertWishlist(Wishlist(userId = viewModel.uid, productId = product.id, id = ""))
            }
            productWishlistAdapter.notifyItemChanged(position)
        }
        productWishlistAdapter.listener = {item, position ->
            val direction = ExploreFragmentDirections.actionExploreFragmentToProductDetailFragment(item.product, item.wishlist)
            findNavController().navigate(direction)
        }
    }

    override fun initObserver() {
        with(binding) {
            viewModel.user.observe(viewLifecycleOwner) {
                Glide.with(root).load(it.data?.imageUrl).into(imgAvatar)
                txtAddress.text = it.data?.address
            }
            viewModel.category.observe(viewLifecycleOwner) {
                categoryAdapter.submitList(it)
                if (it.isNotEmpty()) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        combine(viewModel.products, viewModel.wishlist) { products, wishlists ->
                            products.map { product ->
                                val wishlistItem = wishlists.find { it.productId == product.id }
                                ProductWishlist(
                                    product = product,
                                    wishlist = wishlistItem
                                )
                            }
                        }.collectLatest {
                            productWishlistAdapter.submitList(it)
                            Log.d("ProductWishlist", it.size.toString())
                        }
                    }
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