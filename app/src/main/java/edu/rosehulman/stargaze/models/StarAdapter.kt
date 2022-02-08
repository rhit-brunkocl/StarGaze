package edu.rosehulman.stargaze.models

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.stargaze.R

class StarAdapter(fragment: Fragment, navTo: String): RecyclerView.Adapter<StarAdapter.StarViewHolder>(){
    val model = ViewModelProvider(fragment.requireActivity()).get(StarViewModel::class.java)
    val nav = navTo
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.star_detail_layout, parent, false)
        return StarViewHolder(view, nav)
    }

    fun addListener(fragName: String, useCriteria: Boolean) {
        model.addListener(fragName, useCriteria) {
            notifyDataSetChanged()
        }
    }

    fun removeListener(fragName: String) {
        model.removeListener(fragName)
    }

    override fun onBindViewHolder(holder: StarViewHolder, position: Int) {
        holder.bind(model.getStarAt(position))
    }

    override fun getItemCount()= model.size()

    fun favoriteStar(star: Star?) {
        if (star != null) {
            model.favoriteStar(star)
        }
        notifyDataSetChanged()
    }

    inner class StarViewHolder(itemView: View, navTo: String): RecyclerView.ViewHolder(itemView){
        val starNameTextView: TextView = itemView.findViewById(R.id.star_name)
        val starInfoTextView: TextView = itemView.findViewById(R.id.star_info)
        val openButton: Button = itemView.findViewById(R.id.open_button)
        init {
            openButton.setOnClickListener(){
                model.updatePos(adapterPosition)
                if(navTo == "search"){
                    itemView.findNavController().navigate(R.id.navigation_search_results_detail)
                }else{
                    itemView.findNavController().navigate(R.id.navigation_favorites_detail)
                }
            }
            itemView.setOnLongClickListener(){
                model.updatePos(adapterPosition)
                model.favoriteStar(model.getCurStar())
                notifyItemChanged(adapterPosition)
                true
            }
        }
        fun bind(star: Star){
            starNameTextView.text = star.name
            var starSep = star.sep
            var starMag = star.magnitude
            starInfoTextView.text = "Separation: $starSep arcseconds\nMagnitude: $starMag"
        }
    }
}