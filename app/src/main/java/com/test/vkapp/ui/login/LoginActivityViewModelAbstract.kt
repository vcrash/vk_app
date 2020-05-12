package com.test.vkapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.vk.sdk.VKAccessToken

abstract class LoginActivityViewModelAbstract : ViewModel() {
    abstract val loginSuccessData: LiveData<VKAccessToken>
    abstract val loginFailedData: LiveData<Unit>
    abstract val onAccessTokenResetData: LiveData<Unit>
    abstract fun provideLoginSuccess(result: VKAccessToken)
    abstract fun provideLoginFailed()
    abstract fun tokenHasReset()
}