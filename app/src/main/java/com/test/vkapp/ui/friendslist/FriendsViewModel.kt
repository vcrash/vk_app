package com.test.vkapp.ui.friendslist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.test.vkapp.R
import com.test.vkapp.core.SingleEmitLiveData
import com.test.vkapp.domain.FriendsInteractorInterface
import com.vk.sdk.api.model.VKApiUserFull
import com.vk.sdk.api.model.VKUsersArray
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendsViewModel(
    application: Application,
    private val interactor: FriendsInteractorInterface,
    private val userId: String,
    private val accessToken: String,
    private val testingScope: CoroutineScope? = null
) : FriendsViewModelAbstract(application) {
    private var currentOffset: Int = 0
    private var friendsCount: Int = 0
    private var disposeBag = CompositeDisposable()
    private var friends = mutableListOf<FriendViewData>()
    private val friendsListWithLoaderFooter = MutableLiveData<List<FriendsListItem>>()
    private val fullscreenProgress = MediatorLiveData<Boolean>()
    private var pageWaiting = MutableLiveData(false)
    private val needToast = SingleEmitLiveData<String>()
    private val needCenterRefreshButton = MutableLiveData(false)
    private val needLogin = SingleEmitLiveData<Unit>()

    private val actualScope : CoroutineScope
        get() = testingScope ?: viewModelScope

    override val friendsList: LiveData<List<FriendsListItem>>
        get() = friendsListWithLoaderFooter

    override val fullscreenProgressData: LiveData<Boolean>
        get() = fullscreenProgress

    override val onNeedToastData: LiveData<String>
        get() = needToast

    override val onNeedCenterRefreshButton: LiveData<Boolean>
        get() = needCenterRefreshButton

    override fun getPage() {
        if (pageWaiting.value == true) return
        needCenterRefreshButton.value = false
        pageWaiting.value = true
        interactor.getPage(userId, accessToken, currentOffset, PER_PAGE)
    }

    override val onNeedLogin: LiveData<Unit>
        get() = needLogin

    override fun onPageLoaderProgressHasShown() {
        getPage()
    }

    override fun onPageRequestTapped() {
        publishFriendsListWithFooter(friends, true)
        getPage()
    }

    override fun onAccessTokenReset() {
        interactor.accessTokenHasReset()
    }

    init {
        setupLiveData()
        subscribeInteractor()
    }

    private fun setupLiveData() {
        fullscreenProgress.addSource(pageWaiting) {
            fullscreenProgress.value = it && currentOffset == 0
        }
    }

    private fun subscribeInteractor() {
        interactor.friendsPageProcessor.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { pageResult ->
                pageWaiting.value = false
                if (pageResult.data == null || pageResult.error != null) {
                    handlePageRequestError(pageResult.error)
                    return@subscribe
                }
                processFriendsPage(pageResult.data)
            }, {
                pageWaiting.value = false
                handlePageRequestError(it)
            }).also { disposeBag.add(it) }
        interactor.needLoginProcessor
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _ ->
                needLogin.value = Unit
            }
    }

    private fun handlePageRequestError(t: Throwable?) {
        getApplication<Application>().getString(R.string.friends_request_failed_toast).also {
            needToast.postValue(it)
        }
        if (currentOffset == 0) {
            showCenteredRequestButton()
        } else {
            showFooterRequestButton()
        }
    }

    override fun onCleared() {
        disposeBag.dispose()
        super.onCleared()
    }

    private fun processFriendsPage(friendsData: VKUsersArray) {
        friendsCount = friendsData.count
        val usersList: List<VKApiUserFull> = friendsData.toList()
        currentOffset += usersList.size
        actualScope.launch {
            val ctx = coroutineContext
            launch(Dispatchers.Default) {
                val convertedList = usersList.map { convertItemToViewData(it) }
                withContext(ctx) {
                    friends.plusAssign(convertedList)
                    publishFriendsListWithFooter(friends, true)
                }
            }
        }
    }

    private fun publishFriendsListWithFooter(friends: List<FriendViewData>, showNextPageAutoLoader: Boolean) {
        friendsListWithLoaderFooter.postValue(
            friends.let {
                if (friends.size < friendsCount) {
                    it.plus(getLoaderFooter(showNextPageAutoLoader))
                } else it
            }
        )
    }

    private fun convertItemToViewData(item: VKApiUserFull) : FriendViewData {
        return FriendViewData(
            "${item.first_name} ${item.last_name}",
            item.photo_100
        )
    }

    private fun getLoaderFooter(showNextPageAutoLoader: Boolean) : PagePendingViewData {
        return PagePendingViewData(isAutoLoader = showNextPageAutoLoader)
    }

    private fun showFooterRequestButton() {
        publishFriendsListWithFooter(friends, false)
    }

    private fun showCenteredRequestButton() {
        needCenterRefreshButton.value = true
    }

    companion object {
        const val PER_PAGE = 15
    }
}