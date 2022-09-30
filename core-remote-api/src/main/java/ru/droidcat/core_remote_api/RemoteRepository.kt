package ru.droidcat.core_remote_api

import android.graphics.Bitmap
import ru.droidcat.core_remote_api.model.Order

interface RemoteRepository {

    suspend fun loadActiveOrders(): LoadResult<List<Order>>

    suspend fun downloadImage(photo: String): LoadResult<Bitmap>
}

sealed class LoadResult<out T : Any> {
    data class SUCCESS<out T : Any>(
        val data: T
    ) : LoadResult<T>()

    object ERROR : LoadResult<Nothing>()
}