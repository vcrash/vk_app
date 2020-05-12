package com.test.vkapp.di

import com.test.vkapp.data.VKApiAdapter
import com.test.vkapp.data.VKRepository
import com.test.vkapp.data.VKRepositoryInterface
import org.koin.dsl.module

val repoModule = module {
    single { VKRepository(get()) as VKRepositoryInterface }
    single { VKApiAdapter() }
}