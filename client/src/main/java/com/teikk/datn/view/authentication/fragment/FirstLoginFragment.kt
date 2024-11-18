package com.teikk.datn.view.authentication.fragment

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.setSafeOnClickListener
import com.teikk.datn.base.showShortToast
import com.teikk.datn.data.model.Singleton
import com.teikk.datn.databinding.FragmentFirstLoginBinding
import com.teikk.datn.utils.Resource
import com.teikk.datn.utils.uriToFile
import com.teikk.datn.view.authentication.AuthenticationViewModel
import com.teikk.datn.view.dashboard.DashBoardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstLoginFragment : BaseFragment<FragmentFirstLoginBinding>() {
    private val viewModel: AuthenticationViewModel by activityViewModels()
    private var imgUri: Uri? = null

    override fun getLayoutResId(): Int {
        return R.layout.fragment_first_login
    }

    override fun init() {
    }

    override fun initEvent() {
        with(binding) {
            btnSelectImage.setSafeOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                }
                arlBackground.launch(intent)
            }
            btnCreate.setSafeOnClickListener {
                var imgUrl = ""
                imgUri?.let {
                    requireContext().uriToFile(it)?.let { it1 ->
                        viewModel.uploadFile(it1) { url ->
                            if (url != null) {
                                imgUrl = url
                            }
                        }
                    }
                }
                val user = viewModel.user.value!!.data!!.copy(
                    name = edtName.text.toString(),
                    phone = edtPhone.text.toString(),
                    address = edtAddress.text.toString(),
                    imageUrl = imgUrl
                )
                Singleton.user = user
                viewModel.updateUser(user)
            }
        }
    }

    override fun initObserver() {
        viewModel.userSuccess.observe(this) {
            when (it) {
                is Resource.Success -> {
                    Log.d("sjkhsajklfahsdf", "Come here")
                    openActivity(DashBoardActivity::class.java, isClearBackStack = true)
                }
                is Resource.Error -> {
                    showShortToast(it.message.toString())
                }
                is Resource.Loading -> {

                }
            }
        }
    }

    val arlBackground =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    imgUri = data.data
                }
            }
        }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }
}