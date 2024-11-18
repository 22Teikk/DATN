package com.teikk.datn.view.dashboard.fragment

import android.content.Intent
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.setSafeOnClickListener
import com.teikk.datn.data.model.UserProfile
import com.teikk.datn.databinding.FragmentProfileBinding
import com.teikk.datn.utils.Resource
import com.teikk.datn.utils.uriToFile
import com.teikk.datn.view.dashboard.DashBoardViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.withContext
import java.io.File

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private var avatarFile: File ?= null
    private lateinit var user: UserProfile
    override fun getLayoutResId(): Int {
        return R.layout.fragment_profile
    }

    override fun init() {
        with(binding) {
            viewModel.user.observe(viewLifecycleOwner) { it ->
                when (it) {
                    is Resource.Loading -> {
                        // show loading
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Something error", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        edtEmail.setText(it.data!!.email)
                        edtUserName.setText(it.data.username)
                        edtPhoneNumber.setText(it.data.phone)
                        Glide.with(binding.root).load(it.data.imageUrl).error(R.drawable.icon_profile).into(imgAvatar)
                        user = it.data
                    }
                }
            }
        }
    }

    override fun initEvent() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            imgEditAvatar.setSafeOnClickListener {
                val intent = Intent().apply {
                    setType("image/*")
                    setAction(Intent.ACTION_GET_CONTENT)
                }
                arlImageAvatar.launch(intent)
            }
            btnSave.setSafeOnClickListener {
                if (avatarFile != null) {
                    viewModel.uploadFile(avatarFile!!) {
                        if (it != null) {
                            user.imageUrl = it
                        }
                    }
                }
                viewModel.updateUser(user)
            }
        }
    }

    val arlImageAvatar =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val imageUri = data.data
                    if (imageUri != null) {
                        avatarFile = requireContext().uriToFile(imageUri)
                        Glide.with(this).load(imageUri).into(binding.imgAvatar)
                    }
                }
            }
        }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
}