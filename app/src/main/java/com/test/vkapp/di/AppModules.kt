package com.test.vkapp.di

import com.test.vkapp.domain.FriendsInteractor
import com.test.vkapp.domain.FriendsInteractorInterface
import com.test.vkapp.domain.LoginInteractor
import com.test.vkapp.domain.LoginInteractorInterface
import com.test.vkapp.ui.friendslist.FriendsViewModel
import com.test.vkapp.ui.friendslist.FriendsViewModelAbstract
import com.test.vkapp.ui.login.LoginActivityViewModel
import com.test.vkapp.ui.login.LoginActivityViewModelAbstract
import com.test.vkapp.ui.login.LoginViewModel
import com.test.vkapp.ui.login.LoginViewModelAbstract
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    factory { LoginInteractor() as LoginInteractorInterface }
    viewModel {LoginViewModel(get()) as LoginViewModelAbstract }
    viewModel { LoginActivityViewModel() as LoginActivityViewModelAbstract }
}

val friendsModule = module {
    factory { FriendsInteractor(get()) as FriendsInteractorInterface }
    viewModel {
        (userId: String, accessToken: String) -> FriendsViewModel(get(), get(), userId, accessToken) as FriendsViewModelAbstract
    }
}

val appModules = listOf(repoModule, friendsModule, loginModule)

