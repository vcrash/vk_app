package com.test.vkapp.ui.base

import android.view.View

@Suppress("UNCHECKED_CAST")
abstract class DataSubtypeRecyclerViewViewHolder<BaseDataType, ExactDataType : BaseDataType>(view: View) : AbstractRecyclerViewViewHolder<BaseDataType>(view) {
    override fun bindData(dataItem: BaseDataType?) {
        bindSubtypeData(dataItem as ExactDataType)
    }

    abstract fun bindSubtypeData(dataItem: ExactDataType?)

}