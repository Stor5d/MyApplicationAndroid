package ru.netology.nmedia.db

object PostTable {

    const val NAME = "posts"

    val DDL = """
        CREATE TABLE $NAME (
            ${Column.ID.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Column.AUTHOR.columnName} TEXT NOT NULL,
            ${Column.CONTENT.columnName} TEXT NOT NULL,
            ${Column.PUBLISHED.columnName} TEXT NOT NULL,
            ${Column.LIKED_BY_ME.columnName} BOOLEAN NOT NULL DEFAULT 0,
            ${Column.LIKES.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.SHARE_COUNT.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.VIEW_COUNT.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.URL_VIDEO.columnName} TEXT
        );
        """.trimIndent()

    val ALL_COLUMNS_NAME = Column.values().map { it.columnName }.toTypedArray()

    enum class Column(val columnName: String) {
        ID("id"),
        AUTHOR("author"),
        CONTENT("content"),
        PUBLISHED("published"),
        LIKED_BY_ME("likeByMe"),
        LIKES("likes"),
        SHARE_COUNT("shareCount"),
        VIEW_COUNT("viewCount"),
        URL_VIDEO("urlVideo")
    }


}