package com.photo.sharing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.photo.sharing.R
import com.photo.sharing.model.SharedPhoto
import com.photo.sharing.model.UserSharedPhoto
import com.photo.sharing.utils.AWSConfiguration
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_shared_images.view.*
import java.lang.Exception

class SharedImagesAdapter(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<SharedImagesAdapter.ImagesViewHolder>() {

    private var sharedPhotoList: List<UserSharedPhoto>? = null

    fun updateList(list: List<UserSharedPhoto>?) {
        sharedPhotoList = list
        notifyDataSetChanged()
    }

    inner class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(sharedPhoto: UserSharedPhoto) = with(itemView) {
            progressBar.visibility = View.VISIBLE
            Picasso.get()
                .load(
                    AWSConfiguration.getAWSImagePath() +
                            sharedPhoto.imageName
                )
                .resize(300, 300)
                .into(ivSharedImage, object : Callback {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        ivSharedImage.setImageResource(R.drawable.ic_launcher_background)
                        progressBar.visibility = View.GONE
                    }

                })

            ivSharedImage.setOnLongClickListener {
                listener(sharedPhoto.sharedId)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        return ImagesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_shared_images,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return sharedPhotoList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.bind(sharedPhotoList!![position])
    }
}