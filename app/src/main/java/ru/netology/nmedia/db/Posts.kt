package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.post.Post

fun Cursor.toPost() = Post(
    id = getLong(getColumnIndexOrThrow(PostTable.Column.ID.columnName)),
    author = getString(getColumnIndexOrThrow(PostTable.Column.AUTHOR.columnName)),
    content = getString(getColumnIndexOrThrow(PostTable.Column.CONTENT.columnName)),
    published = getString(getColumnIndexOrThrow(PostTable.Column.PUBLISHED.columnName)),
    likeByMe = getInt(getColumnIndexOrThrow(PostTable.Column.LIKED_BY_ME.columnName)) != 0,
    likes = getInt(getColumnIndexOrThrow(PostTable.Column.LIKES.columnName)),
    shareCount = getInt(getColumnIndexOrThrow(PostTable.Column.SHARE_COUNT.columnName)),
    viewCount = getInt(getColumnIndexOrThrow(PostTable.Column.VIEW_COUNT.columnName)),
    urlVideo = getString(getColumnIndexOrThrow(PostTable.Column.URL_VIDEO.columnName))
)