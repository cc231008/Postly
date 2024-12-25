package edu.cc231008.postly.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cc231008.postly.data.repo.PostRepository
import edu.cc231008.postly.data.repo.PostTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//the PostDetailViewModel is responsible for managing, saving, and updating the UI state.
class PostDetailViewModel(
    private val savedStateHandle: SavedStateHandle, // Provides a way to retrieve and save state information
    private val repository: PostRepository
) : ViewModel() {

    private val postId: Int = savedStateHandle["postId"] ?: 0 // Retrieves postId, when the path is "detail/$postId"

    private val _postDetailUiState = MutableStateFlow(
        PostDetailUiState(
            PostTemplate(0, "", "")
        )
    )

    // Publicly exposed StateFlow for observing the UI state
    val postDetailUiState = _postDetailUiState.asStateFlow()

    // Initialization block to load post details
    init {
        viewModelScope.launch {
            val post = repository.findPostById(postId) // Fetches a post by ID from the repository
            _postDetailUiState.update {
                it.copy(post = post) // Updates the UI state with the fetched post
            }
        }
    }
}