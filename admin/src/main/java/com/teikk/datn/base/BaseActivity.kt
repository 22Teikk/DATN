package com.teikk.datn.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import com.teikk.datn.base.HideNavigation.Companion.systemBarBlack
import javax.inject.Inject


abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    @Inject
    lateinit var networkLiveData: NetworkLiveData

    @Inject
    lateinit var networkHelper: NetworkHelper

    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutResId())
        HideNavigation.fullScreenCall(this)
        HideKeyboard.setupUI(binding.root, this)
        systemBarBlack(true)
        init()
        initData()
        initObserve()
        initView()
        initEvent()
    }

    protected abstract fun getLayoutResId(): Int
    protected open fun init() {}
    protected open fun initData() {}
    protected open fun initObserve() {}
    protected open fun initView() {}
    protected open fun initEvent() {}


    protected fun internetLiveData() : LiveData<Boolean> {
        return networkLiveData
    }

    protected fun checkInternet(): Boolean {
        return networkHelper.isNetworkConnected()
    }

}
