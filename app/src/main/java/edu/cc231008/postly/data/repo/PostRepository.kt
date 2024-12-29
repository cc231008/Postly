package edu.cc231008.postly.data.repo

import edu.cc231008.postly.data.db.PostDAO
import edu.cc231008.postly.data.db.PostEntity
import kotlinx.coroutines.flow.map

class PostRepository(private val postDao: PostDAO) {

    // variable that holds a list of all posts
    val posts = postDao.getAllPosts()
        .map { postList ->
            postList.map { entity ->
                PostTemplate(entity._id, entity.image, entity.description, entity.createdAt)
            }
        }

    suspend fun findPostById(postId: Int): PostTemplate {
        val postEntity = postDao.findPostById(postId)
        return (PostTemplate(postEntity._id, postEntity.image, postEntity.description, postEntity.createdAt))
    }

    suspend fun addPost(image: String, description: String) {
        val currentTime = System.currentTimeMillis()
        postDao.addPost(PostEntity(0, image, description, currentTime))
    }

    suspend fun editPost(postEntity: PostEntity) {
        postDao.editPost(postEntity)
    }
}