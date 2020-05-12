package com.test.vkapp.ui.friendslist


sealed class FriendsListItem

data class FriendViewData(
    val userName: String,
    val imageUrl: String?
) : FriendsListItem()

/**
 * @param isAutoLoader - Если передано значение true, следует отобразить анимацию загрузки следующей страницы.
 * Значение false предполагает отображение кнопки запроса страницы.
 */
data class PagePendingViewData(val isAutoLoader: Boolean) : FriendsListItem()