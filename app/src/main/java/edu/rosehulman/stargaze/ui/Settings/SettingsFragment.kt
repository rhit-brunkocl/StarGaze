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
import edu.rosehulman.stargaze.models.Setting
import edu.rosehulman.stargaze.models.StarViewModel
import edu.rosehulman.stargaze.models.UserViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var model: UserViewModel
    private lateinit var starModel: StarViewModel
    lateinit var settings: Setting
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        starModel = ViewModelProvider(requireActivity()).get(StarViewModel::class.java)
        settings = model.user!!.settings
        updateView()
        setUpButtons()
        return binding.root
    }
    fun updateView(){
        binding.parallaxSetting.isChecked = model.user!!.settings.parallax
        binding.harshawSetting.isChecked = model.user!!.settings.harshaw
        binding.gaiaSetting.isChecked = model.user!!.settings.GAIA
        binding.wdsSetting.isChecked = model.user!!.settings.WDS
        binding.locationSetting.isChecked = model.user!!.settings.location
        binding.cameraSetting.isChecked = model.user!!.settings.camera
        binding.saveSetting.isChecked = model.user!!.settings.fav
        binding.smartSearchSetting.isChecked = model.user!!.settings.limit_search
    }
    fun setUpButtons(){
        binding.parallaxSetting.setOnClickListener {
            settings.parallax = binding.parallaxSetting.isChecked()
            model.updateSettings(settings)
            starModel.curUser.settings = settings
        }
        binding.harshawSetting.setOnClickListener {
            settings.harshaw = binding.harshawSetting.isChecked()
            model.updateSettings(settings)
            starModel.curUser.settings = settings
        }
        binding.gaiaSetting.setOnClickListener {
            settings.GAIA = binding.gaiaSetting.isChecked()
            model.updateSettings(settings)
            starModel.curUser.settings = settings
        }
        binding.wdsSetting.setOnClickListener {
            settings.WDS = binding.wdsSetting.isChecked()
            model.updateSettings(settings)
            starModel.curUser.settings = settings
        }
        binding.locationSetting.setOnClickListener {
            settings.location = binding.locationSetting.isChecked()
            model.updateSettings(settings)
            starModel.curUser.settings = settings
        }
        binding.cameraSetting.setOnClickListener {
            settings.camera = binding.cameraSetting.isChecked()
            model.updateSettings(settings)
            starModel.curUser.settings = settings
        }
        binding.saveSetting.setOnClickListener {
            settings.fav = binding.saveSetting.isChecked()
            model.updateSettings(settings)
            starModel.curUser.settings = settings
        }
        binding.smartSearchSetting.setOnClickListener {
            settings.limit_search = binding.smartSearchSetting.isChecked()
            model.updateSettings(settings)
            starModel.curUser.settings = settings
        }
        binding.logoutButton.setOnClickListener(){
            Firebase.auth.signOut()
        }
    }

}