package com.test.vkapp.domain

import com.test.vkapp.data.VKRepositoryInterface
import com.vk.sdk.api.model.VKUsersArray
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers

class FriendsInteractor(private val repository: VKRepositoryInterface) : FriendsInteractorInterface {
    override val friendsPageProcessor: PublishProcessor<FriendsInteractorInterface.Result<VKUsersArray>> = PublishProcessor.create()
    override val needLoginProcessor: PublishProcessor<Unit> = PublishProcessor.create()

    override fun getPage(userId: String, accessToken: String, offset: Int, perPage: Int) {
        repository.getFriends(userId, accessToken, offset, perPage)
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .subscribe { data, error ->
                friendsPageProcessor.onNext(FriendsInteractorInterface.Result(data, error))
            }
    }

    override fun accessTokenHasReset() {
        needLoginProcessor.onNext(Unit)
    }

}