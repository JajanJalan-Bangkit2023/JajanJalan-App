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
import com.bangkit.jajanjalan.util.hide
import com.bangkit.jajanjalan.util.show
import com.bangkit.jajanjalan.util.showBottomNavView
import com.bangkit.jajanjalan.util.toast
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: ListItemMenuAdapter
    private lateinit var recommendationAdapter: ListItemMenuAdapter
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
        recommendationAdapter = ListItemMenuAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRv()
        getUserDetail()
        setUpSearchBar()
    }

    private fun getUserDetail() {
        viewModel.getUser()
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                // Update UI with user data
                if (user != null) {
                    setupView(user)
                    observe("Bearer ${user.token}")
                } else {
                    Log.d("User Login", "User is null")
                }
            }
        }
    }

    private fun observe(token: String) {
        if (view != null && viewLifecycleOwner != null) {
            viewModel.getAllMenu().observe(viewLifecycleOwner) {menu->
                when(menu) {
                    is Result.Loading -> {
                        showShimmerLoading(true)
                    }
                    is Result.Success -> {
                        showShimmerLoading(false)
                        Log.d("Home Recycler Menu", menu.data.data.toString())
                        adapter.differ.submitList(menu.data.data)
                        setupRvPopular()
                    }
                    is Result.Error -> {
                        showShimmerLoading(false)
                        Log.d("Menu Recycler", menu.error)
                    }
                }
            }
            viewModel.getRecommendMenu(token).observe(viewLifecycleOwner) {menu->
                when(menu) {
                    is Result.Loading -> {
                        showShimmerLoading(true)
                    }
                    is Result.Success -> {
                        showShimmerLoading(false)
                        Log.d("Home Recycler Recommend", menu.data.data.toString())
                        recommendationAdapter.differ.submitList(menu.data.data)
                        setupRvRecommendation()
                    }
                    is Result.Error -> {
                        showShimmerLoading(false)
                        Log.d("Menu Recycler", menu.error)
                    }
                }
            }
        }

    }

    private fun setUpSearchBar() {
        binding.apply {
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val query = searchView.query.toString()
                    val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment(query)
                    findNavController().navigate(action)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })

        }
    }

    private fun setupRv() {
        binding.rvMenuRecommendation.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvPopularMenus.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupRvRecommendation() {
        binding.rvMenuRecommendation.adapter = recommendationAdapter
        recommendationAdapter.onItemClick = { menu ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(menu.penjualId!!)
            findNavController().navigate(action)
        }

    }

    private fun setupRvPopular() {
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

    private fun showShimmerLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerViewContainer.show()
            binding.shimmerViewContainer.startShimmer()
            binding.shimmerViewContainer2.show()
            binding.shimmerViewContainer2.startShimmer()
        } else {
            binding.apply {
                shimmerViewContainer.stopShimmer()
                shimmerViewContainer.hide()
                shimmerViewContainer2.stopShimmer()
                shimmerViewContainer2.hide()
            }
        }
    }


}