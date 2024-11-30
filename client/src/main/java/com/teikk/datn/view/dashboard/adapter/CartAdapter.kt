package com.teikk.datn.view.dashboard.adapter

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.teikk.datn.R
import com.teikk.datn.base.BaseListAdapter
import com.teikk.datn.data.model.advanced.ProductCart
import com.teikk.datn.databinding.ItemCartBinding

class CartAdapter : BaseListAdapter<ProductCart, ItemCartBinding>(DIFF_CALLBACK) {
    var deleteListener: ((item: ProductCart, position: Int) -> Unit)? = null
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductCart>() {
            override fun areItemsTheSame(oldItem: ProductCart, newItem: ProductCart): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ProductCart, newItem: ProductCart): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun getLayout(viewType: Int): Int {
        return R.layout.item_cart
    }

    override fun onBindViewHolder(
        holder: BaseListAdapter.Companion.BaseViewHolder<ItemCartBinding>,
        position: Int
    ) {
        val item = getItem(position)
        val product = item.product
        val cart = item.cart
        with(holder.binding) {
            Glide.with(root).load(product.thumbnail).into(imgCart)
            txtName.text = product.name
            txtPrice.text = "$ " + product.price.toString()
            txtTotal.text = cart.quantity.toString()
            btnMinus.setOnClickListener {
                if (cart.quantity > 1) {
                    cart.quantity--
                    listener?.invoke(item, position)
                }
            }
            btnPlus.setOnClickListener {
                cart.quantity++
                listener?.invoke(item, position)
            }
            btnDelete.setOnClickListener {
                deleteListener?.invoke(item, position)
            }
        }
    }

}