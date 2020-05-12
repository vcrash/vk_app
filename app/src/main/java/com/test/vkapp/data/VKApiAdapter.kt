package com.test.vkapp.data

import com.vk.sdk.api.*
import com.vk.sdk.api.VKRequest.VKRequestListener
import com.vk.sdk.api.model.VKApiModel
import com.vk.sdk.api.model.VKUsersArray
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


class VKApiAdapter @Inject constructor() {

    fun getFriends(userId: String, accessToken: String, offset: Int, count: Int) : Single<VKUsersArray> {
        val request = VKApi.friends().get(VKParameters.from(
            VKApiConst.USER_ID, userId,
            VKApiConst.ACCESS_TOKEN, accessToken,
            VKApiConst.COUNT, count,
            VKApiConst.OFFSET, offset,
            VKApiConst.FIELDS, VK_PARAMS_NICKNAME,
            VKApiConst.FIELDS, VK_PARAMS_PHOTO_100
        ))
        return doRequest(request)
    }

    private inline fun <reified T: VKApiModel> doRequest(request: VKRequest) : Single<T> {
        return Single.create { emitter ->
            request.setModelClass(T::class.java)
            request.executeWithListener(object : VKRequestListener() {
                override fun onComplete(response: VKResponse) {
                    (response.parsedModel as? T)?.also {
                        emitter?.onSuccess(it)
                    } ?: run {
                        emitter?.onError(Throwable(RESPONSE_PARSE_ERROR))
                    }
                }

                override fun onError(error: VKError) {
                    emitter?.onError(error.httpError)
                }

                override fun attemptFailed(
                    request: VKRequest,
                    attemptNumber: Int,
                    totalAttempts: Int
                ) { }
            })
        }
    }

    companion object {
        const val VK_PARAMS_NICKNAME = "nickname"
        const val VK_PARAMS_PHOTO_100 = "photo_100"
        const val RESPONSE_PARSE_ERROR = "response parse error"
    }
}