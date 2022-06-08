package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.post.Post

internal fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    content = content,
    published = published,
    likeByMe = likedByMe,
    likes = likes,
    shareCount = shareCount,
    viewCount = viewCount,
    urlVideo = urlVideo
)

internal fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    published = published,
    likedByMe = likeByMe,
    likes = likes,
    shareCount = shareCount,
    viewCount = viewCount,
    urlVideo = urlVideo
)