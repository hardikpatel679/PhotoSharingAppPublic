package com.photo.sharing.utils

import android.content.Context
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.photo.sharing.BuildConfig

object AWSConfiguration {

    private var s3Client: AmazonS3Client? = null
    private var credentials: BasicAWSCredentials? = null

    fun configureAws(context: Context) {
        AWSMobileClient.getInstance().initialize(context).execute()
        credentials = BasicAWSCredentials(BuildConfig.KEY, BuildConfig.SECRET)
        s3Client = AmazonS3Client(credentials)
    }

    fun getTransferUtilityBuilder(applicationContext: Context?): TransferUtility {
        return TransferUtility.builder()
            .context(applicationContext)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .s3Client(s3Client)
            .build()
    }

    fun getAWSImagePath(): String {
        return BuildConfig.IMAGEPATH
    }
}