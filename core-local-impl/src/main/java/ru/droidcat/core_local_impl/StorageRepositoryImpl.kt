package ru.droidcat.core_local_impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone.GMT_ZONE
import android.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.droidcat.core_local_api.GetResult
import ru.droidcat.core_local_api.SaveResult
import ru.droidcat.core_local_api.StorageRepository
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : StorageRepository {

    override suspend fun getFile(name: String, expireTime: Long): GetResult<Bitmap> {
        val pathname = context.filesDir.absolutePath + File.separator + name
        val file = File(pathname)
        if (!file.exists()) {
            return GetResult.FILE_NOT_FOUND
        }

        val bitmap = BitmapFactory.decodeFile(pathname) ?: return GetResult.CANT_DECODE

        val exif = ExifInterface(file.absolutePath)
        val datetime = exif.dateTime

        if (datetime < 0) {
            return GetResult.NO_DATETIME_DATA
        }

        val calendar = Calendar.getInstance()
        val exifCalendar = Calendar.getInstance()
        exifCalendar.timeInMillis = datetime

        if (calendar.timeInMillis > (exifCalendar.timeInMillis + (expireTime * 1000))) {
            return GetResult.FILE_EXPIRED
        }

        return GetResult.SUCCESS(bitmap)
    }

    override suspend fun saveFile(data: Bitmap, name: String): SaveResult {
        return withContext(Dispatchers.IO) {
            val file =
                File(context.filesDir.absolutePath + File.separator + name)
            file.delete()
            file.createNewFile()

            val outputStream = ByteArrayOutputStream()
            val compressResult = data.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                outputStream
            )

            if (!compressResult) {
                return@withContext SaveResult.ERROR
            }

            val bitmapData = outputStream.toByteArray()

            try {
                context.openFileOutput(name, Context.MODE_PRIVATE).use {
                    it.write(bitmapData)
                    it.flush()
                    it.close()
                }
            } catch (e: Exception) {
                return@withContext SaveResult.ERROR
            }

            val calendar = Calendar.getInstance()
            val format = SimpleDateFormat("yyyy:MM:dd HH:mm:ss")
            format.timeZone = GMT_ZONE
            val exifDatetime = format.format(calendar.time)

            val exif = ExifInterface(file.absolutePath)
            exif.setAttribute(ExifInterface.TAG_DATETIME, exifDatetime)
            exif.saveAttributes()

            return@withContext SaveResult.SUCCESS
        }
    }
}