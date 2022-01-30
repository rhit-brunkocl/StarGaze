package edu.rosehulman.stargaze.models

import android.R
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView

class StarAdapter(fragment: Fragment): RecyclerView.Adapter<StarAdapter.StarViewHolder>(){
    val model = ViewModelProvider(fragment.requireActivity()).get(StarViewModel::class.java)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.star_detail_layout, parent, false)
        return StarViewHolder(view)
    }

    fun addListener(fragName: String) {
        model.addListener(fragName) {
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

    inner class StarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener(){
                model.updatePos(adapterPosition)
                itemView.findNavController().navigate(R.id.navigation_search_results_detail, null, navOptions {
                    anim{
                        enter = R.anim.slide_in_left
                        exit = R.anim.slide_out_right
                    }
                })
            }
            itemView.setOnLongClickListener(){
                model.updatePos(adapterPosition)
                notifyItemChanged(adapterPosition)
                true
            }
        }
        fun bind(star: Star){

        }
    }
}