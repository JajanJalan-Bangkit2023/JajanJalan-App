package com.bangkit.jajanjalan.ui.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.jajanjalan.R
import com.bangkit.jajanjalan.data.Result
import com.bangkit.jajanjalan.databinding.FragmentSignUpBinding
import com.bangkit.jajanjalan.ui.auth.viewmodel.RegisterViewModel
import com.bangkit.jajanjalan.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        validateRegister()
        binding.tvLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeRegister(email: String, name: String, password: String, role: String) {
        viewModel.register(email, name, password, role).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    binding.progressIndicator.show()
                    binding.btnRegister.isEnabled = false
                }
                is Result.Success -> {
                    Log.d("User Detail", it.data.userDetail.toString())
                    binding.progressIndicator.hide()
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("Register")
                        setMessage(getString(R.string.register_succeed))
                        setPositiveButton(getString(R.string.continue_login)) { _, _ ->
                            val intent = Intent(requireContext(), AuthActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        create()
                        show()
                    }
                }
                is Result.Error -> {
                    binding.progressIndicator.hide()
                    toast(it.error)
                    Log.d("Error Register", it.error)
                }
            }
        }
    }

    private fun validateRegister() {

        binding.btnRegister.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString()
            val name = binding.edRegisterName.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val confirmPassword = binding.edRegisterConfirmPassword.text.toString()

            if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Register Account")
                    setMessage(getString(R.string.register_empty))
                    setPositiveButton(getString(R.string.ok)) { _, _ -> }
                    create()
                    show()
                }
            } else if (password != confirmPassword) {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Register")
                    setMessage(getString(R.string.register_password_not_match))
                    setPositiveButton(getString(R.string.ok)) { _, _ -> }
                    create()
                    show()
                }
            } else {
                observeRegister(email, name, password, "user")
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}