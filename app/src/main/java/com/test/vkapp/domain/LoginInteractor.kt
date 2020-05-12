package com.test.vkapp.domain

import com.vk.sdk.VKAccessToken
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.subjects.PublishSubject

class LoginInteractor : LoginInteractorInterface {
    override val shutdownSubject = PublishSubject.create<Unit>()
    override val needLoginSubject = PublishSubject.create<VKAuthScope>()
    override val showFriendsSubject = PublishSubject.create<@NonNull VKAccessToken>()
    override val needRepeatLoginSubjectDialog = PublishSubject.create<Unit>()

    override fun initWorkFlow() {
        needLoginSubject.onNext(VKAuthScope())
    }

    override fun loginSucceed(accessToken: VKAccessToken) {
        showFriendsSubject.onNext(accessToken)
    }

    override fun loginFailed() {
        needRepeatLoginSubjectDialog.onNext(Unit)
    }

    override fun loginRepeatConfirmed() {
        needLoginSubject.onNext(VKAuthScope())
    }

    override fun loginRepeatDeclined() {
        shutdownSubject.onNext(Unit)
    }

}