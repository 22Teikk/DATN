package com.teikk.datn.view.dashboard.fragment

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.setSafeOnClickListener
import com.teikk.datn.databinding.FragmentExploreBinding
import com.teikk.datn.view.dashboard.DashBoardActivity
import com.teikk.datn.view.dashboard.DashBoardViewModel
import com.teikk.datn.view.dashboard.adapter.CategoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreFragment : BaseFragment<FragmentExploreBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private val categoryAdapter by lazy {
        CategoryAdapter()
    }
    override fun getLayoutResId(): Int {
        return R.layout.fragment_explore
    }

    override fun init() {
        with(binding) {
            rcvCategory.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                adapter = categoryAdapter
            }
        }
    }

    override fun initEvent() {
        with (binding) {
            btnMenu.setSafeOnClickListener {
                (requireActivity() as DashBoardActivity).openDrawer()
            }
        }
        categoryAdapter.listener = { item, position ->
            categoryAdapter.selectedPosition = position
            categoryAdapter.notifyDataSetChanged()
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.products.map {
                    it.filter {
                        it.categoryId == categoryAdapter.currentList[categoryAdapter.selectedPosition].id
                    }
                }.collectLatest { products ->
                    Log.d("sdjfkhdsjkfd", products.size.toString())
                }
            }
        }
    }

    override fun initObserver() {
        with(binding) {
            viewModel.user.observe(viewLifecycleOwner) {
                Glide.with(root).load(it.data?.imageUrl).into(imgAvatar)
                txtAddress.text = it.data?.address
            }
            viewModel.category.observe(viewLifecycleOwner) {
                categoryAdapter.submitList(it)
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.products.map {
                    it.filter {
                        it.categoryId == categoryAdapter.currentList[categoryAdapter.selectedPosition].id
                    }
                }.collectLatest { products ->
                    Log.d("sdjfkhdsjkfd", products.size.toString())
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