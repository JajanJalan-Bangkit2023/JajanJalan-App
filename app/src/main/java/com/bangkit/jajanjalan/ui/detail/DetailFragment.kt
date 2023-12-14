package com.bangkit.jajanjalan.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.jajanjalan.R
import com.bangkit.jajanjalan.adapter.ListMenuPenjualAdapter
import com.bangkit.jajanjalan.data.Result
import com.bangkit.jajanjalan.data.remote.response.Penjual
import com.bangkit.jajanjalan.databinding.FragmentDetailBinding
import com.bangkit.jajanjalan.util.glide
import com.bangkit.jajanjalan.util.hideBottomNavView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailViewModel>()
    private lateinit var adapter: ListMenuPenjualAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ListMenuPenjualAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        hideBottomNavView()
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val penjualId = DetailFragmentArgs.fromBundle(arguments as Bundle).penjualId
        Log.d("DetailFrag PenjualId", penjualId.toString())

        setupRv()
        getPenjualDetail(penjualId)
    }

    private fun setupRv() {
        binding.rvMenu.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMenu.adapter = adapter
    }
    private fun getPenjualDetail(penjualId: Int) {
        viewModel.getPenjual(penjualId).observe(viewLifecycleOwner) { penjual ->
            when(penjual) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    setupView(penjual.data.data!!)
                    getMenuByPenjual(penjualId)
                }
                is Result.Error -> {
                    Log.d("Error Detail", penjual.error)
                }
            }

        }
    }

    private fun setupView(penjual: Penjual) {
        binding.apply {
            tvSellerName.text = penjual.name
            tvSellerName2.text = penjual.name
            tvLocation.text = penjual.address
            tvDesc.text = penjual.description
            ivWarung.glide(penjual.image.toString())
        }
    }

    private fun getMenuByPenjual(penjualId: Int) {
        viewModel.getMenuByPenjual(penjualId).observe(viewLifecycleOwner) {menu->
            when(menu) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    adapter.differ.submitList(menu.data.data)
                }
                is Result.Error -> {
                    Log.d("Error Detail Page", menu.error)
                }
            }
        }
    }




}