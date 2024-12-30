package edu.cc231008.postly.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cc231008.postly.data.repo.PostRepository
import edu.cc231008.postly.data.repo.PostTemplate
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PostDetailUiState(
    val post: PostTemplate
)

class PostViewModel(private val repository: PostRepository) : ViewModel() {
    val postUiState = repository.posts.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun onAddButtonClick(image: String, description: String) {
        viewModelScope.launch {
            repository.addPost(image, description)
        }
    }
}