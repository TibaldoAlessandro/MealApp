package com.example.mealapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(navController: NavHostController, mealId: String) {
    var meal by remember { mutableStateOf<Meal?>(null) }

    LaunchedEffect(mealId) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(MealApi::class.java)
        val response = withContext(Dispatchers.IO) { api.getMealDetails(mealId) }

        // Debugging: Stampa i dettagli del piatto recuperati dall'API
        println("Dettagli del piatto: ${response.meals}")

        meal = response.meals?.firstOrNull()

        // Debugging: Stampa se il piatto è stato trovato o meno
        if (meal != null) {
            println("Piatto trovato: ${meal!!.strMeal}")
        } else {
            println("Nessun piatto trovato per l'ID: $mealId")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = meal?.strMeal ?: "Dettagli del piatto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Torna indietro")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (meal != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Immagine del piatto
                Image(
                    painter = rememberImagePainter(data = meal!!.strMealThumb),
                    contentDescription = meal!!.strMeal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Ingredienti
                Text(
                    text = "Ingredienti:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val ingredients = getIngredientsList(meal!!)
                ingredients.forEach { ingredient ->
                    Text(text = ingredient)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Procedimento
                Text(
                    text = "Procedimento:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(text = meal!!.strInstructions ?: "Nessuna procedura disponibile.")
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Caricamento...")
            }
        }
    }
}

private fun getIngredientsList(meal: Meal): List<String> {
    val ingredients = mutableListOf<String>()
    val ingredientList = listOf(
        meal.strIngredient1 to meal.strMeasure1,
        meal.strIngredient2 to meal.strMeasure2,
        meal.strIngredient3 to meal.strMeasure3,
        meal.strIngredient4 to meal.strMeasure4,
        meal.strIngredient5 to meal.strMeasure5,
        meal.strIngredient6 to meal.strMeasure6,
        meal.strIngredient7 to meal.strMeasure7,
        meal.strIngredient8 to meal.strMeasure8,
        meal.strIngredient9 to meal.strMeasure9,
        meal.strIngredient10 to meal.strMeasure10,
        meal.strIngredient11 to meal.strMeasure11,
        meal.strIngredient12 to meal.strMeasure12,
        meal.strIngredient13 to meal.strMeasure13,
        meal.strIngredient14 to meal.strMeasure14,
        meal.strIngredient15 to meal.strMeasure15,
        meal.strIngredient16 to meal.strMeasure16,
        meal.strIngredient17 to meal.strMeasure17,
        meal.strIngredient18 to meal.strMeasure18,
        meal.strIngredient19 to meal.strMeasure19,
        meal.strIngredient20 to meal.strMeasure20
    )

    ingredientList.forEach { (ingredient, measure) ->
        if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
            ingredients.add("$ingredient: $measure")
        }
    }

    return ingredients
}