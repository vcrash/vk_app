package com.test.vkapp.ui.friendslist

import com.bumptech.glide.Glide
import com.test.vkapp.databinding.ItemFriendCardBinding
import com.test.vkapp.ui.base.DataSubtypeRecyclerViewViewHolder

class FriendViewHolder(private val viewBinding: ItemFriendCardBinding)
    : DataSubtypeRecyclerViewViewHolder<FriendsListItem, FriendViewData>(viewBinding.root) {
    override fun bindSubtypeData(dataItem: FriendViewData?) {
        viewBinding.apply {
            dataItem ?: return
            dataItem.imageUrl?.also { url ->
                Glide.with(imageView.context ?: return).load(url).into(imageView)
            }?.run {

            }
            nameLabel.text = dataItem.userName
        }
    }
}