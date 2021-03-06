package com.feylabs.halalkan.view.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.UserAlbumResponse
import com.feylabs.halalkan.databinding.UserProfileFragmentBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class UserProfileFragment : BaseFragment() {

    val viewModel: UserProfileViewModel by viewModel()

    var _binding: UserProfileFragmentBinding? = null
    val binding get() = _binding as UserProfileFragmentBinding

    private val mAdapter by lazy { UserAlbumAdapter() }

    override fun initUI() {
        showLoading(true)
        hideActionBar()
        initRv()
        initAdapter()
    }

    private fun initAdapter() {
        mAdapter.setupAdapterInterface(object : UserAlbumAdapter.AlbumItemInterface {
            override fun onclick(model: UserAlbumResponse.UserAlbumResponseItem?) {
                findNavController().navigate(
                    R.id.listPhotoFragment,
                    bundleOf("albumId" to model?.id.toString())
                )
            }
        })
    }

    private fun initRv() {
        binding.rvAlbum.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    override fun initObserver() {
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    showToast(it.message.toString(), true)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    val data = it.data
                    if (data != null) {
                        binding.tvName.text = data.name
                        binding.tvCompany.text = data.company.name.toString()
                        binding.tvEmail.value(data.email)

                        data.address.apply {
                            val fullAddress = "$street $suite, $city, $zipcode"
                            binding.tvAddress.value(fullAddress)
                        }
                    }
                }
            }
        })

        viewModel.userAlbumLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    showToast(it.message.toString(), true)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    val data = it.data
                    if (data != null) {
                        mAdapter.setWithNewData(data.toMutableList())
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            viewVisible(binding.loading)
        } else {
            viewGone(binding.loading)
        }
    }

    override fun initAction() {
        val userId = arguments?.getString("userId") ?: ""
        viewModel.fetchUsers(userId)
        viewModel.fetchAlbum(userId)
    }

    override fun initData() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UserProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

}