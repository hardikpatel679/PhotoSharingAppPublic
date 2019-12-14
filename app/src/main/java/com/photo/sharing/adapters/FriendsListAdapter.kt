package com.photo.sharing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.photo.sharing.R
import com.photo.sharing.model.UserFriends
import com.photo.sharing.utils.DateTimeUtils
import kotlinx.android.synthetic.main.row_friend_item.view.*

class FriendsListAdapter(private val listener: (UserFriends) -> Unit) :
    RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder>() {

    private var friendsList: List<UserFriends>? = null

    fun updateList(list: List<UserFriends>?) {
        friendsList = list
        notifyDataSetChanged()
    }

    inner class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(friend: UserFriends) = with(itemView) {
            tvFriendName.text = friend.userName

            ivArrowRight.setOnClickListener {
                listener(friend)
            }
            tvFriendFrom.text = itemView.context.getString(
                R.string.friend_since,
                DateTimeUtils.getDate(friend.friendSince)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_friend_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return friendsList?.size ?: 0
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.bind(friendsList!![position])

    }
}