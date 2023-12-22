package com.bangkit.jajanjalan.ui.profile.edit

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.jajanjalan.R
import com.bangkit.jajanjalan.data.Result
import com.bangkit.jajanjalan.data.pref.UserModel
import com.bangkit.jajanjalan.databinding.FragmentEditProfileBinding
import com.bangkit.jajanjalan.databinding.FragmentProfileBinding
import com.bangkit.jajanjalan.ui.profile.ProfileViewModel
import com.bangkit.jajanjalan.util.glide
import com.bangkit.jajanjalan.util.hide
import com.bangkit.jajanjalan.util.reduceFileImage
import com.bangkit.jajanjalan.util.show
import com.bangkit.jajanjalan.util.toast
import com.bangkit.jajanjalan.util.uriToFile
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel>()
    lateinit var userLogin: UserModel
    private lateinit var currentImageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupView()
    }

    private fun setupAction() {
        binding.cardProfile.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest())
        }

        binding.btnSave.setOnClickListener {
            updateProfile()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
            toast("No media selected")
        }
    }

    private fun showImage() {
        currentImageUri.let {
            binding.ivProfil.setImageURI(it)
        }
    }

    private fun getUser() {
        viewModel.getUser()
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                userLogin = user!!
            }
        }
    }

    private fun setupView() {
        getUser()
        binding.apply {
            edUpdateEmail.setText(userLogin.email)
            edUpdateName.setText(userLogin.name)
            ivProfil.glide(userLogin.image)
        }
    }

    private fun updateProfile() {
        currentImageUri.let {uri ->
            val image = uriToFile(uri, requireContext()).reduceFileImage()
            val name = binding.edUpdateName.text.toString()
            val requestName = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestPassword = userLogin.password.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                image.name,
                requestImageFile
            )
            observeUpload("Bearer ${userLogin.token}", userLogin.userId, requestName, requestPassword, multipartBody)
        }
    }

    private fun observeUpload(
        token: String,
        id: String,
        name: RequestBody,
        password: RequestBody,
        image: MultipartBody.Part
    ) {
        viewModel.updateProfile(token, id, name, password, image).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    binding.progressBar.show()
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("Update Profile")
                        setMessage(getString(R.string.profile_updated))
                        setPositiveButton(getString(R.string.ok)) { _, _ ->
                            findNavController().navigateUp()
                        }
                        create()
                        show()
                    }
                    viewModel.saveUser(
                        it.data.userDetail?.id.toString(),
                        it.data.userDetail?.email.toString(),
                        it.data.userDetail?.name.toString(),
                        it.data.userDetail?.image.toString(),
                        userLogin.password,
                        userLogin.token
                    )
                }
                is Result.Error -> {
                    binding.progressBar.hide()
                    toast(it.error)
                    Log.d("EditProfile", it.error)
                }
            }
        }
    }


}