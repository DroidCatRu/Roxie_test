package ru.droidcat.core_local_api

import android.graphics.Bitmap

/**
 * Interface for saving and getting files from local device storage
 */
interface StorageRepository {

    /**
     * @param name file name
     * @param expireTime time in sec after which this file will be marked as expired
     *
     * @return GetResult.SUCCESS with file if file successfully loaded from storage
     * GetResult.FILE_NOT_FOUND if file is missing
     * GetResult.FILE_EXPIRED if file lifetime is over
     *
     * @see GetResult
     */
    suspend fun getFile(
        name: String,
        expireTime: Long
    ): GetResult<Bitmap>

    /**
     * @param data data you want to put in file
     * @param name file name
     *
     * @return SaveResult.ERROR if something went wrong
     * SaveResult.SUCCESS if file successfully saved
     *
     * @see SaveResult
     */
    suspend fun saveFile(
        data: Bitmap,
        name: String
    ): SaveResult
}

sealed class GetResult<out T: Any> {
    data class SUCCESS<out T: Any>(
        val file: T
    ) : GetResult<T>()

    object FILE_NOT_FOUND : GetResult<Nothing>()
    object FILE_EXPIRED : GetResult<Nothing>()
    object CANT_DECODE : GetResult<Nothing>()
    object NO_DATETIME_DATA : GetResult<Nothing>()
}

sealed class SaveResult {
    object SUCCESS : SaveResult()
    object ERROR : SaveResult()
}