package edu.cc231008.postly.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cc231008.postly.data.repo.PostRepository
import edu.cc231008.postly.data.repo.PostTemplate
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

data class PostDetailUiState(
    val post: PostTemplate
)

class PostViewModel(private val repository: PostRepository) : ViewModel() {
    val postsUiState = repository.posts.stateIn(
        viewModelScope, // viewModelScope - automatically cancels coroutines when the ViewModel is cleared.
        SharingStarted.WhileSubscribed(5000), // keeps the state active while there are subscribers, with a delay of 5000 ms before stopping.
        emptyList()
    )
}