package com.fandy.news.ui.login

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
import com.fandy.news.ui.MainActivity


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

        setObserve()

        return binding.root
    }

    private fun setObserve() {
        loginViewModel.loginUser.observe(viewLifecycleOwner) { response ->
            response?.let {
                loginReset()

                Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.navigate(R.id.userProfileFragment)
                (activity as MainActivity).onUserInteraction()
            }
        }

        loginViewModel.errorState.observe(viewLifecycleOwner) { response ->
            response?.let {
                loginReset()
                showLoginError()
                binding.tvErrorMessage.text = response.message
                if (!response.status) {
                    binding.tvErrorMessage.text = response.message
                }
            }
        }
    }

    private fun loginLoading() {
        binding.indicatorLogin.visibility = View.VISIBLE
        binding.textLogin.visibility = View.GONE
    }

    private fun loginReset() {
        binding.indicatorLogin.visibility = View.GONE
        binding.textLogin.visibility = View.VISIBLE
        binding.tvErrorMessage.visibility = View.GONE
    }

    private fun showLoginError() {
        binding.tvErrorMessage.visibility = View.VISIBLE
    }


    private fun setupLoginButton() {
        loginReset()

        binding.btnLogin.setOnClickListener { view ->
            loginLoading()
            doLogin()
        }
    }

    private fun doLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isNullOrBlank()) {
            binding.tvErrorMessage.text = getString(R.string.login_wrong_input)
            loginReset()
            showLoginError()
        } else if (password.isNullOrBlank()) {
            binding.tvErrorMessage.text = getString(R.string.login_wrong_input)
            loginReset()
            showLoginError()
        } else {
            var loginRequest = LoginRequest(
                email = email,
                password = password
            )
            loginViewModel.login(loginRequest)
        }
    }



}