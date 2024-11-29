package com.teikk.datn.view.dashboard.fragment

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.teikk.datn.MyApp
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentLoginBinding
import com.teikk.datn.databinding.FragmentMapBinding
import com.teikk.datn.view.dashboard.DashBoardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_map
    }

    override fun init() {
        val store = MyApp.store
        with(binding) {
            Log.d("sdjkfhsakjfhajsdkf", store.toString())
            Glide.with(requireContext()).load(store.imageUrl).into(imgStore)
            txtName.text = store.name
            txtDescription.text = store.description
            txtAddress.text = store.address
            txtTime.text = store.openTime + " - " + store.closeTime
            edtEmail.setText(store.email)
            edtPhone.setText(store.phone)
        }
    }

    override fun initEvent() {
        with(binding) {
            btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:${MyApp.store.phone}")
                }
                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    requireContext().startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "No dialer app available", Toast.LENGTH_SHORT).show()
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