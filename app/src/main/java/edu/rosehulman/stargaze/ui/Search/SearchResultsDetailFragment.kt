package edu.rosehulman.stargaze.ui.Search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import edu.rosehulman.stargaze.R;
import edu.rosehulman.stargaze.databinding.FragmentSearchResultsDetailBinding
import edu.rosehulman.stargaze.models.StarViewModel
import edu.rosehulman.stargaze.models.UserViewModel

class SearchResultsDetailFragment :Fragment() {
    private lateinit var binding: FragmentSearchResultsDetailBinding
    private lateinit var model: StarViewModel
    private lateinit var userModel: UserViewModel
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchResultsDetailBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(StarViewModel::class.java)
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        setUpButtons()
        updateView()
        return binding.root
    }

    fun setUpButtons() {
        binding.cameraFab.setOnClickListener{
            model.selectedToView = model.getCurStar()
            if(model.curUser.settings.fav){
                model.favoriteStar(model.getCurStar())
            }
            findNavController().navigate(R.id.navigation_camera)
        }
        binding.favFab.setOnClickListener {
            userModel.updateFavorites(model.getCurStar())
            model.favoriteStar(model.getCurStar())
        }
        binding.leftButton.setOnClickListener {
            if(model.currentPos != 0) {
                model.currentPos--
            }
            updateView()
        }
        binding.rightButton.setOnClickListener {
            if(model.currentPos<model.size()-1) {
                model.currentPos++
            }
            updateView()
        }
    }

    fun updateView(){
        binding.starNameText.text = model.getCurStar().WDSName
        binding.starDetailText.text = model.curStarToString()
    }
}