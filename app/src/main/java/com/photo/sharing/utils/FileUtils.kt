package com.photo.sharing.utils

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.amazonaws.util.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileUtils {
    companion object {

        fun createFile(context: Context, srcUri: Uri?, dstFile: File) {
            try {
                val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
                val outputStream = FileOutputStream(dstFile)
                IOUtils.copy(inputStream, outputStream)
                inputStream.close()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun getFileExtension(uri: Uri?, context: Context?): String {
            val contentResolver = context?.contentResolver
            val mime = MimeTypeMap.getSingleton()

            return mime.getExtensionFromMimeType(contentResolver?.getType(uri!!))!!
        }
    }
}