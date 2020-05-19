package com.lokech.taxi.newjourney


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lokech.taxi.Repository

@Suppress("UNCHECKED_CAST")
class StartFragmentViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        StartFragmentViewModel(repository) as T
}