package com.bangkit.jajanjalan.ui.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.jajanjalan.R
import com.bangkit.jajanjalan.adapter.FavoriteSellerAdapter
import com.bangkit.jajanjalan.data.Result
import com.bangkit.jajanjalan.data.local.entity.FavoriteEntity
import com.bangkit.jajanjalan.databinding.FragmentFavoriteBinding
import com.bangkit.jajanjalan.util.hide
import com.bangkit.jajanjalan.util.show
import com.bangkit.jajanjalan.util.showBottomNavView
import com.bangkit.jajanjalan.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FavoriteViewModel>()
    private lateinit var adapter: FavoriteSellerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FavoriteSellerAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        showBottomNavView()
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRv()
        observe()
    }

    private fun setupRvFavorite(listSeller: List<FavoriteEntity>) {
        adapter.differ.submitList(listSeller)
        binding.rvFavorite.adapter = adapter
        adapter.onItemClick = { seller ->
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(seller.id)
            findNavController().navigate(action)
        }
    }

    private fun setUpRv() {
        binding.rvFavorite.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun observe() {
        viewModel.getAllFavorite().observe(viewLifecycleOwner) { listSeller ->
            when(listSeller) {
                is Result.Loading -> {
                    binding.progressBar.show()
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    setupRvFavorite(listSeller.data)
                    Log.d("FavoriteFragment", listSeller.data.toString())
                }
                is Result.Error -> {
                    binding.progressBar.hide()
                    toast(listSeller.error)
                }
            }
        }
    }


}