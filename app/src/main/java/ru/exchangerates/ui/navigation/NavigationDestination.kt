package ru.exchangerates.ui.navigation

import ru.exchangerates.R

object NavigationDestination {
    internal const val HOME_SCREEN_DESTINATION = "home/"
    internal const val CONVERSATION_SCREEN_DESTINATION = "conversation/"
    internal const val COIN_DETAILS_SCREEN_DESTINATION = "coin_details/{item}"

    internal fun getNavigationItems(): List<NavigationItem> {
        return listOf(
            NavigationItem(
                label = "Home",
                iconResId = R.drawable.ic_home_screen,
                route = HOME_SCREEN_DESTINATION,
            ),
            NavigationItem(
                label = "Conversation",
                iconResId = R.drawable.ic_conversation_screen,
                route = CONVERSATION_SCREEN_DESTINATION,
            ),
        )
    }


}