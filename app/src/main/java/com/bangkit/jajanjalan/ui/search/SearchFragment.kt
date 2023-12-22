package com.bangkit.jajanjalan.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.jajanjalan.R
import com.bangkit.jajanjalan.adapter.ListItemSearchAdapter
import com.bangkit.jajanjalan.data.Result
import com.bangkit.jajanjalan.databinding.FragmentSearchBinding
import com.bangkit.jajanjalan.ui.home.HomeFragmentDirections
import com.bangkit.jajanjalan.util.hide
import com.bangkit.jajanjalan.util.hideBottomNavView
import com.bangkit.jajanjalan.util.show
import com.bangkit.jajanjalan.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var query = ""
    private lateinit var searchAdapter: ListItemSearchAdapter
    private val viewModel by viewModels<SearchViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchAdapter = ListItemSearchAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        hideBottomNavView()
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRv()
        query = SearchFragmentArgs.fromBundle(arguments as Bundle).query
        setUpSearchBar(query)
        observe(query)
    }

    private fun setupRv() {
        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setUpSearchBar(query: String) {
        binding.apply {
            searchView.setQuery(query, false)
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    observe(query!!)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })

        }
    }

    private fun setupRvSearch() {
        binding.rvSearch.adapter = searchAdapter
        searchAdapter.onItemClick = { menu ->
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(menu.penjualId!!)
            findNavController().navigate(action)
        }
    }

    private fun observe(keyword: String) {
        viewModel.searchMenu(keyword).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    if (result.data.data.isNullOrEmpty()) {
                        binding.tvNotFound.show()
                        binding.rvSearch.hide()
                    } else {
                        binding.tvNotFound.hide()
                        binding.rvSearch.show()
                        searchAdapter.differ.submitList(result.data.data)
                        setupRvSearch()
                    }
                    Log.d("Search Menu", result.data.data.toString())
                }

                is Result.Error -> {
                    binding.progressBar.hide()
                    toast(result.error)
                    Log.d("Search Menu", result.error)
                }
            }
        }
    }
}