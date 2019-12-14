package com.photo.sharing.utils

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.photo.sharing.activities.HomeActivity
import com.photo.sharing.extensions.createUUID
import java.io.File


class ImageUtils {

    companion object {
        fun openGallery(currentFragment: Fragment?) {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            currentFragment?.startActivityForResult(
                pickPhoto,
                1
            )
        }

        fun openCamera(currentFragment: Fragment?): String {
            val createdFileName = createUUID() + ".jpg"
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val file = File(Environment.getExternalStorageDirectory(), createdFileName)
            val uri: Uri = FileProvider.getUriForFile(
                currentFragment?.activity as HomeActivity,
                (currentFragment.activity as HomeActivity).applicationContext.packageName.toString() + ".provider",
                file
            )
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            currentFragment.startActivityForResult(cameraIntent, 2)
            return createdFileName
        }
    }
}