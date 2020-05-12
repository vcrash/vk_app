package com.test.vkapp.domain

import com.vk.sdk.api.model.VKUsersArray
import io.reactivex.rxjava3.processors.PublishProcessor

interface FriendsInteractorInterface {
    val friendsPageProcessor: PublishProcessor<Result<VKUsersArray>>

    val needLoginProcessor: PublishProcessor<Unit>

    fun getPage(userId: String, accessToken: String, offset: Int, perPage: Int)

    fun accessTokenHasReset()

    data class Result<T>(val data: T?, val error: Throwable? = null)
}