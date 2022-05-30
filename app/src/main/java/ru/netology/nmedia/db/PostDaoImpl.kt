package ru.netology.nmedia.db

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
            TODO("Not yet implemented")
            cursor.toPost()
        }
    }


    override fun save(post: Post): Post {
        TODO("Not yet implemented")
    }

    override fun likeById(postId: Long) {
        db.execSQL()
    }

    override fun removeById(postId: Long) {
        db.delete(
            PostTable.NAME,
            "${PostTable.Column.ID.columnName} = ?",
            arrayOf(postId.toString()),
        )
    }

}