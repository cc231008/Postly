package edu.cc231008.postly.data

import android.app.Application
import edu.cc231008.postly.data.db.PostDatabase
import edu.cc231008.postly.data.repo.PostRepository

/*
PostApplication is a custom Application class that initializes a PostRepository LAZILY.
This setup ensures that a SINGLE INSTANCE of PostRepository is created and used throughout the application.
 */
class PostApplication: Application() {
        val postRepository: PostRepository by lazy {
            val postDao = PostDatabase.getDatabase(this).postDao()
            PostRepository(postDao)
        }
}