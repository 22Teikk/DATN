package com.teikk.datn.view.dashboard.fragment

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.GridSpacingItemDecoration
import com.teikk.datn.data.model.Wishlist
import com.teikk.datn.data.model.advanced.ProductWishlist
import com.teikk.datn.databinding.FragmentFavoriteBinding
import com.teikk.datn.view.dashboard.DashBoardActivity
import com.teikk.datn.view.dashboard.DashBoardViewModel
import com.teikk.datn.view.dashboard.adapter.ProductWishlistAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private val favoriteAdapter by lazy {
        ProductWishlistAdapter()
    }
    override fun getLayoutResId(): Int {
        return R.layout.fragment_favorite
    }

    override fun init() {
        with(binding) {
            rcvFavorite.apply {
                setHasFixedSize(true)
                layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2)
                addItemDecoration(GridSpacingItemDecoration(2, 15, includeEdge = true))
                adapter = favoriteAdapter
            }
        }
    }

    override fun initEvent() {
        with (binding) {
            swipeRefresh.setOnRefreshListener {
                viewModel.fetchWishlistData()
                swipeRefresh.isRefreshing = false
            }
        }
        favoriteAdapter.favoriteListener = { item, position ->
            val product = item.product
            if (item.wishlist != null) {
                viewModel.deleteWishlist(Wishlist(userId = viewModel.uid, productId = product.id, id = item.wishlist!!.id))
            } else {
                viewModel.insertWishlist(Wishlist(userId = viewModel.uid, productId = product.id, id = ""))
            }
            favoriteAdapter.notifyItemChanged(position)
        }
        favoriteAdapter.listener = { item, position ->
            val direction = FavoriteFragmentDirections.actionFavoriteFragmentToProductDetailFragment(item.product, item.wishlist)
            findNavController().navigate(direction)
        }
    }

    override fun initObserver() {
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                combine(viewModel.products, viewModel.wishlist) { products, wishlists ->
                    products.filter { product ->
                        wishlists.any { it.productId == product.id } // Kiểm tra nếu sản phẩm có trong wishlist
                    }.map { product ->
                        val wishlistItem = wishlists.find { it.productId == product.id }
                        ProductWishlist(
                            product = product,
                            wishlist = wishlistItem
                        )
                    }
                }.collectLatest {
                    favoriteAdapter.submitList(it)
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