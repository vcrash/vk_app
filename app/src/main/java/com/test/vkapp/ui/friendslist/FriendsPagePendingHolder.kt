package com.test.vkapp.ui.friendslist

import android.view.View
import com.test.vkapp.databinding.ItemPagePendingBinding
import com.test.vkapp.ui.base.DataSubtypeRecyclerViewViewHolder

class FriendsPagePendingHolder(
    private val viewBinding: ItemPagePendingBinding,
    val onRequestClick: ()->Unit,
    val onAutoLoaderShow: ()->Unit
) : DataSubtypeRecyclerViewViewHolder<FriendsListItem, PagePendingViewData>(viewBinding.root) {

    override fun bindSubtypeData(dataItem: PagePendingViewData?) {
        viewBinding.apply {
            dataItem ?: return
            if (dataItem.isAutoLoader) {
                progress.visibility = View.VISIBLE
                requestPageButton.visibility = View.INVISIBLE
                onAutoLoaderShow()
            } else {
                progress.visibility = View.INVISIBLE
                requestPageButton.visibility = View.VISIBLE
                requestPageButton.setOnClickListener { onRequestClick() }
            }
            progress.requestLayout()
            requestPageButton.requestLayout()
        }
    }

}