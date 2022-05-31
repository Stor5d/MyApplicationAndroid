package ru.netology.nmedia.data.impl

import android.app.Application
import ru.netology.nmedia.data.PostRepository

 abstract class AppPostRepository : PostRepository {

     companion object {

        @Volatile
        private var INSTANCE: AppPostRepository? = null

        fun getInstance(application: Application): AppPostRepository {
            return if (INSTANCE == null) {
                INSTANCE = FilePostRepository(application = application)
                INSTANCE as AppPostRepository
            } else {
                INSTANCE as AppPostRepository
            }
        }
    }


}