package com.bangkit.jajanjalan.ui.detail

import android.content.Intent
import android.net.Uri
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
import com.bangkit.jajanjalan.data.local.entity.FavoriteEntity
import com.bangkit.jajanjalan.data.remote.response.Penjual
import com.bangkit.jajanjalan.databinding.FragmentDetailBinding
import com.bangkit.jajanjalan.util.glide
import com.bangkit.jajanjalan.util.hide
import com.bangkit.jajanjalan.util.hideBottomNavView
import com.bangkit.jajanjalan.util.show
import com.bangkit.jajanjalan.util.toast
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailViewModel>()
    private lateinit var adapter: ListMenuPenjualAdapter
    private lateinit var detailPenjual: Penjual
    private var penjualId: Int = 0

    private var isFavorite = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ListMenuPenjualAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        hideBottomNavView()
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        penjualId = DetailFragmentArgs.fromBundle(arguments as Bundle).penjualId
        Log.d("DetailFrag PenjualId", penjualId.toString())

        setupRv()
        getPenjualDetail(penjualId)
        setupAction()
    }

    private fun setupAction() {
        binding.btnNavigate.setOnClickListener {
            val url = "https://www.google.com/maps/search/?api=1&query=${detailPenjual.lat},${detailPenjual.lon}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        binding.btnShare.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome seller '${detailPenjual.name}' at ${detailPenjual.address}")
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }

        binding.btnChat.setOnClickListener {
            toast("Feature in development stage!")
        }
    }
    private fun setFavorite() {
        val seller = FavoriteEntity(
            penjualId,
            detailPenjual.name.toString(),
            detailPenjual.image.toString(),
            detailPenjual.address.toString()
        )
        viewModel.isFavorite(penjualId).observe(viewLifecycleOwner) { isFav ->
            isFavorite = isFav
            val btnFav = binding.btnFavorite
            if (isFavorite) {
                btnFav.setImageResource(R.drawable.inset_btn_unfavorite)
            } else {
                btnFav.setImageResource(R.drawable.inset_btn_favorite)
            }
        }
        binding.btnFavorite.setOnClickListener {
            if (isFavorite) {
                viewModel.deleteFavoriteSeller(seller).observe(viewLifecycleOwner) {
                    when (it) {
                        is Result.Loading -> {
                            binding.btnFavorite.isEnabled = false
                        }

                        is Result.Success -> {
                            binding.btnFavorite.isEnabled = true
                            isFavorite = false
                            binding.btnFavorite.setImageResource(R.drawable.inset_btn_favorite)
                            toast(it.data)
                        }

                        is Result.Error -> {
                            binding.progressBar.hide()
                            Log.d("Error Delete", it.error)
                        }
                    }
                }
            } else {
                viewModel.addFavoriteSeller(seller).observe(viewLifecycleOwner) {
                    when (it) {
                        is Result.Loading -> {
                            binding.btnFavorite.isEnabled = false
                        }
                        is Result.Success -> {
                            binding.btnFavorite.isEnabled = true
                            isFavorite = true
                            binding.btnFavorite.setImageResource(R.drawable.inset_btn_unfavorite)
                            toast(it.data)
                        }
                        is Result.Error -> {
                            binding.progressBar.hide()
                            toast(it.error)
                            Log.d("Error Add to Favorite", it.error)
                        }
                    }
                }
            }
        }
    }

    private fun setupRv() {
        binding.rvMenu.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMenu.adapter = adapter
    }
    private fun getPenjualDetail(penjualId: Int) {
        viewModel.getPenjual(penjualId).observe(viewLifecycleOwner) { penjual ->
            when(penjual) {
                is Result.Loading -> {
                    binding.progressBar.show()
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    setupView(penjual.data.data!!)
                    getMenuByPenjual(penjualId)
                    detailPenjual = penjual.data.data
                    if (penjual.data.data.isOpen!!) {
                        binding.tvIsOpen.text = "Open Now"
                        binding.cvIsOpen.setBackgroundColor(resources.getColor(R.color.green))

                    } else {
                        binding.tvIsOpen.text = "Closed"
                        binding.cvIsOpen.setBackgroundColor(resources.getColor(R.color.red))
                    }
                    setFavorite()
                }
                is Result.Error -> {
                    binding.progressBar.hide()
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
                    binding.progressBar.show()
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    adapter.differ.submitList(menu.data.data)
                }
                is Result.Error -> {
                    binding.progressBar.hide()
                    Log.d("Error Detail Page", menu.error)
                }
            }
        }
    }




}