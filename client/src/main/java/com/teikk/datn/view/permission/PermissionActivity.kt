package com.teikk.datn.view.permission

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseActivity
import com.teikk.datn.databinding.ActivityPermissionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionActivity : BaseActivity<ActivityPermissionBinding>() {
    private lateinit var navController: NavController
    override fun getLayoutResId(): Int {
        return R.layout.activity_permission
    }
    override fun init() {
        navController = findNavController(R.id.permissionNavController)
    }

    companion object {
        const val TAG = "PermissionActivity-TAG"
    }
}