package ru.netology.nmedia.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.post.Post

class PostDaoImpl(
    private val db: SQLiteDatabase
) : PostDao {

    override fun getAll() = db.query(
        PostTable.NAME,
        PostTable.ALL_COLUMNS_NAME,
        null, null, null, null,
        "${PostTable.Column.ID.columnName} DESC"
    ).use { cursor ->
        List(cursor.count) {
            cursor.moveToNext()
            cursor.toPost()
        }
    }


    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostTable.Column.AUTHOR.columnName, post.author)
            put(PostTable.Column.CONTENT.columnName, post.content)
            put(PostTable.Column.PUBLISHED.columnName, post.published)
            put(PostTable.Column.LIKED_BY_ME.columnName, post.likeByMe)
            put(PostTable.Column.LIKES.columnName, post.likes)
            put(PostTable.Column.SHARE_COUNT.columnName, post.shareCount)
            put(PostTable.Column.VIEW_COUNT.columnName, post.viewCount)
            put(PostTable.Column.URL_VIDEO.columnName, post.urlVideo)
        }
        val id = if (post.id != 0L) {
            db.update(
                PostTable.NAME,
                values,
                "${PostTable.Column.ID.columnName} =?",
                arrayOf(post.id.toString())
            )
            post.id
        } else {
            db.insert(
                PostTable.NAME,
                null,
                values
            )
        }
        return db.query(
            PostTable.NAME,
            PostTable.ALL_COLUMNS_NAME,
            "${PostTable.Column.ID.columnName} =?",
            arrayOf(id.toString()),
            null, null, null
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }


    override fun likeById(postId: Long) {
        db.execSQL(
            """
           UPDATE ${PostTable.NAME} SET
              ${PostTable.Column.LIKES.columnName} = ${PostTable.Column.LIKES.columnName} + CASE WHEN ${PostTable.Column.LIKED_BY_ME.columnName} THEN -1 ELSE 1 END,
               ${PostTable.Column.LIKED_BY_ME.columnName} = CASE WHEN ${PostTable.Column.LIKED_BY_ME.columnName} THEN 0 ELSE 1 END
           WHERE ${PostTable.Column.ID.columnName} = ?;
        """.trimIndent(), arrayOf(postId)
        )
    }

    override fun removeById(postId: Long) {
        db.delete(
            PostTable.NAME,
            "${PostTable.Column.ID.columnName} = ?",
            arrayOf(postId.toString()),
        )
    }

}