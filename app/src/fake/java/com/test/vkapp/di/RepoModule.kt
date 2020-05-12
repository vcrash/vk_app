package com.test.vkapp.di

import com.test.vkapp.data.VKApiAdapter
import com.test.vkapp.data.VKRepositoryInterface
import com.test.vkapp.di.data.VKRepositoryFake
import org.koin.dsl.module

val repoModule = module {
    single { VKRepositoryFake(get()) as VKRepositoryInterface }
    single { VKApiAdapter() }
}