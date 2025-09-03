package com.example.tasktracker

import com.example.tasktracker.ViewConstants.VIEW_TYPE_AD
import com.example.tasktracker.ViewConstants.VIEW_TYPE_LOADING
import com.example.tasktracker.ViewConstants.VIEW_TYPE_POST

sealed class FeedItem {
    data class Post(
        val id: Long,
        val author: String,
        val text: String,
        val likes: Int,
        val isLiked: Boolean,
        val createdAt: Long
    ) : FeedItem()

    data class Ad(
        val id: Long,
        val title: String,
        val cta: String,
        val url: String
    ) : FeedItem()

    data object Loading : FeedItem()
}


fun main() {


    val list =
        listOf(
            FeedItem.Post(
                id = 1,
                author = "John Doe",
                text = "Hello, world!",
                likes = 42,
                isLiked = true,
                createdAt = System.currentTimeMillis()
            ), FeedItem.Post(
                id = 2,
                author = "Jane Doe",
                text = "Goodbye, world!",
                likes = 0,
                isLiked = false,
                createdAt = System.currentTimeMillis()
            ), FeedItem.Ad(
                id = 11,
                title = "Ad title",
                cta = "Call to action",
                url = "https://example.com/ads/1"
            ), FeedItem.Loading
        )

    val toggled = toggleLike(list, 1)

    println(toggled)
}

private fun String.truncate(max: Int) =
    if (length <= max) this else substring(0, max).trimEnd() + "..."

fun renderFeed(item: List<FeedItem>) {
    item.forEach { item ->
        when (item) {
            is FeedItem.Post -> {
                val heart = if (item.isLiked) "❤️" else "🤍"
                println(
                    """
                    - Post #${item.id} · ${item.author}
                      ${item.text.truncate(40)}
                      Likes: ${item.likes} $heart
                """.trimIndent()
                )
            }

            is FeedItem.Ad -> {
                println(
                    """
                    - Ad #${item.id}
                      ${item.title} — ${item.cta}
                """.trimIndent()
                )
            }

            FeedItem.Loading -> println("Loading...")
        }
    }
}

private fun toggleLike(items: List<FeedItem>, postId: Long): List<FeedItem> {
    return items.map {
        when (it) {
            is FeedItem.Post -> {
                if (it.id == postId) {
                    if (it.isLiked) {
                        it.copy(isLiked = false, likes = it.likes - 1)
                    } else {
                        it.copy(isLiked = true, likes = it.likes + 1)
                    }
                } else it
            }

            is FeedItem.Ad -> it
            is FeedItem.Loading -> it
        }
    }
}

object ViewConstants {
    const val VIEW_TYPE_POST = 1
    const val VIEW_TYPE_AD = 2
    const val VIEW_TYPE_LOADING = 3
}

fun FeedItem.viewType(): Int {

    return when (this) {
        is FeedItem.Post -> VIEW_TYPE_POST
        is FeedItem.Ad -> VIEW_TYPE_AD
        is FeedItem.Loading -> VIEW_TYPE_LOADING
    }
}

fun mapToViewTypes(items: List<FeedItem>): List<Int> {
    return items.map { it.viewType() }
}
