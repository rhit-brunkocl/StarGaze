package edu.rosehulman.stargaze.ui.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.stargaze.R
import edu.rosehulman.stargaze.databinding.FragmentSettingsBinding
import edu.rosehulman.stargaze.models.UserViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var model: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        updateView()
        setUpButtons()
        return binding.root
    }
    fun updateView(){

    }
    fun setUpButtons(){
        binding.parallaxSetting.setOnClickListener {
            model.user!!.settings.parallax = binding.parallaxSetting.isChecked()
        }
        binding.harshawSetting.setOnClickListener {
            model.user!!.settings.harshaw = binding.harshawSetting.isChecked()
        }
        binding.gaiaSetting.setOnClickListener {
            model.user!!.settings.GAIA = binding.gaiaSetting.isChecked()
        }
        binding.wdsSetting.setOnClickListener {
            model.user!!.settings.WDS = binding.wdsSetting.isChecked()
        }
        binding.locationSetting.setOnClickListener {
            model.user!!.settings.location = binding.locationSetting.isChecked()
        }
        binding.cameraSetting.setOnClickListener {
            model.user!!.settings.camera = binding.cameraSetting.isChecked()
        }
        binding.saveSetting.setOnClickListener {
            model.user!!.settings.fav = binding.saveSetting.isChecked()
        }
        binding.smartSearchSetting.setOnClickListener {
            model.user!!.settings.limit_search = binding.smartSearchSetting.isChecked()
        }
        binding.logoutButton.setOnClickListener(){
            Firebase.auth.signOut()
        }
    }

}