package com.lokech.taxi.journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lokech.taxi.Repository

@Suppress("UNCHECKED_CAST")
class JourneyViewModelFactory(private val journeyId: Int, private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        JourneyViewModel(journeyId, repository) as T
}