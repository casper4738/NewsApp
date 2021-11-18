package com.fandy.news.ui.profil

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import com.fandy.news.R
import com.fandy.news.databinding.UserProfileFragmentBinding
import com.fandy.news.ui.MainActivity
import com.fandy.news.util.formatDate
import com.fandy.news.util.formatDateRemoveTime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private val viewModel: UserProfileViewModel by hiltNavGraphViewModels(R.id.navgraph)
    private var _binding: UserProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UserProfileFragmentBinding.inflate(inflater, container, false)
        setActionListener()
        return binding.root
    }

    private fun setActionListener() {
        if (viewModel.isLogin()) {
            Toast.makeText(activity, resources.getString(R.string.login_issuccess), Toast.LENGTH_SHORT).show()
            enableViewLogin()
            binding.btnLogout.setOnClickListener { view ->
                view.findNavController().navigate(R.id.loginFragment)
                (activity as MainActivity).onUserInteraction()
            }

            val loginUser = viewModel.getLoginUser()
            binding.profileLastLogin.text = loginUser.lastLogin.formatDate()
            binding.tvProfileSinceMember.text = loginUser.dateCreated.formatDate()
            binding.tvEmail.text = loginUser.email
            binding.tvFullname.text = loginUser.fullName
            binding.tvUsername.text = loginUser.username
            binding.tvLastActivity.text = "Aktivitas Terakhir : ${loginUser.lastActivity}"
        } else {
            Toast.makeText(activity, resources.getString(R.string.login_notsuccess), Toast.LENGTH_SHORT).show()

            binding.tvLastActivity.text = "Aktivitas Terakhir : -"

            enableViewNotLogin()
            binding.btnLogin.setOnClickListener { view ->
                view.findNavController().navigate(R.id.loginFragment)
            }
        }
    }

    private fun enableViewLogin() {
        binding.btnLogin.visibility = View.GONE
        binding.layoutNotLogin.visibility = View.GONE

        binding.layoutLogin.visibility = View.VISIBLE
        binding.btnLogout.visibility = View.VISIBLE
        binding.layoutProfilSaya.visibility = View.VISIBLE
    }

    private fun enableViewNotLogin() {
        binding.btnLogin.visibility = View.VISIBLE
        binding.layoutNotLogin.visibility = View.VISIBLE

        binding.layoutLogin.visibility = View.GONE
        binding.btnLogout.visibility = View.GONE
        binding.layoutProfilSaya.visibility = View.GONE
    }

}