package edu.rosehulman.stargaze.ui.User

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.stargaze.R
import edu.rosehulman.stargaze.databinding.FragmentUserEditBinding
import edu.rosehulman.stargaze.models.UserViewModel

class UserEditFragment : Fragment() {
    private lateinit var binding: FragmentUserEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        Log.d("tag", "User in user edit fragment: ${userModel.user}")

        binding = FragmentUserEditBinding.inflate(inflater, container, false)
        binding.userEditDoneButton.setOnClickListener {
            // Save user info into Firestore.
            userModel.update(
                newName = binding.userEditNameEditText.text.toString(),
                newBio = binding.userEditBioEditText.text.toString(),
                newHasCompletedSetup = true
            )
            findNavController().navigate(R.id.navigation_user)
        }

        userModel.getOrMakeUser {
            with(userModel.user!!) {
                Log.d("tag", "$this")
                binding.userEditNameEditText.setText(name)
                binding.userEditBioEditText.setText(bio)
            }
        }
        return binding.root
    }
}