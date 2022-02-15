package edu.rosehulman.stargaze.ui.Favorites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import edu.rosehulman.stargaze.R;
import edu.rosehulman.stargaze.databinding.FragmentFavoritesDetailBinding
import edu.rosehulman.stargaze.models.StarViewModel

class FavoritesDetailFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesDetailBinding
    private lateinit var model: StarViewModel
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesDetailBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(StarViewModel::class.java)
        setUpButtons()
        updateView()
        return binding.root
    }

    fun setUpButtons() {
        binding.cameraFab.setOnClickListener{
            model.selectedToView.add(model.getCurStar())
            findNavController().navigate(R.id.navigation_camera)
        }
        binding.leftButton.setOnClickListener {
            if(model.currentPos>0) {
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