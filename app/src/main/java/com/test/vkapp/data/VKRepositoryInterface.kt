package com.test.vkapp.data

import com.vk.sdk.api.model.VKUsersArray
import io.reactivex.rxjava3.core.Single

interface VKRepositoryInterface {
    fun getFriends(userId: String, accessToken: String, offset: Int, perPage: Int) : Single<VKUsersArray>
}