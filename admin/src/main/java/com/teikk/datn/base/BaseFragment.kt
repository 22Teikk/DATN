package com.teikk.datn.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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
    }


    protected abstract fun getLayoutResId(): Int
    protected abstract fun init()


    fun openActivity(context : Context, clazz2: Class<*>, first : Boolean){
        val intent = Intent(context, clazz2)
        intent.putExtra("FIRST_APP", first)
        context.startActivity(intent)
    }

    fun openActivityWithData(context: Context, clazz: Class<*>, data: Any, data2: Any) {
        val intent = Intent(context, clazz)
        when (data) {
            is Int -> intent.putExtra("DATA", data)
            is String -> intent.putExtra("DATA", data)
            is Boolean -> intent.putExtra("DATA", data)
        }
        when (data2) {
            is Int -> intent.putExtra("DATA2", data2)
            is String -> intent.putExtra("DATA2", data2)
            is Boolean -> intent.putExtra("DATA2", data2)
        }
        context.startActivity(intent)
    }

    fun openActivityWithPath(context : Context, clazz2: Class<*>, paths : String){
        val intent = Intent(context, clazz2)
        intent.putExtra("PATHS", paths)
        context.startActivity(intent)
    }

    fun openActivityWithObj(context : Context, clazz2: Class<*>, obj : Serializable){
        val intent = Intent(context, clazz2)
        intent.putExtra("OBJ", obj)
        context.startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
    }
}