package com.test.vkapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.test.vkapp.core.SingleEmitLiveData
import com.vk.sdk.VKAccessToken

abstract class LoginViewModelAbstract : ViewModel() {
    /**
     * value is VK login scope (app permissions)
     */
    abstract val needLogin: SingleEmitLiveData<String>

    abstract val needShowFriendsList: SingleEmitLiveData<NavDirections>

    abstract val needShutDown: SingleEmitLiveData<Unit>

    abstract val needShowRepeatLoginDialog: SingleEmitLiveData<Unit>

    abstract fun initWorkFlow()

    abstract fun loginSucceed(accessToken: VKAccessToken)

    abstract fun loginFailed()

    abstract fun loginRepeatConfirmed()

    abstract fun loginRepeatDeclined()
}