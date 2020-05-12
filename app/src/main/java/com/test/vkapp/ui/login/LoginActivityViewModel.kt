package com.test.vkapp.ui.login

import androidx.lifecycle.LiveData
import com.test.vkapp.core.SingleEmitLiveData
import com.vk.sdk.VKAccessToken

class LoginActivityViewModel : LoginActivityViewModelAbstract() {
    private val loginSuccess = SingleEmitLiveData<VKAccessToken>()
    private val loginFailed = SingleEmitLiveData<Unit>()
    private val onAccessTokenReset = SingleEmitLiveData<Unit>()

    override val loginSuccessData: LiveData<VKAccessToken>
        get() = loginSuccess

    override val loginFailedData: LiveData<Unit>
        get() = loginFailed

    override val onAccessTokenResetData: LiveData<Unit>
        get() = onAccessTokenReset

    override fun provideLoginSuccess(result: VKAccessToken) {
        loginSuccess.value = result
    }

    override fun provideLoginFailed() {
        loginFailed.value = Unit
    }

    override fun tokenHasReset() {
        onAccessTokenReset.value = Unit
    }
}