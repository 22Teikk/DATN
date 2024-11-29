package com.teikk.datn.view.dashboard.adapter

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.teikk.datn.R
import com.teikk.datn.base.BaseListAdapter
import com.teikk.datn.data.model.Product
import com.teikk.datn.databinding.ItemProductBinding

class ProductAdapter : BaseListAdapter<Product, ItemProductBinding>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
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
        val product = getItem(position)
        with(holder.binding) {
            Glide.with(root).load(product.thumbnail).error(R.drawable.background_login).into(imgProduct)
            txtName.text = product.name
            txtDuration.text = "Duration: " + product.totalTime
            txtPrice.text = product.price.toString() + "$"
        }
    }
}