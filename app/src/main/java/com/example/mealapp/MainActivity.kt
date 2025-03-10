package com.example.mealapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mealapp.ui.*
import com.example.mealapp.ui.theme.MealAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MealAppTheme {
                val navController = rememberNavController()
                MealApp(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealApp(navController: NavHostController) {
    val navController = rememberNavController()
    val items = listOf("Home", "Search", "Country")
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = items[selectedItem]) }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            when (item) {
                                "Home" -> Image(
                                    painter = painterResource(id = R.drawable.home),
                                    contentDescription = item
                                )
                                "Search" -> Image(
                                    painter = painterResource(id = R.drawable.search),
                                    contentDescription = item
                                )
                                "Country" -> Image(
                                    painter = painterResource(id = R.drawable.flag),
                                    contentDescription = item
                                )
                                else -> Image(
                                    painter = painterResource(id = R.drawable.home),
                                    contentDescription = item
                                )
                            }
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(item)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "Home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("Home") { HomeScreen(navController) }
            composable("Search") { NameSearchScreen(navController) }
            composable("Country") { CountrySearchScreen(navController) }
            composable(
                route = "CountryMeals/{countryName}",
                arguments = listOf(navArgument("countryName") { type = NavType.StringType })
            ) { backStackEntry ->
                val countryName = backStackEntry.arguments?.getString("countryName")
                if (countryName != null) {
                    CountryMealsScreen(navController, countryName)
                } else {
                    Text(text = "Nome della nazione non valido")
                }
            }
            composable(
                route = "MealDetail/{mealId}",
                arguments = listOf(navArgument("mealId") { type = NavType.StringType })
            ) { backStackEntry ->
                val mealId = backStackEntry.arguments?.getString("mealId")
                if (mealId != null) {
                    MealDetailScreen(navController, mealId)
                } else {
                    Text(text = "ID del piatto non valido")
                }
            }
        }
    }
}