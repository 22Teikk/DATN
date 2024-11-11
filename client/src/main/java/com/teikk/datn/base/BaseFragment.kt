package com.teikk.datn.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import java.io.Serializable

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    protected lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (requireActivity().supportFragmentManager.backStackEntryCount == 0) {
                requireActivity().finish()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initView()
        initEvent()
        initObserver()
    }

    fun openActivity(clazz2: Class<*>, isClearBackStack: Boolean){
        val intent = Intent(requireContext(), clazz2)
        if (isClearBackStack) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        requireContext().startActivity(intent)
    }

    protected abstract fun getLayoutResId(): Int
    protected open fun init() {}
    protected open fun initView() {}
    protected open fun initEvent() {}
    protected open fun initObserver() {}

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    protected abstract val onBackPressedCallback: OnBackPressedCallback
}

fun Fragment.showShortToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
fun Fragment.showLongToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()