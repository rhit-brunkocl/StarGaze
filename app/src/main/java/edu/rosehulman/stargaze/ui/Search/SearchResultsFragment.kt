package edu.rosehulman.stargaze.ui.Search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.stargaze.databinding.FragmentSearchResultsBinding
import edu.rosehulman.stargaze.models.StarAdapter

class SearchResultsFragment : Fragment() {
    lateinit var binding: FragmentSearchResultsBinding
    lateinit var adapter: StarAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        adapter = StarAdapter(this, "search")
        adapter.addListener(fragmentName)
        binding.searchRecyclerView.adapter = adapter
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecyclerView.setHasFixedSize(true)
        binding.searchRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.removeListener(fragmentName)
    }
    companion object{
        const val fragmentName = "SearchResultsFragment"
    }

}