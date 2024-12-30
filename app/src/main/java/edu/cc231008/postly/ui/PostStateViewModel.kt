package edu.cc231008.postly.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cc231008.postly.data.db.PostEntity
import edu.cc231008.postly.data.repo.PostRepository
import edu.cc231008.postly.data.repo.PostTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//The PostStateViewModel is responsible for managing, saving, and updating the UI state.
class PostStateViewModel(
    private val savedStateHandle: SavedStateHandle, // Provides a way to retrieve and save state information.
    private val repository: PostRepository
) : ViewModel() {

    private val postId: Int = savedStateHandle["postId"] ?: 0 // Retrieves postId, when the path is "edit/$postId"
    private val _postUiState = MutableStateFlow(
        PostDetailUiState(
            PostTemplate(0, "", "", System.currentTimeMillis())
        )
    )

    //Publicly exposed private variable that is visible and observable, but can't be changed by user.
    val postUiState = _postUiState.asStateFlow()

    // Initialization block to load post details
    init {
        viewModelScope.launch {
            val post = repository.findPostById(postId) // Fetches a post by ID from the repository
            _postUiState.update {
                it.copy(post = post) // Updates the UI state with the fetched post
            }
        }
    }

    fun onEditButtonClick(post: PostTemplate) {
        viewModelScope.launch {
            repository.editPost(PostEntity(
                _id = post.id,
                image = post.image,
                description = post.description,
                createdAt = post.createdAt,
            ))
        }
    }

    fun onDeleteButtonClick(post: PostTemplate) {
     viewModelScope.launch {
         repository.deletePost(PostEntity(
             _id = post.id,
             image = post.image,
             description = post.description,
             createdAt = post.createdAt,
         ))
     }
    }
}