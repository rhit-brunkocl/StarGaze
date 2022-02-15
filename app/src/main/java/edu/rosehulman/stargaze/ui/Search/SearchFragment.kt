package edu.rosehulman.stargaze.ui.Search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.stargaze.R
import edu.rosehulman.stargaze.databinding.FragmentSearchBinding
import edu.rosehulman.stargaze.models.SearchCriteria
import edu.rosehulman.stargaze.models.StarViewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var model: StarViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(StarViewModel::class.java)
        setUpButton()
        updateView()
        val root: View = binding.root
        return root
    }

    private fun updateView(){
        binding.WDSNameEdittext.setText(model.criteria.WDS_name)
        binding.minRAEdittext.setText(model.criteria.min_RA.toString())
        binding.maxRAEdittext.setText(model.criteria.max_RA.toString())
        binding.minDecEdittext.setText(model.criteria.min_Dec.toString())
        binding.maxDecEdittext.setText(model.criteria.max_Dec.toString())
        binding.minSepEdittext.setText(model.criteria.min_sep.toString())
        binding.maxSepEdittext.setText(model.criteria.max_sep.toString())
        binding.minDeltasepEdittext.setText(model.criteria.min_deltaSep.toString())
        binding.maxDeltasepEdittext.setText(model.criteria.max_deltaSep.toString())
        binding.minMagEdittext.setText(model.criteria.min_mag.toString())
        binding.maxMagEdittext.setText(model.criteria.max_mag.toString())
        binding.minDeltamagEdittext.setText(model.criteria.min_deltaMag.toString())
        binding.maxDeltamagEdittext.setText(model.criteria.max_deltaMag.toString())
        binding.minObsEdittext.setText(model.criteria.min_obs.toString())
        binding.maxObsEdittext.setText(model.criteria.max_obs.toString())
        binding.firstObsEdittext.setText(model.criteria.firstObs.toString())
        binding.lastObsEdittext.setText(model.criteria.lastObs.toString())
    }

    private fun setUpButton() {
        binding.searchButton.setOnClickListener {
            Log.d("tag", "search button pressed")
            Toast.makeText(requireContext(), "It may take a bit to load your results, please be patient!", Toast.LENGTH_LONG).show()
            model.criteria.WDS_name = binding.WDSNameEdittext.text.toString()
            model.criteria.min_RA = binding.minRAEdittext.text.toString().toDouble()
            model.criteria.max_RA = binding.maxRAEdittext.text.toString().toDouble()
            model.criteria.min_Dec = binding.minDecEdittext.text.toString().toDouble()
            model.criteria.max_Dec = binding.maxDecEdittext.text.toString().toDouble()
            model.criteria.min_sep = binding.minSepEdittext.text.toString().toDouble()
            model.criteria.max_sep = binding.maxSepEdittext.text.toString().toDouble()
            model.criteria.min_deltaSep = binding.minDeltasepEdittext.text.toString().toDouble()
            model.criteria.max_deltaSep = binding.maxDeltasepEdittext.text.toString().toDouble()
            model.criteria.min_mag = binding.minMagEdittext.text.toString().toDouble()
            model.criteria.max_mag = binding.maxMagEdittext.text.toString().toDouble()
            model.criteria.min_deltaMag = binding.minDeltamagEdittext.text.toString().toDouble()
            model.criteria.max_deltaMag = binding.maxDeltamagEdittext.text.toString().toDouble()
            model.criteria.min_obs = binding.minObsEdittext.text.toString().toInt()
            model.criteria.max_obs = binding.maxObsEdittext.text.toString().toInt()
            model.criteria.firstObs = binding.firstObsEdittext.text.toString().toInt()
            model.criteria.lastObs = binding.lastObsEdittext.text.toString().toInt()
            findNavController().navigate(R.id.navigation_search_results)
        }
    }

}