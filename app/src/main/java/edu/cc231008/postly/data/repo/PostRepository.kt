package edu.cc231008.postly.data.repo

import edu.cc231008.postly.data.db.PostDAO
import edu.cc231008.postly.data.db.PostEntity
import kotlinx.coroutines.flow.map

class PostRepository(private val postDao: PostDAO) {
    // variable that holds a list of all posts
    val posts = postDao.getAllPosts()
        .map { postList ->
            postList.map { entity ->
                PostTemplate(entity._id, entity.image, entity.description)
            }
        }

    suspend fun findPostById(postId: Int): PostTemplate {
        val userEntity = postDao.findPostById(postId)
        return (PostTemplate(userEntity._id, userEntity.image, userEntity.description))
    }

    suspend fun addPost(image: String, description: String) {
        postDao.addPost(PostEntity(0, image, description))
    }

    suspend fun editPost(postEntity: PostEntity) {
        postDao.editPost(postEntity)
    }
}