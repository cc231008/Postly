package edu.cc231008.postly.data

import android.app.Application
import edu.cc231008.postly.data.db.PostDatabase
import edu.cc231008.postly.data.repo.PostRepository

//Ensures that repository is created only once.
class PostApplication: Application() {
        val postRepository: PostRepository by lazy {
            val postDao = PostDatabase.getDatabase(this).postDao()
            PostRepository(postDao)
        }
}