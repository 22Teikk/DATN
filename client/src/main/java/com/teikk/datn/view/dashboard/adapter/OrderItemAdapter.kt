package com.teikk.datn.view.dashboard.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.teikk.datn.R
import com.teikk.datn.base.BaseListAdapter
import com.teikk.datn.data.model.advanced.ProductOrderItem
import com.teikk.datn.databinding.ItemOrderItemBinding

class OrderItemAdapter(
    private val isFeedback: Boolean
) : BaseListAdapter<ProductOrderItem, ItemOrderItemBinding>(DIFF_CALLBACK) {

    val ratingMap = mutableMapOf<Int, Int>()
    val textMap = mutableMapOf<Int, String>()
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductOrderItem>() {
            override fun areItemsTheSame(oldItem: ProductOrderItem, newItem: ProductOrderItem): Boolean {
                return oldItem.orderItem.id == newItem.orderItem.id
            }

            override fun areContentsTheSame(oldItem: ProductOrderItem, newItem: ProductOrderItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun getLayout(viewType: Int): Int {
        return R.layout.item_order_item
    }

    override fun onBindViewHolder(
        holder: BaseListAdapter.Companion.BaseViewHolder<ItemOrderItemBinding>,
        position: Int
    ) {
        val item = getItem(position)
        val product = item.product
        val orderItem = item.orderItem
        with(holder.binding) {
            ratingBar.rating = ratingMap[position]?.toFloat() ?: 5f
            edtFeedback.setText(textMap[position] ?: "")
            Glide.with(root).load(product.thumbnail).into(imgCart)
            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                ratingMap[position] = rating.toInt()
            }
            txtName.text = product.name
            txtPrice.text = "$ " + product.price.toString()
            txtTotal.text = orderItem.quantity.toString()
            edtFeedback.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textMap[position] = s.toString()
                }
                override fun afterTextChanged(s: Editable?) {}
            })
            if (!isFeedback) {
                edtFeedback.visibility = View.GONE
                ratingBar.visibility = View.GONE
            }
        }
    }

}