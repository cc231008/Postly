package edu.cc231008.postly.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//We use dao to control the database by fetching, inserting, updating or deleting data.
@Dao
interface PostDAO {
    @Query("SELECT * FROM posts")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE _id = :id")
    suspend fun findPostById(id: Int) : PostEntity

    @Insert
    suspend fun addPost(postEntity: PostEntity)

    @Update
    suspend fun editPost(postEntity: PostEntity)

    @Delete
    suspend fun deletePost(postEntity: PostEntity)
}