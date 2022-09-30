package ru.droidcat.core_remote_impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.jvm.javaio.*
import ru.droidcat.core_remote_api.LoadResult
import ru.droidcat.core_remote_api.model.Order
import ru.droidcat.core_remote_api.RemoteRepository
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : RemoteRepository {

    private val hostUrl = "www.roxiemobile.ru/careers/test"

    override suspend fun loadActiveOrders(): LoadResult<List<Order>> {
        try {
            val response = httpClient.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = hostUrl
                    appendPathSegments("orders.json")
                }
            }

            if (!response.status.isSuccess()) {
                return LoadResult.ERROR
            }

            val orders: List<Order> = response.body()

            return LoadResult.SUCCESS(orders)

        } catch (e: Exception) {
            Log.e("Remote", e.message.toString())
            return LoadResult.ERROR
        }
    }

    override suspend fun downloadImage(photo: String): LoadResult<Bitmap> {
        try {
            val response = httpClient.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = hostUrl
                    path("images")
                    appendPathSegments(photo)
                }
            }

            if (!response.status.isSuccess()) {
                return LoadResult.ERROR
            }

            val output = ByteArrayOutputStream()
            response.bodyAsChannel().copyTo(output)

            val byteArray = output.toByteArray()
            if (byteArray.isEmpty()) {
                return LoadResult.ERROR
            }

            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            return LoadResult.SUCCESS(bitmap)

        } catch (e: Exception) {
            Log.e("Remote", e.message.toString())
            return LoadResult.ERROR
        }
    }
}