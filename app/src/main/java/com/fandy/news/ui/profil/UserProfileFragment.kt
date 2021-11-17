package com.fandy.news.ui.profil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import com.fandy.news.R
import com.fandy.news.databinding.LoginFragmentBinding
import com.fandy.news.databinding.UserProfileFragmentBinding
import com.fandy.news.model.LoginUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private val viewModel: UserProfileViewModel by hiltNavGraphViewModels(R.id.navgraph)
    private var _binding: UserProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginUser: LoginUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UserProfileFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}