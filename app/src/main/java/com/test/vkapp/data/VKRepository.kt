package com.test.vkapp.data

import com.vk.sdk.api.model.VKUsersArray
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VKRepository @Inject constructor(private val vkApiAdapter: VKApiAdapter) : VKRepositoryInterface {

    override fun getFriends(userId: String, accessToken: String, offset: Int, perPage: Int) : Single<VKUsersArray> {
        return vkApiAdapter.getFriends(userId, accessToken, offset, perPage)
    }
}