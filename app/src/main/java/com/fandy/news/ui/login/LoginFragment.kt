package com.fandy.news.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import com.fandy.news.R
import com.fandy.news.databinding.LoginFragmentBinding
import com.fandy.news.model.LoginUser
import com.fandy.news.ui.article.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by hiltNavGraphViewModels(R.id.navgraph)
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginUser: LoginUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)

        setupLoginButton()

        return binding.root
    }

    private fun setupLoginButton() {
        binding.btnLogin.setOnClickListener { view ->
            navigateToArticleList(view)
        }
    }

    private fun doLogin() {
        loginUser.email = binding.etEmail.text.toString()
        loginUser.password = binding.etPassword.text.toString()
        loginViewModel.login(loginUser)
    }

    private fun navigateToArticleList(view: View) {
        val directions = LoginFragmentDirections.actionLoginFragmentToArticleListFragment()
        view.findNavController().navigate(directions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}