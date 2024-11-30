package com.teikk.datn.view.dashboard.adapter

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.teikk.datn.R
import com.teikk.datn.base.BaseListAdapter
import com.teikk.datn.data.model.advanced.ProductWishlist
import com.teikk.datn.databinding.ItemProductBinding

class ProductWishlistAdapter : BaseListAdapter<ProductWishlist, ItemProductBinding>(DIFF_CALLBACK) {
    var favoriteListener: ((item: ProductWishlist, position: Int) -> Unit)? = null
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductWishlist>() {
            override fun areItemsTheSame(oldItem: ProductWishlist, newItem: ProductWishlist): Boolean {
                return oldItem.product.id == newItem.product.id
            }

            override fun areContentsTheSame(oldItem: ProductWishlist, newItem: ProductWishlist): Boolean {
                return oldItem == newItem
            }

        }
    }
    override fun getLayout(viewType: Int): Int {
        return R.layout.item_product
    }

    override fun onBindViewHolder(
        holder: BaseListAdapter.Companion.BaseViewHolder<ItemProductBinding>,
        position: Int
    ) {
        val productWishlist = getItem(position)
        val product = productWishlist.product
        with(holder.binding) {
            Glide.with(root).load(product.thumbnail).error(R.drawable.background_login).into(imgProduct)
            txtName.text = product.name
            txtDuration.text = "Duration: " + product.totalTime
            txtPrice.text = product.price.toString() + "$"
            if (productWishlist.wishlist != null) {
                btnFavorite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.orange))
            } else {
                btnFavorite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.white))
            }
            btnFavorite.setOnClickListener {
                favoriteListener?.invoke(productWishlist, position)
            }
            root.setOnClickListener {
                listener?.invoke(productWishlist, position)
            }
        }
    }
}