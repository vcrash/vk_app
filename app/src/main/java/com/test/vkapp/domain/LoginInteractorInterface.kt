package com.test.vkapp.domain

import com.vk.sdk.VKAccessToken
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.subjects.PublishSubject

interface LoginInteractorInterface {
    val shutdownSubject: PublishSubject<Unit>
    val needLoginSubject: PublishSubject<VKAuthScope>
    val showFriendsSubject: PublishSubject<@NonNull VKAccessToken>
    val needRepeatLoginSubjectDialog: PublishSubject<Unit>

    fun initWorkFlow()
    fun loginSucceed(accessToken: VKAccessToken)
    fun loginFailed()
    fun loginRepeatConfirmed()
    fun loginRepeatDeclined()
}