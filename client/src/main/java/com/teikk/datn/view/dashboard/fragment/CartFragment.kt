package com.teikk.datn.view.dashboard.fragment

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentCartBinding
import com.teikk.datn.view.dashboard.DashBoardActivity
import com.teikk.datn.view.dashboard.DashBoardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    override fun getLayoutResId(): Int {
        return R.layout.fragment_cart
    }

    override fun init() {
        with(binding) {
            rcvPrice.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        }
    }

    override fun initEvent() {
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                viewModel.fetchCartData()
                swipeRefresh.isRefreshing = false
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