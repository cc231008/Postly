package edu.cc231008.postly.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val image: String,
    val description: String,
    val createdAt: Long, //Shows time when post is created.
)