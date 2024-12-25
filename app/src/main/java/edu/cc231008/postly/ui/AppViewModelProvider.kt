package edu.cc231008.postly.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.cc231008.postly.data.PostApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val postApplication = this[APPLICATION_KEY] as PostApplication
            PostViewModel(postApplication.postRepository)
        }

        initializer {
            val postApplication = this[APPLICATION_KEY] as PostApplication
            PostDetailViewModel(
                this.createSavedStateHandle(),
                postApplication.postRepository
            )
        }

    }
}