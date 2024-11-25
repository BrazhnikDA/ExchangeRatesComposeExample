package ru.exchangerates

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.exchangerates.ui.navigation.BottomNavigationBar
import ru.exchangerates.ui.navigation.NavigationDestination.COIN_DETAILS_SCREEN_DESTINATION
import ru.exchangerates.ui.navigation.NavigationDestination.CONVERSATION_SCREEN_DESTINATION
import ru.exchangerates.ui.navigation.NavigationDestination.HOME_SCREEN_DESTINATION
import ru.exchangerates.ui.screen.coindetails.CoinDetailsScreen
import ru.exchangerates.ui.screen.conversation.ConversationScreen
import ru.exchangerates.ui.screen.main.MainScreen
import ru.exchangerates.ui.theme.ExchangeRatesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            ExchangeRatesTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        // Белоснежный
                        .background(Color(red = 255, green = 250, blue = 250)),
                ) {
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }, content = { padding ->
                            Navigation(navController = navController)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = HOME_SCREEN_DESTINATION) {
        composable(HOME_SCREEN_DESTINATION) {
            MainScreen(navController)
        }
        composable(CONVERSATION_SCREEN_DESTINATION) {
            ConversationScreen()
        }
        composable(COIN_DETAILS_SCREEN_DESTINATION) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            CoinDetailsScreen(item)
        }
    }
}
