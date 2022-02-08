package edu.rosehulman.stargaze.ui.User

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.stargaze.R
import edu.rosehulman.stargaze.databinding.FragmentUserBinding
import edu.rosehulman.stargaze.models.StarViewModel
import edu.rosehulman.stargaze.models.UserViewModel

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var model: StarViewModel
    private lateinit var userModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(StarViewModel::class.java)
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        setUpButtons()
        updateView()
        return binding.root
    }
    fun updateView(){
        binding.emailTextview.text = userModel.user!!.email ?: ""
        binding.usernameTextview.text = userModel.user!!.name ?: ""
        binding.biographyTextview.text = userModel.user!!.bio ?: ""
    }
    fun setUpButtons(){
        binding.editInfoButton.setOnClickListener(){
            findNavController().navigate(R.id.navigation_user_edit)
        }
    }
}