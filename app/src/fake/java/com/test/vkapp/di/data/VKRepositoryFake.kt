package com.test.vkapp.di.data

import com.test.vkapp.data.VKApiAdapter
import com.test.vkapp.data.VKRepositoryInterface
import com.vk.sdk.api.model.VKUsersArray
import io.reactivex.rxjava3.core.Single
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VKRepositoryFake @Inject constructor(private val vkApiAdapter: VKApiAdapter) :
    VKRepositoryInterface {

    override fun getFriends(userId: String, accessToken: String, offset: Int, perPage: Int) : Single<VKUsersArray> {
        val usersData = JSONObject(USERS_DATA)
        usersData.getJSONObject("response").put("items", getUsersJsonList(offset, count = perPage))
        return Single.timer(1, TimeUnit.SECONDS).flatMap {
            return@flatMap Single.create<VKUsersArray> {
                it.onSuccess(VKUsersArray().parse(usersData) as VKUsersArray)
            }
        }
    }

    private fun getUsersJsonList(offset: Int, count: Int) : JSONArray {
        val users = JSONArray()
        val pageRange = offset + count
        val end = if (pageRange > FRIENDS_COUNT) FRIENDS_COUNT else pageRange
        for (i in offset until end) {
            users.put(JSONObject().apply {
                put("id", "000000")
                put("first_name", "First name ${i + 1}")
                put("last_name", "Last name ${i + 1}")
                put("photo_100", "https://vk.com/images/camera_b.gif")
            })
        }
        return users
    }

    companion object {
        const val FRIENDS_COUNT = 20
        const val USERS_DATA = """
{
    "response": {
        "count": $FRIENDS_COUNT,
        "items": [

        ]
    }
}
"""
    }
}