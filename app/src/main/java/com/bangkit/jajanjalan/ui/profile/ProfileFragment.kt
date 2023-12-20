package com.bangkit.jajanjalan.ui.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.jajanjalan.R
import com.bangkit.jajanjalan.data.pref.UserModel
import com.bangkit.jajanjalan.databinding.FragmentProfileBinding
import com.bangkit.jajanjalan.ui.auth.AuthActivity
import com.bangkit.jajanjalan.util.showBottomNavView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel>()
    lateinit var userLogin: UserModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        showBottomNavView()
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupView()
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
        binding.tvName.text = userLogin.name
        Glide.with(requireActivity())
            .load(userLogin.image)
            .into(binding.ivProfil)
    }

    private fun setupAction() {
        binding.apply {
            btnLogout.setOnClickListener {
                viewModel.logout()
                navigateToAuthActivity()
            }
            btnLanguage.setOnClickListener{
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            tvEditProfile.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
                findNavController().navigate(action)
            }

        }

    }

    private fun navigateToAuthActivity() {
        Intent(requireContext(), AuthActivity::class.java).also { intent->
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        setupView()
    }

}