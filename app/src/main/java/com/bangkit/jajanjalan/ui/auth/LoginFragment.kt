package com.bangkit.jajanjalan.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.jajanjalan.data.Result
import com.bangkit.jajanjalan.data.pref.UserModel
import com.bangkit.jajanjalan.data.remote.response.User
import com.bangkit.jajanjalan.databinding.FragmentLoginBinding
import com.bangkit.jajanjalan.ui.MainActivity
import com.bangkit.jajanjalan.ui.auth.viewmodel.LoginViewModel
import com.bangkit.jajanjalan.util.TokenUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        validateLogin()
    }

    private fun observeLogin(email: String, password: String) {
        viewModel.login(email, password).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    binding.progressIndicator.show()
                    binding.btnLogin.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressIndicator.hide()
                    Log.d("User Detail", it.data.toString())
                    val userId = it.data.userInfo?.userId.toString()
                    Log.d("User Id", userId)
                    getDetailUser(userId, it.data.userInfo?.token.toString())
                }
                is Result.Error -> {
                    binding.progressIndicator.hide()
                    binding.btnLogin.isEnabled = true
                }
            }
        }
    }

    private fun validateLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            if (email.isEmpty()) {
                binding.edLoginEmail.error = "Email cannot be empty"
                binding.edLoginEmail.requestFocus()
            } else if (password.isEmpty()) {
                binding.edLoginPassword.error = "Password cannot be empty"
                binding.edLoginPassword.requestFocus()
            } else {
                observeLogin(email, password)
            }
        }
    }

    private fun saveSession(
        userId: String,
        email: String,
        name: String,
        image: String,
        password: String,
        token: String
    ) {
        viewModel.saveUser(userId, email, name, image, password, token)
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun getDetailUser(userId: String, token: String) {
        viewModel.getDetailUser(userId).observe(viewLifecycleOwner) { response ->
            when (response) {
                is Result.Loading -> {
                    // Handle loading state
                }
                is Result.Success -> {
                    Log.d("User Login Info", response.data.toString())
                    val user = response.data.userDetail!!
                    // Access user properties as needed
                    saveSession(
                        user.id.toString(),
                        user.email.toString(),
                        user.name.toString(),
                        user.image.toString(),
                        user.password.toString(),
                        token
                    )
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupAction() {
        binding.tvRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }

}