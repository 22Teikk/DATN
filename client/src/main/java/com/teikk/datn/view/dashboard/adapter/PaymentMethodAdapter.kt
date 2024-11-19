package com.teikk.datn.view.dashboard.adapter

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.teikk.datn.R
import com.teikk.datn.base.BaseListAdapter
import com.teikk.datn.data.model.PaymentMethod
import com.teikk.datn.databinding.ItemPaymentMethodBinding

class PaymentMethodAdapter(var paymentMethodID: String): BaseListAdapter<PaymentMethod, ItemPaymentMethodBinding>(LIST_PAYMENT_METHOD_DIFF_CALLBACK) {
    companion object {
        val LIST_PAYMENT_METHOD_DIFF_CALLBACK = object : DiffUtil.ItemCallback<PaymentMethod>() {
            override fun areItemsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getLayout(viewType: Int): Int {
        return R.layout.item_payment_method
    }

    override fun onBindViewHolder(
        holder: BaseListAdapter.Companion.BaseViewHolder<ItemPaymentMethodBinding>,
        position: Int
    ) {
        with (holder.binding) {
            val paymentMethod = getItem(position)
            txtName.text = paymentMethod.name
            Glide.with(root).load(paymentMethod.imageUrl).into(imgIcon)
            rdSelect.isChecked = paymentMethod.id == paymentMethodID
            rdSelect.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    paymentMethodID = paymentMethod.id
                    listener?.invoke(paymentMethod, position)
                }
            }
        }
    }

}