package com.teikk.datn.view.dashboard.fragment

import android.content.res.ColorStateList
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.setSafeOnClickListener
import com.teikk.datn.data.model.Wishlist
import com.teikk.datn.databinding.FragmentProductDetailBinding
import com.teikk.datn.view.dashboard.DashBoardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private val args by navArgs<ProductDetailFragmentArgs>()
    private var total = 0
    override fun getLayoutResId(): Int {
        return R.layout.fragment_product_detail
    }

    override fun init() {
        val product = args.product
        with(binding) {
            txtName.text = product.name
            txtDuration.text = "Duration: " + product.totalTime
            txtPrice.text = product.price.toString() + "$"
            txtTotal.text = total.toString()
            txtQuantitySold.text = product.quantity.toString()
            if (args.wishlist != null) {
                btnFavorite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.orange))
            } else {
                btnFavorite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.white))
            }
        }
    }

    override fun initEvent() {
        with(binding) {
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
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }
}