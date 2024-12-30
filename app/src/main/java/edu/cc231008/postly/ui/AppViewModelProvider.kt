package edu.cc231008.postly.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.cc231008.postly.data.PostApplication

//This part helps to create ViewModel instances with necessary dependencies or parameters.
object AppViewModelProvider {
    //Factory ensures that the ViewModels get the necessary dependencies (like PostRepository) when they are created.
    val Factory = viewModelFactory {
        initializer {
            val postApplication = this[APPLICATION_KEY] as PostApplication
            PostViewModel(postApplication.postRepository)
        }

        initializer {
            val postApplication = this[APPLICATION_KEY] as PostApplication
            PostStateViewModel(
                this.createSavedStateHandle(),
                postApplication.postRepository
            )
        }

    }
}