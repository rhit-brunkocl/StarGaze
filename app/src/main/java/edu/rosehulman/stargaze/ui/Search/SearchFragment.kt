package edu.rosehulman.stargaze.ui.Search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.rosehulman.stargaze.databinding.FragmentSearchBinding
import edu.rosehulman.stargaze.models.SearchCriteria

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setUpButton()
        val root: View = binding.root
        return root
    }

    private fun setUpButton() {
        binding.searchButton.setOnClickListener {
            var criteria = SearchCriteria()
            criteria.WDS_name = binding.WDSNameEdittext.text.toString()
            criteria.min_RA = binding.minRAEdittext.text.toString()
            criteria.max_RA = binding.maxRAEdittext.text.toString()
            criteria.min_Dec = binding.minDecEdittext.text.toString()
            criteria.max_Dec = binding.maxDecEdittext.text.toString()


        }
    }

}