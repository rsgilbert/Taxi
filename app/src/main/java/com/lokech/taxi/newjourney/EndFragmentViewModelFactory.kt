package com.lokech.taxi.newjourney


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lokech.taxi.Repository

@Suppress("UNCHECKED_CAST")
class EndFragmentViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        EndFragmentViewModel(repository) as T
}
