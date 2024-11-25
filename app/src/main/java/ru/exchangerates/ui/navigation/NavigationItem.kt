package ru.exchangerates.ui.navigation

import androidx.annotation.DrawableRes

data class NavigationItem(
    val label: String,
    @DrawableRes
    val iconResId: Int,
    val route:String,
)