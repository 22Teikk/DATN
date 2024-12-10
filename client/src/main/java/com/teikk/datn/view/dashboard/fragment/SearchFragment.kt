package com.teikk.datn.view.dashboard.fragment

import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.GridSpacingItemDecoration
import com.teikk.datn.data.model.Wishlist
import com.teikk.datn.data.model.advanced.ProductWishlist
import com.teikk.datn.databinding.FragmentLoginBinding
import com.teikk.datn.databinding.FragmentSearchBinding
import com.teikk.datn.utils.getAddressByLocation
import com.teikk.datn.view.dashboard.DashBoardActivity
import com.teikk.datn.view.dashboard.DashBoardViewModel
import com.teikk.datn.view.dashboard.adapter.ProductWishlistAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private val productAdapter by lazy {
        ProductWishlistAdapter()
    }
    override fun getLayoutResId(): Int {
        return R.layout.fragment_search
    }

    override fun init() {
        (requireActivity() as DashBoardActivity).closeDrawerAndHideBottomNav()
        with(binding) {
            rcvProduct.apply {
                setHasFixedSize(true)
                layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2)
                addItemDecoration(GridSpacingItemDecoration(2, 15, includeEdge = true))
                adapter = productAdapter
            }
        }
    }

    override fun initEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.svProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        combine(viewModel.products, viewModel.wishlist) { products, wishlists ->
                            products.filter { product ->
                                product.name.contains(p0, ignoreCase = true) // Lọc sản phẩm theo tên
                            }.map { product ->
                                val wishlistItem = wishlists.find { it.productId == product.id }
                                ProductWishlist(
                                    product = product,
                                    wishlist = wishlistItem // Truyền vào wishlist nếu tìm thấy, ngược lại là null
                                )
                            }
                        }.collectLatest {
                            productAdapter.submitList(it)
                        }
                    }
                }
                return true
            }
        })

        productAdapter.favoriteListener = { item, position ->
            val product = item.product
            if (item.wishlist != null) {
                viewModel.deleteWishlist(Wishlist(userId = viewModel.uid, productId = product.id, id = item.wishlist!!.id))
            } else {
                viewModel.insertWishlist(Wishlist(userId = viewModel.uid, productId = product.id, id = ""))
            }
            productAdapter.notifyItemChanged(position)
        }
        productAdapter.listener = { item, position ->
            val direction = FavoriteFragmentDirections.actionFavoriteFragmentToProductDetailFragment(item.product, item.wishlist)
            findNavController().navigate(direction)
        }
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