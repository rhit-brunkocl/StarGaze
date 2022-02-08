package edu.rosehulman.stargaze.ui.Favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.stargaze.databinding.FragmentFavoritesBinding
import edu.rosehulman.stargaze.models.StarAdapter

class FavoritesFragment : Fragment() {
    lateinit var binding: FragmentFavoritesBinding
    lateinit var adapter: StarAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        adapter = StarAdapter(this, "favorites")
        adapter.addListener(fragmentName, false)
        binding.searchRecyclerView.adapter=adapter
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
        const val fragmentName = "FavoritesFragment"
    }

}