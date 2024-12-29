package edu.cc231008.postly.data.repo

//A template is the pattern that posts need to have.
data class PostTemplate(
    val id: Int = 0,
    var image: String,
    var description: String,
    val createdAt: Long,
)
