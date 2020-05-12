package com.test.vkapp.ui.friendslist

import com.test.vkapp.R
import com.test.vkapp.databinding.ItemFriendCardBinding
import com.test.vkapp.databinding.ItemPagePendingBinding
import com.test.vkapp.ui.base.AbstractRecyclerViewAdapter
import com.test.vkapp.ui.base.AbstractRecyclerViewViewHolder

class FriendsListAdapter(
    itemsData: List<FriendsListItem>,
    onRequestPageClick: ()->Unit,
    onNextPageAutoLoaderShow: ()->Unit
) : AbstractRecyclerViewAdapter<FriendsListItem, AbstractRecyclerViewViewHolder<FriendsListItem>>(
    itemsData,
    onGetItemLayoutId = resId@{position, dataSource ->
        val item = if (position < dataSource.size)
            dataSource[position]
        else
            null
        return@resId  when (item) {
            is FriendViewData -> R.layout.item_friend_card
            is PagePendingViewData -> R.layout.item_page_pending
            else -> 0
        }
    },
    onGetViewHolder = holder@{ view, viewType ->
        return@holder when (viewType) {
            R.layout.item_friend_card -> FriendViewHolder(ItemFriendCardBinding.bind(view))
            R.layout.item_page_pending  -> FriendsPagePendingHolder(ItemPagePendingBinding.bind(view), onRequestPageClick, onNextPageAutoLoaderShow)
            else -> EmptyViewHolder(view)
        }
    },
    onGetData = onGetData@{ offset, dataSource ->
        return@onGetData if (dataSource.isNotEmpty())
            dataSource[offset]
        else
            null
    }
)