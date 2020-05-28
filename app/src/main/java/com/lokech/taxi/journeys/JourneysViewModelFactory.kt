package com.lokech.taxi.journeys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lokech.taxi.Repository

@Suppress("UNCHECKED_CAST")
class JourneysViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        JourneysViewModel(repository) as T
}