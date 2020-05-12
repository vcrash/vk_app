package com.test.vkapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.test.vkapp.domain.FriendsInteractor
import com.test.vkapp.ui.friendslist.FriendsListItem
import com.test.vkapp.ui.friendslist.FriendsViewModel
import com.test.vkapp.ui.friendslist.PagePendingViewData
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import kotlinx.coroutines.runBlocking
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.junit.MockitoJUnitRunner
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import io.reactivex.rxjava3.schedulers.Schedulers

@RunWith(MockitoJUnitRunner::class)
class FriendsViewModelTest : KoinTest {

    private lateinit var viewModel: FriendsViewModel
    private val pagesRequestLimit = 3
    private var pageRequestCount = 0

    val dataObserver = MutatingObserver()

    class MutatingObserver : Observer<List<FriendsListItem>> {
        private var onChangedDelegate: ((List<FriendsListItem>?)->Unit)? = null

        fun setDelegate(delegate: ((List<FriendsListItem>?)->Unit)) {
            synchronized(this) {
                onChangedDelegate = delegate
            }
        }

        override fun onChanged(t: List<FriendsListItem>?) {
            onChangedDelegate?.invoke(t)
        }
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test(timeout = 15000)
    fun testPaging() {
        runBlocking {
            viewModel = FriendsViewModel(
                get(),
                FriendsInteractor(get()),
                "MyTestUserId",
                "MyTestAccessToken",
                    this
                )

            var running = true
            while (running) {
                val listData = getPage()
                assertThat(listData).isNotNull()
                pageRequestCount++
                assert(pageRequestCount <= pagesRequestLimit)
                if (listData.last() !is PagePendingViewData) {
                    running = false
                }
            }
            stopObserving(dataObserver)
        }
    }

    private suspend fun getPage() = suspendCoroutine<List<FriendsListItem>> { continuation ->
        synchronized(this) {
            viewModel.friendsList.observeForever(dataObserver)
            dataObserver.setDelegate {
                continuation.resume(it ?: emptyList())
            }
            viewModel.getPage()
        }
    }

    private fun stopObserving(observer: Observer<List<FriendsListItem>>?) {
        synchronized(this) {
            viewModel.friendsList.removeObserver(observer ?: return)
        }
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun before() {
            println("Rx plugins setup")
            RxAndroidPlugins.reset()
            RxJavaPlugins.reset()
            RxJavaPlugins.setIoSchedulerHandler handler@{
                return@handler Schedulers.trampoline()
            }
            RxJavaPlugins.setComputationSchedulerHandler handler@{
                return@handler Schedulers.trampoline()
            }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler handler@{
                return@handler Schedulers.trampoline()
            }
        }
    }
}