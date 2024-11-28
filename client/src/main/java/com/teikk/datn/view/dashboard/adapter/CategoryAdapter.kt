package com.teikk.datn.view.dashboard.adapter

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.teikk.datn.R
import com.teikk.datn.base.BaseListAdapter
import com.teikk.datn.data.model.Category
import com.teikk.datn.databinding.ItemCategoryBinding

class CategoryAdapter : BaseListAdapter<Category, ItemCategoryBinding>(DIFF_CALLBACK) {
    var selectedPosition = 0
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }

        }
    }
    override fun getLayout(viewType: Int): Int {
        return R.layout.item_category
    }

    override fun onBindViewHolder(
        holder: BaseListAdapter.Companion.BaseViewHolder<ItemCategoryBinding>,
        position: Int
    ) {
        val category = getItem(position)
        with(holder.binding) {
            Glide.with(root).load(category.imageUrl).into(imgCategory)
            txtCategory.text = category.name
            if (position == selectedPosition) {
                layoutCategory.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.orange))
                txtCategory.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.white)))
            } else {
                layoutCategory.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.white))
                txtCategory.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.black)))
            }
            root.setOnClickListener {
                if (position != selectedPosition) {
                    listener?.invoke(category, position)
                }
            }
        }
    }
}