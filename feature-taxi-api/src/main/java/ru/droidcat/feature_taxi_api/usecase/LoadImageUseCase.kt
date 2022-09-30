package ru.droidcat.feature_taxi_api.usecase

import android.graphics.Bitmap
import ru.droidcat.core_local_api.GetResult
import ru.droidcat.core_local_api.SaveResult
import ru.droidcat.core_local_api.StorageRepository
import ru.droidcat.core_remote_api.LoadResult
import ru.droidcat.core_remote_api.RemoteRepository
import javax.inject.Inject

class LoadImageUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
    private val remoteRepository: RemoteRepository
) {

    suspend operator fun invoke(photo: String): Bitmap? {
        val imageFromDb = storageRepository.getFile(photo, 10 * 60)

        when (imageFromDb) {
            is GetResult.SUCCESS -> {
                return imageFromDb.file
            }
            else -> {
                val downloadResult = remoteRepository.downloadImage(photo)
                if (downloadResult is LoadResult.SUCCESS) {
                    val saveResult = storageRepository.saveFile(downloadResult.data, photo)
                    if (saveResult == SaveResult.SUCCESS) {
                        return downloadResult.data
                    }
                }
                return null
            }
        }
    }
}