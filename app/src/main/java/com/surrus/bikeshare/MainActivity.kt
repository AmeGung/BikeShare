package com.surrus.bikeshare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.surrus.bikeshare.ui.BikeShareTheme
import org.osmdroid.config.Configuration


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // needed for osmandroid
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID


        setContent {
            MainLayout()
        }
    }
}


sealed class Screen(val title: String) {
    object CountryListScreen : Screen("CountryList")
    object NetworkListScreen : Screen("NetworkList")
    object StationsScreen : Screen("Stations")
}


@Composable
fun MainLayout() {
    val navController = rememberNavController()

    BikeShareTheme {
        NavHost(navController, startDestination = Screen.CountryListScreen.title) {
            composable(Screen.CountryListScreen.title) {
                CountryListScreen {
                    navController.navigate(Screen.NetworkListScreen.title + "/$it")
                }
            }
            composable(Screen.NetworkListScreen.title + "/{countryCode}") { backStackEntry ->
                NetworkListScreen(backStackEntry.arguments?.get("countryCode") as String,
                    networkSelected = { navController.navigate(Screen.StationsScreen.title
                            + "/${it.id}/${it.location.latitude}/${it.location.longitude}") },
                    popBack = { navController.popBackStack() })
            }
            composable(Screen.StationsScreen.title + "/{networkId}/{latitude}/{longitude}",) { backStackEntry ->
                StationsScreen(backStackEntry.arguments?.get("networkId") as String,
                    backStackEntry.arguments?.get("latitude") as String,
                    backStackEntry.arguments?.get("longitude") as String,
                    popBack = { navController.popBackStack() })
            }
        }
    }
}
