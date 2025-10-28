package com.example.projet1diiage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.projet1diiage.NavHost.AppNavHost
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import com.example.projet1diiage.home.api.HomeModule
import com.example.projet1diiage.profile.api.ProfileModule
import com.example.projet1diiage.core.ui.theme.AppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            modules(
                HomeModule,
                ProfileModule,
            )
        }
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}
