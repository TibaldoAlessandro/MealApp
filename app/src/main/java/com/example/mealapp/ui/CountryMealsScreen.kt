package com.example.mealapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.mealapp.api.Meal
import com.example.mealapp.api.MealApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun CountryMealsScreen(navController: NavHostController, countryName: String) {
    var meals by remember { mutableStateOf<List<Meal>>(emptyList()) }

    LaunchedEffect(countryName) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(MealApi::class.java)
        val response = withContext(Dispatchers.IO) { api.getMealsByCountry(countryName) }
        meals = response.meals ?: emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Piatti tipici di $countryName",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(meals) { meal ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("MealDetail/${meal.idMeal}")
                        }
                ) {
                    // Immagine del piatto
                    Image(
                        painter = rememberImagePainter(data = meal.strMealThumb),
                        contentDescription = meal.strMeal,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    // Nome del piatto
                    Text(
                        text = meal.strMeal,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }
        }
    }
}