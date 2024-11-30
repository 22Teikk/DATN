package com.teikk.datn.view.dashboard.fragment

import android.content.res.ColorStateList
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.setSafeOnClickListener
import com.teikk.datn.data.model.Cart
import com.teikk.datn.data.model.Wishlist
import com.teikk.datn.databinding.FragmentProductDetailBinding
import com.teikk.datn.view.dashboard.DashBoardActivity
import com.teikk.datn.view.dashboard.DashBoardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private val args by navArgs<ProductDetailFragmentArgs>()
    private var total = 1
    override fun getLayoutResId(): Int {
        return R.layout.fragment_product_detail
    }

    override fun init() {
        (requireActivity() as DashBoardActivity).closeDrawerAndHideBottomNav()
        val product = args.product
        with(binding) {
            Glide.with(root).load(product.thumbnail).into(imgProduct)
            txtName.text = product.name
            txtDuration.text = "Duration: " + product.totalTime
            txtPrice.text = product.price.toString() + "$"
            txtTotal.text = total.toString()
            txtQuantitySold.text = product.quantity.toString()
            txtDescription.text = product.description
            if (args.wishlist != null) {
                btnFavorite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.orange))
            } else {
                btnFavorite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.white))
            }
        }
    }

    override fun initEvent() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            btnMinus.setOnClickListener {
                if (total > 1){
                    total--;
                    txtTotal.text = total.toString()
                }
            }
            btnPlus.setOnClickListener {
                total++;
                txtTotal.text = total.toString()
            }
            btnFavorite.setSafeOnClickListener {
                if (args.wishlist != null) {
                    viewModel.deleteWishlist(args.wishlist!!)
                    btnFavorite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.white))
                } else {
                    viewModel.insertWishlist(Wishlist("", viewModel.uid, args.product.id))
                    btnFavorite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.orange))
                }
            }
            btnAdd.setSafeOnClickListener {
                val cart = Cart("", userId = viewModel.uid, args.product.id, quantity = txtTotal.text.toString().toInt())
                viewModel.insertCart(cart)
                Toast.makeText(requireContext(), "Add To cart", Toast.LENGTH_SHORT).show()
            }
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