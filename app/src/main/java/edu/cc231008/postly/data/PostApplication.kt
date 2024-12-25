package edu.cc231008.postly.data

import android.app.Application
import edu.cc231008.postly.data.db.PostDatabase
import edu.cc231008.postly.data.repo.PostRepository

class PostApplication: Application() {
        val postRepository: PostRepository by lazy {
            val postDao = PostDatabase.getDatabase(this).postDao()
            PostRepository(postDao)
        }
}