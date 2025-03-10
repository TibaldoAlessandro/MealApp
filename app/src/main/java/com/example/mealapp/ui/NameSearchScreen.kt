package com.example.mealapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
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
fun NameSearchScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    var meals by remember { mutableStateOf<List<Meal>>(emptyList()) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(MealApi::class.java)
            val response = withContext(Dispatchers.IO) { api.searchMealsByName(searchQuery) }
            meals = response.meals ?: emptyList()
        } else {
            meals = emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campo di ricerca
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Cerca per nome") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista dei risultati
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