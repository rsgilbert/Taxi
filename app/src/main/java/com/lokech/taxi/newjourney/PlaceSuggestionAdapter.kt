package com.lokech.taxi.newjourney


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lokech.taxi.data.Place
import com.lokech.taxi.databinding.PlaceSuggestionItemBinding
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter


class PlaceSuggestionsAdapter(inflater: LayoutInflater?) :
    SuggestionsAdapter<Place, PlaceSuggestionsAdapter.ViewHolder>(inflater) {
    override fun getSingleViewHeight(): Int {
        return 80
    }


    override fun onBindSuggestionHolder(
        suggestion: Place,
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(suggestion)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    class ViewHolder private constructor(private val binding: PlaceSuggestionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(place: Place) {
            binding.place = place
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PlaceSuggestionItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}