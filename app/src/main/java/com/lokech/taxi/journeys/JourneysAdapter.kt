package com.lokech.taxi.journeys

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lokech.taxi.data.Journey
import com.lokech.taxi.databinding.JourneysItemBinding
import timber.log.Timber

class JourneysAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Journey, JourneysAdapter.ViewHolder>(ItemDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val journey: Journey = getItem(position)
        Timber.i("Journey to bind is $journey")
        holder.itemView.setOnClickListener {
            onClickListener.onClick(journey)
        }
        holder.bind(journey)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    class ViewHolder private constructor(private val binding: JourneysItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(journey: Journey) {
            binding.journey = journey
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = JourneysItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class OnClickListener(val clickListener: (journey: Journey) -> Unit) {
        fun onClick(journey: Journey) = clickListener(journey)
    }
}

class ItemDiffCallback : DiffUtil.ItemCallback<Journey>() {
    override fun areItemsTheSame(oldJourney: Journey, newJourney: Journey) =
        oldJourney.id == newJourney.id

    override fun areContentsTheSame(oldJourney: Journey, newJourney: Journey) =
        oldJourney == newJourney
}
