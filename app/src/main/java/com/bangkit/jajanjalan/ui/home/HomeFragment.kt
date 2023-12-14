package com.bangkit.jajanjalan.ui.home

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.jajanjalan.R
import com.bangkit.jajanjalan.adapter.ListItemMenuAdapter
import com.bangkit.jajanjalan.data.Result
import com.bangkit.jajanjalan.data.pref.UserModel
import com.bangkit.jajanjalan.data.remote.response.DataMenuItem
import com.bangkit.jajanjalan.databinding.FragmentHomeBinding
import com.bangkit.jajanjalan.util.showBottomNavView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: ListItemMenuAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        showBottomNavView()
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ListItemMenuAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserDetail()
        setupRv()
        observe()
    }

    private fun getUserDetail() {
        viewModel.getUser()
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                // Update UI with user data
                if (user != null) {
                    setupView(user)
                } else {
                    Log.d("User Login", "User is null")
                }
            }
        }
    }

    private fun observe() {
        viewModel.getAllMenu().observe(viewLifecycleOwner) {menu->
            when(menu) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    Log.d("Home Recycler Menu", menu.data.data.toString())
                    adapter.differ.submitList(menu.data.data)
                    setupRvRecommendation()
                }
                is Result.Error -> {
                    Log.d("Menu Recycler", menu.error)
                }
            }
        }
    }

    private fun setupRv() {
        binding.rvMenuRecommendation.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupRvRecommendation() {
        binding.rvMenuRecommendation.adapter = adapter
        binding.rvPopularMenus.adapter = adapter
        adapter.onItemClick = { menu ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(menu.penjualId!!)
            findNavController().navigate(action)
        }

    }

    private fun setupView(user: UserModel) {
        binding.apply {
            tvWelcomeUser.text = "Hello, ${user.name}"
            Glide.with(requireActivity())
                .load(user.image)
                .into(ivProfil)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}