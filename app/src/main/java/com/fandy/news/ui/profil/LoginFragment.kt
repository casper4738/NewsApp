package com.fandy.news.ui.profil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import com.fandy.news.R
import com.fandy.news.databinding.LoginFragmentBinding
import com.fandy.news.model.LoginRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by hiltNavGraphViewModels(R.id.navgraph)
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

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
            doLogin()
            loginViewModel.loginUser.observe(viewLifecycleOwner) { response ->
                response?.let {
                    Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
                    navigateTo(view)
                }
            }

        }
    }

    private fun doLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if(email.isNullOrBlank()) {
            Toast.makeText(activity, getString(R.string.login_wrong_input), Toast.LENGTH_SHORT).show()
        } else if(email.isNullOrBlank()) {
            Toast.makeText(activity, getString(R.string.login_wrong_input), Toast.LENGTH_SHORT).show()
        } else {
            var loginRequest = LoginRequest(
                email = email,
                password = password
            )
            loginViewModel.login(loginRequest)
        }
    }

    private fun navigateTo(view: View) {
        val directions = LoginFragmentDirections.actionLoginFragmentToArticleListFragment()
        view.findNavController().navigate(directions)
    }

}