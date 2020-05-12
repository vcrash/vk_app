package com.test.vkapp.ui.login

import androidx.navigation.NavDirections
import com.test.vkapp.core.SingleEmitLiveData
import com.test.vkapp.domain.LoginInteractorInterface
import com.vk.sdk.VKAccessToken
import io.reactivex.rxjava3.disposables.CompositeDisposable

class LoginViewModel(private val interactor: LoginInteractorInterface) : LoginViewModelAbstract() {
    private val disposeBag = CompositeDisposable()

    /**
     * value is VK login scope (app permissions)
     */
    override val needLogin = SingleEmitLiveData<String>()

    override val needShowFriendsList = SingleEmitLiveData<NavDirections>()

    override val needShutDown = SingleEmitLiveData<Unit>()

    override val needShowRepeatLoginDialog = SingleEmitLiveData<Unit>()

    override fun initWorkFlow() {
        interactor.initWorkFlow()
    }

    override fun loginSucceed(accessToken: VKAccessToken) {
        interactor.loginSucceed(accessToken)
    }

    override fun loginFailed() {
        interactor.loginFailed()
    }

    override fun loginRepeatConfirmed() {
        interactor.loginRepeatConfirmed()
    }

    override fun loginRepeatDeclined() {
        interactor.loginRepeatDeclined()
    }

    init {
        subscribeAll()
    }

    override fun onCleared() {
        disposeBag.clear()
        super.onCleared()
    }

    private fun subscribeAll() {
        interactor.needLoginSubject.subscribe { needLogin.value = it.value }.also { disposeBag.add(it)}
        interactor.needRepeatLoginSubjectDialog.subscribe { needShowRepeatLoginDialog.value = Unit }.also { disposeBag.add(it) }
        interactor.showFriendsSubject.subscribe { accessToken ->
            needShowFriendsList.value = LoginFragmentDirections.actionToFriends(
                accessToken.userId,
                accessToken.accessToken
            )
        }.also { disposeBag.add(it) }
        interactor.shutdownSubject.subscribe { needShutDown.value = Unit }.also { disposeBag.add(it) }
    }

}