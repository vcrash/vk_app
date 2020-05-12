package com.test.vkapp.ui.friendslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

abstract class FriendsViewModelAbstract(application: Application) : AndroidViewModel(application) {

    abstract val friendsList: LiveData<List<FriendsListItem>>

    abstract val fullscreenProgressData: LiveData<Boolean>

    abstract val onNeedToastData: LiveData<String>

    abstract val onNeedCenterRefreshButton: LiveData<Boolean>

    abstract val onNeedLogin: LiveData<Unit>

    abstract fun getPage()

    abstract fun onPageLoaderProgressHasShown()

    abstract fun onPageRequestTapped()

    abstract fun onAccessTokenReset()

}