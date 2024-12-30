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

    //This function helps to find a post by its id.
    suspend fun findPostById(postId: Int): PostTemplate {
        val postEntity = postDao.findPostById(postId)
        return (PostTemplate(postEntity._id, postEntity.image, postEntity.description, postEntity.createdAt))
    }

    //Function for adding posts.
    suspend fun addPost(image: String, description: String) {
        //currentTime is a variable that holds time when post is created or when addPost function is used.
        val currentTime = System.currentTimeMillis()
        postDao.addPost(PostEntity(0, image, description, currentTime))
    }

    //Function for editing or updating data of a post.
    suspend fun editPost(postEntity: PostEntity) {
        postDao.editPost(postEntity)
    }
    //Function for deleting data of a post.
    suspend fun deletePost(postEntity: PostEntity) {
        postDao.deletePost(postEntity)
    }
}